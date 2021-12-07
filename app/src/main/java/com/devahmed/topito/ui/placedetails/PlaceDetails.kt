package com.devahmed.topito.ui.placedetails

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.devahmed.topito.R
import com.devahmed.topito.databinding.PlaceDetailsFragmentBinding
import com.devahmed.topito.models.Place


class PlaceDetails : Fragment() {

    private lateinit var viewModel: PlaceDetailsViewModel
    private var _binding: PlaceDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = PlaceDetailsFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val place = arguments?.getParcelable<Place>("Place")
        binding.detailsTitle.text = place?.title
        binding.detailsDescription.text = place?.description
        binding.detailsRate.text = place?.rate.toString()
        binding.detailsTags.text = place?.tags?.get(0)
        Glide
            .with(this)
            .load(place?.image)
            .centerCrop()
            .placeholder(R.drawable.image_30)
            .into(binding.detailsImage)
        Log.i("Details", "loaded here${place?.image}")
        binding.detailsOpenmaps.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"))
            startActivity(intent)
        }
        binding.detailsCallnow.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this.requireContext(),android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE),
                    1)

            } else {
            // else block means user has already accepted.And make your phone call here.
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + place?.phones?.get(0)))
                startActivity(intent)
            }
        }

        binding.detailsCallnow.setOnLongClickListener{
            val clipboard: ClipboardManager? =
                this.requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("phone number", place?.phones?.get(0))
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(this.requireContext(), "Number copied!", Toast.LENGTH_LONG).show()
            true
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlaceDetailsViewModel::class.java)

        // TODO: Use the ViewModel
    }

}