package com.example.penjualan

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_manage_barang.*
import org.json.JSONObject


class ManageBarang : AppCompatActivity() {
    lateinit var i: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_barang)

        i = intent
        getIntentValue()
        if(i.hasExtra("editmode")){
            if(i.getStringExtra("editmode").equals("1")){
                onEditMode()
            }
        }
        if(i.hasExtra("judul")){
            tv_judul.text = i.getStringExtra("judul")
        }

        btn_tambah.setOnClickListener {
            createBarang()
        }

        btn_update.setOnClickListener {
            updateBarang()
        }

        btn_delete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Hapus data ini?")
                .setPositiveButton("Hapus", DialogInterface.OnClickListener { dialogInterface, i ->
                    deleteBarang()
                })
                .setNegativeButton("Batal", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
                .show()
        }

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = ""
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getIntentValue() {

    }

    private fun onEditMode() {
        tv_judul.setText("Edit Data Barang")
        val jual = i.getIntExtra("jual", 0)
        val modal = i.getIntExtra("modal",0)
        val terjual = i.getIntExtra("terjual", 0)
        val nama = i.getStringExtra("nama")
        val jumlah = i.getIntExtra("jml",0)
        val id = i.getIntExtra("id", 0).toString()

        layoutEtId.visibility = View.VISIBLE
        etId.visibility = View.VISIBLE
        etId.setText(id)
        etId.isEnabled = false

        layoutKeuntungan.visibility = View.VISIBLE
        etKeuntungan.visibility = View.VISIBLE
        val untung = ( jual - modal ) * terjual
        etKeuntungan.setText(untung.toString())
        etKeuntungan.isEnabled = false

        etNama.setText(nama)
        etJml.setText(jumlah.toString())
        etTerjual.setText(terjual.toString())
        etModal.setText(modal.toString())
        etJual.setText(jual.toString())
        etTerjual.setText(terjual.toString())

        btn_tambah.visibility = View.GONE
        btn_update.visibility = View.VISIBLE
        btn_delete.visibility = View.VISIBLE

    }

    private fun createBarang() {
        progress_bar.visibility = View.VISIBLE

        AndroidNetworking.post(ApiBarang.CREATE)
            .addBodyParameter("nama", etNama.text.toString())
            .addBodyParameter("modal", etModal.text.toString())
            .addBodyParameter("jual", etJual.text.toString())
            .addBodyParameter("jml", etJml.text.toString())
            .addBodyParameter("terjual", etTerjual.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_LONG).show()

                    if (response?.getString("message")?.contains("successfull")!!){
                        this@ManageBarang.finish()
                    }
                }

                override fun onError(anError: ANError?) {
                    progress_bar.visibility = View.GONE
                    Log.d("ONERROR", anError?.errorDetail.toString()!!)
                    Toast.makeText(applicationContext, "Connection error", Toast.LENGTH_LONG ).show()
                }

            })
    }

    private fun updateBarang() {
        progress_bar.visibility = View.VISIBLE

        AndroidNetworking.post(ApiBarang.UPDATE)
            .addBodyParameter("id", etId.text.toString())
            .addBodyParameter("nama", etNama.text.toString())
            .addBodyParameter("modal", etModal.text.toString())
            .addBodyParameter("jual", etJual.text.toString())
            .addBodyParameter("jml", etJml.text.toString())
            .addBodyParameter("terjual", etTerjual.text.toString())
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_LONG).show()

                    if (response?.getString("message")?.contains("successfull")!!){
                        this@ManageBarang.finish()
                    }
                }

                override fun onError(anError: ANError?) {
                    progress_bar.visibility = View.GONE
                    Log.d("ONERROR", anError?.errorDetail.toString()!!)
                    Toast.makeText(applicationContext, "Connection error", Toast.LENGTH_LONG ).show()
                }

            })
    }

    private fun deleteBarang() {
        progress_bar.visibility = View.VISIBLE

        AndroidNetworking.get(ApiBarang.DELETE+"?id=${etId.text.toString()}")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(applicationContext, response?.getString("message"), Toast.LENGTH_SHORT).show()

                    if (response?.getString("message")?.contains("successfull")!!) {
                        this@ManageBarang.finish()
                    }
                }

                override fun onError(anError: ANError?) {
                    progress_bar.visibility = View.GONE
                    Log.d("ONERROR", anError?.errorDetail.toString()!!)
                    Toast.makeText(applicationContext, "Connection error", Toast.LENGTH_LONG ).show()
                }

            })
    }






}