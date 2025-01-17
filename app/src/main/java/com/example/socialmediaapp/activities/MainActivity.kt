package com.example.socialmediaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.socialmediaapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton.setOnClickListener {
            performLogin()
        }

        registerButtonGo.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent) }


    }
    private fun performLogin(){
        val login=emailTextRegister.text.toString()
        val password=passwordTextRegister.text.toString()
        if(login.isEmpty()||password.isEmpty()) {

            Toast.makeText(this,"Please enter email/password", Toast.LENGTH_SHORT).show()
            return

        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(login,password).addOnCompleteListener {
            if(!it.isSuccessful)return@addOnCompleteListener
            val intent = Intent(this, appActivity::class.java)
            startActivity(intent)
            finish()

        }.addOnFailureListener {
            Toast.makeText(this,"Zly login haslo", Toast.LENGTH_SHORT).show()
        }
    }
}