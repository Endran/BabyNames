/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_names.*
import kotlinx.android.synthetic.main.row_item_name.view.*
import nl.endran.babynames.injections.getAppComponent
import kotlin.collections.arrayListOf

class AllNamesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_names, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appComponent = context.getAppComponent()
        val extractor = appComponent.babyNameExtractor
        val adapter = NamesAdapter()

        //        extractor.babyNames.names.toObservable()
        //                .skip(20)
        //                .delay(200, TimeUnit.MILLISECONDS)
        //                .take(40)
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe {
        //                    adapter.names.add(it)
        //                    adapter.notifyItemInserted(adapter.names.size - 1)
        //                }

        adapter.names = extractor.babyNames.names

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private inner class NamesAdapter : RecyclerView.Adapter<NamesAdapter.ViewHolder>() {
        var names: MutableList<BabyName> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
            val view = LayoutInflater.from(context).inflate(R.layout.row_item_name, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return names.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.textView.text = names[position].name
            holder.itemView.checkbox.isChecked = position % 2 == 1
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    }
}
