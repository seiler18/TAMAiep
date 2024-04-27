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

//enum para el tipo de proveedor de autenticación
enum class ProviderType {
    BASIC
}
class HomeActivity : ComponentActivity() {
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

        // Iniciar la obtención de la última ubicación conocida
        getLastKnownLocation()

        //Inicio de aplicacion HomeActivity
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        start(email ?: "", provider ?: "")
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


    //Función para obtener la última ubicación conocida
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun getLastKnownLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si los permisos de ubicación no están concedidos, solicitarlos.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Obtener la última ubicación conocida.
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Guardar la ubicación en Firebase si se obtiene con éxito.
                location?.let { saveLocationToFirebase(it) }
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