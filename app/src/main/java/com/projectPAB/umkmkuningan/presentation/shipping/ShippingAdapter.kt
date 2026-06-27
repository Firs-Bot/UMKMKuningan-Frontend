package com.projectPAB.umkmkuningan.presentation.shipping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectPAB.umkmkuningan.data.remote.CostDetail
import com.projectPAB.umkmkuningan.databinding.ItemShippingCostBinding
import java.text.NumberFormat
import java.util.Locale

class ShippingAdapter(private val costs: List<CostDetail>) : RecyclerView.Adapter<ShippingAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemShippingCostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(costDetail: CostDetail) {
            binding.tvService.text = "Layanan: ${costDetail.service}"
            binding.tvDesc.text = "Tipe: ${costDetail.type ?: "-"}"
            
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            
            // BinderByte returns price as String, convert to Double for formatting
            val price = costDetail.price.toDoubleOrNull() ?: 0.0
            binding.tvCost.text = "Tarif: ${formatRupiah.format(price)}"
            binding.tvEtd.text = "Estimasi: ${costDetail.estimated}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShippingCostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(costs[position])
    }

    override fun getItemCount(): Int = costs.size
}
