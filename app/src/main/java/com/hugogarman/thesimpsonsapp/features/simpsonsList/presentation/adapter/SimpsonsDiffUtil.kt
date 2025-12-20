package com.hugogarman.thesimpsonsapp.features.simpsonsList.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hugogarman.thesimpsonsapp.features.simpsonsList.domain.Simpson

class SimpsonsDiffUtil : DiffUtil.ItemCallback<Simpson>() {
    override fun areItemsTheSame(oldItem: Simpson, newItem: Simpson): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Simpson, newItem: Simpson): Boolean {
        return oldItem == newItem
    }
}