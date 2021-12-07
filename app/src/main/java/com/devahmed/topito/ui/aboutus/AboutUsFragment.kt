package com.devahmed.topito.ui.aboutus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.devahmed.topito.databinding.FragmentAboutusBinding

class AboutUsFragment : Fragment() {

    private lateinit var aboutUsViewModel: AboutUsViewModel
    private var _binding: FragmentAboutusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        aboutUsViewModel =
            ViewModelProvider(this).get(AboutUsViewModel::class.java)

        _binding = FragmentAboutusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}