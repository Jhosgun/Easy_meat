package com.example.easymeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.lang.Exception

class EditTiendaProduct : AppCompatActivity() {

    var DB = FirebaseFirestore.getInstance()
    // Objeto de la clase TableLayout
    var tabla_productos: TableLayout?=null
    lateinit var idStore: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tienda_product)
        tabla_productos = findViewById(R.id.tabla_productos)
        llenartabla()
    }

    //Método que realiza el llenado de la tabla con datos traidos de Firebase
    private fun llenartabla(){
        //encontrar la información de la tienda
        DB.collection("Tienda").get().addOnSuccessListener { tiendas ->
            for (tienda in tiendas) {
                if (sesion == tienda.get("email").toString()) {
                    idStore = tienda.id
                }
            }
        }
        //buscamos los productos asociados a esas tiendas
        DB.collection("Tienda_Producto").get().addOnSuccessListener {
                tiendas_productos ->
            for (tienda_producto in tiendas_productos){
                //Crear la colección y obtener los datos
                DB.collection("Producto").get().addOnSuccessListener {
                        documents ->
                    for (document in documents){
                        if(tienda_producto.get("idStore").toString()== idStore &&
                            tienda_producto.get("idProduct").toString()==document.id){
                            val row = LayoutInflater.from(this).inflate(R.layout.item_table_layout_producto_tienda, null, false)
                            // Obtener los campos de la fila
                            val tvName = row.findViewById(R.id.tvName_PT) as TextView
                            //val tvPrecio = row.findViewById(R.id.tvPrecio) as TextView
                            //val tvTienda = row.findViewById(R.id.tvCliente_Pedido) as TextView
                            val tvTipo = row.findViewById(R.id.tvTipo_PT) as TextView
                            val botonAdd = row.findViewById(R.id.buttonAdd_PT) as Button
                            val botonElim = row.findViewById(R.id.buttonElim_PT) as Button
                            val etPrecio = row.findViewById(R.id.eTPrecio) as EditText
                            // Agregar datos de la consulta a los campos de la fila
                            tvName.setText("${document.get("name").toString()}")
                            etPrecio.setText(tienda_producto["cost"].toString())
                            //tvPrecio.setText("\$: ${document["cost"].toString()}")
                            //tvTienda.setText("${document["shop"].toString()}")
                            tvTipo.setText("${document["type"].toString()}")
                            //Agregar clickListener al botón
                            botonElim.visibility = View.VISIBLE
                            botonAdd.setOnClickListener {
                                try {
                                    if(etPrecio.text != null) {
                                        DB.collection("Tienda_Producto")
                                            .document(tienda_producto.id)
                                            .update("cost", etPrecio.text.toString().toDouble())
                                    }
                                } catch (e: Exception) {
                                    Log.d("Error", e.toString())
                                }
                            }
                            botonElim.setOnClickListener {
                                DB.collection("Tienda_Producto").document(tienda_producto.id).delete()
                            }
                            tabla_productos?.addView(row)
                        }
                    }
                }
            }
        }
    }
}