package com.example.roommatefinder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class RecyclerView_Config {
    private Context mcontext;

    class PostItemView extends RecyclerView.ViewHolder{

        private TextView mtitle;
        private TextView mdesc;
        private TextView msex;
        private TextView mnoofroommates;
        private TextView mlocation;

        public PostItemView( ViewGroup parent) {
            super(LayoutInflator.from(mcontext));
            inflate(R.layout.post_list_item, parent,)
        }
    }
}
