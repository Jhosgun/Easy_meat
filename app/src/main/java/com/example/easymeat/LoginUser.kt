package com.example.easymeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class LoginUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        val DB = FirebaseFirestore.getInstance()
        val mutableData = MutableLiveData<MutableList<Tienda>>()
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
            }
            mutableData.value = listData
        }


    }
}
