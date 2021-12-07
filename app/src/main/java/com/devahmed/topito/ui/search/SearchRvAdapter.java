package com.devahmed.topito.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devahmed.topito.R;
import com.devahmed.topito.models.Place;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchRvAdapter extends RecyclerView.Adapter<SearchRvAdapter.ViewHolder>
        implements Filterable {

    private ArrayList<Place> places;
    private ArrayList<Place> searchPlacesList;
    public interface Listener{
        void onPlaceItemClicked(Place place);
    }
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public SearchRvAdapter() {
    }

    public SearchRvAdapter(ArrayList<Place> places) {
        this.places = places;
        this.searchPlacesList = places;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_view_item,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Place item = searchPlacesList.get(position);
        holder.placeTitle.setText(item.getTitle());
        holder.placeAddress.setText(item.getAddress());
        holder.placeTag.setText(item.getTags().get(0));
        Glide.with(holder.placeImage.getContext())
                .load(item.getImage())
                .placeholder(R.drawable.image_30)
                .into(holder.placeImage);
        holder.itemView.setOnClickListener(view -> {
            listener.onPlaceItemClicked(item);
        });
    }

    @Override
    public int getItemCount() {
        return searchPlacesList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSearch = charSequence.toString();
                if (charSearch.isEmpty()) {
                    searchPlacesList = places;
                } else {
                    ArrayList<Place> resultList = new ArrayList<>();
                    for (Place place : places) {
                        if (place.getTitle().toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(place);
                        }
                    }
                    searchPlacesList = resultList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = searchPlacesList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                searchPlacesList = (ArrayList<Place>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setData(@Nullable List<Place> placesList) {
        this.places = (ArrayList<Place>) placesList;
        this.searchPlacesList = this.places;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeTitle, placeAddress, placeTag;
        ImageView placeImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeTitle = itemView.findViewById(R.id.place_title);
            placeAddress = itemView.findViewById(R.id.place_address);
            placeTag = itemView.findViewById(R.id.place_tags);
            placeAddress = itemView.findViewById(R.id.place_address);
            placeImage = itemView.findViewById(R.id.place_image);

        }
    }



}
