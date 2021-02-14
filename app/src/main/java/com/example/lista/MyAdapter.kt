@file:Suppress("DEPRECATION")

package com.example.lista

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MyAdapter(
    private val productList: MutableList<Product>,
    private val context: Context,
    private val firestoreDB: FirebaseFirestore
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private val TAG = "ModifyProduct"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(
            R.layout.list_element,
            parent,
            false
        )

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productList[position]

        holder!!.produkt.text = product.product
        holder.cena.text = product.cena
        holder.ilosc.text = product.ilosc
        holder.zakup.isChecked = product.zakup
        holder.itemView.setOnClickListener{
                val intent = Intent(holder.itemView.context, Modify::class.java)
                intent.putExtra("produkt", holder.produkt.text as String)
                intent.putExtra("cena", holder.cena.text as String)
                intent.putExtra("ilosc", holder.ilosc.text as String)
                intent.putExtra("id", productList[position].id)
                holder.itemView.context.startActivity(intent)
            notifyDataSetChanged()
        }

        holder.zakup.setOnClickListener{
            productList[position].zakup = holder.zakup.isChecked
            //viewModel.modify(productList[position])
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = productList.size

    inner class MyViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var produkt: TextView = view.findViewById(R.id.tv_produkt)
        internal var cena: TextView = view.findViewById(R.id.tv_cena)
        internal var ilosc: TextView = view.findViewById(R.id.tv_ilosc)
        internal var zakup: CheckBox = view.findViewById(R.id.cb_zakup)


    }
}