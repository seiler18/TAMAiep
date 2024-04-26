package cl.jesusseiler.radianes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.jesusseiler.radianes.ui.theme.RadianesTheme
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    // Declaramos una propiedad llamada radianes y la inicializamos en 0.0
    private var radianes: Double = 0.0
    //Declaramos la variable R para definir el radio de la tierra
    private val R = 6371 // Radio de la Tierra en kilómetros

    // Coordenadas de la Plaza de Armas de puerto Montt buscada en google Maps
    private val plazaDeArmasLat = -41.47235812853806
    private val plazaDeArmasLong = -72.94042228897648

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RadianesTheme {
                // Creamos una superficie que ocupe el tamaño disponible y tenga el color de fondo del tema Material
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Utilizamos un Column para apilar los elementos uno debajo del otro
                    Column(modifier = Modifier.padding(16.dp)) { // Agregamos un espaciado alrededor de los elementos
                        // Llamamos al método para convertir el número a radianes
                        convertirARadianes(70.0)
                        // Mostramos un texto en la interfaz de usuario con el resultado en radianes
                        Text(
                            text = "Hola, bienvenidos a la aplicación de prueba de Jesus Seiler. El resultado en radianes es: $radianes",
                            modifier = Modifier.padding(vertical = 8.dp) // Agregamos un espaciado vertical entre textos
                        )
                        // Calculamos la distancia desde la ubicación del dispositivo hasta la Plaza de Armas
                        //Para este caso las coordenadas las saque de google maps de mi ubicación actual
                        //En un futuro esto podrian ser 2 variables que pueden venir de un servicio que pida
                        //la ubicación de mi telefono cada vez que se ejecuta.
                        val distancia = calcularDistancia(-41.3818299918932, -72.89793815958112, plazaDeArmasLat, plazaDeArmasLong)
                        // Mostramos un texto en la interfaz de usuario con la distancia calculada
                        Text(
                            text = "La distancia desde tu ubicación hasta la Plaza de Armas es: $distancia kilómetros",
                            modifier = Modifier.padding(vertical = 8.dp) // Agregamos un espaciado vertical entre textos
                        )
                    }
                }
            }
        }
    }
    // Método para calcular la distancia entre dos ubicaciones GPS utilizando la fórmula de Haversine
    private fun calcularDistancia(lat1: Double, long1: Double, lat2: Double, long2: Double): Double {
        val deltaLat = Math.toRadians(lat2 - lat1)
        val deltaLong = Math.toRadians(long2 - long1)
        val a = sin(deltaLat / 2).pow(2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(deltaLong / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    // Método para convertir un número decimal a radianes y asignar el resultado a la propiedad radianes
    private fun convertirARadianes(numeroDecimal: Double) {
        radianes = Math.toRadians(numeroDecimal)
        Log.d("MainActivity", "El resultado en radianes es: $radianes") // Agregamos esta línea para mostrar en logcat
    }
}
