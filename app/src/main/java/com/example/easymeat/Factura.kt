package com.example.easymeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Factura : AppCompatActivity() {
    var tabla_productos: TableLayout?=null
    var tvEstado: TextView?=null
    var tvFecha: TextView?=null
    var tvTotal: TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factura)
        tabla_productos = findViewById(R.id.tabla_factura)
        inicializar()
        llenartabla()

    }
    private fun inicializar(){
        tvEstado = findViewById(R.id.tvEstado)
        tvFecha = findViewById(R.id.tvFecha_Factura)
        tvTotal = findViewById(R.id.tvTotal_Factura)


        val storage = applicationContext.getSharedPreferences("MyFacturaDetails", 0)

        val da1 = storage.getString("date","DEFAULT")
        val estado = storage.getString("state","DEFAULT")
        val venta = storage.getString("venta","not")
        if(venta.equals("yes")){
            val idF = storage.getString("id","DEFAULT")
            var btnAccept = findViewById(R.id.btnAceptar) as Button
            btnAccept.visibility = View.VISIBLE
            btnAccept.setOnClickListener {
                var DB = FirebaseFirestore.getInstance()
                DB.collection("Pedido").document(idF.toString()).update("state","Aceptado").addOnSuccessListener {
                    tvEstado!!.setText("Estado: Aceptado")
                }

            }
            var btnCancel = findViewById(R.id.btnCancelar) as Button
            btnCancel.visibility = View.VISIBLE
            btnCancel.setOnClickListener {
                var DB = FirebaseFirestore.getInstance()
                DB.collection("Pedido").document(idF.toString()).update("state","Cancelado")
                tvEstado!!.setText("Estado: Cancelado")
            }
        }else{
            var tvAviso = findViewById(R.id.tvAviso) as TextView
            tvAviso.visibility = View.VISIBLE
        }


        tvEstado!!.setText("Estado: "+estado)
        tvFecha!!.setText("Fecha: "+da1)
    }
    private fun llenartabla(){
        var total = 0.0
        //Crear la colección y obtener los datos
        Log.d("INICIO","llenarTablaCarro")
        val storage = applicationContext.getSharedPreferences("MyFactura", 0)
        Log.d("STORAGE",storage.toString())
        val row1 = LayoutInflater.from(this).inflate(R.layout.item_table_layout_factura_head, null, false)
        val tvName1 = row1.findViewById(R.id.tvNombre_header) as TextView
        val tvPrecio1 = row1.findViewById(R.id.tvPrecioUni_header) as TextView
        val tvCantidad1 = row1.findViewById(R.id.tvCantidad_header) as TextView
        val tvPrecioT1 = row1.findViewById(R.id.tvPrecioT_header) as TextView
        val tvTiendaT1 = row1.findViewById(R.id.tvTienda_header) as TextView
        tvName1.setText("Nombre")
        tvPrecio1.setText("Precio")
        tvCantidad1.setText("Cant")
        tvPrecioT1.setText("Total")
        tvTiendaT1.setText("Tienda")
        tabla_productos?.addView(row1)
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
                val tvTiendaT = row.findViewById(R.id.tvTienda_Fac) as TextView
                total+=(pro.cost*pro.cantidad)
                // Agregar datos de la consulta a los campos de la fila
                tvName.setText(pro.name)
                tvTiendaT.setText(pro.tienda)
                tvPrecio.setText("$ "+pro.cost.toString())
                tvCantidad.setText(pro.cantidad.toString())
                tvPrecioT.setText((pro.cost*pro.cantidad).toString())

                tabla_productos?.addView(row)
            }catch (e: Exception){
                Log.d("ERROR",e.toString())
            }
        }
        tvTotal?.setText("Total: "+total.toString())
    }
}