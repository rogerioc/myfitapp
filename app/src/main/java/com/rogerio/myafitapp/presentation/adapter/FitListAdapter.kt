package com.rogerio.myafitapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rogerio.myafitapp.R
import com.rogerio.myafitapp.databinding.FitRowDataBinding
import com.rogerio.myafitapp.presentation.model.FitItemViewEntity

class FitListAdapter() : RecyclerView.Adapter<FitListAdapter.ViewHolder>() {
    private lateinit var items: List<FitItemViewEntity>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            FitViewHolder(parent)


    override fun getItemCount(): Int =
            items.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is FitViewHolder && items.size > position) {
            holder.bind(items[position])
        }
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private fun update(items: List<FitItemViewEntity>) {
        this.items = items
        notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("fititems")
        fun RecyclerView.bindItems(items: List<FitItemViewEntity>?) {
            items?.let {
                val adapter = adapter as FitListAdapter
                adapter.update(items)
            }
        }
    }

    class FitViewHolder(
            private val parent: ViewGroup,
            private val binding: FitRowDataBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.fit_row_data,
                    parent,
                    false
            )
    ) : ViewHolder(binding.root) {
        fun bind(item: FitItemViewEntity) {
            binding.viewData = item

        }
    }
}