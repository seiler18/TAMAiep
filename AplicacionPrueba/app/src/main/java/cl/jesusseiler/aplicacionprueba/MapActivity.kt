package cl.jesusseiler.aplicacionprueba

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.maps.android.SphericalUtil

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var pointALocation: LatLng
    private lateinit var pointBLocation: LatLng
    private lateinit var editTextPointB: EditText
    private lateinit var editTextTotalCompra: EditText
    private lateinit var textViewDistance: TextView
    private lateinit var textViewCost: TextView
    private lateinit var buttonCalculate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        editTextPointB = findViewById(R.id.editTextPointB)
        editTextTotalCompra = findViewById(R.id.editTextTotalCompra)
        textViewDistance = findViewById(R.id.textViewDistance)
        textViewCost = findViewById(R.id.textViewCost)
        buttonCalculate = findViewById(R.id.buttonCalculate)

        buttonCalculate.setOnClickListener {
            val pointBAddress = editTextPointB.text.toString()
            val totalCompraString = editTextTotalCompra.text.toString()

            if (pointBAddress.isNotEmpty() && totalCompraString.isNotEmpty()) {
                val totalCompra = totalCompraString.toDoubleOrNull()
                if (totalCompra != null) {
                    calculateDistanceAndCost(pointBAddress, totalCompra)
                } else {
                    Toast.makeText(this, "Ingrese un monto de compra válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingrese la dirección del punto B y el total de la compra", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Obtener la ubicación del punto A desde el intent
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        pointALocation = LatLng(latitude, longitude)

        // Marcar la ubicación del punto A en el mapa
        mMap.addMarker(MarkerOptions().position(pointALocation).title("Punto A"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointALocation, 15f))
    }

    private fun calculateDistanceAndCost(pointBAddress: String, totalCompra: Double) {
        // En este ejemplo asumimos que la dirección ingresada es válida y tiene el formato "lat,lng"
        val pointBCoordinates = pointBAddress.split(",")
        val pointBLat = pointBCoordinates[0].toDouble()
        val pointBLng = pointBCoordinates[1].toDouble()
        pointBLocation = LatLng(pointBLat, pointBLng)

        // Marcar la ubicación del punto B en el mapa
        mMap.addMarker(MarkerOptions().position(pointBLocation).title("Punto B"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointBLocation, 15f))

        // Calcular la distancia entre el punto A y el punto B
        val distance = SphericalUtil.computeDistanceBetween(pointALocation, pointBLocation) / 1000 // en km
        textViewDistance.text = "Distancia: %.2f km".format(distance)

        // Calcular el costo del despacho según las reglas establecidas en el caso de uso
        //Aca podremos definir cuanto se cobra por cada km , si el total de compra excede 50mil
        //el despacho es gratuito.
        val cost = when {
            totalCompra >= 50000 && distance <= 20 -> 0.0
            totalCompra in 25000.0..49999.99 -> distance * 150
            else -> distance * 300
        }

        textViewCost.text = "Costo de despacho: $%.2f".format(cost)
    }
}
