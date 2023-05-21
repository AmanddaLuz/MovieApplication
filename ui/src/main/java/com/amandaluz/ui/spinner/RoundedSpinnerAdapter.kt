package com.amandaluz.ui.spinner

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.amandaluz.ui.databinding.RoundedSpinnerItemBinding

class RoundedSpinnerAdapter(
    context: Context,
    resource: Int,
    objects: List<String>
) : ArrayAdapter<String>(context, resource, objects) {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var items: List<String> = objects
    private var firstTime = true
    private var isDropDownOpen = false

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        isDropDownOpen = true
        return createItemView(position, parent)
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        firstTime = true
        isDropDownOpen = false
        return createItemView(position, parent)
    }

    private fun createItemView(position: Int, parent: ViewGroup): View {
        val binding = RoundedSpinnerItemBinding.inflate(mInflater, parent, false)

        val description = items[position]
        binding.tvItemSelected.text = description

        if (isDropDownOpen) {
            binding.arrowItem.visibility = View.GONE
            if (position != 0) {
                binding.tvItemSelected.setTextColor(Color.BLACK)
            }
        }

        if (position == items.size - 1 || firstTime) {
            firstTime = false
            binding.dividerItemRecord.visibility = View.GONE
        }

        return binding.root
    }
}