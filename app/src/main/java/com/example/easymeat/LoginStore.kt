package com.example.easymeat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.io.BufferedReader

class LoginStore : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_store)
        /*<Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/button_text"
        ... />*/

        val btnAddProductStore = findViewById<Button>(R.id.add_producto_store)
        val btnEditTiendaProduct = findViewById<Button>(R.id.btnEditTiendaProduct)
        val btnPedidos = findViewById<Button>(R.id.btnPedidos)
        val btnPedidosInac = findViewById<Button>(R.id.btnPedidosInac)
        btnAddProductStore.setOnClickListener {
            val addProductsStore = Intent(this, AddProductsStore::class.java)
            startActivity(addProductsStore)
        }
        btnEditTiendaProduct.setOnClickListener {
            val editProduct = Intent(this, EditTiendaProduct::class.java)
            startActivity(editProduct)
        }
        btnPedidos.setOnClickListener {
            val verPedidos = Intent(this, PedidosTienda::class.java)
            startActivity(verPedidos)
        }
        btnPedidosInac.setOnClickListener {
            val verPedidosIna = Intent(this, PedidosTiendaInact::class.java)
            startActivity(verPedidosIna)
        }
    }
}