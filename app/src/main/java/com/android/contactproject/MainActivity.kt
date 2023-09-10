package com.android.contactproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.android.contactproject.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val tabList = listOf("ContactList", "Favorites", "MyPage")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabList[position]
        }.attach()

        // tabLayout의 tabItem 찾기
        for(i in 0 until binding.tabLayout.tabCount) {
            val tab = binding.tabLayout.getTabAt(i)
            val tabView = tab?.view

            // 선택한 Tab 스타일 변경
            if(i == binding.viewPager.currentItem) {
                tab?.text = tabList[i]
                tab?.view?.findViewById<com.google.android.material.textview.MaterialTextView>(com.google.android.material.R.id.text)?.setTextColor(resources.getColor(R.color.black))
            } else {
                tabView?.setBackgroundResource(R.color.black)
                tab?.text = tabList[i]
                tab?.view?.findViewById<com.google.android.material.textview.MaterialTextView>(com.google.android.material.R.id.title)?.setTextColor(resources.getColor(R.color.white))
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var currentState = 0
            var currentPosition = 0

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (currentState == ViewPager2.SCROLL_STATE_DRAGGING && currentPosition == position) {
                    if (currentPosition == 0) binding.viewPager.currentItem = 2
                    else if (currentPosition == 2) binding.viewPager.currentItem = 0
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                super.onPageSelected(position)

                // 선택한, 선택하지 않은 tab 바 업데이트
                for(i in 0 until binding.tabLayout.tabCount) {
                    val tab = binding.tabLayout.getTabAt(i)
                    val tabView = tab?.view

                    // 선택한 Tab 스타일 변경
                    if(i == binding.viewPager.currentItem) {
                        tabView?.setBackgroundResource(R.color.white)
                        tab?.text = tabList[i]
                        tab?.view?.findViewById<com.google.android.material.textview.MaterialTextView>(com.google.android.material.R.id.title)?.setTextColor(resources.getColor(R.color.black))
                    } else {
                        tabView?.setBackgroundResource(R.color.black)
                        tab?.text = tabList[i]
                        tab?.view?.findViewById<com.google.android.material.textview.MaterialTextView>(com.google.android.material.R.id.title)?.setTextColor(resources.getColor(R.color.white))
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                currentState = state
                super.onPageScrollStateChanged(state)
            }
        })
    }
}