/*
 * Copyright (c) 2015 by David Hardy. Licensed under the Apache License, Version 2.0.
 */

package nl.endran.babynames

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import nl.endran.babynames.fragments.AllNamesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewPager.adapter = GameFragmentPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

//        fab.setOnClickListener {
//            val e = BabyNameExtractor()
//            val extract = e.extract(resources)
//            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
//        }
    }

    private inner class GameFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        // All, Populair, Favo
        // Fab: Search
        // Menu: Share, Export, Gender

        private val fragments: Array<Fragment> = arrayOf(
                AllNamesFragment(), Fragment(), Fragment())

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "Pos=$position"
        }
    }
}
