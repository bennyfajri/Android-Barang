package com.example.penjualan

data class ModelBarang(
    val id : Int,
    val nama : String,
    val hargaModal : Int,
    val hargaJual : Int,
    val jumlahBarang : Int,
    val terjual : Int
)