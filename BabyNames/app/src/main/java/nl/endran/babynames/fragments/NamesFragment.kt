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
import com.trello.rxlifecycle.RxLifecycle
import com.trello.rxlifecycle.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_names.*
import nl.endran.babynames.R
import nl.endran.babynames.SectionIndicator
import nl.endran.babynames.injections.AppComponent
import nl.endran.babynames.injections.getAppComponent
import nl.endran.babynames.names.BabyName
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller

abstract class NamesFragment : RxFragment() {

    private lateinit var adapter: NamesAdapter
    private lateinit var presenter: NamesFragmentPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_names, container, false)

        val fastScroller = view.findViewById(R.id.fastScroller) as VerticalRecyclerViewFastScroller
        val recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        val sectionIndicator = view.findViewById(R.id.sectionIndicator) as SectionIndicator

        adapter = getNamesAdapter()
        adapter.isFavorite = { presenter.isFavorite(it) }
        adapter.toggleFavorite = { presenter.toggleFavorite(it) }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        fastScroller.setRecyclerView(recyclerView);
        fastScroller.sectionIndicator = sectionIndicator;

        recyclerView.addOnScrollListener(fastScroller.onScrollListener)

        val appComponent = context.getAppComponent()
        val babyNameObservable = getBabyNameObservable(appComponent)
        val favoritesPreference = appComponent.favoritesPreference

        presenter = NamesFragmentPresenter(babyNameObservable, favoritesPreference.asObservable(), favoritesPreference.asAction())
        presenter.start()
        presenter.nameSubject
                .compose(RxLifecycle.bindFragment<List<BabyName>>(lifecycle()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ showNames(it) })

        return view
    }

    abstract  fun getNamesAdapter(): NamesAdapter

    abstract fun getBabyNameObservable(appComponent: AppComponent): Observable<List<BabyName>>

    override fun onDestroyView() {
        presenter.stop()
        recyclerView.removeOnScrollListener(fastScroller.onScrollListener);
        super.onDestroyView()
    }

    fun showNames(names: List<BabyName>) {
        if (adapter.names.isEmpty() || adapter.names.size == names.size) {
            showAllNames(names)
        } else if (adapter.names.size > names.size) {
            removeMissingNames(names)
        } else if (adapter.names.size < names.size) {
            addNewNames(names)
        }
    }

    private fun addNewNames(names: List<BabyName>) {
        // Insert all names from the incoming list, that are not already in the adapter

        names
                // Filter out all names that are in the adapter and in the incoming list
                .filter {
                    !adapter.names.map { it.name }
                            .contains(it.name)
                }
                // The add all remaining names to the adapter
                .forEach {
                    adapter.names = adapter.names.plus(it)
                    adapter.notifyItemInserted(adapter.names.size - 1)
                }
    }

    private fun removeMissingNames(names: List<BabyName>) {
        // Remove all names from the adapter, that are missing already in the incoming list
        var itemsRemoved = 0
        for (i in adapter.names.indices) {
            if (names
                    .filter { it.name == adapter.names[i - itemsRemoved].name }
                    .isEmpty()) {
                adapter.names = adapter.names.minus(adapter.names[i - itemsRemoved])
                adapter.notifyItemRemoved(i - itemsRemoved)
                itemsRemoved++
            }
        }
    }

    private fun showAllNames(names: List<BabyName>) {
        adapter.names = names
        adapter.notifyDataSetChanged()
    }
}
