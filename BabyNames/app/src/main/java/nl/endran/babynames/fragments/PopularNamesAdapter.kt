/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import kotlinx.android.synthetic.main.row_item_name.view.*
import nl.endran.babynames.R
import nl.endran.babynames.injections.getLayoutInflater
import nl.endran.babynames.names.BabyName

class PopularNamesAdapter : RecyclerView.Adapter<PopularNamesAdapter.ViewHolder>(), SectionIndexer {
    var names: List<BabyName> = listOf()

    var isFavorite: ((String) -> Boolean)? = null
    var toggleFavorite: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val view = parent.getLayoutInflater().inflate(R.layout.row_item_name, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = names[position]

        val itemView = holder.itemView
        itemView.textView.text = "${name.place}. ${name.name}"

        val checkbox = itemView.checkbox
        checkbox.isClickable = false
        checkbox.isChecked = isFavorite?.invoke(name.name) ?: false;
        itemView.setOnClickListener {
            toggleFavorite?.invoke(name.name)
            checkbox.isChecked = isFavorite?.invoke(name.name) ?: false;
        }
    }

    override fun getSectionForPosition(position: Int) = 0

    override fun getSections(): Array<out Any>? = arrayOf("")

    override fun getPositionForSection(sectionIndex: Int) = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}
