package com.example.myfacebook

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_tabhost.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TabHostFragment : Fragment() {


    private lateinit var tabAdapter: TabAdapter

    private val viewModel: PagerViewModel by viewModel()

    private val tabChangeListener by lazy {
        object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // TODO("Not yet implemented")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // TODO("Not yet implemented")
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> {
                            translate()
                        }
                        1 -> {
                            translateDown()
                        }
                        else -> {
                            //no-op
                        }
                    }
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tabhost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.currentUser() == null) {
            findNavController().popBackStack()
            findNavController().navigate(R.id.logInFragment)
            return
        }
        toolbar_main.inflateMenu(R.menu.search)
        toolbar_main.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    findNavController().navigate(TabHostFragmentDirections.actionTabHostFragmentToSearchFragment())
                    true
                }
                else -> false
            }
        }
        tabAdapter = TabAdapter(this)
        pager.adapter = tabAdapter
        tab_layout.addOnTabSelectedListener(tabChangeListener)
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.ic_home)
                1 -> tab.setIcon(R.drawable.ic_profile)
                else -> {
                    //no-op
                }
            }
        }.attach()


        val list = viewModel.getTabs()
        tabAdapter.data = list
    }

    private fun translate() {
        val animator = ObjectAnimator.ofFloat(toolbar_main, View.TRANSLATION_Y, 0f)
        val animator2 = ObjectAnimator.ofFloat(tab_layout, View.TRANSLATION_Y, 0f)
        animator.start()
        animator2.start()
    }

    private fun translateDown() {
        val animator = ObjectAnimator.ofFloat(toolbar_main, View.TRANSLATION_Y, -200f)
        val animator2 = ObjectAnimator.ofFloat(tab_layout, View.TRANSLATION_Y, -200f)
        animator.start()
        animator2.start()
    }
}