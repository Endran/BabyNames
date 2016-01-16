/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.SectionIndexer
import nl.endran.babynames.names.Baby

abstract class NamesAdapter : RecyclerView.Adapter<NamesAdapter.ViewHolder>(), SectionIndexer {
    var babies: List<Baby> = listOf()

    var isFavorite: ((String) -> Boolean)? = null
    var toggleFavorite: ((String) -> Unit)? = null

    override fun getItemCount(): Int {
        return babies.size
    }

    override fun getPositionForSection(sectionIndex: Int) = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    public fun updateBabies(babies: List<Baby>) {
        if (this.babies.isEmpty() || this.babies.size == babies.size) {
            showAllNames(babies)
        } else if (this.babies.size > babies.size) {
            removeMissingNames(babies)
        } else if (this.babies.size < babies.size) {
            addNewNames(babies)
        }
    }

    private fun addNewNames(names: List<Baby>) {
        // Insert all names from the incoming list, that are not already in the adapter

        names
                // Filter out all names that are in the adapter and in the incoming list
                .filter {
                    !this.babies.map { it.name }
                            .contains(it.name)
                }
                // The add all remaining names to the adapter
                .forEach {
                    this.babies = this.babies.plus(it)
                    this.notifyItemInserted(this.babies.size - 1)
                }
    }

    private fun removeMissingNames(names: List<Baby>) {
        // Remove all names from the adapter, that are missing already in the incoming list
        var itemsRemoved = 0
        for (i in this.babies.indices) {
            if (names
                    .filter { it.name == this.babies[i - itemsRemoved].name }
                    .isEmpty()) {
                this.babies = this.babies.minus(this.babies[i - itemsRemoved])
                this.notifyItemRemoved(i - itemsRemoved)
                itemsRemoved++
            }
        }
    }

    private fun showAllNames(names: List<Baby>) {
        this.babies = names
        this.notifyDataSetChanged()
    }
}
