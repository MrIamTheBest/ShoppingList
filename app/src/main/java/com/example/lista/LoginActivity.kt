package com.example.lista

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var reference: CollectionReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val auth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            auth.signInWithEmailAndPassword(et_user.text.toString(), et_password.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){

                        Toast.makeText(this, "Zalogowano", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java).also {
                            it.putExtra("user", auth.currentUser?.email)
                        })
                    }
                    else{
                        Toast.makeText(this, "Błąd logowania", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        login.setOnLongClickListener {
            auth.createUserWithEmailAndPassword(
                et_user.text.toString(),
                et_password.text.toString()
            )
                .addOnCompleteListener {
                    if(it.isSuccessful){

                        Toast.makeText(this, "Rejestracja przebiegła pomyślnie", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Błąd rejestracji", Toast.LENGTH_SHORT).show()
                    }
                }
            true
        }
        nologin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}