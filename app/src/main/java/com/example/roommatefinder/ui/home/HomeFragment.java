package com.example.roommatefinder.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommatefinder.R;
import com.example.roommatefinder.singletone.GetNavController;
import com.example.roommatefinder.singletone.MySharedPref;
import com.example.roommatefinder.singletone.ProgressDialog;
import com.example.roommatefinder.ui.favourites.FavouriteViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private String[] PERMISSIONS=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int REQUEST_PERMISSION_KEY=100;
    private Context context;
    private RecyclerView recyclerView;
    private View view;
    private HomeAdapter adapter;
    private List<HomeModel> models = new ArrayList<HomeModel>();
    private List<FavouriteViewModel> modelsForFav = new ArrayList<FavouriteViewModel>();
    ///for FirebaseDatabase
   private DatabaseReference databaseReference,databaseReferenceForFav;
   private MySharedPref sharedPref;
    private String email;
    private boolean isFromFilter=false;
    private StorageReference myStorage;


    public HomeFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        sharedPref = new MySharedPref(context);
        sharedPref.getInstance(context);
        sharedPref.getUserLoggedInData();
        myStorage = FirebaseStorage.getInstance().getReference();
        //the progress
        ProgressDialog.loaderForPagination(context);
        ProgressDialog.progressDialog.show();

         if(sharedPref.Email.contains(".")){
             String[] ema = sharedPref.Email.split("\\.");
          email =  ema[0];
             System.out.println("email __"+email);

         }else {
             email = sharedPref.Email;
         }

        System.out.println("id in home fragment__"+sharedPref.UserId);
        getPermission();
        //for Filter
        try {
            isFromFilter = getArguments().getBoolean("isFromFilter");
            System.out.println("isFromFilter_______"+isFromFilter);
        }catch (Exception e){
            System.out.println("exception___"+e.getMessage()

            );
        }


    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_home, container, false);

         recyclerView = view.findViewById(R.id.recycler);
         adapter = new HomeAdapter(false,sharedPref.UserId,context,models,modelsForFav);
         recyclerView.setAdapter(adapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(context));
//         databaseReference = FirebaseDatabase.getInstance().getReference("UserData"+sharedPref.UserId);
//        '.', '#', '$', '[', or ']'
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("post");

            databaseReferenceForFav = FirebaseDatabase.getInstance().getReference("FavouriteData"+sharedPref.UserId);
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        goToFilterFragment( view);

        return view;

    }

    private void goToFilterFragment(View view) {
        view.findViewById(R.id.linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetNavController.getNavController().navigate(R.id.userFilterFragment);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

//        if(isFromFilter){
//
//        }else {


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    models.clear();
                     ProgressDialog.progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    System.out.println("name__"+dataSnapshot);
                        String key = dataSnapshot.getKey();
                        Object value = dataSnapshot.getValue();

                        System.out.println("name of user___" + value.toString());


                        HomeModel homeModel = dataSnapshot.getValue(HomeModel.class);
//                    System.out.println("model class__"+homeModel.getTxt_contact());
                        models.add(homeModel);
                    }
//                Toast.makeText(context, "home model data size"+models.size(), Toast.LENGTH_SHORT).show();

                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    ProgressDialog.progressDialog.dismiss();
                }
            });
            //for fav check
            forFav();

            getAllImages();
//        }

    }

    private void getAllImages() {

//        final StorageReference imagePath = myStorage.child("MyImage").child(post_key+".jpg");
//        imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                System.out.println("task snapshot__"+taskSnapshot);
//                Toast.makeText(context, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, "Image Not Uploaded.", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    private void forFav() {

       databaseReferenceForFav.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               modelsForFav.clear();
               for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                   FavouriteViewModel homeModel = dataSnapshot.getValue(FavouriteViewModel.class);

                   modelsForFav.add(homeModel);
               }
               adapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

           }
       });
    }

    private void getPermission() {
        // Checking if permission is not granted
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat
                        .requestPermissions(
                                (Activity) context,
                                PERMISSIONS,
                                REQUEST_PERMISSION_KEY);
            } else {
//                Toast.makeText(context, "Permission already granted", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_PERMISSION_KEY) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(context,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(context,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == REQUEST_PERMISSION_KEY) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(context,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


}