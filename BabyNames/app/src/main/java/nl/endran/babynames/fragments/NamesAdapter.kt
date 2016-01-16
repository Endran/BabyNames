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

    public fun updateNames(names: List<BabyName>) {
        if (this.names.isEmpty() || this.names.size == names.size) {
            showAllNames(names)
        } else if (this.names.size > names.size) {
            removeMissingNames(names)
        } else if (this.names.size < names.size) {
            addNewNames(names)
        }
    }

    private fun addNewNames(names: List<BabyName>) {
        // Insert all names from the incoming list, that are not already in the adapter

        names
                // Filter out all names that are in the adapter and in the incoming list
                .filter {
                    !this.names.map { it.name }
                            .contains(it.name)
                }
                // The add all remaining names to the adapter
                .forEach {
                    this.names = this.names.plus(it)
                    this.notifyItemInserted(this.names.size - 1)
                }
    }

    private fun removeMissingNames(names: List<BabyName>) {
        // Remove all names from the adapter, that are missing already in the incoming list
        var itemsRemoved = 0
        for (i in this.names.indices) {
            if (names
                    .filter { it.name == this.names[i - itemsRemoved].name }
                    .isEmpty()) {
                this.names = this.names.minus(this.names[i - itemsRemoved])
                this.notifyItemRemoved(i - itemsRemoved)
                itemsRemoved++
            }
        }
    }

    private fun showAllNames(names: List<BabyName>) {
        this.names = names
        this.notifyDataSetChanged()
    }
}
