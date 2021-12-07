package com.devahmed.topito.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.devahmed.topito.R
import com.devahmed.topito.databinding.PlaceViewItemBinding
import com.devahmed.topito.models.Place
import com.devahmed.topito.utils.ItemAnimation
import java.util.*
import kotlin.collections.ArrayList

class PlacesRvAdapter(var places: List<Place>, var listener: Listener, var animationType: Int = 2) :
    Adapter<PlacesRvAdapter.ViewHolder>(), Filterable {

    private var searchPlacesList: MutableList<Place> = places.toMutableList()

//    init {
//        places.forEach({
//            searchPlacesList.add(it)
//        })
//        Log.i("adapter", "" + searchPlacesList.size + " : " + places.size)
//        notifyDataSetChanged()
//    }

    fun setData(data: List<Place>){
        this.searchPlacesList = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlaceViewItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = if (searchPlacesList.size == 0) places.get(position) else places.get(position)

        holder.binding.placeTitle.text = place.title
        holder.binding.placeAddress.text = place.address
        holder.binding.placeTags.text = place.tags?.get(0)
        Glide
            .with(holder.binding.placeImage.context)
            .load(place?.image)
            .centerCrop()
            .placeholder(R.drawable.image_30)
            .into(holder.binding.placeImage)

        holder.itemView.setOnClickListener {
            listener.onClick(place)
        }

        setAnimation(holder.itemView, position)
    }


    override fun getItemCount(): Int {
        return if (searchPlacesList.size == 0) places.size else places.size
    }

    inner class ViewHolder(val binding: PlaceViewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class Listener(val clickListener: (Place: Place) -> Unit) {
        fun onClick(Place: Place) = clickListener(Place)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                on_attach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    private var lastPosition = -1
    private var on_attach = true


    private fun setAnimation(view: View, position: Int) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, if (on_attach) position else -1, animationType)
            lastPosition = position
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    searchPlacesList = places.toMutableList()
                } else {
                    val resultList = ArrayList<Place>()
                    for (row in places) {
                        if (row.title.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    searchPlacesList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = searchPlacesList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                searchPlacesList = results?.values as ArrayList<Place>
                print("places are:${searchPlacesList.get(0).title}")
                notifyDataSetChanged()
            }

        }
    }


}