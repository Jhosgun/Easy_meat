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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.lang.Exception

// Permite la visualización de productos en una tabla
class VerProductos : AppCompatActivity() {
    var DB = FirebaseFirestore.getInstance()
    // Objeto de la clase TableLayout
    var tabla_productos:TableLayout?=null
    var botonCar:Button ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_productos)
        tabla_productos = findViewById(R.id.tabla_productos)
        botonCar = findViewById(R.id.botonCar) as Button
        botonCar!!.setOnClickListener{
            val carrito = Intent(this, Carrito::class.java)
            startActivity(carrito)
        }
        llenartabla()
    }

    //Método que realiza el llenado de la tabla con datos traidos de Firebase
    private fun llenartabla(){
        //Crear la colección y obtener los datos
        DB.collection("Producto").get().addOnSuccessListener {
            //Recorrer la estructura de datos traidos con una función lambda
            documents ->
            for(document in documents){
                // Crear una nueva fila a partir del modelo "item_table_layout"
                val row = LayoutInflater.from(this).inflate(R.layout.item_table_layout, null,false)
                // Obtener los campos de la fila
                val tvName = row.findViewById(R.id.tvName) as TextView
                val tvPrecio = row.findViewById(R.id.tvPrecio) as TextView
                val tvTienda = row.findViewById(R.id.tvTienda) as TextView
                val tvTipo = row.findViewById(R.id.tvTipo) as TextView
                val botonAdd = row.findViewById(R.id.buttonAdd) as Button
                // Agregar datos de la consulta a los campos de la fila
                tvName.setText("${document.get("name").toString()}")
                tvPrecio.setText("\$: ${document["cost"].toString()}")
                tvTienda.setText("${document["shop"].toString()}")
                tvTipo.setText("${document["type"].toString()}")
                val id = "${document.id}"
                var pro = ProductoCarrito(document.get("name").toString(),id,"", document.get("cost").toString().toDouble(),document["shop"].toString(),1)
                //Agregar clickListener al botón
                botonAdd.setOnClickListener{
                    try {
                        var gson = Gson();
                        val storage = applicationContext.getSharedPreferences("MyCar", 0)
                        if(storage.contains(id)) {
                            val prdJson2 = gson.toJson(pro);
                            val pros = storage.getString(id,prdJson2)
                            pro = gson.fromJson(pros, ProductoCarrito::class.java)
                            pro.cantidad += 1
                        }else{
                            pro.cantidad = 1
                        }
                        val prdJson = gson.toJson(pro);

                        storage.edit().putString(id, prdJson).apply();
                        Toast.makeText(this, "Producto Agregado", Toast.LENGTH_LONG).show()
                    }catch (e: Exception){
                        Log.d("Error",e.toString())
                    }
                }
                tabla_productos?.addView(row)
            }
        }
    }
}