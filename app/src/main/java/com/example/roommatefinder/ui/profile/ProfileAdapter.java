package com.example.roommatefinder.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roommatefinder.R;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileHolder> {
    private List<UserProfile> userProfiles;
    private Context context;
    private View view;

    public ProfileAdapter(Context context,List<UserProfile> userProfiles){
        this.context = context;
        this.userProfiles=userProfiles;
    }
    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.profile_adapter,parent,false);
        return new ProfileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
            UserProfile user = userProfiles.get(position);
            holder.title.setText(user.getEvent_title());
            holder.date.setText(user.getDate());
    }

    @Override
    public int getItemCount() {
        return userProfiles.size();
    }

    class ProfileHolder extends RecyclerView.ViewHolder {
        private TextView title,date;

        public ProfileHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
        }
    }
}
