package com.example.lista

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class StoreAdapter(
        private val storeList: MutableList<Store>,
        private val context: Context,
        private val firestoreDB: FirebaseFirestore
) : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(
                R.layout.store_element,
                parent,
                false
        )

        return StoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val stores = storeList[position]

        holder!!.store.text = stores.store
        holder.description.text = stores.description
        holder.location.text = stores.location.toString()
        holder.radius.text = stores.radius.toString()
        val intent = Intent(context, MapsActivity::class.java)
        intent.putExtra("locationlat", stores.location!!.latitude)
        intent.putExtra("locationlon", stores.location!!.longitude)
        intent.putExtra("radius", stores.radius)
        holder.add_Store.setOnClickListener {
            stores.store?.let { it1 -> stores.description?.let { it2 ->
            stores.location?.let { it3 -> stores.radius?.let { it4 -> addfavorite(it1, it2, it3, it4) } } } }
            val intent = Intent(context, MapsActivity::class.java)
            holder.add_Store.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = storeList.size
    inner class StoreViewHolder internal constructor(view: View):RecyclerView.ViewHolder(view){
        internal  var add_Store: Button = view.findViewById(R.id.add_store)

        internal var store: TextView = view.findViewById(R.id.store)
        internal var description: TextView = view.findViewById(R.id.description)
        internal var location: TextView = view.findViewById(R.id.coordinates)
        internal var radius: TextView = view.findViewById(R.id.radius)
    }
    private fun addfavorite(store:String,description:String,location:GeoPoint,radius:Float){

        val store = Store(store, description,location,radius).toMap()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            firestoreDB!!.collection(user.uid + "store")
                .add(store)
                .addOnSuccessListener { documentReference ->
                    Log.e("TAG", "DocumentSnapshot written with ID: " + documentReference.id)
                    Toast.makeText(context, "Dodano do ulubionych", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("TAG", "Error adding product", e)
                    Toast.makeText(context, "Błąd dodawania do ulubionych", Toast.LENGTH_SHORT).show()
                }
        }
    }
}