package com.example.mye_commerceadminapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mye_commerceadminapp.R;
import com.example.mye_commerceadminapp.callbacks.OnProductItemClickListener;
import com.example.mye_commerceadminapp.databinding.FragmentProductDetailsBinding;
import com.example.mye_commerceadminapp.models.ProductsModel;
import com.example.mye_commerceadminapp.models.PurchaseModel;
import com.example.mye_commerceadminapp.viewmodels.ProductViewModel;
import com.squareup.picasso.Picasso;


public class ProductDetailsFragment extends Fragment{
    private FragmentProductDetailsBinding binding;
    ProductViewModel productViewModel;
    private String productId;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProductDetailsBinding.inflate(inflater);
        productViewModel=new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        productId=getArguments().getString("pid");
        Log.e("ABC", "sjsjsj"+productId );
        productViewModel.getProductByProductId(productId).observe(getViewLifecycleOwner(), productsModel -> {
            binding.setProduct(productsModel);
        });

        productViewModel.getPurchasesByProductId(productId)
                .observe(getViewLifecycleOwner(), purchaseModels -> {
                    String purchaseHistory = "";
                    for (PurchaseModel p : purchaseModels) {
                        purchaseHistory += "Purchased on: " +
                                p.getPurchaseDate()+"\n"+
                                "for BDT "+p.getPurchasePrice()+"\n"+
                                "with quantity: "+p.getPurchaseQuantity()+"\n\n";
                        binding.purchaseHistory.setText(purchaseHistory);
                    }
                });
        binding.updateBtn.setOnClickListener(v -> {
            showUpdatePriceAlertDialog();
        });
        return binding.getRoot();
    }

    private void showUpdatePriceAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Price");
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter new price");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(editText);
        builder.setPositiveButton("Update", (dialog, which) -> {
            final String price = editText.getText().toString();
            //validate
            productViewModel.updateProductPrice(productId, Double.parseDouble(price));
        });
        builder.setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

}

