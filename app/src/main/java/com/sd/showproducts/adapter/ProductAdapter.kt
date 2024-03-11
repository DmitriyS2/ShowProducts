package com.sd.showproducts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sd.showproducts.R
import com.sd.showproducts.databinding.ItemProductBinding
import com.sd.showproducts.dto.Category
import com.sd.showproducts.dto.Product

interface Listener{
    fun goToProduct(product: Product)
    fun showCategory(category: Category)
}

class ProductAdapter(private val listener: Listener) :
    ListAdapter<Product, ProductAdapter.ProductHolder>(ProductDiffCallback()) {

    class ProductHolder(item: View, private val listener: Listener) :
        RecyclerView.ViewHolder(item) {

        private val binding = ItemProductBinding.bind(item)

        fun bind(product: Product) = with(binding) {

            title.text = product.title
            description.text = product.description

            Glide.with(image)
                .load(product.thumbnail)
                .placeholder(R.drawable.download_64)
                .error(R.drawable.error_64)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .timeout(10_000)
                .into(image)

            cardViewItemProduct.setOnClickListener {
                listener.goToProduct(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Product, newItem: Product): Any =
        Payload()
}

data class Payload(
    val id: Int? = null
)