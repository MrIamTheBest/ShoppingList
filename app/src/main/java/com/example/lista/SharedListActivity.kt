package com.example.lista

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lista.databinding.ActivityProductListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_product_list.*


class SharedListActivity : AppCompatActivity() {
    private val TAG = "ProductListActivity"
    private lateinit var binding: ActivityProductListBinding
    private lateinit var pref: SharedPreferences
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var firestoreListener: ListenerRegistration
    private var mAdapter: SharedAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        loadColor()
        loadFontSize()

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        loadProductList()

        //LayoutManager
        binding.rv1.layoutManager = LinearLayoutManager(this)
        //DividerItemDecorator(linia dzieląca)
        binding.rv1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

            firestoreListener = db!!.collection("Products")
                .addSnapshotListener(EventListener { documentSnapshots, e ->
                    if (e != null) {
                        Log.e(TAG, "Listen failed!", e)
                        return@EventListener
                    }

                    val notesList = mutableListOf<Product>()

                    if (documentSnapshots != null) {
                        for (doc in documentSnapshots) {
                            val note = doc.toObject(Product::class.java)
                            note.id = doc.id
                            notesList.add(note)

                        }
                    }

                    mAdapter = SharedAdapter(notesList, applicationContext, db!!)
                    rv1.adapter = mAdapter
                })

        binding.addbutton.setOnClickListener{
            val intent = Intent(this, AddProductShared::class.java)
            startActivity(intent)
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        firestoreListener!!.remove()
    }
    fun loadColor(){
        binding.root.setBackgroundColor(pref.getInt("colorBg", Color.WHITE))
    }
    fun loadFontSize():Float{
        val currentsize = pref.getFloat("fontsize", 14f)
        binding.t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentsize)
        binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentsize)
        binding.textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentsize)
        return currentsize
    }
    private fun loadProductList() {

            db!!.collection("Products")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val productList = mutableListOf<Product>()

                        for (doc in task.result!!) {
                            val note = doc.toObject<Product>(Product::class.java)
                            note.id = doc.id
                            productList.add(note)
                        }

                        mAdapter = SharedAdapter(productList, applicationContext, db!!)
                        val mLayoutManager = LinearLayoutManager(applicationContext)
                        rv1.layoutManager = mLayoutManager
                        rv1.itemAnimator = DefaultItemAnimator()
                        rv1.adapter = mAdapter
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.exception)
                    }
                }
    }
}