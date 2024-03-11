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
import com.sd.showproducts.dto.Category
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

            override fun showCategory(category: Category) {
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
            viewModel.removeCountForLoading()
        }
        binding.buttonNext.setOnClickListener {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonNext,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
            ).start()
            viewModel.addCountForLoading()
        }

        viewModel.idForLoading.observe(viewLifecycleOwner) {
            if (viewModel.flagFilterSearch.value != 0) {
                return@observe
            }
            binding.buttonPrev.isEnabled = it != 0
            Log.d("MyLog", "idForLoading observe=${it}")
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

                if (viewModel.flagFilterSearch.value != 0) {
                    binding.groupButtonPrevNext.visibility = View.GONE
                }
            }
        }

        binding.buttonSearch.setOnClickListener {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonSearch,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
            ).start()
            if (viewModel.flagFilterSearch.value == 0) {
                viewModel.changeFlagFilterSearch(2)
            } else {
                viewModel.changeFlagFilterSearch(0)
                viewModel.loadData()
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

        viewModel.flagFilterSearch.observe(viewLifecycleOwner) {
            it?.let { flag ->
                when (flag) {
                    //фильтр и поиск не нажаты
                    0 -> {
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
                    1 -> {
                        binding.groupButtonPrevNext.isVisible = false
                        binding.groupFilter.isVisible = true
                        binding.buttonSearch.isVisible = false
                        binding.editSearch.isVisible = false
                        binding.buttonFilter.setImageResource(R.drawable.cancel_48)
                    }
                    //поиск нажат
                    2 -> {
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
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonFilter,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
            ).start()
            if (viewModel.flagFilterSearch.value == 0) {
                viewModel.changeFlagFilterSearch(1)
                findNavController()
                    .navigate(R.id.action_mainFragment_to_allCategoriesFragment)
            } else {
                viewModel.changeFlagFilterSearch(0)
                viewModel.changeTextCategory("")
                viewModel.loadData()
            }
        }

        binding.buttonRetry.setOnClickListener {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonRetry,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
            ).start()
            viewModel.loadData()
        }

        viewModel.textCategory.observe(viewLifecycleOwner) {
            binding.textCategory.text = it
        }
        return binding.root
    }
}