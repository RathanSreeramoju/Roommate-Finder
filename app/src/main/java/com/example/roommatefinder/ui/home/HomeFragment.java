package com.example.roommatefinder.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommatefinder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private String[] PERMISSIONS=new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int REQUEST_PERMISSION_KEY=100;
    private HomeModel homeViewModel;
    private Context context;
    private RecyclerView recyclerView;
    private View view;
    private HomeAdapter adapter;
    private List<HomeModel> models = new ArrayList<HomeModel>();
    ///for FirebaseDatabase
   private DatabaseReference databaseReference;

    public HomeFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        getPermission();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_home, container, false);

         recyclerView = view.findViewById(R.id.recycler);
         adapter = new HomeAdapter(context,models);
         recyclerView.setAdapter(adapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(context));
         databaseReference = FirebaseDatabase.getInstance().getReference("UserData");
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        models.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    System.out.println("name__"+dataSnapshot);
                    String key = dataSnapshot.getKey();
                    Object value =  dataSnapshot.getValue();

                        System.out.println("name of user___"+value.toString());



                    HomeModel homeModel = dataSnapshot.getValue(HomeModel.class);
                    System.out.println("model class__"+homeModel.getTxt_contact());
                    models.add(homeModel);
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