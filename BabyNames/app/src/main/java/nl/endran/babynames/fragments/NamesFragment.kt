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
import android.widget.SectionIndexer
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_names.*
import kotlinx.android.synthetic.main.row_item_name.view.*
import nl.endran.babynames.R
import nl.endran.babynames.injections.getAppComponent
import nl.endran.babynames.names.BabyName

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
        return inflater?.inflate(R.layout.fragment_names, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        fastScroller.setRecyclerView(recyclerView);
        fastScroller.sectionIndicator = sectionIndicator;

        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
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
            adapter.names = names
            adapter.notifyDataSetChanged()
        } else if (adapter.names.size > names.size) {
            var itemsRemoved = 0
            for (i in adapter.names.indices) {
                if (names
                        .filter { it.name == adapter.names[i - itemsRemoved].name }
                        .isNotEmpty()) {
                    adapter.names.removeAt(i - itemsRemoved)
                    adapter.notifyItemRemoved(i - itemsRemoved)
                    itemsRemoved++
                }
            }
        } else if (adapter.names.size < names.size) {
            names.forEach {
                if (!adapter.names.contains(it)) {
                    val index = names.indexOf(it)
                    adapter.names.add(index, it)
                    adapter.notifyItemInserted(index)
                }
            }
        }
    }

    private inner class NamesAdapter : RecyclerView.Adapter<NamesAdapter.ViewHolder>(), SectionIndexer {
        var names: MutableList<BabyName> = arrayListOf()

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
            itemView.textView.text = name.name

            val checkbox = itemView.checkbox
            checkbox.isClickable = false
            checkbox.isChecked = presenter?.isFavorite(name.name) ?: false
            itemView.setOnClickListener {
                presenter?.toggleFavorite(name.name)
                checkbox.isChecked = presenter?.isFavorite(name.name) ?: false
            }
        }

        override fun getSectionForPosition(position: Int): Int {
            if (position >= names.size) {
                return 26
            }

            val indexOf = sections!!.indexOf(names[position].name.first())
            return if (indexOf >= 0 && indexOf < 26) indexOf else 26
        }

        override fun getSections(): Array<out Any>? {
            return arrayOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                    'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#')
        }

        override fun getPositionForSection(sectionIndex: Int): Int {
            return 0
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    }
}
