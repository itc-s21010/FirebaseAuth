package com.example.firebaseauth

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var textFullname: TextView
    lateinit var textEmail: TextView
    lateinit var btnLogout: Button
    lateinit var buttonemer: Button

    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("status", "onCreate")
        setContentView(R.layout.activity_profile)
        textFullname = findViewById(R.id.full_name)
        textEmail = findViewById(R.id.email)
        btnLogout = findViewById(R.id.btn_logout)

        val buttonimage: Button = findViewById(R.id.selectbutton)
        buttonimage.setOnClickListener(this)

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser!=null){
            textFullname.text = firebaseUser.displayName
            textEmail.text = firebaseUser.email
        }else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        buttonemer.setOnClickListener {
            openUrl("http://54.199.142.48/wordpress/")
        }

        val btn_translate = findViewById<Button>(R.id.btn_translate)
        btn_translate.setOnClickListener {
            val intent = Intent(this, TranslateActivity::class.java)
            startActivity(intent)
        }
        val btn_maps = findViewById<Button>(R.id.btn_maps)
        btn_maps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        val btn_emergency = findViewById<Button>(R.id.btn_emergency)
        btn_emergency.setOnClickListener {
            val intent = Intent(this, EmergencyActivity::class.java)
            startActivity(intent)
        }
        val btn_reset = findViewById<Button>(R.id.btn_resetpage)
        btn_reset.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }


    private fun openUrl(link:String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.selectbutton -> {
                selectPhoto()
            }
        }
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("registerForActivityResult(result)", result.toString())

        if (result.resultCode != RESULT_OK) {
            return@registerForActivityResult
        } else {
            try {
                result.data?.data?.also { uri : Uri ->
                    val inputStream = contentResolver?.openInputStream(uri)
                    val image = BitmapFactory.decodeStream(inputStream)
                    val imageView: ImageView = findViewById(R.id.imageView)
                    imageView.setImageBitmap(image)
                }
            } catch (e: Exception) {
                Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
            }
        }
    }

    //-------------------------------------------------------------------------
    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        launcher.launch(intent)
    }
}