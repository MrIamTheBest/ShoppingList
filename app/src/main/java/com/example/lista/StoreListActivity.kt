package com.example.lista

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lista.databinding.ActivityProductListBinding
import com.example.lista.databinding.ActivityStoreListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.activity_store_list.*

class StoreListActivity : AppCompatActivity() {
    private val TAG = "StoreListActivity"
    private lateinit var binding: ActivityStoreListBinding
    private lateinit var pref: SharedPreferences
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var firestoreListener: ListenerRegistration
    private var mAdapter: StoreAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        loadColor()

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        loadStoreList()

        //LayoutManager
        binding.rvStore.layoutManager = LinearLayoutManager(this)
        //DividerItemDecorator(linia dzieląca)
        binding.rvStore.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        firestoreListener = db!!.collection("Stores")
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed!", e)
                    return@EventListener
                }

                val storeList = mutableListOf<Store>()

                if (documentSnapshots != null) {
                    for (doc in documentSnapshots) {
                        val note = doc.toObject(Store::class.java)
                        note.storeId = doc.id
                        storeList.add(note)

                    }
                }

                mAdapter = StoreAdapter(storeList, applicationContext, db!!)
                rv_store.adapter = mAdapter
            })


    }
    override fun onDestroy() {
        super.onDestroy()

        firestoreListener!!.remove()
    }
    fun loadColor(){
        binding.root.setBackgroundColor(pref.getInt("colorBg", Color.WHITE))
    }
    private fun loadStoreList() {

        db!!.collection("Stores")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val storeList = mutableListOf<Store>()

                    for (doc in task.result!!) {
                        val store = doc.toObject<Store>(Store::class.java)
                        store.storeId = doc.id
                        storeList.add(store)
                    }

                    mAdapter = StoreAdapter(storeList, applicationContext, db!!)
                    val mLayoutManager = LinearLayoutManager(applicationContext)
                    rv_store.layoutManager = mLayoutManager
                    rv_store.itemAnimator = DefaultItemAnimator()
                    rv_store.adapter = mAdapter
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }
}