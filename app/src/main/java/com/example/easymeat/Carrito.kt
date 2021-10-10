package com.example.easymeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Carrito : AppCompatActivity() {
    var tabla_productos: TableLayout?=null
    var btn_Carrito:Button?=null
    var DB = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)
        tabla_productos = findViewById(R.id.tabla_productos_carrito)
        btn_Carrito = findViewById(R.id.btn_Comprar)
        var listaProductos = arrayListOf<String>()
        var gson = Gson();
        btn_Carrito!!.setOnClickListener {
            val storage = applicationContext.getSharedPreferences("MyCar", 0)
            val storage2 = applicationContext.getSharedPreferences("MyFactura",0)
            val storage3 = applicationContext.getSharedPreferences("User", 0)
            val usuario = storage3.getString("user","DEFAULT")
            val yaRevisado = arrayListOf<String>()
            storage2.edit().clear().apply()
            storage.all.forEach{
                try {
                    Log.d("ALGO",yaRevisado.toString())
                    var arrayCurrent = arrayListOf<String>()
                    var pro = gson.fromJson<ProductoCarrito>(it.value.toString(), ProductoCarrito::class.java)
                    if(pro.tienda !in yaRevisado) {

                        yaRevisado.add(pro.tienda)
                        storage.all.forEach { it2 ->
                            try {
                                var pro2 = gson.fromJson<ProductoCarrito>(
                                    it2.value.toString(),
                                    ProductoCarrito::class.java
                                )
                                if (pro2.tienda.equals(pro.tienda)) {
                                    arrayCurrent.add(it2.value.toString())
                                }
                            } catch (e: Exception) {
                                Log.d("ERROR", e.toString())
                            }
                        }

                        var id_Tienda = ""
                        DB.collection("Tienda_Producto").document(pro.id).get()
                            .addOnSuccessListener { document ->
                                id_Tienda = document.get("idStore").toString()
                            }.addOnCompleteListener {
                                Log.d("Agregando", arrayCurrent.toString())
                                val docData = hashMapOf(
                                    "state" to "En Proceso",
                                    "date" to Timestamp(Date()),
                                    "products" to arrayCurrent,
                                    "user" to usuario,
                                    "shop" to id_Tienda
                                )

                                DB.collection("Pedido").add(docData)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d(
                                            "SUCCESS",
                                            "DocumentSnapshot written with ID: ${documentReference.id}"
                                        )
                                    }.addOnFailureListener { e ->
                                        Log.w("ERROR", "Error adding document", e)
                                    }
                            }
                    }
                    //var pro = gson.fromJson<ProductoCarrito>(it.value.toString(), ProductoCarrito::class.java)
                    storage2.edit().putString(it.key,it.value.toString()).apply()
                }catch (e:Exception){
                    Log.d("ERROR",e.toString())
                }

            }

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
            val currentDate = sdf.format(Date())
            val storageDetails = applicationContext.getSharedPreferences("MyFacturaDetails", 0)
            storageDetails.edit().clear().apply()
            storageDetails.edit().putString("date",currentDate).putString("state","En Proceso").apply()

            val factura = Intent(this, Factura::class.java)
            startActivity(factura)

        }
        llenartabla()
    }

    private fun llenartabla(){
        //Crear la colección y obtener los datos
        Log.d("INICIO","llenarTablaCarro")
        val storage = applicationContext.getSharedPreferences("MyCar", 0)
        Log.d("STORAGE",storage.toString())
        storage.all.forEach{
            try {
                var gson = Gson();
                Log.d("Element:", it.value.toString())
                var pro = gson.fromJson<ProductoCarrito>(it.value.toString(), ProductoCarrito::class.java)
                val row = LayoutInflater.from(this).inflate(R.layout.item_table_layout_carrito, null, false)
                // Obtener los campos de la fila
                val tvName = row.findViewById(R.id.tvName) as TextView
                val tvPrecio = row.findViewById(R.id.tvPrecio) as TextView
                val tvTienda = row.findViewById(R.id.tvCliente_Pedido) as TextView
                val tvTipo = row.findViewById(R.id.tvTipo) as TextView
                val tvCantidad = row.findViewById(R.id.tvCantidad) as TextView
                var botonMore = row.findViewById(R.id.buttonMore) as Button
                var botonMinus = row.findViewById(R.id.buttonMinus) as Button
                botonMore.setOnClickListener{
                    pro.cantidad+=1
                    val prdJson = gson.toJson(pro);
                    storage.edit().putString(pro.id, prdJson).apply()
                    tvCantidad.setText(pro.cantidad.toString())
                }
                botonMinus.setOnClickListener{
                    pro.cantidad-=1
                    if(pro.cantidad<=0){
                        storage.edit().remove(pro.id).apply()
                        tabla_productos?.removeView(row)
                    }else {
                        val prdJson = gson.toJson(pro);
                        storage.edit().putString(pro.id, prdJson).apply()
                        tvCantidad.setText(pro.cantidad.toString())
                    }

                }
                // Agregar datos de la consulta a los campos de la fila
                tvName.setText(pro.name)
                tvPrecio.setText("$ "+pro.cost.toString())
                tvTienda.setText("Tienda: "+pro.tienda.toString())
                tvTipo.setText(pro.type.toString())
                tvCantidad.setText(pro.cantidad.toString())
                tabla_productos?.addView(row)
            }catch (e:Exception){
                Log.d("ERROR",e.toString())
            }
        }
    }
}