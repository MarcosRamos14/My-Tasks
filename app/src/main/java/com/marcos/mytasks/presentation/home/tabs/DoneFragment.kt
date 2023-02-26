package com.marcos.mytasks.presentation.home.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marcos.mytasks.databinding.FragmentDoneBinding

class DoneFragment : Fragment() {

    private lateinit var binding: FragmentDoneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDoneBinding.inflate(
    inflater,
    container,
    false
    ).apply {
        binding = this
    }.root
}