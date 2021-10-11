package com.example.easymeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PedidosCliente : AppCompatActivity() {
    var DB = FirebaseFirestore.getInstance()
    var tabla_pedidos: TableLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos_tienda)
        tabla_pedidos = findViewById(R.id.tabla_pedidos)
        llenarTabla()
    }

    private fun llenarTabla(){
        val storage = applicationContext.getSharedPreferences("User", 0)
        val idCliente = storage.getString("user","DEFAULT")
        var gson = Gson();
        DB.collection("Pedido").whereEqualTo("user",idCliente).whereIn("state",
            listOf("En Proceso","Aceptado")).get().addOnSuccessListener {
            documents ->
            for(document in documents){
                //Log.d("ARRAY","${document.get("products")!!::class.qualifiedName}")
                var productosL:ArrayList<String> =document.get("products") as ArrayList<String>
                var productosM = arrayListOf<ProductoCarrito>()
                var total = 0.0
                for ( i in 0..(productosL?.size-1 ?: 0)){
                    try {
                        var pro = gson.fromJson<ProductoCarrito>(
                            productosL?.get(i).toString(),
                            ProductoCarrito::class.java
                        )
                        total += pro.cost*pro.cantidad
                        DB.collection("Tienda_Producto").document(pro.id).get()
                            .addOnSuccessListener { document ->
                                productosM.add(pro)

                            }.addOnCompleteListener {
                                if (productosM.size > 0 && i == productosL.size - 1) {

                                    val row = LayoutInflater.from(this)
                                        .inflate(
                                            R.layout.item_table_layout_pedidos_tienda,
                                            null,
                                            false
                                        )
                                    val tvFecha = row.findViewById(R.id.tvFecha_Pedido) as TextView
                                    val tvTotal = row.findViewById(R.id.tvTotal_Pedido) as TextView
                                    val tvAddres = row.findViewById(R.id.tvAddress_Pedido) as TextView
                                    val tvCliente = row.findViewById(R.id.tvCliente_Pedido) as TextView
                                    val tvEstado = row.findViewById(R.id.tvEstado_Pedido) as TextView
                                    val btnVer = row.findViewById(R.id.btnVer_Pedido) as Button
                                    if (document.getString("state").equals("Aceptado")) {
                                        val btnCnf = row.findViewById(R.id.btnConf) as Button
                                        btnCnf.visibility = View.VISIBLE
                                        btnCnf.setOnClickListener {
                                            var DB = FirebaseFirestore.getInstance()
                                            DB.collection("Pedido").document(document.id)
                                                .update("state", "Completado")
                                            tvEstado!!.setText("Completado")
                                            btnCnf.visibility = View.INVISIBLE
                                        }
                                    }

                                    DB.collection("Usuario").document("${document.get("user")}")
                                        .get().addOnSuccessListener { document2 ->
                                        tvAddres.setText(document2.get("address").toString()+" barrio "+document2.get("barrio").toString())
                                    }
                                    DB.collection("Tienda").document("${document.get("shop")}")
                                        .get().addOnSuccessListener { document2 ->
                                            tvCliente.setText(
                                                document2.get("name").toString()
                                            )
                                        }

                                    var fecha: Timestamp = document.get("date") as Timestamp
                                    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
                                    var fechaStr = sdf.format(fecha.toDate())
                                    tvFecha.setText(fechaStr)
                                    tvTotal.setText(total.toString())
                                    tvEstado.setText(document.getString("state"))

                                    btnVer.setOnClickListener {
                                        var listaProductos = arrayListOf<String>()
                                        val storage = applicationContext.getSharedPreferences("MyCar", 0)
                                        val storage2 = applicationContext.getSharedPreferences("MyFactura",0)
                                        val storage3 = applicationContext.getSharedPreferences("User", 0)
                                        storage2.edit().clear().apply()
                                        productosM.forEach {
                                            try {
                                                var prod = gson.toJson(it)
                                                listaProductos.add(prod)
                                                storage2.edit().putString(it.id,prod.toString()).apply()
                                            }catch (e:Exception){
                                                Log.d("ERROR",e.toString())
                                            }

                                        }

                                        val storageDetails = applicationContext.getSharedPreferences("MyFacturaDetails", 0)
                                        storageDetails.edit().putString("date",fechaStr).putString("id",document.id).putString("state",document.get("state").toString()).apply()

                                        val factura = Intent(this, Factura::class.java)
                                        startActivity(factura)
                                    }
                                    tabla_pedidos?.addView(row)
                                }
                            }
                    }catch (e: Exception) {
                        Log.d("Error", e.toString())
                    }
                }

            }
        }
    }
}