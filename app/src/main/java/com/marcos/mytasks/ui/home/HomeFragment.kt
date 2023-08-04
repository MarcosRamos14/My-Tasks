package com.marcos.mytasks.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marcos.mytasks.R
import com.marcos.mytasks.databinding.FragmentHomeBinding
import com.marcos.mytasks.ui.home.tabs.DoingFragment
import com.marcos.mytasks.ui.home.tabs.DoneFragment
import com.marcos.mytasks.ui.home.tabs.TodoFragment

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(
        inflater,
        container,
        false
    ).apply {
       binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        setupTabLayout()
        setupListener()
    }

    private fun setupListener() {
        binding.imgBtnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        auth.signOut()
        findNavController().navigate(R.id.action_homeFragment_to_authentication)
    }

    private fun setupTabLayout() {
        val adapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        adapter.addFragment(TodoFragment(), R.string.form_task_todo)
        adapter.addFragment(DoingFragment(), R.string.form_task_doing)
        adapter.addFragment(DoneFragment(), R.string.form_task_done)

        binding.viewPager.offscreenPageLimit = adapter.itemCount

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) {
            tab, position ->
            tab.text = getString(adapter.getTitle(position))
        }.attach()
    }
}