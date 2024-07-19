package com.example.playall

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playall.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.pianoBtn.setOnClickListener {
            startActivity(Intent(this, PianoActivity3::class.java))
        }

        binding.guitarBtn.setOnClickListener {
            startActivity(Intent(this, GuitarActivity::class.java))
        }

        binding.drumBtn.setOnClickListener {
            startActivity(Intent(this, DrumActivity::class.java))
        }
    }
}
