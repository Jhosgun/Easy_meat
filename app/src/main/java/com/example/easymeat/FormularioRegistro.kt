package com.example.easymeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class FormularioRegistro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_registro)

        Registrarse()
    }

    private fun Registrarse(){

        val bntRegistro = findViewById<Button>(R.id.btnRegistro)
        val etnames = findViewById<EditText>(R.id.etnames)


        bntRegistro.setOnClickListener {

        }
    }
}