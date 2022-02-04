package com.example.mye_commerceadminapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mye_commerceadminapp.R;
import com.example.mye_commerceadminapp.databinding.FragmentLoginBinding;
import com.example.mye_commerceadminapp.viewmodels.LoginViewModel;

public class AdminLoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;

    public AdminLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLoginBinding.inflate(inflater);
        loginViewModel=new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        binding.loginBtn.setOnClickListener(v -> {
            final String email=binding.emailInputET.getText().toString();
            final String password=binding.passwordInputET.getText().toString();
            if(email.isEmpty()||password.isEmpty()){
                Toast.makeText(getActivity(), "Please provide all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginViewModel.login(email,password);

        });

        loginViewModel.getAuthLiveData().observe(getViewLifecycleOwner(), authState -> {
            if (authState == LoginViewModel.AuthState.AUTHENTICATED) {
                Navigation.findNavController(container)
                        .navigate(R.id.login_to_dashboard_action);
            }
        });
        loginViewModel.getErrMsgLiveData()
                .observe(getViewLifecycleOwner(), errMsg -> {
                    binding.errMsgTV.setText(errMsg);
                });
        return binding.getRoot();
    }
}