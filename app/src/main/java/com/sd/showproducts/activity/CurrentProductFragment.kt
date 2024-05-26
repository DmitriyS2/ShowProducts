package com.sd.showproducts.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sd.showproducts.R
import com.sd.showproducts.databinding.FragmentCurrentProductBinding
import com.sd.showproducts.util.StringArg
import com.sd.showproducts.util.animTouch
import com.sd.showproducts.viewmodel.MainViewModel

class CurrentProductFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentCurrentProductBinding =
            FragmentCurrentProductBinding.inflate(inflater, container, false)

        val currentId = arguments?.textArgument?.toInt() ?: 0

        viewModel.dataModel.observe(viewLifecycleOwner) { model ->
            val product = model.products.find {
                it.id == currentId
            }
            product?.let {
                binding.apply {
                    category.text = it.category
                    titleCurrent.text = it.title
                    brandCurrent.text = getString(R.string.brand, it.brand)
                    priceCurrent.text = getString(R.string.cost, it.price.toString())
                    descriptionCurrent.text = it.description
                    stockCurrent.text = getString(R.string.stock, it.stock)

                    Glide.with(imageCurrent)
                        .load(it.thumbnail)
                        .placeholder(R.drawable.download_64)
                        .error(R.drawable.error_64)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .timeout(10_000)
                        .into(imageCurrent)
                }
            }
        }

        binding.buttonBack.setOnClickListener {
            it.animTouch()
            findNavController()
                .navigate(R.id.action_currentProductFragment_to_mainFragment)
        }

        return binding.root
    }

    companion object {
        var Bundle.textArgument: String? by StringArg
    }
}