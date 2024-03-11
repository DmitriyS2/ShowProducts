package com.sd.showproducts.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sd.showproducts.R
import com.sd.showproducts.adapter.CategoryAdapter
import com.sd.showproducts.adapter.Listener
import com.sd.showproducts.databinding.FragmentAllCategoriesBinding
import com.sd.showproducts.dto.Category
import com.sd.showproducts.dto.Product
import com.sd.showproducts.viewmodel.MainViewModel

class AllCategoriesFragment : Fragment() {

    private val viewModel:MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentAllCategoriesBinding.inflate(inflater, container, false)

        val adapter = CategoryAdapter(object : Listener {
            override fun goToProduct(product: Product) {
            }

            override fun showCategory(category: Category) {
                viewModel.changeTextCategory(category.desc)
                viewModel.loadCurrentCategory(category.desc)
                findNavController()
                    .navigate(R.id.action_allCategoriesFragment_to_mainFragment)
            }
        })

        binding.rwCategories.layoutManager = LinearLayoutManager(activity)
        binding.rwCategories.adapter = adapter

        viewModel.dataCategory.observe(viewLifecycleOwner) {model->
            model?.let {
                binding.groupError.isVisible = model.error
                binding.progress.isVisible = model.loading
            }
            val list:MutableList<Category> = model.categories.map {
                Category(it)
            } as MutableList<Category>
            adapter.submitList(list)
        }

        binding.buttonRetryCat.setOnClickListener {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonRetryCat,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
            ).start()
            viewModel.loadAllCategories()
        }

        binding.buttonBackCat.setOnClickListener {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonBackCat,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
            ).start()
            viewModel.changeFlagFilterSearch(0)
            viewModel.changeTextCategory("")
            findNavController()
                .navigate(R.id.action_allCategoriesFragment_to_mainFragment)
        }

        return binding.root
    }
}