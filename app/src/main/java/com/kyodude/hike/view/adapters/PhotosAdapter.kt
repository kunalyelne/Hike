package com.kyodude.hike.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kyodude.hike.databinding.PhotoItemBinding
import com.kyodude.hike.model.dataModel.Photo
import com.kyodude.hike.utils.extensions.getUri

class PhotosAdapter: RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    private var photoList: List<Photo> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        Glide.with(holder.binding.photo)
            .load(photoList[position].getUri())
            .apply( RequestOptions().override(500, 500))
            .centerCrop()
            .into(holder.binding.photo)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    fun setPhotos(photos: List<Photo>) {
        photoList = photos
        notifyDataSetChanged()
    }

    inner class PhotoViewHolder(val binding: PhotoItemBinding): RecyclerView.ViewHolder(binding.root)
}