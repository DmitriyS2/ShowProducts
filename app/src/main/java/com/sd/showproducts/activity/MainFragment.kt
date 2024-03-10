package com.sd.showproducts.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sd.showproducts.R
import com.sd.showproducts.activity.CurrentProductFragment.Companion.textArgument
import com.sd.showproducts.adapter.Listener
import com.sd.showproducts.adapter.ProductAdapter
import com.sd.showproducts.databinding.FragmentMainBinding
import com.sd.showproducts.dto.Product
import com.sd.showproducts.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)

        val adapter = ProductAdapter(object : Listener {
            override fun goToProduct(product: Product) {
                findNavController()
                    .navigate(R.id.action_mainFragment_to_currentProductFragment,
                        Bundle().apply {
                            textArgument = product.id.toString()
                        })
            }

        })

        binding.rwProducts.layoutManager = GridLayoutManager(activity, 2)
        binding.rwProducts.adapter = adapter

        binding.buttonPrev.setOnClickListener {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonPrev,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
            ).start()
            Log.d("MyLog", "idForLoading prev1=${viewModel.idForLoading.value}")
            viewModel.removeCountForLoading()
            Log.d("MyLog", "idForLoading prev2=${viewModel.idForLoading.value}")
        }
        binding.buttonNext.setOnClickListener {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonNext,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
            ).start()
            Log.d("MyLog", "idForLoading next1=${viewModel.idForLoading.value}")
            viewModel.addCountForLoading()
            Log.d("MyLog", "idForLoading next2=${viewModel.idForLoading.value}")
        }

        viewModel.idForLoading.observe(viewLifecycleOwner) {
            binding.buttonPrev.isEnabled = it != 0
            Log.d("MyLog", "idForLoading observe=${it}")
            binding.textCounter.text =
                (it + 1).toString() + " - " + (it + viewModel.countForShowList).toString()
            viewModel.loadData()
        }

        viewModel.data.observe(viewLifecycleOwner) {
            val lastProduct = it.getOrNull(viewModel.countForShowList)
            binding.buttonNext.isEnabled = lastProduct != null
            val newList: MutableList<Product> = it as MutableList<Product>
            newList.remove(lastProduct)
            //     adapter.submitList(it)
            adapter.submitList(newList)
            //    Log.d("MyLog", "data observe=${it}")
            Log.d("MyLog", "lastProduct=${lastProduct}")
        }

        return binding.root
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}