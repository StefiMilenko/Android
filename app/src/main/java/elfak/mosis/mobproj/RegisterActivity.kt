package elfak.mosis.mobproj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent : Intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        val editEmail: EditText = findViewById<EditText>(R.id.edit_email)
        val editPassword: EditText = findViewById<EditText>(R.id.edit_password)
        val regButton: Button = findViewById<Button>(R.id.register_button)
        val gotoButton: Button = findViewById<Button>(R.id.gotologin_button)

        regButton.setOnClickListener {
            val email:String =editEmail.text.toString()
            val pass: String = editPassword.text.toString()

            if (TextUtils.isEmpty(email)){
                Toast.makeText(this,"Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pass)){
                Toast.makeText(this,"Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account created.", Toast.LENGTH_SHORT,).show()
                        val intent : Intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT,).show()

                    }
                }
        }
        gotoButton.setOnClickListener {
            val intent : Intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}