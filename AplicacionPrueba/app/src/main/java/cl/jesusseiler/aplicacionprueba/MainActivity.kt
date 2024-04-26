package cl.jesusseiler.aplicacionprueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        //Para comprobar que funciona la conexion con Realtime Database
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hola, Grupo de Aiep!")

        super.onCreate(savedInstanceState)
        //Abrir la ventana de login al iniciar la aplicacion
        setContentView(R.layout.activity_login)
    }
}
