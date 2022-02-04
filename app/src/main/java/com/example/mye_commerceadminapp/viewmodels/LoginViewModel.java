package com.example.mye_commerceadminapp.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mye_commerceadminapp.callbacks.OnCheckAdminListener;
import com.example.mye_commerceadminapp.repository.AdminRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    public enum AuthState {
        AUTHENTICATED, UNAUTHENTICATED
    }
    private MutableLiveData<AuthState> authLiveData;
    private MutableLiveData<String> errMsgLiveData;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public LoginViewModel() {
        authLiveData = new MutableLiveData<>();
        errMsgLiveData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            authLiveData.postValue(AuthState.UNAUTHENTICATED);
        }else {
            authLiveData.postValue(AuthState.AUTHENTICATED);
        }
    }

    public LiveData<AuthState> getAuthLiveData() {
        return authLiveData;
    }

    public LiveData<String> getErrMsgLiveData() {
        return errMsgLiveData;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    //user = authResult.getUser();

                    checkAdmin(authResult.getUser().getUid());
                    //authLiveData.postValue(AuthState.AUTHENTICATED);
                }).addOnFailureListener(e -> {
                    errMsgLiveData.postValue(e.getLocalizedMessage());
                });
    }

    private void checkAdmin(String uid){
        AdminRepository.checkAdmin(uid, status -> {
            if(status){
                user=auth.getCurrentUser();
                authLiveData.postValue(AuthState.AUTHENTICATED);
            }else{
                errMsgLiveData.postValue("You are not an admin.");
            }
        });
    }

    public void logout() {
        if (user != null) {
            auth.signOut();
            authLiveData.postValue(AuthState.UNAUTHENTICATED);
        }
    }

    public boolean isEmailVerified() {
        return user.isEmailVerified();
    }

    public void sendVerificationMail() {
        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
