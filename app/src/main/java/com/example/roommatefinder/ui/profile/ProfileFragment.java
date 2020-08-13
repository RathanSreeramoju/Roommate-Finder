package com.example.roommatefinder.ui.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roommatefinder.R;
import com.example.roommatefinder.singletone.MySharedPref;
import com.example.roommatefinder.ui.home.HomeAdapter;
import com.example.roommatefinder.ui.home.HomeModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private View view;
    private CircleImageView imageView;
    private TextView email;
    private Context context;
    private MySharedPref sharedPref;
    private FirebaseAuth mAuth;
    private StorageReference myStorage;
    private ImageView logout;
    private List<UserProfile> userProfiles = new ArrayList<>();
    private ProfileAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference profileDatabase;
    private String email1;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =getContext();
        sharedPref = new MySharedPref(context);
        sharedPref.getInstance(context);
        sharedPref.getUserLoggedInData();
        //for firebase storage
        mAuth = FirebaseAuth.getInstance();
        myStorage = FirebaseStorage.getInstance().getReference();
        if(sharedPref.Email.contains(".")){
            String[] ema = sharedPref.Email.split("\\.");
            email1 =  ema[0];
            System.out.println("email __"+email);

        }else {
            email1 = sharedPref.Email;
        }


    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       view = inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        adapter = new ProfileAdapter(context,userProfiles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        imageView = view.findViewById(R.id.profile_image);
        email     = view.findViewById(R.id.email);
        logout    = view.findViewById(R.id.logout);

        try {
            profileDatabase = FirebaseDatabase.getInstance().getReference("Schedule"+email1);
            System.out.println("uuid in splash___"+mAuth.getCurrentUser().getUid()+".jpg");
            email.setText(sharedPref.Email);
            myStorage.child("images").child(mAuth.getCurrentUser().getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {


                    Glide.with(context).load(uri).apply(new RequestOptions().placeholder(R.drawable.profile)).error(R.drawable.profile).into(imageView);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    System.out.println(":msg_____"+exception.getMessage());
                }
            });

        }catch (Exception e){
            System.out.println("exception in uploading images__"+e.getLocalizedMessage());
        }

        getUserProfileData();
        logOutTheProfile();


    }

    private void getUserProfileData() {

        profileDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Object value = dataSnapshot.getValue();
                    System.out.println("name of user___" + value.toString());
                    UserProfile homeModel = dataSnapshot.getValue(UserProfile.class);
                   userProfiles.add(homeModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something Went Wrong Please try Again.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void logOutTheProfile() {
        // logout purpose
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout.setVisibility(View.GONE);
                sharedPref.clearInstaData();
                sharedPref.loggedin=false;
                mAuth.signOut();
                Toast.makeText(context, "You are Successfully logout.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}