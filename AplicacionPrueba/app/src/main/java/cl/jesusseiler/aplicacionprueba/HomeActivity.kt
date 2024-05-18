package cl.jesusseiler.aplicacionprueba


import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.Manifest
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Vibrator
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

//enum para el tipo de proveedor de autenticación
enum class ProviderType {
    BASIC
}
class HomeActivity : ComponentActivity() {

    private lateinit var editTextTemperature: EditText
    private lateinit var buttonConvert: Button
    private lateinit var textViewResult: TextView

    // Temperatura máxima soportada en Celsius
    private val temperaturaMaximaSoportada = 37 // Ejemplo: 37 grados Celsius

    //Companion object para el código de solicitud de permiso de ubicación
    companion object {
        // Código de solicitud de permiso de ubicación
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
    //Sobreescribir el método onCreate
    //Se requiere la API 31
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Verifica y solicita permisos antes de obtener la ubicación
        if (checkLocationPermission()) {
            getLastKnownLocation()
        } else {
            requestLocationPermission()
        }


        // Iniciar la obtención de la última ubicación conocida
        getLastKnownLocation()

        //Inicio de aplicacion HomeActivity
        editTextTemperature = findViewById(R.id.editTextTemperature)
        buttonConvert = findViewById(R.id.buttonConvert)
        textViewResult = findViewById(R.id.textViewResult)

        buttonConvert.setOnClickListener {
            convertirYCompararTemperatura()
        }
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        start(email ?: "", provider ?: "")

        val buttonShowMap = findViewById<Button>(R.id.buttonShowMap)
        buttonShowMap.setOnClickListener {
            // Obtener la última ubicación conocida
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val intent = Intent(this, MapActivity::class.java)
                        intent.putExtra("latitude", it.latitude)
                        intent.putExtra("longitude", it.longitude)
                        startActivity(intent)
                    }
                }
        }

    }


    //Sobreescribir el método onRequestPermissionsResult
    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
//Sobreescribir el método onRequestPermissionsResult
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }


    private fun convertirYCompararTemperatura() {
        // Obtener la temperatura en Fahrenheit
        val temperaturaFahrenheit = editTextTemperature.text.toString().toDoubleOrNull()
        // Patrón de vibración
        val pattern = longArrayOf(0, 100, 1000, 300, 200, 100, 500, 200, 100)

        if (temperaturaFahrenheit != null) {
            // Convertir Fahrenheit a Celsius
            val temperaturaCelsius = (temperaturaFahrenheit - 32) * 5 / 9

            // Mostrar la temperatura en Celsius
            textViewResult.text = "Temperatura en Celsius: $temperaturaCelsius °C"

            // Comparar con la temperatura máxima soportada
            if (temperaturaCelsius > temperaturaMaximaSoportada) {
                // Si la temperatura supera el límite, activar alarma
                val mediaPlayer = MediaPlayer.create(this, R.raw.notification)
                mediaPlayer.start()
                val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(pattern, -1)

                // Guardar la temperatura máxima registrada en Firebase
                val currentUser = FirebaseAuth.getInstance().currentUser
                val databaseReference = FirebaseDatabase.getInstance().reference

                currentUser?.uid?.let { userId ->
                    databaseReference.child("user_temperatures").child(userId).setValue(
                        mapOf(
                            "temperatura_maxima_registrada" to temperaturaCelsius
                        )
                    )
                }
            }
        } else {
            textViewResult.text = "Ingrese una temperatura válida"
        }
    }

    //Función para iniciar la aplicación
    private fun start(email: String, provider: String) {
        title = "Inicio de Aplicación"
        //Boton logOutButton
        val logOutButton = findViewById<android.widget.Button>(R.id.logOutButton)
        //TextView emailTextView
        val emailTextView = findViewById<android.widget.TextView>(R.id.emailTextView)
        //TextView providerTextView
        val providerTextView = findViewById<android.widget.TextView>(R.id.providerTextView)
        emailTextView.text = email
        providerTextView.text = provider
        //Boton logOutButton el cual sirve para cerrar sesión
        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun getLastKnownLocation() {
        if (!checkLocationPermission()) {
            requestLocationPermission()
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let { saveLocationToFirebase(it) }
            }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun getLastKnownLocationAndOpenMap() {
        if (!checkLocationPermission()) {
            requestLocationPermission()
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val intent = Intent(this, MapActivity::class.java)
                    intent.putExtra("latitude", it.latitude)
                    intent.putExtra("longitude", it.longitude)
                    startActivity(intent)
                }
            }
    }



    //Función para guardar la ubicación en Firebase
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun saveLocationToFirebase(location: Location) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val databaseReference = FirebaseDatabase.getInstance().reference

        currentUser?.uid?.let { userId ->
            databaseReference.child("user_locations").child(userId).setValue(
                mapOf(
                    "latitude" to location.latitude,
                    "longitude" to location.longitude
                )
            )
        }
    }


}