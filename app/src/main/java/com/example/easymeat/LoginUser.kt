package com.example.easymeat

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easymeat.databinding.ActivityLoginUserBinding
import com.example.easymeat.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class LoginUser : AppCompatActivity() {

    private lateinit var storeAdapter: StoreAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var newArrayList: MutableList<Tienda>

    private lateinit var binding: ActivityLoginUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newArrayList= mutableListOf()

        val DB = FirebaseFirestore.getInstance()
        DB.collection("Tienda").get().addOnSuccessListener { resultQuery ->
            val listData = mutableListOf<Tienda>()
            resultQuery.forEach { document ->
                listData.add(
                    Tienda(
                        document.getString("name")!!,
                        document.getString("address")!!,
                        document.getString("email")!!,
                        document.getString("phone")!!
                    )
                )
                //tvName.text = listData.get(0).name.toString()
                newArrayList.add(Tienda(
                    document.getString("name")!!,
                    document.getString("address")!!,
                    document.getString("email")!!,
                    document.getString("phone")!!
                )
                )
            }
            Toast.makeText(
                this,
                ""+ newArrayList.get(0).name,
                Toast.LENGTH_LONG
            ).show()

        }


        storeAdapter = StoreAdapter(newArrayList)
        linearLayoutManager = LinearLayoutManager(this)

        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = storeAdapter
        }

    }

}
