package com.example.roommatefinder.ui.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.roommatefinder.Activty.MainActivity;
import com.example.roommatefinder.R;
import com.example.roommatefinder.singletone.GetNavController;
import com.example.roommatefinder.singletone.MySharedPref;
import com.example.roommatefinder.singletone.ProgressDialog;
import com.example.roommatefinder.ui.filter.FilterInfo;
import com.example.roommatefinder.ui.listOfImages.AllImages;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.net.InternetDomainName;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment  {
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference myref;
//    private static final int RESULT_OK = 2 ;
    private View addPost1;
    private TextView txt_title,txt_desc,txt_loc,txt_roommates,txt_gender,txt_contact,txt_add;
    private ImageView postImage;

    private Button addBtn;
    private Uri uri  = null;
    private Context context;
    private DatabaseReference firebaseDatabase,filterDatabase,priceDatabase,locationDatabase,databaseBasedOnPriceAndLocation;
    private Bitmap bitmap;
    private MySharedPref sharedPref;


    //spinner
    private Spinner spUnitType,spBedrooms,spGender;
    private EditText contact_number1,location1,price1;
    private String selectedBedRooms="",selectedUnitType = "",selectedGender = "";
    private String email;
    private  StorageReference myStorage;



    public PostFragment(){

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addPost1 = inflater.inflate(R.layout.fragment_post, container, false);
        return addPost1;

    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
//        ProgressDialog.loaderForPagination(context);

        sharedPref = new MySharedPref(context);
        sharedPref.getInstance(context);
        sharedPref.getUserLoggedInData();
        System.out.println("user id in Post__"+sharedPref.UserId);
        if(sharedPref.Email.contains(".")){
            String[] ema = sharedPref.Email.split("\\.");
            email =  ema[0];
            System.out.println("email __"+email);

        }else {
            email = sharedPref.Email;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void openGallery() {

//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(context, "result", Toast.LENGTH_SHORT).show();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            System.out.println("Uri path"+data.getData().getPath()+"encoded path__"+data.getData().getEncodedPath());

            System.out.println("uri is___"+uri);

            try {
                 bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

                System.out.println("bitmap in Post__"+bitmap);

                // Log.d(TAG, String.valueOf(bitmap));
                Glide.with(context).load(bitmap).apply(new RequestOptions().bitmapTransform(new RoundedCorners(20)).placeholder(R.drawable.profile)).error(R.drawable.profile).into(postImage);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IOException___________"+ e.getMessage());
                Toast.makeText(context, "Image not shown", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

//        firebaseDatabase = FirebaseDatabase.getInstance().getReference("UserData"+sharedPref.UserId);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("post");
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Admin_Email_"+email);


        postImage = addPost1.findViewById(R.id.add_photo);

        addBtn = addPost1.findViewById(R.id.post);
        contact_number1 = addPost1.findViewById(R.id.contact_number);
        location1       = addPost1.findViewById(R.id.location);
        price1           = addPost1.findViewById(R.id.price);
        myref = FirebaseDatabase.getInstance().getReference("Posts");

        spUnitType = addPost1.findViewById(R.id.spUnitType);
        spBedrooms  = addPost1.findViewById(R.id.spBedrooms);
        spGender    = addPost1.findViewById(R.id.spGender);


         setListenerOnSpinner();


        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog.progressDialog.show();
                final String location   = location1 .getText().toString();
                String contact_number = contact_number1.getText().toString();
                final String priceOf        =         price1.getText().toString().trim();


                if(!selectedUnitType.isEmpty()&&!selectedBedRooms.isEmpty()&&!selectedGender.isEmpty()&&!priceOf.isEmpty()&&!location.isEmpty()
                        &&!contact_number.isEmpty()){


                    final String post_key = firebaseDatabase.push().getKey();
                    try {
                        final Post post = new Post(post_key, selectedUnitType, selectedBedRooms, priceOf,selectedGender, location, contact_number, uri.toString());



                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadImagesToFirebase(post_key,uri);
                                        getFilterDatabase(location,priceOf,selectedUnitType,selectedBedRooms,post);
                                        firebaseDatabase.child(post_key).setValue(post);
                                        addBtn.setVisibility(View.INVISIBLE);
                                    }
                                },2000);




                    }catch (Exception e){

                        System.out.println("exception___"+e.getMessage());
                        Toast.makeText(context, "Please check all input values.", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ProgressDialog.progressDialog.dismiss();
                            }
                        },2000);

                    }
                }
                else {
                    showMessage("Please check all input values.");
                    addBtn.setVisibility((View.VISIBLE));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialog.progressDialog.dismiss();
                        }
                    },2000);


                }
            }
        });

    }


    private void uploadImagesToFirebase(String post_key, Uri uri) {
        myStorage = FirebaseStorage.getInstance().getReference();

        final StorageReference imagePath = myStorage.child("MyImage").child(post_key+".jpg");
        imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("task snapshot__"+taskSnapshot);
                ProgressDialog.progressDialog.dismiss();
                Toast.makeText(context, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                GetNavController.getNavController().popBackStack(R.id.navigation_home,false);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ProgressDialog.progressDialog.dismiss();
                        dissmissProgressDialog();
                        Toast.makeText(context, "Image Not Uploaded.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void dissmissProgressDialog() {

    }


    private void getFilterDatabase(String location, String priceOf, String selectedUnitType, String selectedBedRooms, Post post) {
//        FilterInfo filter = new FilterInfo(selectedUnitType,selectedBedRooms,priceOf,location);
        priceDatabase = FirebaseDatabase.getInstance().getReference("FilterDatabaseBasedOnPrice"+priceOf);
        locationDatabase = FirebaseDatabase.getInstance().getReference("FilterDatabaseBasedOnLocation"+location);
        databaseBasedOnPriceAndLocation = FirebaseDatabase.getInstance().getReference("FilterDatabaseBasedOnLocationAndPrice"+location+priceOf);
        filterDatabase = FirebaseDatabase.getInstance().getReference("FilterDatabase"+selectedUnitType+selectedBedRooms+priceOf+location);
        try {
            filterDatabase.child(post.getPostKey()).setValue(post);
            priceDatabase.child(post.getPostKey()).setValue(post);
            locationDatabase.child(post.getPostKey()).setValue(post);
            databaseBasedOnPriceAndLocation.child(post.getPostKey()).setValue(post);
//            Toast.makeText(context, "done in filter", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void setListenerOnSpinner() {
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(position != 0){

                    selectedGender = parent.getItemAtPosition(position).toString();
//                    Toast.makeText(context, ""+selectedGender, Toast.LENGTH_SHORT).show();
                    System.out.println("selected genedre__"+selectedGender);
                }
                else {
                    selectedGender = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spUnitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(position != 0){
                    selectedUnitType = parent.getItemAtPosition(position).toString();
//                    Toast.makeText(context, ""+selectedUnitType, Toast.LENGTH_SHORT).show();
                }
                else {
                    selectedUnitType = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spBedrooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if(position != 0){
                    selectedBedRooms = parent.getItemAtPosition(position).toString();
//                    Toast.makeText(context, ""+selectedBedRooms, Toast.LENGTH_SHORT).show();
                }
                else {
                    selectedBedRooms = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void showMessage(String message) {

        Toast.makeText(getActivity().getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }


}