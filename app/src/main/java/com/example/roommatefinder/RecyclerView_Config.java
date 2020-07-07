package com.example.roommatefinder;

import android.app.LauncherActivity;
import android.content.Context;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.roommatefinder.ui.home.HomeFragment;

import java.util.List;

public class RecyclerView_Config {
    private Context mcontext;
    private PostsAdapter mPostsAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Posts> posts, List<String> keys){
       mcontext = context;
       mPostsAdapter = new PostsAdapter(posts, keys);
       recyclerView.setLayoutManager(new LinearLayoutManager(context));
       recyclerView.setAdapter(mPostsAdapter);
    }



    static class PostItemView extends RecyclerView.ViewHolder{

        private TextView mtitle;
        private TextView mdesc;
        private TextView msex;
        private TextView mnoofroommates;
        private TextView mlocation;
        private String key;


        public PostItemView(@NonNull View parent) {
            super(parent);

            mtitle = (TextView) itemView.findViewById((R.id.title_textView));
            mdesc = (TextView) itemView.findViewById((R.id.desc_textView));
            msex = (TextView) itemView.findViewById((R.id.sex_textView));
            mnoofroommates = (TextView) itemView.findViewById((R.id.noofroommtes_textView));
            mlocation = (TextView) itemView.findViewById((R.id.location_textView));


        }
        public void bind(Posts posts, String key){
            mtitle.setText(posts.getPostTitle());
            mdesc.setText(posts.getPostDescription());
            msex.setText(posts.getSex());
            mnoofroommates.setText(posts.getNumberOfRoommates());
            mlocation.setText(posts.getLocation());
            this.key = key;
        }

    }
     public static class PostsAdapter extends RecyclerView.Adapter<PostItemView>{

        private List<Posts> mPostlist;
        private List<String> mkeys;

        public PostsAdapter(List<Posts> mPostlist, List<String> mkeys) {
            this.mPostlist = mPostlist;
            this.mkeys = mkeys;
        }



         @NonNull
        @Override
        public PostItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull PostItemView holder, int position) {
            holder.bind(mPostlist.get(position),mkeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mPostlist.size();
        }
    }
}

