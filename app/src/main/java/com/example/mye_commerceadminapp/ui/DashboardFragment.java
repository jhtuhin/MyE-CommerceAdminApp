package com.example.mye_commerceadminapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mye_commerceadminapp.R;
import com.example.mye_commerceadminapp.adapters.DashboardAdapter;
import com.example.mye_commerceadminapp.callbacks.OnDashboardItemSelectedListener;
import com.example.mye_commerceadminapp.databinding.FragmentDashboardBinding;
import com.example.mye_commerceadminapp.models.DashboardItem;
import com.example.mye_commerceadminapp.utils.AdminConstants;
import com.example.mye_commerceadminapp.viewmodels.LoginViewModel;

public class DashboardFragment extends Fragment implements OnDashboardItemSelectedListener {
    private FragmentDashboardBinding binding;
    private LoginViewModel loginViewModel;


    public DashboardFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentDashboardBinding.inflate(inflater);
        loginViewModel=new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        loginViewModel.getAuthLiveData().observe(getViewLifecycleOwner(), authState -> {
            if (authState == LoginViewModel.AuthState.UNAUTHENTICATED) {
                Navigation.findNavController(container)
                        .navigate(R.id.dashboard_to_login_action);
            }
        });
        final DashboardAdapter adapter=new DashboardAdapter(this,DashboardItem.getDashboardItems());
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        binding.dashboardRV.setLayoutManager(gridLayoutManager);
        binding.dashboardRV.setAdapter(adapter);
       return binding.getRoot();
    }

    @Override
    public void onDashboardItemSelected(String item) {
        switch (item){
            case AdminConstants.Item.ADD_PRODUCT:
                Navigation.findNavController(getActivity(),R.id.navContainer).navigate(R.id.dashboard_to_addProduct);
                break;
            case AdminConstants.Item.VIEW_PRODUCT:
                Navigation.findNavController(getActivity(),
                        R.id.navContainer)
                        .navigate(R.id.dashboard_to_productList_action);
                break;
        }

    }
}