package com.example.roommatefinder.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.roommatefinder.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{
    private List<HomeModel> homeViewModelList;
    private Context context;
    private View view;

    public HomeAdapter(Context context,List<HomeModel> list){
        this.homeViewModelList=list;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.recycler_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       HomeModel model =  homeViewModelList.get(position);
       holder.title.setText(model.getTxt_title());
       holder.desc.setText(model.getTxt_desc());
       holder.roommates.setText(model.getTxt_roommates());
       holder.location.setText(model.getTxt_loc());
       holder.gender.setText(model.getTxt_gender());
       holder.contact_number.setText(model.getTxt_contact());
        Glide.with(context).load(model.getUserPhoto()).apply(new RequestOptions().placeholder(R.drawable.profile)).error(R.drawable.profile).into(holder.imageView);

    }

    @Override
    public int getItemCount() {

        return homeViewModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageView;
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


        }
    }

}
