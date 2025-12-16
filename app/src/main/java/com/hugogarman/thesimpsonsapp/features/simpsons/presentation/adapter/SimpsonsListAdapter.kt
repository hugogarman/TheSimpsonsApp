package com.hugogarman.thesimpsonsapp.features.simpsons.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hugogarman.thesimpsonsapp.core.presentation.ext.loadUrl
import com.hugogarman.thesimpsonsapp.databinding.ViewItemSimpsonBinding
import com.hugogarman.thesimpsonsapp.features.simpsons.domain.Simpson

class SimpsonsListAdapter : ListAdapter<Simpson, SimpsonsListAdapter.ViewHolder>(SimpsonsDiffUtil()) {

    class ViewHolder(private val binding: ViewItemSimpsonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(simpson: Simpson) {
            binding.apply {
                ivSimpson.loadUrl(simpson.portraitPath)
                tvSimpsonName.text = simpson.name
                tvSimpsonOccupation.text = simpson.occupation
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewItemSimpsonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}