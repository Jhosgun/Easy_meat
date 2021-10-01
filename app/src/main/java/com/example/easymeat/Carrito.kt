package com.example.easymeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import com.google.gson.Gson
import java.lang.Exception

class Carrito : AppCompatActivity() {
    var tabla_productos: TableLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)
        tabla_productos = findViewById(R.id.tabla_productos_carrito)

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
                val tvTienda = row.findViewById(R.id.tvTienda) as TextView
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
                tvPrecio.setText(pro.cost.toString())
                tvTienda.setText(pro.tienda.toString())
                tvTipo.setText("")
                tvCantidad.setText(pro.cantidad.toString())
                tabla_productos?.addView(row)
            }catch (e:Exception){
                Log.d("ERROR",e.toString())
            }
        }
    }
}