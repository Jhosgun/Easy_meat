package com.example.easymeat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.easymeat.databinding.RecyclerBinding


class StoreAdapter(private val tiendas: MutableList<Tienda>): RecyclerView.Adapter<StoreAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.recycler, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreAdapter.ViewHolder, position: Int) {

        val tienda= tiendas[position]

        with(holder) {
            binding.tvNameCV.text = tienda.name
        }

    }

    override fun getItemCount(): Int {

        return tiendas.size

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = RecyclerBinding.bind(view)
    }

}