package example.hp.restaurantfinder.view_holder;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import example.hp.restaurantfinder.databinding.SearchItemBinding;
import example.hp.restaurantfinder.model.SearchResponse;

public class SearchViewHolder extends RecyclerView.ViewHolder {

    private SearchItemBinding binding;
    public ImageView restImage;
    public TextView tvRating;
    public TextView tvLocate;
    public TextView tvViewUrl;

    public SearchViewHolder(SearchItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        restImage = binding.ivImage;
        tvRating = binding.tvRating;
        tvLocate = binding.tvLocate;
        tvViewUrl = binding.tvUrl;
    }

    public void bindData(SearchResponse model) {
        binding.setSearch(model);
        binding.executePendingBindings();
    }
}
