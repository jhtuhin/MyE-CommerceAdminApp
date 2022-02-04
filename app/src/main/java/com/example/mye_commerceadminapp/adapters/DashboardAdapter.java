package com.example.mye_commerceadminapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mye_commerceadminapp.callbacks.OnDashboardItemSelectedListener;
import com.example.mye_commerceadminapp.databinding.DashboardItemRowBinding;
import com.example.mye_commerceadminapp.models.DashboardItem;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {
    DashboardItemRowBinding binding;
    private List<DashboardItem> items;
    private OnDashboardItemSelectedListener listener;

    public DashboardAdapter(Fragment fragment, List<DashboardItem> items) {
        this.items = items;
       this.listener= (OnDashboardItemSelectedListener) fragment;
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding=DashboardItemRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new DashboardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class DashboardViewHolder extends RecyclerView.ViewHolder{
        DashboardItemRowBinding binding;

        public DashboardViewHolder(DashboardItemRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.dashItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position=getAdapterPosition();
                    listener.onDashboardItemSelected(items.get(position).getItemName());
                }
            });
        }

        public void bind(DashboardItem item) {
            binding.setItems(item);
        }
    }
}
