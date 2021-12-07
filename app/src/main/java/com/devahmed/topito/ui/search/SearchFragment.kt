package com.devahmed.topito.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.devahmed.topito.R
import com.devahmed.topito.databinding.FragmentSearchBinding
import com.devahmed.topito.models.Place
import com.devahmed.topito.utils.FireStoreStatus


class SearchFragment : Fragment(), SearchRvAdapter.Listener {

    private lateinit var searchViewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var placesRvAdapter: SearchRvAdapter
    private lateinit var observer: Observer<List<Place>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        placesRvAdapter = SearchRvAdapter(ArrayList())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.searchView.isIconifiedByDefault = false
        binding.searchView.requestFocus()

        placesRvAdapter.setListener(this)
        binding.searchRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = placesRvAdapter
        }

        observer = Observer<List<Place>> { placesList ->
            placesRvAdapter.setData(placesList)
        }

        searchViewModel.places.observe(viewLifecycleOwner , observer)

        searchViewModel.status.observe(viewLifecycleOwner, { status ->
            when(status){
                FireStoreStatus.LOADING -> {
                    binding.progressBarSearch.visibility = View.VISIBLE
                }
                FireStoreStatus.DONE -> {
                    binding.progressBarSearch.visibility = View.GONE
                }
                FireStoreStatus.ERROR -> {

                }
            }
        })

        binding.searchView.query
        binding.searchView.queryHint = "Search"
        binding.searchView.setOnFocusChangeListener { v,hasFocus ->
            placesRvAdapter.filter.filter("")
        }
        // perform set on query text listener event
        // perform set on query text listener event
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                placesRvAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                placesRvAdapter.filter.filter(newText)
                return false
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPlaceItemClicked(place: Place?) {
        var placeBundle = bundleOf("Place" to place)
        this.findNavController().navigate(R.id.action_nav_search_to_placeDetails,
            placeBundle,
            navOptions { // Use the Kotlin DSL for building NavOptions
                anim {
                    enter = android.R.anim.slide_in_left
                    exit = android.R.anim.slide_out_right
                }
            }
        )
    }
}