package com.snadinao.ecommerce.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.snadinao.ecommerce.R
import com.snadinao.ecommerce.model.SliderItem

class SliderAdapter(val context: Context, val viewPager: ViewPager2, val imageList: ArrayList<SliderItem>):
 RecyclerView.Adapter<SliderAdapter.SliderViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.slider_item, parent, false)
        return SliderViewHolder(view)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val listImages = imageList[position]
        Glide.with(context)
            .load(listImages.sliderImage)
            .placeholder(R.drawable.avatar)
            .into(holder.imageView)
        if (position == imageList.size - 2){
            viewPager.post(run)
        }
    }

    override fun getItemCount(): Int = imageList.size
    @SuppressLint("NotifyDataSetChanged")
    val run = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }


    inner class SliderViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.imageSlider)
    }
}