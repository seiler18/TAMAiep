package cl.jesusseiler.aplicacionprueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //Para comprobar que funciona la conexion con Realtime Database
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hola, Grupo de Aiep!")

        super.onCreate(savedInstanceState)
        //Abrir la ventana de login al iniciar la aplicacion
        setContentView(R.layout.activity_login)

        //Comienzo de mi aplicación con Firebase
        start()
    }
    private fun start() {
        title = "Authentication Firebase"
        //Boton signUpButton
        val signUpButton = findViewById<android.widget.Button>(R.id.logOutButton)
        //EditText emailEditText
        val emailEditText = findViewById<android.widget.EditText>(R.id.emailEditText)
        //EditText passwordEditText
        val passwordEditText = findViewById<android.widget.EditText>(R.id.passwordEditText)
        signUpButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }
        }

        //Boton loginButton
        val loginButton = findViewById<android.widget.Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    //Función para mostrar un mensaje de error si no se completan los campos
    //Debo trabajar en esta para cuando no se complete el campo de email o password
    //Muestre un mensaje de error - No implementada en esta versión
    private fun showAlert() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Error")
        builder.setMessage("Complete all fields")
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Función para mostrar la ventana HomeActivity si el login es exitoso
    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

}

