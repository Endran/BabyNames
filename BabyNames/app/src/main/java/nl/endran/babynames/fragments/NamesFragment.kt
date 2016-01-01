/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_names.*
import kotlinx.android.synthetic.main.row_item_name.view.*
import nl.endran.babynames.R
import nl.endran.babynames.injections.getAppComponent

class NamesFragment : Fragment() {

    companion object {

        val TYPE_KEY = "TYPE_KEY"

        fun createFragment(type: Type): NamesFragment {
            val fragment = NamesFragment()
            fragment.arguments = Bundle()
            fragment.arguments.putInt(TYPE_KEY, type.ordinal)
            return fragment
        }

        fun getType(arguments: Bundle): Type {
            val typeOrdinal = arguments.getInt(TYPE_KEY, 0)
            return Type.values()[typeOrdinal]
        }
    }

    public enum class Type {
        ALPHABET, POPULARITY, FAVORITES
    }

    private val adapter = NamesAdapter()
    private var presenter: NamesFragmentPresenter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_names, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }


    override fun onResume() {
        super.onResume()

        val appComponent = context.getAppComponent()
        presenter = appComponent.allNamesFragmentPresenter
        presenter?.start(this, getType(arguments))
    }

    override fun onPause() {
        super.onPause()
        presenter?.stop()
        presenter = null
    }

    fun showNames(names: MutableList<String>) {
        adapter.names = names
    }

    private inner class NamesAdapter : RecyclerView.Adapter<NamesAdapter.ViewHolder>() {
        var names: MutableList<String> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
            val view = LayoutInflater.from(context).inflate(R.layout.row_item_name, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return names.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val name = names[position]

            val itemView = holder.itemView
            itemView.textView.text = name

            val checkbox = itemView.checkbox
            checkbox.isClickable = false
            checkbox.isChecked = presenter?.isFavorite(name) ?: false
            itemView.setOnClickListener {
                presenter?.toggleFavorite(name)
                checkbox.isChecked = presenter?.isFavorite(name) ?: false
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    }
}
