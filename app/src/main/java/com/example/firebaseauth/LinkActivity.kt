package com.example.firebaseauth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LinkActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var button2: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link)

        button = findViewById(R.id.button)
        button = findViewById(R.id.button2)

        button.setOnClickListener {
            openUrl("https://chat.google.com/dm/m8MplEAAAAE/kLxdCDDvkAw/kLxdCDDvkAw?cls=10")
        }

        button2.setOnClickListener {
            openUrl("")
        }

    }

    private fun openUrl(link:String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)

    }
}