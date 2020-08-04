package com.example.roommatefinder.ui.post;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.roommatefinder.R;
import com.example.roommatefinder.singletone.GetNavController;
import com.example.roommatefinder.singletone.ProgressDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference myref;
//    private static final int RESULT_OK = 2 ;
    private View addPost1;
    private TextView txt_title,txt_desc,txt_loc,txt_roommates,txt_gender,txt_contact,txt_add;
    private ImageView postImage;

    private Button addBtn;
    private Uri uri  = null;
    private Context context;
    private DatabaseReference firebaseDatabase;
    private Bitmap bitmap;

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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void openGallery() {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//        Glide.with(context).load(filemanagerstring).apply(new RequestOptions().placeholder(R.drawable.profile)).error(R.drawable.profile).into(profile_picture);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(context, "result", Toast.LENGTH_SHORT).show();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            System.out.println("uri is___"+uri);

            try {
                 bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

                System.out.println("bitmap in Post__"+bitmap);

                // Log.d(TAG, String.valueOf(bitmap));
                Glide.with(context).load(bitmap).apply(new RequestOptions().bitmapTransform(new RoundedCorners(20)).placeholder(R.drawable.profile)).error(R.drawable.profile).into(postImage);
//                uploadImagesToFirebase(bitmap,uri);


//                ImageView imageView = findViewById(R.id.imageView2);
//                imageView.setImageBitmap(bitmap);
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

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("UserData");

        postImage = addPost1.findViewById(R.id.add_photo);
        txt_title = addPost1.findViewById(R.id.post_title);
        txt_desc = addPost1.findViewById(R.id.post_description);
        txt_loc = addPost1.findViewById(R.id.location);
        txt_gender = addPost1.findViewById(R.id.gender);
        txt_roommates = addPost1.findViewById(R.id.roomates);
        txt_contact = addPost1.findViewById(R.id.contact_number);
        addBtn = addPost1.findViewById(R.id.post);
        myref = FirebaseDatabase.getInstance().getReference("Posts");

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBtn.setVisibility(View.INVISIBLE);
//                ProgressDialog.progressDialog.show();

                if(!txt_title.getText().toString().isEmpty() && !txt_desc.getText().toString().isEmpty() && !txt_loc.getText().toString().isEmpty()
                        && !txt_gender.getText().toString().isEmpty()&& !txt_contact.getText().toString().isEmpty()&&
                        !txt_roommates.getText().toString().isEmpty()){
                    String post_title = txt_title.getText().toString();
                    String post_desc  = txt_desc.getText().toString();
                    String room_mates = txt_roommates.getText().toString();
                    String gender     = txt_gender.getText().toString();
                    String location   = txt_loc.getText().toString();
                    String contact_number = txt_contact.getText().toString();


                    String post_key = firebaseDatabase.push().getKey();

                    Post post = new Post(post_key,post_title,post_desc,room_mates,gender,location,contact_number,uri.toString());
                    firebaseDatabase.child(post_key).setValue(post);
                    Toast.makeText(context, "Data Added.", Toast.LENGTH_SHORT).show();
                    GetNavController.getNavController().popBackStack(R.id.navigation_home,false);

//                    ProgressDialog.progressDialog.dismiss();


                }
                else {
                    showMessage("Please check all input values");
                    addBtn.setVisibility((View.VISIBLE));


                }
            }
        });

    }



    private void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}