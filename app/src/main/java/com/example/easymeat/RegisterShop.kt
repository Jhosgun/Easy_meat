package com.example.easymeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class RegisterShop : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_shop)

        val DB = FirebaseFirestore.getInstance()

        val etName = findViewById<EditText>(R.id.etName)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val btnAccept = findViewById<Button>(R.id.btnAccept)
        val etPassword = findViewById<TextView>(R.id.etPassword)

        btnAccept.setOnClickListener {

            DB.collection("Tienda").add(
                hashMapOf(
                    "name" to etName.text.toString(),
                    "address" to etAddress.text.toString(),
                    "email" to etEmail.text.toString(),
                    "phone" to etPhone.text.toString(),
                    "password" to etPassword.text.toString()
                )
            )
            Toast.makeText(this, "Tienda registrada", Toast.LENGTH_LONG).show()

            val registerShop = Intent(this, RegisterShop::class.java)
            startActivity(registerShop)
        }
    }
}