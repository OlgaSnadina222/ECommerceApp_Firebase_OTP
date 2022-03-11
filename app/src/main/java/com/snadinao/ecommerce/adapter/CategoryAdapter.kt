package com.snadinao.ecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.snadinao.ecommerce.R
import com.snadinao.ecommerce.model.CategoryData

class CategoryAdapter(val context: Context, val categoryList: ArrayList<CategoryData>):
    RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    class CategoryHolder(val view: View): RecyclerView.ViewHolder(view) {
        val categoryImage = view.findViewById<ImageView>(R.id.catImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_category, parent, false)
        return CategoryHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val listCat = categoryList[position]
        holder.categoryImage.setImageResource(listCat.image)
    }

    override fun getItemCount(): Int = categoryList.size

}