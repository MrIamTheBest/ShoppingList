package com.example.lista

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.lista.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_options.*

class MainActivity() : AppCompatActivity() {

    private lateinit var sp: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: SharedPreferences
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.t1.text = intent.getStringExtra("user")
        sp = getPreferences(Context.MODE_PRIVATE)
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        loadColor()
        loadFontSize()
        var user =auth.currentUser
        binding.b1.setOnClickListener {
            val intent2 = Intent(this, OptionsActivity::class.java)
            startActivity(intent2)
            //binding.t1.text = binding.et1.text
        }
        binding.b3.setOnClickListener {
            val intent1 = Intent(this, SharedListActivity::class.java)
            // intent1.putExtra("t1text", binding.t1.text)
            startActivity(intent1)
        }
        binding.imageButton.setOnClickListener {
            val intent1 = Intent(this, MapsActivity::class.java)
            // intent1.putExtra("t1text", binding.t1.text)
            startActivity(intent1)
        }
        if(user !=null) {
            binding.login2.visibility = View.GONE;
            binding.t5.visibility = View.GONE;
            binding.logout.setOnClickListener {
                auth.signOut()
                val intent3 = Intent(this, LoginActivity::class.java)
                startActivity(intent3)
            }
            binding.b2.setOnClickListener {
                val intent1 = Intent(this, ProductListActivity::class.java)
                // intent1.putExtra("t1text", binding.t1.text)
                startActivity(intent1)
            }
        }
        else{
            binding.logout.visibility = View.GONE;
            binding.b2.visibility = View.GONE;
            binding.t3.visibility = View.GONE;
            binding.login2.setOnClickListener {
                val intent1 = Intent(this, LoginActivity::class.java)
                startActivity(intent1)
            }
        }
    }

    override fun onStart() {
        super.onStart()
    //    binding.t1.text = sp.getString("tv_text", getString(R.string.nic))
    }

    override fun onStop() {
        super.onStop()
        val editor = sp.edit()
        editor.putString("tv_text", binding.t1.text.toString())
        editor.apply()
    }
    fun loadColor(){
        binding.root.setBackgroundColor(pref.getInt("colorBg", Color.WHITE ))
    }
    fun loadFontSize():Float{
        val currentsize = pref.getFloat("fontsize", 14f)
        binding.t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentsize)
        binding.t3.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentsize)
        binding.t4.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentsize)
        binding.t5.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentsize)
        return currentsize
    }
    }
