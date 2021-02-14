package com.example.lista

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lista.databinding.ActivityModifyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Modify : AppCompatActivity() {
    private val TAG = "ModifyProduct"
    private lateinit var binding: ActivityModifyBinding
    private lateinit var pref: SharedPreferences
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        loadColor()
        database = FirebaseFirestore.getInstance()

        binding.updateProduct.setText(intent.getStringExtra("produkt"))
        binding.updatePrice.setText(intent.getStringExtra("cena"))
        binding.updateAmount.setText(intent.getStringExtra("ilosc"))

        binding.update.setOnClickListener{
            val product = binding.updateProduct.text.toString()
            val cena = binding.updatePrice.text.toString()
            val ilosc = binding.updateAmount.text.toString()
            val zakup = binding.updateCheck.isChecked
            val id= intent.getStringExtra("id")
             updateProduct(id, product, cena, ilosc, zakup)

                val intent = Intent(this, ProductListActivity::class.java)
                startActivity(intent)

            }
        binding.delete.setOnClickListener {
            val id= intent.getStringExtra("id")
            deleteProduct(id)
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }




    }

    fun loadColor(){
        binding.root.setBackgroundColor(pref.getInt("colorBg", Color.WHITE))
    }
    private fun updateProduct(
        id: String?,
        product: String,
        cena: String,
        ilosc: String,
        zakup: Boolean
    ) {
        val product = Product(id, product, cena, ilosc, zakup).toMap()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            database!!.collection(user.uid)
                .document(id.toString())
                .set(product)
                .addOnSuccessListener { documentReference ->
                    Log.e(TAG, "Update successful! ")
                    Toast.makeText(applicationContext, "Zmodyfikowano produkt", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error modify product", e)
                    Toast.makeText(
                        applicationContext,
                        "Błąd modyfikacji produktu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun deleteProduct(id: String?) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            database.collection(user.uid)
                .document(id.toString())
                .delete()
        }

    }
}