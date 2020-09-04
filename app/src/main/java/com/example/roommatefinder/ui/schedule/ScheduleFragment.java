package com.example.roommatefinder.ui.schedule;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.roommatefinder.R;
import com.example.roommatefinder.singletone.MySharedPref;
import com.example.roommatefinder.ui.profile.UserProfile;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ScheduleFragment extends Fragment {


    private DatePicker picker;
    private MaterialButton btnGet;
    private TextView tvw;
    private  View root;
    private EditText title,description;
    private DatabaseReference profileDatabase;
    private Context context;
    private MySharedPref sharedPref;
    private String email;

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
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_schedule, container, false);
        profileDatabase = FirebaseDatabase.getInstance().getReference("Schedule"+email);
           setDate1();
        return root;
    }

    private void setDate1() {
        tvw=root.findViewById(R.id.textView1);
        picker=root.findViewById(R.id.datePicker1);
        btnGet=root.findViewById(R.id.button1);
        title = root.findViewById(R.id.title);
        Calendar today = Calendar.getInstance();
        long now = today.getTimeInMillis()+24*60*60*1000;

        picker.setMinDate(now);
        description = root.findViewById(R.id.desc);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date=""+picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear();
                String userTitle= title.getText().toString().trim();
                String desc = description.getText().toString();
                UserProfile userProfile = new UserProfile(date,userTitle,desc);
                String key = profileDatabase.push().getKey();
                //profile database has been set here.
                if(!date.isEmpty()&&!userTitle.isEmpty()&&!desc.isEmpty()){
                    profileDatabase.child(key).setValue(userProfile);
                    Toast.makeText(context, "Your Schedule date is Successfully Created.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "All fields required.", Toast.LENGTH_SHORT).show();
                }


//                tvw.setText("Selected Date: "+ picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear());

            }
        });
    }


}
