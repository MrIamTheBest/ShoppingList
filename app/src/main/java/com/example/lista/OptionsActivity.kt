package com.example.lista

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.example.lista.databinding.ActivityOptionsBinding
import kotlinx.android.synthetic.main.activity_options.*


class OptionsActivity : AppCompatActivity() {
    private var size= 14f
    private lateinit var pref: SharedPreferences
    private lateinit var binding: ActivityOptionsBinding
    private var bccolors = intArrayOf(Color.WHITE, Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW, Color.LTGRAY, Color.MAGENTA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        loadColor()
        size= loadFontSize()

        binding.bwieksze.setOnClickListener {
            if(size<=30f) {
                size += 2f
                saveFontSize(size)
                loadFontSize()
            }
           // tfont.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
        binding.bmniejsze.setOnClickListener {

            if(size>=0) {
                size -=2f
                saveFontSize(size)
                loadFontSize()
            }

          //  tfont.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);

        }
        binding.bkolor.setOnClickListener {
          //  binding.root.setBackgroundColor(Color.BLUE)
            saveColor()
            loadColor()
        }

        }
    fun saveColor(){
        val editor = pref.edit()
        for (i in bccolors.indices) {
            if(bccolors[i]==pref.getInt("colorBg", Color.WHITE)){
                editor.putInt("colorBg", bccolors[(i+1)%bccolors.size]);
                editor.apply()
                return
            }

        }
    }
    fun loadColor(){
        binding.root.setBackgroundColor(pref.getInt("colorBg", Color.WHITE ))
    }
    fun saveFontSize(fontsize: Float){
        val editor = pref.edit()
        editor.putFloat("fontsize", fontsize);
        editor.apply()
    }
    fun loadFontSize():Float{
        val currentsize = pref.getFloat("fontsize", 14f)
        tfont.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentsize)
        return currentsize
    }
    }
