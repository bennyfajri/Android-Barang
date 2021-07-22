package com.example.penjualan

class ApiBarang {
    companion object{
        private val URL = "http://192.168.100.184/penjualan/"
        val CREATE = URL+"createData.php"
        val UPDATE = URL+"updateData.php"
        val READ = URL+"readData.php"
        val DELETE = URL+"deleteData.php"
    }
}