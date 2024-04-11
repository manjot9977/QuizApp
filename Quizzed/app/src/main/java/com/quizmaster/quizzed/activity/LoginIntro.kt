package com.quizmaster.quizzed.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.quizmaster.quizzed.databinding.ActivityLoginIntroBinding
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class LoginIntro : AppCompatActivity() {
    private lateinit var binding: ActivityLoginIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the binding object using the layout inflater
        binding = ActivityLoginIntroBinding.inflate(layoutInflater)
        // Set the content view to the root view of the binding object
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()

        if(auth.currentUser !=null){
            Toast.makeText(this,"User Already Logged in", Toast.LENGTH_SHORT).show()
            redirect("MAIN")
        }
        // Access the views using binding object
        binding.btnGetStarted.setOnClickListener{
            redirect("LOGIN")
        }
    }

    private fun redirect(name:String){
        val intent = when(name){
            "LOGIN" -> Intent(this, LoginActivity::class.java)
            "MAIN" -> Intent(this, MainActivity::class.java)
            else -> throw Exception ("no path exists")
        }
        startActivity(intent)
        finish()
    }
}
