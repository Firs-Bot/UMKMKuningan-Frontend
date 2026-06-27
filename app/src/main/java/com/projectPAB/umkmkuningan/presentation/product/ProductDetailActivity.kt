package com.projectPAB.umkmkuningan.presentation.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.projectPAB.umkmkuningan.core.utils.UserPreferences
import com.projectPAB.umkmkuningan.data.remote.RetrofitClient
import com.projectPAB.umkmkuningan.databinding.ActivityProductDetailBinding
import com.projectPAB.umkmkuningan.domain.model.Product
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var userPrefs: UserPreferences
    private var product: Product? = null
    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPrefs = UserPreferences(this)

        product = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_PRODUCT", Product::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_PRODUCT")
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        product?.let { 
            bindData(it) 
            checkBookmarkStatus(it.id)
        }

        binding.btnWhatsApp.setOnClickListener {
            product?.let { p -> openWhatsApp(p.phone ?: "", p.name) }
        }

        binding.btnBookmark.setOnClickListener {
            product?.let { p -> toggleBookmark(p.id) }
        }
    }

    private fun bindData(product: Product) {
        binding.tvProductNameDetail.text = product.name
        binding.tvShopNameDetail.text = product.shopName
        binding.tvProductDescDetail.text = product.description
        
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        binding.tvProductPriceDetail.text = formatRupiah.format(product.price)

        // Menggunakan Glide untuk memuat gambar
        com.bumptech.glide.Glide.with(this)
            .load(product.imageUrl)
            .into(binding.ivProductDetail)
    }

    private fun checkBookmarkStatus(productId: String) {
        val userId = userPrefs.getUserId()
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.backendApiService.getBookmarks(userId)
                if (response.isSuccessful && response.body()?.status == "success") {
                    val bookmarks = response.body()?.data ?: emptyList()
                    isBookmarked = bookmarks.any { it.id == productId }
                    updateBookmarkButtonUI()
                }
            } catch (e: Exception) {
                // Ignore or log error
            }
        }
    }

    private fun toggleBookmark(productId: String) {
        val userId = userPrefs.getUserId()
        binding.btnBookmark.isEnabled = false
        
        lifecycleScope.launch {
            try {
                if (isBookmarked) {
                    val response = RetrofitClient.backendApiService.removeBookmark(userId, productId.toInt())
                    if (response.isSuccessful && response.body()?.status == "success") {
                        Toast.makeText(this@ProductDetailActivity, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()
                        isBookmarked = false
                    } else {
                        Toast.makeText(this@ProductDetailActivity, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val response = RetrofitClient.backendApiService.addBookmark(userId, productId.toInt())
                    if (response.isSuccessful && response.body()?.status == "success") {
                        Toast.makeText(this@ProductDetailActivity, "Disimpan ke Favorit", Toast.LENGTH_SHORT).show()
                        isBookmarked = true
                    } else {
                        Toast.makeText(this@ProductDetailActivity, "Gagal menyimpan", Toast.LENGTH_SHORT).show()
                    }
                }
                updateBookmarkButtonUI()
            } catch (e: Exception) {
                Toast.makeText(this@ProductDetailActivity, "Koneksi gagal", Toast.LENGTH_SHORT).show()
            } finally {
                binding.btnBookmark.isEnabled = true
            }
        }
    }

    private fun updateBookmarkButtonUI() {
        if (isBookmarked) {
            binding.btnBookmark.text = "Tersimpan"
        } else {
            binding.btnBookmark.text = "Simpan Favorit"
        }
    }

    private fun openWhatsApp(phoneNumber: String, productName: String) {
        try {
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=Halo, saya tertarik dengan produk $productName dari aplikasi UMKMKuningan."
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp tidak terinstal atau error", Toast.LENGTH_SHORT).show()
        }
    }
}
