package com.example.roommatefinder.ui.favourites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roommatefinder.R;
import com.example.roommatefinder.ui.home.HomeAdapter;
import com.example.roommatefinder.ui.home.HomeModel;
import com.example.roommatefinder.ui.post.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder>{

    private List<FavouriteViewModel> favModel;
    private Context context;
    private View view;
    private String UserId;
    private DatabaseReference firebaseDatabase;
    private boolean is_fav;

    public FavAdapter(DatabaseReference firebaseDatabase,boolean is_fav,String userId,Context context,List<FavouriteViewModel> favModel){
        this.firebaseDatabase = firebaseDatabase;
        this.context = context;
        this.UserId = userId;
        this.is_fav=is_fav;
        this.favModel=favModel;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.recycler_layout,parent,false);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("FavouriteData"+UserId);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final FavouriteViewModel model = favModel.get(position);
        final String post_key = model.getPostKey();
        holder.title.setText(model.getTxt_title());
        holder.desc.setText(model.getTxt_desc());
        holder.roommates.setText("$"+model.getTxt_Price());
        holder.location.setText(model.getTxt_loc());
        holder.gender.setText(model.getTxt_gender());
        holder.contact_number.setText(model.getTxt_contact());
        Glide.with(context).load(model.getUserPhoto()).apply(new RequestOptions().placeholder(R.drawable.profile)).error(R.drawable.profile).into(holder.imageView);
        holder.star.setVisibility(View.GONE);
        holder.fav_star.setVisibility(View.VISIBLE);
        holder.fav_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favModel.remove(holder.getAdapterPosition());
                System.out.println("data base child__"+firebaseDatabase.child(model.getPostKey()));
                firebaseDatabase.child(model.getPostKey()).removeValue();
                notifyDataSetChanged();


            }
        });


    }

    @Override
    public int getItemCount() {
        return favModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageView;
        private ImageView star,fav_star;
        private TextView title,desc,roommates,gender,location,contact_number;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.post);
            desc  = itemView.findViewById(R.id.post_description);
            roommates = itemView.findViewById(R.id.roomates);
            gender    = itemView.findViewById(R.id.gender);
            location  = itemView.findViewById(R.id.location);
            contact_number = itemView.findViewById(R.id.contact_number);
            imageView      = itemView.findViewById(R.id.profile_image);
            star           = itemView.findViewById(R.id.fav);
            fav_star       = itemView.findViewById(R.id.fav1);
        }
    }
}
