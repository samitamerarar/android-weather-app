@file:Suppress("PropertyName")

package com.example.simpleweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.simpleweather.fragments.FirstFragment
import com.example.simpleweather.fragments.SecondFragment
import java.util.*


class MainActivity : AppCompatActivity() {
    private var list: MutableList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.add(FirstFragment())
        list.add(SecondFragment())

        var pageAdapter = ViewPagerAdapter(supportFragmentManager, list)
        var viewPager: VerticalViewPager = findViewById(R.id.pager)
        viewPager.adapter = pageAdapter
    }
}
