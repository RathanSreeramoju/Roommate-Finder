package com.example.roommatefinder.ui.home;

import android.content.Context;

import android.net.Uri;
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
import com.example.roommatefinder.ui.favourites.FavouriteViewModel;
import com.example.roommatefinder.ui.post.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{
    private List<HomeModel> homeViewModelList;
    private List<FavouriteViewModel> favModel;
    private Context context;
    private View view;
    private String UserId;
    private DatabaseReference firebaseDatabase;
    private boolean is_fav;
    private List<String> favId= new ArrayList<String>();
    private List<String> UsrId= new ArrayList<String>();
    private StorageReference myStorage;

    public HomeAdapter(boolean is_fav,String userId,Context context,List<HomeModel> list,List<FavouriteViewModel> favModel){
        this.homeViewModelList=list;
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
        myStorage = FirebaseStorage.getInstance().getReference();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {


       final HomeModel model =  homeViewModelList.get(position);
       getAllImg(model,position,holder);
//       FavouriteViewModel favouriteViewModel = favModel.get(position);
       final String post_key = model.getPostKey();
       holder.title.setText(model.getTxt_title());
       holder.desc.setText(model.getTxt_desc());
       holder.roommates.setText("$"+model.getTxt_Price());
        System.out.println("get your price__"+model.getTxt_Price());
       holder.location.setText(model.getTxt_loc());
       holder.gender.setText(model.getTxt_gender());
       holder.contact_number.setText(model.getTxt_contact());
//        Glide.with(context).load(model.getUserPhoto()).apply(new RequestOptions().placeholder(R.drawable.profile)).error(R.drawable.profile).into(holder.imageView);
        if(!is_fav) {
            holder.star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    holder.star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.fav_star));
                    holder.fav_star.setVisibility(View.VISIBLE);
                    holder.star.setVisibility(View.INVISIBLE);

                    Post post = new Post(post_key, model.getTxt_title(), model.getTxt_desc(), model.getTxt_Price()
                            , model.getTxt_gender(), model.getTxt_loc(), model.getTxt_contact(), model.getUserPhoto());
                    firebaseDatabase.child(post_key).setValue(post);
                    Toast.makeText(context, "Favourite Added.", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            holder.star.setVisibility(View.GONE);
        }



        showFav(model,position,holder);

    }

    private void getAllImg(final HomeModel model, final int position, final MyViewHolder holder) {
        System.out.println("____________");
        final StorageReference imagePath = myStorage.child("MyImage").child(model.getPostKey()+".jpg");
      imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
          @Override
          public void onSuccess(Uri uri) {
              System.out.println("___"+uri);
              Glide.with(context).load(uri).apply(new RequestOptions().placeholder(R.drawable.icon_home)).error(R.drawable.icon_home).into(holder.imageView);

          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              System.out.println("unable to Download___"+e.getMessage());
          }
      });

//
    }
//  imagePath.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                System.out.println("task snapshot__"+taskSnapshot);
//                Toast.makeText(context, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, "Image Not Uploaded.", Toast.LENGTH_SHORT).show();
//                    }
//                });
    private void showFav(HomeModel model, int pos, MyViewHolder holder) {
          UsrId.clear();
          favId.clear();
        FavouriteViewModel favouriteViewModel ;


        for (int i=0;i<homeViewModelList.size();i++){
            UsrId.add(model.getPostKey());
        }
        for (int i=0;i<favModel.size();i++){
            favouriteViewModel = favModel.get(i);
            favId.add(favouriteViewModel.getPostKey());
        }
   boolean isIdAval=false;
   for (String unique:favId){
       if(unique.equals(UsrId.get(pos))){
           isIdAval=true;
       }
   }

   if(isIdAval){


       if(holder.fav_star.getVisibility()==View.INVISIBLE){
           holder.fav_star.setVisibility(View.VISIBLE);
           holder.star.setVisibility(View.INVISIBLE);
       }
   }


    }

    @Override
    public int getItemCount() {

        return homeViewModelList.size();
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
