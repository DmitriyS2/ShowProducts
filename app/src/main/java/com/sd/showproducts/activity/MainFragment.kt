package com.sd.showproducts.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.sd.showproducts.R
import com.sd.showproducts.adapter.Listener
import com.sd.showproducts.adapter.ProductAdapter
import com.sd.showproducts.databinding.FragmentMainBinding
import com.sd.showproducts.dto.Product

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding:FragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)

        val adapter = ProductAdapter(object: Listener {
            override fun goToProduct(product: Product) {
                TODO("Not yet implemented")
            }

        } )

        binding.rwProducts.layoutManager = GridLayoutManager(activity, 2)
        binding.rwProducts.adapter = adapter

        return binding.root
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}