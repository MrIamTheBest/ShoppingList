package com.example.lista
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lista.databinding.ActivityAddProductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class AddProductShared : AppCompatActivity() {
    private val TAG = "AddProduct"
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var pref: SharedPreferences
    private lateinit var database: FirebaseFirestore
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         val auth = FirebaseAuth.getInstance()

        binding = ActivityAddProductBinding.inflate(layoutInflater)
         setContentView(binding.root)
         pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
         loadColor()
         database = FirebaseFirestore.getInstance()
         //val ref = database.getReference("Product")

        // val viewModel = ProductViewModel(application)
         binding.add.setOnClickListener{
                 val product = binding.product.text.toString()
                 val cena = binding.price.text.toString()
                 val ilosc = binding.amount.text.toString()
                 val zakup = binding.check.isChecked
                 addproduct(product, cena, ilosc, zakup)
                 finish()

             //val broadcast = Intent("com.example.lista.AddProduct")
             //broadcast.component = ComponentName(this, ProductReceiver::class.java)
            // broadcast.component = ComponentName("com.example.productreceiver", "com.example.productreceiver.ProductReceiver")
               // broadcast.putExtra("produkt", binding.product.text.toString())
                //sendBroadcast(broadcast)
                //sendOrderedBroadcast(broadcast, "com.example.lista.MY_PERMISSION")
             //val intent = Intent(this, ProductListActivity::class.java)
            // startActivity(intent)
         }
    }


    fun loadColor(){
        binding.root.setBackgroundColor(pref.getInt("colorBg", Color.WHITE ))
    }
    fun addproduct(product: String, cena: String, ilosc: String, zakup: Boolean){
        val product = Product(product, cena,ilosc,zakup).toMap()

            database!!.collection("Products")
                .add(product)
                .addOnSuccessListener { documentReference ->
                    Log.e( TAG,"DocumentSnapshot written with ID: " + documentReference.id)
                    Toast.makeText(applicationContext, "Dodano produkt", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e( TAG,"Error adding product", e)
                    Toast.makeText(applicationContext, "Błąd dodawania produktu", Toast.LENGTH_SHORT).show()
                }

    }
}