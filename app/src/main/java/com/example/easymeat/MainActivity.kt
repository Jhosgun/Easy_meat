package com.example.easymeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//estas 2 librerias son necesarias
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    lateinit var  emailUser:String
    lateinit var passwordUser:String
    lateinit var  emailStore:String
    lateinit var passwordStore:String

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


        btnIngresar.setOnClickListener {

            emailUser = ""
            passwordUser = ""
            emailStore = ""
            passwordStore = ""

            DB.collection("Usuario").whereEqualTo("email", etEmail.text.toString()).get()
                .addOnSuccessListener { users ->
                    for (user in users) {
                        emailUser = user.data.get("email").toString()
                        passwordUser = user.data.get("password").toString()
                    }

                    if (etEmail.text.isNotEmpty()){
                        if(etPassword.text.isNotEmpty()) {
                            // el administrador tiene su propio login
                            if(emailUser == "administrador@easymeat.com" && passwordUser == "admin"){
                                sesion = emailUser
                                Toast.makeText(this, "" + sesion.toString(), Toast.LENGTH_LONG).show()
                                val adminlogin = Intent(this, RegisterShop::class.java)
                                startActivity(adminlogin)
                            }else {
                                // login de los usuarios
                                if (emailUser == etEmail.text.toString() && passwordUser == etPassword.text.toString()) {
                                    sesion = emailUser
                                    Toast.makeText(this, "" + sesion.toString(), Toast.LENGTH_LONG).show()
                                    val userlogin = Intent(this, LoginUser::class.java)
                                    startActivity(userlogin)

                                } else {
                                    var correct = 0
                                    //se supone aqui se implementa el login de la tienda
                                    DB.collection("Tienda").get().addOnSuccessListener { vendedores ->
                                            for (vendedor in vendedores) {
                                                var email2 = vendedor.get("email").toString()
                                                var password2 = vendedor.get("password").toString()

                                                if (email2 == etEmail.text.toString() && password2 == etPassword.text.toString()) {
                                                    emailStore = email2
                                                    passwordStore = password2
                                                }
                                            }
                                        if (emailStore == etEmail.text.toString() && passwordStore == etPassword.text.toString()) {
                                            sesion = emailStore
                                            Toast.makeText(this, "" + sesion.toString(), Toast.LENGTH_LONG).show()
                                            val storelogin = Intent(this, LoginStore::class.java)
                                            startActivity(storelogin)
                                            Toast.makeText(this, "Bienvenido", Toast.LENGTH_LONG).show()
                                        }else{
                                            Toast.makeText(this, "Usuario o Contraseña Incorrecta", Toast.LENGTH_LONG).show()

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

