package com.amandaluz.ui.customView.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.amandaluz.ui.R


class CustomAutomaticAdapter(applicationContext: Context, names: Array<String>, images: IntArray) :
    BaseAdapter() {
    var context: Context
    private var images: IntArray
    private var names: Array<String>
    private var inflater: LayoutInflater

    init {
        context = applicationContext
        this.images = images
        this.names = names
        inflater = LayoutInflater.from(applicationContext)
    }

    override fun getCount(): Int {
        return names.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, containerView: View?, parent: ViewGroup): View {
        var view = containerView

        if (view == null) {

            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.custom_adapter_layout, parent, false)

        }
        // Link those objects with their respective id's
        // that we have given in .XML file
        val name = view?.findViewById<View>(R.id.name) as TextView
        val image = view.findViewById<View>(R.id.image) as ImageView

        // Set the data in text view
        name.text = names[position]

        // Set the image in Image view
        image.setImageResource(images[position])
        return view
    }

}