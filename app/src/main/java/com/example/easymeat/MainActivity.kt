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

        Thread.sleep(2000)
        setTheme(R.style.Theme_Easymeat)

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

                    if (etEmail.text.isNotEmpty()){
                        if(etPassword.text.isNotEmpty()) {
                            // el administrador tiene su propio login
                            if(email == "administrador@easymeat.com" && password == "admin"){
                                val adminlogin = Intent(this, RegisterShop::class.java)
                                startActivity(adminlogin)
                            }else {
                                // login de los usuarios
                                if (email == etEmail.text.toString() && password == etPassword.text.toString()) {
                                    val userlogin = Intent(this, LoginUser::class.java)
                                    startActivity(userlogin)

                                } else {
                                    //se supone aqui se implementa el login de la tienda
                                    DB.collection("Tienda")
                                        .whereEqualTo("email", etEmail.text.toString()).get()
                                        .addOnSuccessListener { vendedores ->
                                            for (vendedor in vendedores) {
                                                email = vendedor.data.get("email").toString()
                                                password = vendedor.data.get("password").toString()
                                            }
                                            if (email == etEmail.text.toString() && password == etPassword.text.toString()) {

                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    "Usuario o Contraseña Incorrecta",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                }
                            }

                        }else {
                            Toast.makeText(this, "Ingresa La Contraseña", Toast.LENGTH_LONG)
                                .show()
                        }
                    }else{
                        Toast.makeText(this, "Ingresa El Correo Electrónico", Toast.LENGTH_LONG)
                            .show()
                    }
                }
        }
    }
}

