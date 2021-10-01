package com.example.easymeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class AddProducto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_producto)

        var DB = FirebaseFirestore.getInstance()

        val etName = findViewById<EditText>(R.id.etName)
        val etType = findViewById<EditText>(R.id.etType)
        val etCost = findViewById<EditText>(R.id.etCost)
        val btnAddproduct = findViewById<Button>(R.id.btnAddproduct)
        val btnVerProductos = findViewById<Button>(R.id.btnVerProductos)

        btnAddproduct.setOnClickListener {

            DB.collection("Producto").add(
                hashMapOf(
                    "type" to etType.text.toString(),
                    "name" to etName.text.toString(),
                    "cost" to etCost.text.toString()
                )
            )
            Toast.makeText(this, "Producto Agregado", Toast.LENGTH_LONG).show()

            val addProduct = Intent(this, AddProducto::class.java)
            startActivity(addProduct)
        }


    }
}