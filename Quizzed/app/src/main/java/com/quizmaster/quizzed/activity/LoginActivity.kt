package com.quizmaster.quizzed.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.quizmaster.quizzed.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInHelper: GoogleSignInHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        googleSignInHelper = GoogleSignInHelper(this)

        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnGoogleSignIn.setOnClickListener {
            googleSignInHelper.signIn()
        }
    }

    private fun login() {
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Email and Password can't Be Blank", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
