package com.example.easymeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//estas 2 librerias son necesarias
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnRegistro = findViewById<Button>(R.id.btnRegistro)
        btnRegistro.setOnClickListener {
            val registro = Intent(this, FormularioRegistro::class.java)
            startActivity(registro)
        }

        val DB = FirebaseFirestore.getInstance()
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        var email:String = ""
        var password:String = ""

        btnIngresar.setOnClickListener {

            DB.collection("Usuario").whereEqualTo("email", etEmail.text.toString()).get()
                .addOnSuccessListener { users ->
                    for (user in users) {
                        email = user.data.get("email").toString()
                        password = user.data.get("password").toString()
                    }
                    if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()) {
                        if (email == etEmail.text.toString() && password == etPassword.text.toString()) {
                            Toast.makeText(this, "Lo lograste", Toast.LENGTH_LONG).show()
                            if(email == "administrador@easymeat.com" && password == "admin"){
                                val adminlogin = Intent(this, RegisterShop::class.java)
                                startActivity(adminlogin)
                            }


                        }else{
                            Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        Toast.makeText(this, "Por favor llena los campos", Toast.LENGTH_LONG).show()
                    }
                }

        }
    }
}

