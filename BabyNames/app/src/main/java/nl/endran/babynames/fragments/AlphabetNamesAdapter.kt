/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_item_name.view.*
import nl.endran.babynames.R
import nl.endran.babynames.injections.getLayoutInflater

class AlphabetNamesAdapter : NamesAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamesAdapter.ViewHolder {
        val view = parent.getLayoutInflater().inflate(R.layout.row_item_name, parent, false)
        return NamesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NamesAdapter.ViewHolder, position: Int) {
        val name = babies[position]

        val itemView = holder.itemView
        itemView.textView.text = name.name

        val checkbox = itemView.checkbox
        checkbox.isClickable = false
        checkbox.isChecked = isFavorite?.invoke(name.name) ?: false;
        itemView.setOnClickListener {
            toggleFavorite?.invoke(name.name)
            checkbox.isChecked = isFavorite?.invoke(name.name) ?: false;
        }
    }

    override fun getSectionForPosition(position: Int): Int {
        if (position >= babies.size) {
            return 26
        }

        val indexOf = sections!!.indexOf(babies[position].name.first())
        return if (indexOf >= 0 && indexOf < 26) indexOf else 26
    }

    override fun getSections(): Array<out Any>? {
        return arrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#')
    }
}
