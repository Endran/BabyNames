/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import nl.endran.babynames.fragments.NamesFragment

class MainActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewPager.adapter = PagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        //        fab.setOnClickListener {
        //            val e = BabyNameExtractor()
        //            val extract = e.extract(resources)
        //            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        //        }
    }

    private inner class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        // Fab: Search
        // Menu: Share, Export, Gender

        private val fragments: Array<Fragment> = arrayOf(
                NamesFragment.createFragment(NamesFragment.Type.POPULARITY),
                NamesFragment.createFragment(NamesFragment.Type.ALPHABET),
                NamesFragment.createFragment(NamesFragment.Type.FAVORITES))

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "${NamesFragment.getType(fragments[position].arguments)}"
        }
    }
}
