package com.example.easymeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import com.google.gson.Gson
import java.lang.Exception

class Factura : AppCompatActivity() {
    var tabla_productos: TableLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factura)
        tabla_productos = findViewById(R.id.tabla_factura)
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
                val row = LayoutInflater.from(this).inflate(R.layout.item_table_layout_factura, null, false)
                // Obtener los campos de la fila
                val tvName = row.findViewById(R.id.tvNombre_Factura) as TextView
                val tvPrecio = row.findViewById(R.id.tvPrecioUni_Factura) as TextView
                val tvCantidad = row.findViewById(R.id.tvCantidad_Factura) as TextView
                val tvPrecioT = row.findViewById(R.id.tvPrecioT_Factura) as TextView
                
                // Agregar datos de la consulta a los campos de la fila
                tvName.setText(pro.name)
                tvPrecio.setText("$ "+pro.cost.toString())
                tvCantidad.setText(pro.cantidad.toString())
                tvPrecioT.setText((pro.cost*pro.cantidad).toString())
                tabla_productos?.addView(row)
            }catch (e: Exception){
                Log.d("ERROR",e.toString())
            }
        }
    }
}