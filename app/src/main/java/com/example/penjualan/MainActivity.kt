package com.example.penjualan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.snackbar.Snackbar
import com.jacksonandroidnetworking.JacksonParserFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    var arrayList = ArrayList<ModelBarang>()
    private lateinit var rvBarang: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidNetworking.setParserFactory(JacksonParserFactory())

        rvBarang = findViewById(R.id.rv_barang)
        rvBarang.setHasFixedSize(true)
        showRecyclerView()

        fab_tambah_barang.setOnClickListener {
            val intent = Intent(applicationContext, ManageBarang::class.java)
            intent.putExtra("judul", "Tambah Produk")
            startActivity(intent)
        }

        swipe_layout.setOnRefreshListener {
            Handler().postDelayed(Runnable {
                swipe_layout.isRefreshing = false
                loadBarang()
                showRecyclerView()
            }, 1000)
        }

        val actionBar = supportActionBar
        actionBar?.title = ""



    }

    override fun onResume() {
        super.onResume()
        loadBarang()
    }

    private fun loadBarang() {
        AndroidNetworking.get(ApiBarang.READ)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    arrayList.clear()
                    val jsonArray = response?.optJSONArray("result")

                    if (jsonArray?.length() == 0) {
                        Toast.makeText(
                            applicationContext,
                            "Data Kosong, tambah baru",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    for (i in 0 until jsonArray?.length()!!) {
                        val jsonObject = jsonArray?.optJSONObject(i)

                        arrayList.add(
                            ModelBarang(
                                jsonObject.getInt("id"),
                                jsonObject.getString("nama"),
                                jsonObject.getInt("hargaModal"),
                                jsonObject.getInt("hargaJual"),
                                jsonObject.getInt("jml"),
                                jsonObject.getInt("terjual")
                            )
                        )

                        if (jsonArray?.length() - 1 == i) {
                            val adapter = BarangAdapter(applicationContext, arrayList)
                            adapter.notifyDataSetChanged()
                            rvBarang.adapter = adapter
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.e("ONERROR", anError?.errorDetail?.toString()!!)
                    Toast.makeText(applicationContext, "Connection failed", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    private fun showRecyclerView() {
        rvBarang.layoutManager = LinearLayoutManager(this)
        val barangAdapter = BarangAdapter(this, arrayList)
        rvBarang.adapter = barangAdapter
    }
}