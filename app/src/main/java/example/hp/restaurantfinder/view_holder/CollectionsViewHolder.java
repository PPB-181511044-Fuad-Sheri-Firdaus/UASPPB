package example.hp.restaurantfinder.view_holder;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import example.hp.restaurantfinder.databinding.CollectionItemBinding;
import example.hp.restaurantfinder.model.CollectionResponse;

public class CollectionsViewHolder extends RecyclerView.ViewHolder {

    private CollectionItemBinding binding;
    public ImageView collectionImage;

    public CollectionsViewHolder(@NonNull CollectionItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        collectionImage = binding.ivImage;
    }

    public void bindData(CollectionResponse model) {
        binding.setCollection(model);
        binding.executePendingBindings();
    }

}
