package com.example.mye_commerceadminapp.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mye_commerceadminapp.R;
import com.example.mye_commerceadminapp.callbacks.OnProductItemClickListener;
import com.example.mye_commerceadminapp.databinding.ProductRowBinding;
import com.example.mye_commerceadminapp.models.ProductsModel;

public class ProductAdapter extends ListAdapter<ProductsModel, ProductAdapter.ProductViewHolder>{
    private OnProductItemClickListener listener;


    public ProductAdapter(Fragment fragment) {
        super(new ProductDiff());
        listener= (OnProductItemClickListener) fragment;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ProductRowBinding binding = ProductRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(getItem(position));
    }



    class ProductViewHolder extends RecyclerView.ViewHolder{
        ProductRowBinding binding;

        public ProductViewHolder(ProductRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.productRowCardView.setOnClickListener(v -> {
         listener.onProductItemClicked(getItem(getAdapterPosition()).getProductID());
            });
        }

        public void bind(ProductsModel item) {
            binding.setProduct(item);
        }
    }
    static class ProductDiff extends DiffUtil.ItemCallback<ProductsModel>{


        @Override
        public boolean areItemsTheSame(@NonNull ProductsModel oldItem, @NonNull ProductsModel newItem) {
            return oldItem.getProductID().equals(newItem.getProductID());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProductsModel oldItem, @NonNull ProductsModel newItem) {
            return oldItem.getProductID().equals(newItem.getProductID());
        }
    }

}
