package com.example.roommatefinder.ui.userPost;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.roommatefinder.R;
import com.example.roommatefinder.singletone.MySharedPref;
import com.example.roommatefinder.ui.favourites.FavAdapter;
import com.example.roommatefinder.ui.favourites.FavouriteViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserPostFragment extends Fragment {
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private FavAdapter adapter;
//    private List<HomeModel> models = new ArrayList<HomeModel>();

    private List<FavouriteViewModel> models = new ArrayList<FavouriteViewModel>();
    ///for FirebaseDatabase
    private DatabaseReference databaseReference;
    private MySharedPref sharedPref;
    private String email;

    public UserPostFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        sharedPref = new MySharedPref(context);
        sharedPref.getInstance(context);
        sharedPref.getUserLoggedInData();
        if(sharedPref.Email.contains(".")){
            String[] ema = sharedPref.Email.split("\\.");
            email =  ema[0];
            System.out.println("email __"+email);

        }else {
            email = sharedPref.Email;
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Admin_Email_" +email);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_post, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        adapter = new FavAdapter(databaseReference, true, sharedPref.UserId, context, models);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        models.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    System.out.println("name__"+dataSnapshot);
                    String key = dataSnapshot.getKey();
//                    Object value =  dataSnapshot.getValue();

//                        System.out.println("name of user___"+value.toString());


                    FavouriteViewModel homeModel = dataSnapshot.getValue(FavouriteViewModel.class);
//                    System.out.println("model class__"+homeModel.getTxt_contact());
                    models.add(homeModel);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}