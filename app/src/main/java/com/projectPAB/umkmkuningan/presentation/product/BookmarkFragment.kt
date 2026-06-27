package com.projectPAB.umkmkuningan.presentation.product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectPAB.umkmkuningan.core.utils.UserPreferences
import com.projectPAB.umkmkuningan.data.remote.RetrofitClient
import com.projectPAB.umkmkuningan.databinding.FragmentBookmarkBinding
import kotlinx.coroutines.launch

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPrefs: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPrefs = UserPreferences(requireContext())
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to this fragment
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val userId = userPrefs.getUserId()
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.backendApiService.getBookmarks(userId)
                if (response.isSuccessful && response.body()?.status == "success") {
                    val bookmarkedProducts = response.body()?.data ?: emptyList()
                    
                    if (bookmarkedProducts.isEmpty()) {
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.rvBookmarks.visibility = View.GONE
                    } else {
                        binding.tvEmpty.visibility = View.GONE
                        binding.rvBookmarks.visibility = View.VISIBLE
                        
                        val adapter = ProductAdapter(bookmarkedProducts) { selectedProduct ->
                            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
                            intent.putExtra("EXTRA_PRODUCT", selectedProduct)
                            startActivity(intent)
                        }
                        binding.rvBookmarks.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvBookmarks.adapter = adapter
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat bookmark", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Koneksi gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
