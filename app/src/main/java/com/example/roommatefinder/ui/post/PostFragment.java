package com.example.roommatefinder.ui.post;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.roommatefinder.MainActivity;
import com.example.roommatefinder.ProfileActivity;
import com.example.roommatefinder.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.BreakIterator;

public class PostFragment extends Fragment {
    DatabaseReference myref;
    private static final int RESULT_OK = 2 ;
    Dialog addPost1;
    TextView txt_title,txt_desc,txt_loc,txt_roommates,txt_gender,txt_contact,txt_add;
    ImageView imageView,postImage;
    TextView userPhoto;
    TextView picture;
    Button addBtn;
    ProgressBar progressBar;
    private static final int PreqCode= 2;
    private static final int RequesCode= 2;
    private Uri imageViewUri  = null;

    public PostFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);

    }

    private void inipopup() {
        addPost1 = new Dialog(getContext());
        addPost1.setContentView(R.layout.fragment_post);
        addPost1.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.MATCH_PARENT);
        addPost1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        addPost1.getWindow().getAttributes().gravity = Gravity.TOP;
        addPost1.show();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inipopup();




    }

    private void setupPostImage() {
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermission();
            }
        });
    }

    private void checkAndRequestForPermission() {

        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity().getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions((Activity) getActivity().getApplicationContext(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PreqCode);
            }

        }
        else
            openGallery();
    }

    private void openGallery() {

        Intent galleryIntent =  new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType(("image/*"));
        startActivityForResult(galleryIntent,RequesCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RequesCode && data != null ){
             imageViewUri = data.getData();


        }
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        postImage = addPost1.findViewById(R.id.add_photo);
        txt_title = addPost1.findViewById(R.id.post_title);
        txt_desc = addPost1.findViewById(R.id.post_description);
        txt_loc = addPost1.findViewById(R.id.location);
        txt_gender = addPost1.findViewById(R.id.gender);
        txt_roommates = addPost1.findViewById(R.id.roomates);
        txt_contact = addPost1.findViewById(R.id.contact_number);
        addBtn = addPost1.findViewById(R.id.post);
        progressBar = addPost1.findViewById(R.id.progressBar);
        myref = FirebaseDatabase.getInstance().getReference("Posts");

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                if(!txt_title.getText().toString().isEmpty() && !txt_desc.getText().toString().isEmpty() && !txt_loc.getText().toString().isEmpty()
                        && !txt_gender.getText().toString().isEmpty()&& !txt_contact.getText().toString().isEmpty()&& !txt_roommates.getText().toString().isEmpty()){

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blob_images");
                    final StorageReference imageFilePath = storageReference.child((imageViewUri.getLastPathSegment()));
                    imageFilePath.putFile(imageViewUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();
                                    Post post = new Post(txt_title.getText().toString(), txt_gender.getText().toString(), txt_roommates.getText().toString(),
                                            txt_desc.getText().toString(), txt_loc.getText().toString(), txt_contact.getText().toString(),userPhoto.getText().toString()
                                    ,picture.getText().toString(),imageDownloadLink);
                                            addPost(post);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showMessage((e.getMessage()));
                                    progressBar.setVisibility(View.INVISIBLE);
                                    addBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });




                }
                else {
                    showMessage("Please check all input values");
                    addBtn.setVisibility((View.VISIBLE));
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        String key = myRef.getKey();
        post.setPostKey(key);
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added Successfully");
                progressBar.setVisibility(View.INVISIBLE);
                addBtn.setVisibility(View.VISIBLE);
                addPost1.dismiss();
            }
        });


    }

    private void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}