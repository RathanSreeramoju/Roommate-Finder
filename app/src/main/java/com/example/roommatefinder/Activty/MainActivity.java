package com.example.roommatefinder.Activty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roommatefinder.R;
import com.example.roommatefinder.singletone.GetNavController;
import com.example.roommatefinder.singletone.MySharedPref;
import com.example.roommatefinder.singletone.ProgressDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public DrawerLayout drawerLayout;
    public NavController navController;
    private NavigationView navigationView;
    private ImageView menu,profile_picture;
    private Toolbar toolbar;
    private View headerView;
    private TextView email1;
    private TextView login,logout;
    private ImageView log_img,logout_img;
    private FirebaseAuth mAuth;;
    private Context context;
    private MySharedPref sharedPref;
     //YOU CAN EDIT THIS TO WHATEVER YOU WANT
    private  AppBarConfiguration appBarConfiguration;
    private ImageView home,profile,post,fav,schedule;
    private TextView home1,profile1,post1,fav1,schedule1;
    private int PICK_IMAGE_REQUEST = 1;

    //for header layout
    private ImageView headerHome,headerProfile,headerPost,headerFavourite,headerCalendar;
    private TextView  headerHome1,headerProfile1,headerPost1,headerFavourite1,headerCalendar1;


    //for firebase
    private  StorageReference myStorage;
//    private Intent intent ;
    private GetNavController getNavController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        ProgressDialog.loaderForPagination(context);

//        intent = getIntent();
//
//        userEmail = intent.getStringExtra("emailId");
//        System.out.println("email login___"+userEmail);

        //init MySharedPref

        sharedPref = new MySharedPref(context);
        sharedPref.getInstance(context);
        sharedPref.getUserLoggedInData();
        //for firebase storage
        mAuth = FirebaseAuth.getInstance();
        myStorage = FirebaseStorage.getInstance().getReference();


        init();


    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        menu    = findViewById(R.id.menu);
//        navView = findViewById(R.id.nav_view);
        navigationView = findViewById(R.id.nav);
        drawerLayout   = findViewById(R.id.drawer_layout);

        headerView = navigationView.getHeaderView(0);
        profile_picture = headerView.findViewById(R.id.profile_image);
        email1           = headerView.findViewById(R.id.email);

        log_img         = headerView.findViewById(R.id.login);
        login           = headerView.findViewById(R.id.login_info);
        logout_img      = headerView.findViewById(R.id.logout);
        logout          = headerView.findViewById(R.id.logout_info);

        logout.setOnClickListener(this);
        logout_img.setOnClickListener(this);
        login.setOnClickListener(this);
        log_img.setOnClickListener(this);
        menu.setOnClickListener(this);
        profile_picture.setOnClickListener(this);

        //for costume bottom navigation view
        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        post = findViewById(R.id.post);
        fav = findViewById(R.id.favourite);
        schedule = findViewById(R.id.schedule);

        home1  = findViewById(R.id.home1);
        profile1 = findViewById(R.id.profile1);
        post1 = findViewById(R.id.post1);
        fav1 = findViewById(R.id.favourite1);
        schedule1 = findViewById(R.id.schedule1);


        home.setOnClickListener(this);
        profile.setOnClickListener(this);
        post.setOnClickListener(this);
        fav.setOnClickListener(this);
        schedule.setOnClickListener(this);

        home1.setOnClickListener(this);
        profile1.setOnClickListener(this);
        post1.setOnClickListener(this);
        fav1.setOnClickListener(this);
        schedule1.setOnClickListener(this);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        getNavController = new GetNavController(navController);
        //for header layout initialization

        headerHome = headerView.findViewById(R.id.home);
        headerProfile = headerView.findViewById(R.id.profile);
        headerPost   = headerView.findViewById(R.id.post);
        headerFavourite = headerView.findViewById(R.id.favourite);

        headerHome1 = headerView.findViewById(R.id.text_home);
        headerProfile1 = headerView.findViewById(R.id.contact_info);
        headerPost1   = headerView.findViewById(R.id.post_info);
        headerFavourite1 = headerView.findViewById(R.id.fav_info);


        headerHome.setOnClickListener(this);
        headerProfile.setOnClickListener(this);
        headerPost.setOnClickListener(this);
        headerFavourite.setOnClickListener(this);

        headerHome1.setOnClickListener(this);
        headerProfile1.setOnClickListener(this);
        headerPost1.setOnClickListener(this);
        headerFavourite1.setOnClickListener(this);



        //set User Image
         email1.setText(sharedPref.Email);

        try {
//            List<FileDownloadTask> tasks = myStorage.child("images/"+mAuth.getCurrentUser().getUid()).getActiveDownloadTasks();
//            System.out.println("size___"+tasks.size()+" uuid  "+myStorage.getName());
            System.out.println("uuid in splash___"+mAuth.getCurrentUser().getUid()+".jpg");
           myStorage.child("images").child(mAuth.getCurrentUser().getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    System.out.println("uri__"+uri);

//                    Toast.makeText(context, "Downloaded.", Toast.LENGTH_SHORT).show();
                     Glide.with(context).load(uri).apply(new RequestOptions().placeholder(R.drawable.profile)).error(R.drawable.profile).into(profile_picture);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
//                    Toast.makeText(context, "Unable to download.", Toast.LENGTH_SHORT).show();
                    System.out.println(":msg_____"+exception.getMessage());
                }
            });

        }catch (Exception e){
            System.out.println("exception in uploading images__"+e.getLocalizedMessage());
        }




    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_image:
                chooseUserImageFromGallery();
                break;

            case R.id.login:
                login();
                closeDrawer();
                break;
            case R.id.login_info:
                closeDrawer();
                login();
                break;
            case R.id.logout:
                closeDrawer();
                logout();
                break;
            case R.id.logout_info:
                closeDrawer();
                logout();
                break;

            case R.id.menu:
                checkedUserDrawer();
                break;
            case R.id.profile:
                setProfileFragment();
                closeDrawer();
                break;
            case R.id.home:
                setHomeFragment();
                closeDrawer();
                break;
            case R.id.post:
                setPostFragment();
                closeDrawer();
                break;

            case R.id.favourite:
                setFavouriteFragment();
                closeDrawer();

                break;

            case R.id.schedule:
                setScheduleFragment();
                closeDrawer();
                break;
            case R.id.text_home:
                setHomeFragment();
                closeDrawer();
                break;
            case R.id.contact_info:
                setProfileFragment();
                closeDrawer();
                break;
            case R.id.post_info:
                setPostFragment();
                closeDrawer();
                break;
            case R.id.fav_info:
                setFavouriteFragment();
                closeDrawer();
                break;



        }
    }

    private void setHomeFragment() {
        if(navController.getCurrentDestination().getId()==R.id.navigation_home){

        }else {
            navController.navigate(R.id.navigation_home);
        }
        profile.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.profile_click));
        home.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.home));
        post.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.post_click));
        fav.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.favorite_click));
        schedule.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.calender_click));

    }

    private void setPostFragment() {
        navController.navigate(R.id.navigation_post);
        profile.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.profile_click));
        home.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.home_click));
        post.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.post));
        fav.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.favorite_click));
        schedule.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.calender_click));

    }

    private void setFavouriteFragment() {
        navController.navigate(R.id.navigation_favourites);
        profile.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.profile_click));
        home.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.home_click));
        post.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.post_click));
        fav.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.favorite));
        schedule.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.calender_click));

    }

    private void setScheduleFragment() {
        navController.navigate(R.id.navigation_schedule);
        profile.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.profile_click));
        home.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.home_click));
        post.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.post_click));
        fav.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.favorite_click));
        schedule.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.calendar));

    }

    private void setProfileFragment() {
        navController.navigate(R.id.navigation_profile);
        profile.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.profile));
        home.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.home_click));
        post.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.post_click));
        fav.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.favorite_click));
        schedule.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.calender_click));

    }


    private void checkedUserDrawer() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        //check wheather user is login or not.
        System.out.println("logged_in mainactivty"+sharedPref.loggedin);
        if(sharedPref.loggedin){
            log_img.setVisibility(View.INVISIBLE);
            login.setVisibility(View.INVISIBLE);
            logout_img.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        }else {
            log_img.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
            logout_img.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.INVISIBLE);
        }
    }


    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void logout() {
        sharedPref.clearInstaData();
        startActivity(new Intent(this,LoginActivity.class));
        finishAffinity();
       sharedPref.loggedin=false;
       login.setVisibility(View.VISIBLE);
       log_img.setVisibility(View.VISIBLE);
       logout.setVisibility(View.INVISIBLE);
       logout_img.setVisibility(View.INVISIBLE);
       mAuth.signOut();

        Toast.makeText(context, "You are Successfully logout.", Toast.LENGTH_SHORT).show();
    }

    private void login() {
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }



    private void chooseUserImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//        Glide.with(context).load(filemanagerstring).apply(new RequestOptions().placeholder(R.drawable.profile)).error(R.drawable.profile).into(profile_picture);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                System.out.println("bitmap__"+bitmap);

                // Log.d(TAG, String.valueOf(bitmap));
        Glide.with(context).load(bitmap).apply(new RequestOptions().placeholder(R.drawable.profile)).error(R.drawable.profile).into(profile_picture);
                      uploadImagesToFirebase(bitmap,uri);


//                ImageView imageView = findViewById(R.id.imageView2);
//                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IOException___________"+ e.getMessage());
            }
        }
    }

    private void uploadImagesToFirebase(Bitmap bitmap, Uri uri) {



        final StorageReference imagePath = myStorage.child("images").child(mAuth.getCurrentUser().getUid()+".jpg");
        imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Image Not Uploaded.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

        System.out.println("nav id____"+navController.getCurrentDestination().getId());
       if(navController.getCurrentDestination().getId()==R.id.navigation_home){
           finishAffinity();

       }
        super.onBackPressed();

    }
}
