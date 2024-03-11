package com.sd.showproducts.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
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
            //      Log.d("MyLog", "idForLoading prev1=${viewModel.idForLoading.value}")
            viewModel.removeCountForLoading()
            //        Log.d("MyLog", "idForLoading prev2=${viewModel.idForLoading.value}")
        }
        binding.buttonNext.setOnClickListener {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonNext,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
            ).start()
            //      Log.d("MyLog", "idForLoading next1=${viewModel.idForLoading.value}")
            viewModel.addCountForLoading()
            //      Log.d("MyLog", "idForLoading next2=${viewModel.idForLoading.value}")
        }

        viewModel.idForLoading.observe(viewLifecycleOwner) {
            if (viewModel.flagSearch.value == true) {
                return@observe
            }
            binding.buttonPrev.isEnabled = it != 0
            Log.d("MyLog", "idForLoading observe=${it}")
            binding.textCounter.text =
                getString(R.string.counter_page, (it + 1).toString(), (it + viewModel.countForShowList).toString())
            viewModel.loadData()
        }

        viewModel.dataModel.observe(viewLifecycleOwner) { model ->
            model?.let {
                binding.groupError.isVisible = model.error
                binding.progress.isVisible = model.loading
                binding.groupButtonPrevNext.isVisible = !model.error

                val lastProduct = model.products?.getOrNull(viewModel.countForShowList)
                binding.buttonNext.isEnabled = lastProduct != null
                val newList: MutableList<Product> = model.products as MutableList<Product>
                newList.remove(lastProduct)
                adapter.submitList(newList)
                Log.d(
                    "MyLog",
                    "lastProduct=${lastProduct}, error=${model.error}, loading=${model.loading}, products.isEmpty=${model.products.isEmpty()}"
                )

                if (viewModel.flagSearch.value == true) {
                    binding.groupButtonPrevNext.visibility = View.GONE
                }
            }
        }

        binding.buttonSearch.setOnClickListener {
            viewModel.changeFlagSearch()
        }

        viewModel.flagSearch.observe(viewLifecycleOwner) {
            it?.let { flag ->
                when (flag) {
                    true -> {
                        binding.editSearch.isVisible = true
                        binding.groupButtonPrevNext.visibility = View.GONE
                        binding.buttonSearch.setImageResource(R.drawable.cancel_48)
                    }

                    false -> {
                        binding.editSearch.isVisible = false
                        binding.groupButtonPrevNext.visibility = View.VISIBLE
                        binding.buttonSearch.setImageResource(R.drawable.search_48)
                        binding.textCounter.text =
                            getString(R.string.counter_page, (viewModel.idForLoading.value?.plus(1)).toString(), (viewModel.idForLoading.value?.plus(
                                viewModel.countForShowList
                            )).toString())
                        viewModel.loadData()
                    }
                }
            }
        }

        binding.editSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.search(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        binding.buttonRetry.setOnClickListener {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonRetry,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
            ).start()
            viewModel.loadData()
        }

        return binding.root
    }
}