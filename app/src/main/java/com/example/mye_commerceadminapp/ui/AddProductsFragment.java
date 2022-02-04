package com.example.mye_commerceadminapp.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.mye_commerceadminapp.R;
import com.example.mye_commerceadminapp.databinding.FragmentAddProductsBinding;
import com.example.mye_commerceadminapp.models.ProductsModel;
import com.example.mye_commerceadminapp.models.PurchaseModel;
import com.example.mye_commerceadminapp.pickers.DatePickerDialogFragment;
import com.example.mye_commerceadminapp.utils.AdminConstants;
import com.example.mye_commerceadminapp.viewmodels.LoginViewModel;
import com.example.mye_commerceadminapp.viewmodels.ProductViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddProductsFragment extends Fragment {
    private final String TAG = AddProductsFragment.class.getSimpleName();
    private ProductViewModel productViewModel;
    private List<String> categories=new ArrayList<>();
    private FragmentAddProductsBinding binding;
    String dateString;
    String imageUrl,category;
    int year;
    int month;
    int day;

    private ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Uri photoUri = result.getData().getData();
                                if (photoUri != null) {
                                    binding.productIV.setImageURI(photoUri);
                                }else {
                                    final Bitmap bitmap = (Bitmap) result.getData()
                                            .getExtras().get("data");
                                    binding.productIV.setImageBitmap(bitmap);
                                }

                                binding.saveBtn.setText("Please wait");
                                binding.saveBtn.setEnabled(false);
                                uploadImage(photoUri);
                            }
                        }
                    });

    public AddProductsFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     binding=FragmentAddProductsBinding.inflate(inflater);
     productViewModel=new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

     productViewModel.categoryListLiveData.observe(getViewLifecycleOwner(), catList -> {
         categories.clear();
         categories.add("Select category");
         categories.addAll(catList);
         final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getActivity(),
                 android.R.layout.simple_dropdown_item_1line,categories);
         binding.categorySP.setAdapter(arrayAdapter);
     });

        binding.categorySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    category = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

     binding.selectDateBtn.setOnClickListener(v -> {
         new DatePickerDialogFragment().show(getChildFragmentManager(),null);
     });

     binding.captureBtn.setOnClickListener(v -> {
         Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         try {
             launcher.launch(takePictureIntent);
         }catch (ActivityNotFoundException e) {

         }
     });

     binding.galleryBtn.setOnClickListener(v -> {
         final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
         intent.setType("image/*");
         launcher.launch(intent);
     });

     binding.saveBtn.setOnClickListener(v -> saveProducts());

        getChildFragmentManager().setFragmentResultListener(AdminConstants.REQUEST_KEY, this, (requestKey, result) -> {
            if (result.containsKey(AdminConstants.DATE_KEY)){
                dateString=result.getString(AdminConstants.DATE_KEY);
                day=result.getInt(AdminConstants.DAY);
                month=result.getInt(AdminConstants.MONTH);
                year=result.getInt(AdminConstants.YEAR);
                binding.selectDateBtn.setText(dateString);
            }
        });
     return binding.getRoot();
    }

    private void saveProducts() {
        final String productName=binding.productNameET.getText().toString();
        final String sellPrice=binding.sellPriceET.getText().toString();
        final String purchasePrice=binding.purchasePriceET.getText().toString();
        final String quantity=binding.quantityET.getText().toString();
        final String productDescription=binding.productDescriptionET.getText().toString();
        if(productName.isEmpty()|| sellPrice.isEmpty()|| purchasePrice.isEmpty()|| quantity.isEmpty()){
            Toast.makeText(getActivity(), "Please provide all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(category==null){
            Toast.makeText(getActivity(), "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dateString==null){
            Toast.makeText(getActivity(), "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        if(imageUrl==null){
            Toast.makeText(getActivity(), "Please provide an image", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProductsModel productsModel=new ProductsModel(productName,null,category,
                imageUrl,Double.parseDouble(sellPrice),productDescription,true);

        final PurchaseModel purchaseModel=new PurchaseModel(null,null,dateString,day,
                month,year,Double.parseDouble(purchasePrice),Integer.parseInt(quantity));


        productViewModel.addNewProduct(productsModel, purchaseModel);

    }

    private void uploadImage(Uri photoUri) {
       // Log.e("firebasestorage", photoUri.toString());
        final StorageReference photoRef =
                FirebaseStorage.getInstance().getReference()
                        .child("images/"+System.currentTimeMillis());

        // Get the data from an ImageView as bytes
        binding.productIV.setDrawingCacheEnabled(true);
        binding.productIV.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) binding.productIV.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = photoRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Log.e("firebasestorage", exception.getLocalizedMessage());
                binding.saveBtn.setText("Save");
                binding.saveBtn.setEnabled(true);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               // Log.e("firebasestorage", "Uploaded");
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return photoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();
                    binding.saveBtn.setText("Save");
                    binding.saveBtn.setEnabled(true);
                   // Log.e("firebasestorage", downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}