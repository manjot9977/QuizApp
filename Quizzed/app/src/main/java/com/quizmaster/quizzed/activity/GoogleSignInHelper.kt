package com.quizmaster.quizzed.activity

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInHelper(private val activity: Activity) {

    private val RC_SIGN_IN = 9001
    private var googleSignInClient: GoogleSignInClient? = null
    private val auth = FirebaseAuth.getInstance()

    init {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun signIn() {
        Log.d("GoogleSignInHelper", "signIn() method called")
        val signInIntent = googleSignInClient?.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            Log.d("GoogleSignInHelper", "onActivityResult() method called")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken)
            } catch (e: ApiException) {
                Toast.makeText(activity, "Google Sign In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(activity, "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    openMainActivity() // Open main activity upon successful sign-in
                } else {
                    Toast.makeText(activity, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun openMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}
