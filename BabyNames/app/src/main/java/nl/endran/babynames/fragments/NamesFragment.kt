/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_names.*
import nl.endran.babynames.R
import nl.endran.babynames.SectionIndicator
import nl.endran.babynames.injections.getAppComponent
import nl.endran.babynames.names.BabyName
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller

class NamesFragment : RxFragment() {

    companion object {

        val TYPE_KEY = "TYPE_KEY"

        fun createFragment(type: NamesUtilFactory.Type): NamesFragment {
            val fragment = NamesFragment()
            fragment.arguments = Bundle()
            fragment.arguments.putInt(TYPE_KEY, type.ordinal)
            return fragment
        }

        fun getType(arguments: Bundle): NamesUtilFactory.Type {
            val typeOrdinal = arguments.getInt(TYPE_KEY, 0)
            return NamesUtilFactory.Type.values()[typeOrdinal]
        }
    }

    private val adapter = NamesAdapter()
    private var presenter: NamesFragmentPresenter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_names, container, false)

        val fastScroller = view.findViewById(R.id.fastScroller) as VerticalRecyclerViewFastScroller
        val recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        val sectionIndicator = view.findViewById(R.id.sectionIndicator) as SectionIndicator

        adapter.isFavorite = { presenter?.isFavorite(it) ?: false }
        adapter.toggleFavorite = { presenter?.toggleFavorite(it) }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        fastScroller.setRecyclerView(recyclerView);
        fastScroller.sectionIndicator = sectionIndicator;

        recyclerView.addOnScrollListener(fastScroller.onScrollListener)

        return view
    }

    override fun onDestroyView() {
        recyclerView.removeOnScrollListener(fastScroller.onScrollListener);
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()

        val appComponent = context.getAppComponent()
        val namesUtilFactory = appComponent.namesUtilFactory
        presenter = namesUtilFactory.createPresenter(getType(arguments))
        presenter?.start(this)
    }

    override fun onPause() {
        super.onPause()
        presenter?.stop()
        presenter = null
    }

    fun showNames(names: MutableList<BabyName>) {
        if (adapter.names.isEmpty() || adapter.names.size == names.size) {
            showAllNames(names)
        } else if (adapter.names.size > names.size) {
            removeMissingNames(names)
        } else if (adapter.names.size < names.size) {
            addNewNames(names)
        }
    }

    private fun addNewNames(names: MutableList<BabyName>) {
        names.forEach {
            if (!adapter.names.contains(it)) {
                val index = names.indexOf(it)
                adapter.names.add(index, it)
                adapter.notifyItemInserted(index)
            }
        }
    }

    private fun removeMissingNames(names: MutableList<BabyName>) {
        var itemsRemoved = 0
        for (i in adapter.names.indices) {
            if (names
                    .filter { it.name == adapter.names[i - itemsRemoved].name }
                    .isEmpty()) {
                adapter.names.removeAt(i - itemsRemoved)
                adapter.notifyItemRemoved(i - itemsRemoved)
                itemsRemoved++
            }
        }
    }

    private fun showAllNames(names: MutableList<BabyName>) {
        adapter.names = names
        adapter.notifyDataSetChanged()
    }

}
