package com.projectPAB.umkmkuningan.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.projectPAB.umkmkuningan.core.utils.UserPreferences
import com.projectPAB.umkmkuningan.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPrefs: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPrefs = UserPreferences(requireContext())
        
        binding.tvUserName.text = "${userPrefs.getUserName()}!"

        // Event listener untuk kategori Kuliner
        binding.glCategories.getChildAt(0).setOnClickListener {
            val intent = android.content.Intent(requireContext(), com.projectPAB.umkmkuningan.presentation.product.ProductListActivity::class.java)
            intent.putExtra("EXTRA_CATEGORY", "Kuliner")
            startActivity(intent)
        }

        // Event listener untuk kategori Kerajinan
        binding.glCategories.getChildAt(1).setOnClickListener {
            val intent = android.content.Intent(requireContext(), com.projectPAB.umkmkuningan.presentation.product.ProductListActivity::class.java)
            intent.putExtra("EXTRA_CATEGORY", "Kerajinan")
            startActivity(intent)
        }

        // Event listener untuk fitur Cek Ongkir
        binding.btnShipping.setOnClickListener {
            val intent = android.content.Intent(requireContext(), com.projectPAB.umkmkuningan.presentation.shipping.ShippingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
