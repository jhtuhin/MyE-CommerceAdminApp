package com.example.mye_commerceadminapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mye_commerceadminapp.R;
import com.example.mye_commerceadminapp.adapters.ProductAdapter;
import com.example.mye_commerceadminapp.callbacks.OnProductItemClickListener;
import com.example.mye_commerceadminapp.databinding.FragmentProductListBinding;
import com.example.mye_commerceadminapp.models.ProductsModel;
import com.example.mye_commerceadminapp.viewmodels.ProductViewModel;

import java.util.List;

public class ProductListFragment extends Fragment implements OnProductItemClickListener {
    private FragmentProductListBinding binding;
    private ProductViewModel productViewModel;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding=FragmentProductListBinding.inflate(inflater);
       productViewModel=new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
       final ProductAdapter adapter=new ProductAdapter(this);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        binding.productRV.setLayoutManager(linearLayoutManager);
        binding.productRV.setAdapter(adapter);
        productViewModel.productsLiveData.observe(getViewLifecycleOwner(), productsModels -> {
            adapter.submitList(productsModels);
        });
       return binding.getRoot();
    }

    @Override
    public void onProductItemClicked(String productId) {
        final Bundle bundle = new Bundle();
        bundle.putString("pid",productId);
        Navigation.findNavController(getActivity(), R.id.navContainer).navigate(R.id.productList_to_productDetails_action,bundle);
    }
}