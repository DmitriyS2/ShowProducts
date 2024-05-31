package com.sd.showproducts.activity

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
import com.sd.showproducts.dto.Category
import com.sd.showproducts.dto.Product
import com.sd.showproducts.util.State
import com.sd.showproducts.util.animTouch
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

            override fun showCategory(category: Category) {
            }

        })

        binding.rwProducts.layoutManager = GridLayoutManager(activity, 2)
        binding.rwProducts.adapter = adapter

        binding.buttonPrev.setOnClickListener {
            it.animTouch()
            viewModel.removeCountForLoading()
        }
        binding.buttonNext.setOnClickListener {
            it.animTouch()
            viewModel.addCountForLoading()
        }

        viewModel.idForLoading.observe(viewLifecycleOwner) {
            binding.buttonPrev.isEnabled = it != 0
            if (viewModel.flagFilterSearch.value != State.ALL_OFF) {
                return@observe
            }

            binding.textCounter.text =
                getString(
                    R.string.counter_page,
                    (it + 1).toString(),
                    (it + viewModel.countForShowList).toString()
                )
            viewModel.loadData()
        }

        viewModel.dataModel.observe(viewLifecycleOwner) { model ->
            model?.let {
                binding.textNotFound.isVisible =
                    model.error && viewModel.flagFilterSearch.value == State.SEARCH_ON
                binding.groupError.isVisible =
                    model.error && viewModel.flagFilterSearch.value != State.SEARCH_ON
                binding.progress.isVisible = model.loading
                binding.groupButtonPrevNext.isVisible = !model.error

                val lastProduct = model.products.getOrNull(viewModel.countForShowList)
                binding.buttonNext.isEnabled = lastProduct != null
                if (model.products.isNotEmpty()) {
                    val newList: MutableList<Product> = model.products as MutableList<Product>
                    newList.remove(lastProduct)
                    adapter.submitList(newList)
                } else adapter.submitList(emptyList())
                if (viewModel.flagFilterSearch.value != State.ALL_OFF) {
                    binding.groupButtonPrevNext.visibility = View.GONE
                }
            }
        }

        binding.buttonSearch.setOnClickListener {
            it.animTouch()
            if (viewModel.flagFilterSearch.value == State.ALL_OFF) {
                viewModel.changeFlagFilterSearch(State.SEARCH_ON)
            } else {
                viewModel.changeFlagFilterSearch(State.ALL_OFF)
                viewModel.loadData()
            }
        }

        binding.editSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.search(it)
                    binding.editSearch.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        viewModel.flagFilterSearch.observe(viewLifecycleOwner) {
            it?.let { state ->
                when (state) {
                    //фильтр и поиск не нажаты
                    State.ALL_OFF -> {
                        binding.groupButtonPrevNext.isVisible = true
                        binding.groupFilter.isVisible = true
                        binding.buttonSearch.isVisible = true
                        binding.editSearch.isVisible = false
                        binding.buttonSearch.setImageResource(R.drawable.search_48)
                        binding.buttonFilter.setImageResource(R.drawable.filter_48)
                        binding.textCounter.text =
                            getString(
                                R.string.counter_page,
                                (viewModel.idForLoading.value?.plus(1)).toString(),
                                (viewModel.idForLoading.value?.plus(
                                    viewModel.countForShowList
                                )).toString()
                            )
                    }
                    //фильтр нажат
                    State.FILTER_ON -> {
                        binding.groupButtonPrevNext.isVisible = false
                        binding.groupFilter.isVisible = true
                        binding.buttonSearch.isVisible = false
                        binding.editSearch.isVisible = false
                        binding.buttonFilter.setImageResource(R.drawable.cancel_48)
                    }
                    //поиск нажат
                    State.SEARCH_ON -> {
                        binding.groupButtonPrevNext.isVisible = false
                        binding.groupFilter.isVisible = false
                        binding.buttonSearch.isVisible = true
                        binding.editSearch.isVisible = true
                        binding.buttonSearch.setImageResource(R.drawable.cancel_48)
                    }
                }
            }
        }

        binding.buttonFilter.setOnClickListener {
            it.animTouch()
            if (viewModel.flagFilterSearch.value == State.ALL_OFF) {
                viewModel.changeFlagFilterSearch(State.FILTER_ON)
                findNavController()
                    .navigate(R.id.action_mainFragment_to_allCategoriesFragment)
            } else {
                viewModel.changeFlagFilterSearch(State.ALL_OFF)
                viewModel.changeTextCategory("")
                viewModel.loadData()
            }
        }

        binding.buttonRetry.setOnClickListener {
            it.animTouch()
            when (viewModel.flagFilterSearch.value) {
                State.FILTER_ON -> viewModel.loadCurrentCategory()
                else -> viewModel.loadData()
            }


        }

        viewModel.textCategory.observe(viewLifecycleOwner) {
            binding.textCategory.text = it
        }
        return binding.root
    }
}