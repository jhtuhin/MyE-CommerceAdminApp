package com.example.mye_commerceadminapp.viewmodels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mye_commerceadminapp.models.ProductsModel;
import com.example.mye_commerceadminapp.models.PurchaseModel;
import com.example.mye_commerceadminapp.utils.AdminConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private final FirebaseFirestore db=FirebaseFirestore.getInstance();
    public MutableLiveData<List<String>> categoryListLiveData=new MutableLiveData<>();
    public MutableLiveData<List<ProductsModel>> productsLiveData=new MutableLiveData<>();
    public MutableLiveData<PurchaseModel> purchaseLiceData=new MutableLiveData<>();

    public ProductViewModel(){
        getAllCategories();
        getAllProducts();
    }

    public void addNewProduct(ProductsModel productsModel, PurchaseModel purchaseModel){
       final WriteBatch writeBatch= db.batch();
       final  DocumentReference proDoc=db.collection(AdminConstants.dbCollections.COLLECTION_PRODUCT).document();
       productsModel.setProductID(proDoc.getId());
       final DocumentReference purDoc=db.collection(AdminConstants.dbCollections.COLLECTION_PURCHASE).document();
       purchaseModel.setPurchaseId(purDoc.getId());
       purchaseModel.setProductId(proDoc.getId());

       writeBatch.set(proDoc,productsModel);
       writeBatch.set(purDoc,purchaseModel);

       writeBatch.commit().addOnSuccessListener(unused -> {

       }).addOnFailureListener(e -> {

       });
    }

    private void getAllCategories(){
        db.collection(AdminConstants.dbCollections.COLLECTION_CATEGORY)
                .addSnapshotListener((value, error) -> {
                    if(error != null) return;;
                    final List<String> items=new ArrayList<>();
                    for(DocumentSnapshot doc: value.getDocuments()){
                        items.add(doc.get("name",String.class));
                    }
                    categoryListLiveData.postValue(items);
                });
    }

    public void getAllProducts(){
        db.collection(AdminConstants.dbCollections.COLLECTION_PRODUCT).addSnapshotListener((value, error) -> {
            if(error != null) return;;

            final List<ProductsModel> items=new ArrayList<>();
            for (DocumentSnapshot doc : value.getDocuments()) {
                items.add(doc.toObject(ProductsModel.class));
            }

            productsLiveData.postValue(items);
        });
    }

    public LiveData<ProductsModel> getProductByProductId(String productId){
        final MutableLiveData<ProductsModel> productLiveData=new MutableLiveData<>();
        db.collection(AdminConstants.dbCollections.COLLECTION_PRODUCT).document(productId)
                .addSnapshotListener((value, error) -> {
            if(error !=null) return;
            productLiveData.postValue(value.toObject(ProductsModel.class));
        });
        return productLiveData;
    }

    public LiveData<List<PurchaseModel>> getPurchasesByProductId(String productId){
        final MutableLiveData<List<PurchaseModel>> purchaseListId=new MutableLiveData<>();
        db.collection(AdminConstants.dbCollections.COLLECTION_PURCHASE).whereEqualTo("productId",productId)
                .addSnapshotListener((value, error) -> {
                    if(error !=null) return;
                    final List<PurchaseModel> items=new ArrayList<>();
                    for(DocumentSnapshot doc : value.getDocuments()){
                        items.add(doc.toObject(PurchaseModel.class));
                    }
                    purchaseListId.postValue(items);
                });
        return purchaseListId;
    }
    public void updateProductPrice(String productId, double price){
        final DocumentReference doc=db.collection(AdminConstants.dbCollections
                .COLLECTION_PRODUCT).document(productId);
        doc.update("price",price).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {

        });
    }

}
