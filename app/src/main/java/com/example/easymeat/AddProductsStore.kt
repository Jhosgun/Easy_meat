package com.example.easymeat


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.lang.Exception

class AddProductsStore : AppCompatActivity() {

    var DB = FirebaseFirestore.getInstance()

    // Objeto de la clase TableLayout
    var tabla_productos: TableLayout? = null
    lateinit var idStore: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_products_store)
        tabla_productos = findViewById(R.id.tabla_productos)
        llenartabla()
    }

    //Método que realiza el llenado de la tabla con datos traidos de Firebase
    private fun llenartabla() {
        //encontrar la información de la tienda
        DB.collection("Tienda").get().addOnSuccessListener { tiendas ->
            for (tienda in tiendas) {
                if (sesion == tienda.get("email").toString()) {
                    idStore = tienda.id
                }
            }

            //Crear la colección y obtener los datos
            DB.collection("Producto").get().addOnSuccessListener {
                //Recorrer la estructura de datos traidos con una función lambda
                    documents ->
                for (document in documents) {
                    // Crear una nueva fila a partir del modelo "item_table_layout"
                    val row = LayoutInflater.from(this).inflate(R.layout.item_table_layout, null, false)
                    // Obtener los campos de la fila
                    val tvName = row.findViewById(R.id.tvName) as TextView
                    val tvPrecio = row.findViewById(R.id.tvPrecio) as TextView
                    val tvTipo = row.findViewById(R.id.tvTipo) as TextView
                    val botonAdd = row.findViewById(R.id.buttonAdd) as Button
                    // Agregar datos de la consulta a los campos de la fila
                    tvName.setText("${document.get("name").toString()}")
                    tvPrecio.setText("\$: ${document["cost"].toString()}")
                    tvTipo.setText("${document["type"].toString()}")
                    val id = "${document.id}"
                    //Agregar clickListener al botón
                    botonAdd.setOnClickListener {
                        DB.collection("Tienda_Producto").add(
                            hashMapOf(
                                "idStore" to idStore,
                                "idProduct" to document.id,
                                "cost" to 0.0
                            )
                        )
                        Toast.makeText(this, "Producto agregado: ", Toast.LENGTH_LONG).show()
                    }
                    tabla_productos?.addView(row)
                }
            }

        }
    }
}