package com.projectPAB.umkmkuningan.presentation.shipping

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectPAB.umkmkuningan.data.remote.RetrofitClient
import com.projectPAB.umkmkuningan.data.remote.WilayahItem
import com.projectPAB.umkmkuningan.databinding.ActivityShippingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShippingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShippingBinding
    private val apiKey = "RAHASIA"

    private var selectedOriginKecamatanId: String? = null
    private var selectedDestKecamatanId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShippingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Muat data Provinsi saat Activity pertama kali dibuka
        loadProvinsi(isOrigin = true)
        loadProvinsi(isOrigin = false)

        binding.btnCheck.setOnClickListener {
            val origin = selectedOriginKecamatanId
            val destination = selectedDestKecamatanId
            val weightStr = binding.etWeight.text.toString().trim()
            val courier = binding.spCourier.selectedItem.toString().lowercase()

            if (origin == null) {
                Toast.makeText(this, "Silakan pilih Kecamatan Asal terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (destination == null) {
                Toast.makeText(this, "Silakan pilih Kecamatan Tujuan terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (weightStr.isEmpty()) {
                Toast.makeText(this, "Isi berat paket", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weight = weightStr.toDoubleOrNull()?.toInt()
            if (weight == null || weight <= 0) {
                Toast.makeText(this, "Berat tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            checkShippingCost(origin, destination, weight, courier)
        }
    }

    private fun loadProvinsi(isOrigin: Boolean) {
        showLoading(true)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.shippingApiService.getProvinsi(apiKey)
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    if (response.isSuccessful && response.body()?.code == "200") {
                        val listProvinsi = response.body()?.value ?: emptyList()
                        setupProvinsiSpinner(listProvinsi, isOrigin)
                    } else {
                        Toast.makeText(this@ShippingActivity, "Gagal memuat Provinsi: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(this@ShippingActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupProvinsiSpinner(list: List<WilayahItem>, isOrigin: Boolean) {
        val spinner = if (isOrigin) binding.spOriginProvinsi else binding.spDestProvinsi
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        if (isOrigin) {
            val defaultIndex = list.indexOfFirst { it.name.contains("Jawa Barat", ignoreCase = true) }
            if (defaultIndex >= 0) spinner.setSelection(defaultIndex)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = list[position]
                loadKabupaten(selected.id, isOrigin)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadKabupaten(idProvinsi: String, isOrigin: Boolean) {
        showLoading(true)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.shippingApiService.getKabupaten(apiKey, idProvinsi)
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    if (response.isSuccessful && response.body()?.code == "200") {
                        val listKabupaten = response.body()?.value ?: emptyList()
                        setupKabupatenSpinner(listKabupaten, isOrigin)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(this@ShippingActivity, "Error Kabupaten: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupKabupatenSpinner(list: List<WilayahItem>, isOrigin: Boolean) {
        val spinner = if (isOrigin) binding.spOriginKabupaten else binding.spDestKabupaten
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        if (isOrigin) {
            val defaultIndex = list.indexOfFirst { it.name.contains("Kuningan", ignoreCase = true) }
            if (defaultIndex >= 0) spinner.setSelection(defaultIndex)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = list[position]
                loadKecamatan(selected.id, isOrigin)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadKecamatan(idKabupaten: String, isOrigin: Boolean) {
        showLoading(true)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.shippingApiService.getKecamatan(apiKey, idKabupaten)
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    if (response.isSuccessful && response.body()?.code == "200") {
                        val listKecamatan = response.body()?.value ?: emptyList()
                        setupKecamatanSpinner(listKecamatan, isOrigin)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(this@ShippingActivity, "Error Kecamatan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupKecamatanSpinner(list: List<WilayahItem>, isOrigin: Boolean) {
        val spinner = if (isOrigin) binding.spOriginKecamatan else binding.spDestKecamatan
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        if (isOrigin) {
            val defaultIndex = list.indexOfFirst { it.name.contains("Kuningan", ignoreCase = true) }
            if (defaultIndex >= 0) spinner.setSelection(defaultIndex)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = list[position]
                // Tambahkan awalan "dist_" karena v1/cost memerlukan format prefix ID (Kecamatan = dist_)
                if (isOrigin) {
                    selectedOriginKecamatanId = "dist_${selected.id}"
                } else {
                    selectedDestKecamatanId = "dist_${selected.id}"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                if (isOrigin) selectedOriginKecamatanId = null else selectedDestKecamatanId = null
            }
        }
    }

    private fun checkShippingCost(origin: String, destination: String, weight: Int, courier: String) {
        showLoading(true)
        binding.rvShippingCosts.visibility = View.GONE
        binding.tvResult.visibility = View.GONE
        binding.btnCheck.isEnabled = false

        Log.d("ONGKIR", "POST v1/cost → origin=$origin | destination=$destination | weight=$weight | courier=$courier")

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.shippingApiService.getShippingCost(
                    apiKey, origin, destination, weight, courier
                )

                val httpCode = response.code()
                val errorBodyStr = if (!response.isSuccessful) response.errorBody()?.string() ?: "" else ""
                Log.d("ONGKIR", "HTTP $httpCode | errorBody: $errorBodyStr")

                withContext(Dispatchers.Main) {
                    showLoading(false)
                    binding.btnCheck.isEnabled = true

                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.code == "200") {
                            val results = body.data?.results
                            val allCosts = results?.flatMap { it.costs } ?: emptyList()

                            if (allCosts.isNotEmpty()) {
                                binding.tvResult.visibility = View.VISIBLE
                                val adapter = ShippingAdapter(allCosts)
                                binding.rvShippingCosts.layoutManager = LinearLayoutManager(this@ShippingActivity)
                                binding.rvShippingCosts.adapter = adapter
                                binding.rvShippingCosts.visibility = View.VISIBLE
                            } else {
                                Toast.makeText(this@ShippingActivity, "Tarif tidak tersedia untuk rute & kurir ini", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this@ShippingActivity, "API Error: ${body?.message}", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Log.e("ONGKIR", "HTTP ${response.code()}: $errorBodyStr")
                        val msg = when (response.code()) {
                            400 -> "Format/ID wilayah tidak valid (400)"
                            401 -> "API Key tidak valid (401)"
                            403 -> "Akses ditolak (403) — cek paket BinderByte"
                            422 -> "Wilayah tidak ditemukan (422)"
                            429 -> "Kuota API habis (429)"
                            else -> "HTTP Error ${response.code()}"
                        }
                        Toast.makeText(this@ShippingActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("ONGKIR", "Exception: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    binding.btnCheck.isEnabled = true
                    Toast.makeText(this@ShippingActivity, "Koneksi gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
