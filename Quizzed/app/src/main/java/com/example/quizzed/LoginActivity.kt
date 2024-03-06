package com.example.quizzed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizzed.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the binding object using the layout inflater
        binding = ActivityLoginBinding.inflate(layoutInflater)
        // Set the content view to the root view of the binding object
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Access the views using binding object
        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
         }
    }

    private fun login(){
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isBlank() || password.isBlank()){
            Toast.makeText(this,"Email and Password can't Be Blank", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
            if(it.isSuccessful){
                Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Authentication Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
