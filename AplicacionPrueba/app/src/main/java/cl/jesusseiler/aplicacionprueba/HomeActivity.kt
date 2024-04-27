package cl.jesusseiler.aplicacionprueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


enum class ProviderType {
    BASIC
}
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //Inicio de aplicacion HomeActivity
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        start(email ?: "", provider ?: "")
    }

    private fun start(email: String, provider: String) {
        title = "Inicio de Aplicación"
        val logOutButton = findViewById<android.widget.Button>(R.id.logOutButton)
        //TextView emailTextView
        val emailTextView = findViewById<android.widget.TextView>(R.id.emailTextView)
        //TextView providerTextView
        val providerTextView = findViewById<android.widget.TextView>(R.id.providerTextView)
        emailTextView.text = email
        providerTextView.text = provider

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
}