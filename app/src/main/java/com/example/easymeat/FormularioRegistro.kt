package com.example.easymeat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore


class FormularioRegistro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_registro)

        val DB = FirebaseFirestore.getInstance()

        val etnames = findViewById<EditText>(R.id.etnames)
        val etlastnames = findViewById<EditText>(R.id.etlastnames)
        val etId = findViewById<EditText>(R.id.etId)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etCity = findViewById<EditText>(R.id.etCity)
        val etBarrio = findViewById<EditText>(R.id.etBarrio)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etPassword2 = findViewById<EditText>(R.id.etPassword2)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {

            DB.collection("Usuario").document(etEmail.text.toString()).set(
                hashMapOf(
                    "names" to etnames.text.toString(),
                    "lastnames" to etlastnames.text.toString(),
                    "id" to etId.text.toString(),
                    "email" to etEmail.text.toString(),
                    "phone" to etPhone.text.toString(),
                    "city" to etCity.text.toString(),
                    "barrio" to etBarrio.text.toString(),
                    "address" to etAddress.text.toString(),
                    "password" to etPassword.text.toString()
                )
            )
            Toast.makeText(this, "Usuario agregado: " + etEmail.text.toString(), Toast.LENGTH_LONG).show()
            val inicio = Intent(this, MainActivity::class.java)
            startActivity(inicio)

        }


    }

}