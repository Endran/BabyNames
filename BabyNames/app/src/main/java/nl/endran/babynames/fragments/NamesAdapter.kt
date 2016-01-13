/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.SectionIndexer
import nl.endran.babynames.names.BabyName

abstract class NamesAdapter : RecyclerView.Adapter<NamesAdapter.ViewHolder>(), SectionIndexer {
    var names: List<BabyName> = listOf()

    var isFavorite: ((String) -> Boolean)? = null
    var toggleFavorite: ((String) -> Unit)? = null

    override fun getItemCount(): Int {
        return names.size
    }

    override fun getPositionForSection(sectionIndex: Int) = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}
