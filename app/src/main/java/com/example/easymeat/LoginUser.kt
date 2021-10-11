package com.example.easymeat

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easymeat.databinding.ActivityLoginUserBinding
import com.example.easymeat.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.lang.Exception

class LoginUser : AppCompatActivity() {

    var DB = FirebaseFirestore.getInstance()

    // Objeto de la clase TableLayout
    var tabla_tiendas: TableLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        tabla_tiendas = findViewById(R.id.tabla_tiendas)

        llenar_tabla_tiendas()
        var btnPedidos = findViewById(R.id.btnVerPedidosCli) as Button
        btnPedidos.setOnClickListener {
            val verPedidos = Intent(this, PedidosCliente::class.java)
            startActivity(verPedidos)
        }
        val btnVisitar = findViewById<Button>(R.id.btnVisitar)
        val id = findViewById<TextView>(R.id.tvId)


    }

        //Método que realiza el llenado de la tabla con datos traidos de Firebase
        private fun llenar_tabla_tiendas() {
        //Crear la colección y obtener los datos
        DB.collection("Tienda").get().addOnSuccessListener {
            //Recorrer la estructura de datos traidos con una función lambda
                documents ->
            for (document in documents) {
                // Crear una nueva fila a partir del modelo "item_table_layout"
                val row = LayoutInflater.from(this).inflate(R.layout.recycler, null, false)
                // Obtener los campos de la fila
                val tvNameCV1 = row.findViewById(R.id.tvNameCV1) as TextView
                val tvId = row.findViewById<TextView>(R.id.tvId)
                val btnVisitar = row.findViewById(R.id.btnVisitar) as Button
                // Agregar datos de la consulta a los campos de la fila
                tvNameCV1.setText("${document.get("name").toString()}")
                val id = "${document.id}"
                tvId.setText(id)
                btnVisitar.setOnClickListener{
                    val storage = applicationContext.getSharedPreferences("MyCar", 0)
                    storage.edit().putString("TiendaView","${document.id}").apply()
                    idTienda = document.id
                    val verProductos = Intent(this, VerProductos::class.java)
                    startActivity(verProductos)
                }

                tabla_tiendas?.addView(row)
            }
        }
    }


}
