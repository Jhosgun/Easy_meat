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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnRegistro = findViewById<Button>(R.id.btnRegistro)
        btnRegistro.setOnClickListener {
            val registro = Intent(this, FormularioRegistro::class.java)
            startActivity(registro)
        }

        val btnIngresar = findViewById<Button>(R.id.btnIngresar)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnIngresar.setOnClickListener {

            val addProduct = Intent(this, AddProducto::class.java)
            startActivity(addProduct)

            /*if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty())
            {
                val user = FirebaseDatabase.getInstance().getReference()

                val password = user.child("Usuario").child(etEmail.text.toString()).child("password").get().toString()


                Toast.makeText(this, password, Toast.LENGTH_LONG).show()

                //.getReference().child("unknown").child("Shrubs");
                if(password==etPassword)
                {
                 val login = Intent(this, Login::class.java)
                    startActivity(login)
                }*/


        }
    }
}

