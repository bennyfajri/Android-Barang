package com.example.penjualan

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class BarangAdapter (private val context : Context, private val arrayList : ArrayList<ModelBarang>): RecyclerView.Adapter<BarangAdapter.ViewHolder>(){
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        var nama : TextView =  view.findViewById(R.id.tv_nama)
        var hterjual : TextView =  view.findViewById(R.id.tv_terjual)
        var cvBarang : CardView = view.findViewById(R.id.card_barang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false))
    }

    override fun onBindViewHolder(holder: BarangAdapter.ViewHolder, position: Int) {
        val barang = arrayList[position]
        holder.nama.text = barang.nama
        holder.hterjual.text = "terjual : ${barang.terjual.toString()}"
        holder.cvBarang.setOnClickListener {
            val intent = Intent(context, ManageBarang::class.java)
            intent.putExtra("editmode", "1")
            intent.putExtra("id", barang.id)
            intent.putExtra("nama", barang.nama)
            intent.putExtra("modal", barang.hargaModal)
            intent.putExtra("jual", barang.hargaJual)
            intent.putExtra("jml", barang.jumlahBarang)
            intent.putExtra("terjual", barang.terjual)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}