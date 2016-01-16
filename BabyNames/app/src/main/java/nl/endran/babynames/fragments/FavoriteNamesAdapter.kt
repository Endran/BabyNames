/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import android.graphics.Paint
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_item_name.view.*
import nl.endran.babynames.R
import nl.endran.babynames.injections.getLayoutInflater

class FavoriteNamesAdapter(val isDeprecated: ((String) -> Boolean)) : NamesAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamesAdapter.ViewHolder {
        val view = parent.getLayoutInflater().inflate(R.layout.row_item_name, parent, false)
        return NamesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NamesAdapter.ViewHolder, position: Int) {
        val name = babies[position]

        val itemView = holder.itemView
        itemView.textView.text = "${name.place}. ${name.name}"

        val checkbox = itemView.checkbox
        checkbox.isClickable = false
        checkbox.isChecked = isFavorite?.invoke(name.name) ?: false;
        itemView.setOnClickListener {
            val paintFlags: Int
//            if (isDeprecated(name.name)) {
                paintFlags = itemView.textView.paintFlags.or(Paint.STRIKE_THRU_TEXT_FLAG)
//            } else {
//                paintFlags = itemView.textView.paintFlags.and(Paint.STRIKE_THRU_TEXT_FLAG.inv())
//            }
            itemView.textView.paintFlags = paintFlags
        }

        itemView.setOnLongClickListener {
            toggleFavorite?.invoke(name.name)
            checkbox.isChecked = isFavorite?.invoke(name.name) ?: false;
            return@setOnLongClickListener true
        }
    }

    override fun getSectionForPosition(position: Int) = 0

    override fun getSections(): Array<out Any>? = arrayOf("")
}
