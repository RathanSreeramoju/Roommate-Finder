package com.example.roommatefinder.ui.filter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.roommatefinder.R;
import com.example.roommatefinder.singletone.MySharedPref;
import com.example.roommatefinder.singletone.ProgressDialog;
import com.example.roommatefinder.ui.favourites.FavouriteViewModel;
import com.example.roommatefinder.ui.home.HomeAdapter;
import com.example.roommatefinder.ui.home.HomeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FiltrateUserFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private Context context;
    private MySharedPref sharedPref;
    private HomeAdapter adapter;
    private List<HomeModel> models = new ArrayList<HomeModel>();
    private List<FavouriteViewModel> modelsForFav = new ArrayList<FavouriteViewModel>();
    private DatabaseReference databaseReference,databaseReferenceForFav;
    private String location,flate_type,unit_type,price;


    public FiltrateUserFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        sharedPref = new MySharedPref(context);
        sharedPref.getInstance(context);
        sharedPref.getUserLoggedInData();
        ProgressDialog.progressDialog.show();

        try {
            location = getArguments().getString("location");
            unit_type = getArguments().getString("unitType");
            price     = getArguments().getString("price");
            flate_type = getArguments().getString("beds");

        }catch (Exception e){
            System.out.println("exception___"+e.getMessage());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_filtrate_user, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        adapter = new HomeAdapter(false,sharedPref.UserId,context,models,modelsForFav);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("FilterDatabase"+unit_type+flate_type+price+location);

            databaseReferenceForFav = FirebaseDatabase.getInstance().getReference("FavouriteData"+sharedPref.UserId);
        }catch (Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();

        super.onStart();

//        if(isFromFilter){
//
//        }else {
        models.clear();
        modelsForFav.clear();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProgressDialog.progressDialog.dismiss();
                if(snapshot.getValue()==null){
                    RelativeLayout rel=view.findViewById(R.id.rel);
                    rel.setVisibility(View.VISIBLE);
                }
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
                ProgressDialog.progressDialog.dismiss();
                Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //for fav check
        forFav();
//        }
    }
    private void forFav() {

        databaseReferenceForFav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
}
