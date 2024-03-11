package com.sd.showproducts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sd.showproducts.R
import com.sd.showproducts.databinding.ItemCategoryBinding
import com.sd.showproducts.dto.Category

class CategoryAdapter(private val listener: Listener) :
    ListAdapter<Category, CategoryAdapter.CategoryHolder>(CategoryDiffCallback()) {

    class CategoryHolder(item: View, private val listener: Listener) :
        RecyclerView.ViewHolder(item) {

        private val binding = ItemCategoryBinding.bind(item)

        fun bind(category: Category) = with(binding) {

            txCatForAllCat.text = category.desc

            cardViewItemCategory.setOnClickListener {
                listener.showCategory(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryHolder(view, listener)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.desc == newItem.desc
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Category, newItem: Category): Any =
        Payload()
}


