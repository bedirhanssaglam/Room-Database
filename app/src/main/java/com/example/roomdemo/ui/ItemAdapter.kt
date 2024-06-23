package com.example.roomdemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.R
import com.example.roomdemo.databinding.ItemsRowBinding
import com.example.roomdemo.model.EmployeeEntity

class ItemAdapter(
    private val items: List<EmployeeEntity>,
    private val updateListener: (id: Int) -> Unit,
    private val deleteListener: (id: Int) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivEdit.setOnClickListener {
                updateListener.invoke(items[getBindingAdapterPosition()].id)
            }
            binding.ivDelete.setOnClickListener {
                deleteListener.invoke(items[getBindingAdapterPosition()].id)
            }
        }

        fun bind(item: EmployeeEntity) {
            binding.apply {
                tvName.text = item.name
                tvEmail.text = item.email
                llMain.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        if (getBindingAdapterPosition() % 2 == 0) R.color.black else R.color.white
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemsRowBinding = ItemsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
