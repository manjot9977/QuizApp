package com.quizmaster.quizzed.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.quizmaster.quizzed.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the binding object using the layout inflater
        binding = ActivitySignupBinding.inflate(layoutInflater)
        // Set the content view to the root view of the binding object
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Access the views using binding object
        binding.btnSignUp.setOnClickListener {
            signUpUser()
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity ::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signUpUser(){
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
            Toast.makeText(this,"Email and Password can't Be Blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword){
            Toast.makeText(this,"Password didn't match", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if(it.isSuccessful){
                    Toast.makeText(this,"Login Successful",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Error Creating User", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
