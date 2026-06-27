package com.projectPAB.umkmkuningan.presentation.product

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectPAB.umkmkuningan.data.remote.RetrofitClient
import com.projectPAB.umkmkuningan.databinding.ActivityProductListBinding
import kotlinx.coroutines.launch

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = intent.getStringExtra("EXTRA_CATEGORY") ?: "Semua Produk"
        binding.toolbar.title = category
        
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        fetchProducts(category)
    }

    private fun fetchProducts(category: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.backendApiService.getProducts()
                if (response.isSuccessful && response.body()?.status == "success") {
                    val products = response.body()?.data ?: emptyList()
                    
                    // Filter berdasarkan kategori dari database
                    val filteredProducts = products.filter { product ->
                        if (category == "Semua Produk") {
                            true
                        } else {
                            product.category.equals(category, ignoreCase = true)
                        }
                    }

                    val adapter = ProductAdapter(filteredProducts) { selectedProduct ->
                        val intent = Intent(this@ProductListActivity, ProductDetailActivity::class.java)
                        intent.putExtra("EXTRA_PRODUCT", selectedProduct)
                        startActivity(intent)
                    }

                    binding.rvProducts.layoutManager = LinearLayoutManager(this@ProductListActivity)
                    binding.rvProducts.adapter = adapter
                } else {
                    Toast.makeText(this@ProductListActivity, "Gagal mengambil data produk", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProductListActivity, "Koneksi gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
