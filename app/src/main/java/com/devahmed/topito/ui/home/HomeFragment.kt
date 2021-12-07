package com.devahmed.topito.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.devahmed.topito.R
import com.devahmed.topito.databinding.FragmentHomeBinding
import com.devahmed.topito.models.Place
import com.devahmed.topito.utils.FireStoreStatus
import com.google.firebase.FirebaseApp

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    lateinit var placesRvAdapter: PlacesRvAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var observer: Observer<List<Place>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        placesRvAdapter = PlacesRvAdapter(places = listOf<Place>(),
            listener = PlacesRvAdapter.Listener { user ->
            onPlaceRvItemClicked(user)
        })
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.placesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = placesRvAdapter
        }

        binding.swipToRefreshLayout.setOnRefreshListener {
            homeViewModel.getAllPlaces()
            binding.swipToRefreshLayout.setRefreshing(false);
        }

        observer = Observer<List<Place>> { placesList ->
            placesRvAdapter.places = placesList
            placesRvAdapter.notifyDataSetChanged()
        }

        homeViewModel.places.observe(viewLifecycleOwner , observer)

        homeViewModel.status.observe(viewLifecycleOwner, { status ->
            when(status){
                FireStoreStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                FireStoreStatus.DONE -> {
                    binding.progressBar.visibility = View.GONE
                }
                FireStoreStatus.ERROR -> {

                }
            }
        })
        return root
    }

    private fun onPlaceRvItemClicked(place:Place) {
        var placeBundle = bundleOf("Place" to place)
        this.findNavController().navigate(R.id.action_nav_home_to_placeDetails,
            placeBundle,
            navOptions { // Use the Kotlin DSL for building NavOptions
                anim {
                    enter = android.R.anim.slide_in_left
                    exit = android.R.anim.slide_out_right
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}