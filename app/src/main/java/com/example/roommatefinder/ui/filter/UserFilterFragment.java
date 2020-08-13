package com.example.roommatefinder.ui.filter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.roommatefinder.R;
import com.example.roommatefinder.singletone.GetNavController;


public class UserFilterFragment extends Fragment {
    private View view;
    private Spinner spBeds,spUnitType;
    private EditText location,price;
    private Button filter;
    private String unitType="";
    private String beds="";
    private Context context;
    private String price1="";
    private String loc="";
    private  Bundle b;



    public UserFilterFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        b = new Bundle();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_user_filter, container, false);
       getData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        b.remove("unitType");
        b.remove("beds");
        b.remove("price");
        b.remove("location");
    }

    private void getData() {
      spBeds =  view.findViewById(R.id.spBedrooms);
      spUnitType=view.findViewById(R.id.spUnitType);
      price = view.findViewById(R.id.price);
      location = view.findViewById(R.id.location);
      filter   = view.findViewById(R.id.filter);

      //getText()


      setSpinnerListener();
      filter.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              price1 = price.getText().toString().trim();
              loc = location.getText().toString();
              System.out.println("unitype"+unitType+"beds__"+beds+"price1"+price1+"loc"+loc);
              if(!unitType.isEmpty()&&!beds.isEmpty()&&!price1.isEmpty()&&!loc.isEmpty()){
                  b.putString("unitType",unitType);
                  b.putString("beds",beds);
                  b.putString("price",price1);
                  b.putString("location",loc);
                  GetNavController.getNavController().navigate(R.id.filtrateUserFragment,b);
              }else {
                  if(!loc.isEmpty()&&!price1.isEmpty()){
                      b.putString("price",price1);
                      b.putString("location",loc);
                      GetNavController.getNavController().navigate(R.id.filtrateUserFragment,b);
                  }else {
                      if(!price1.isEmpty()){
                          b.putString("price",price1);
                          GetNavController.getNavController().navigate(R.id.filtrateUserFragment,b);
                      }else {
                          if(!loc.isEmpty()){
                              b.putString("location",loc);
                              GetNavController.getNavController().navigate(R.id.filtrateUserFragment,b);
                          }else {
                              Toast.makeText(context, "Select any one of them or All", Toast.LENGTH_SHORT).show();
                          }
                      }
                  }

              }
          }

      });
    }

    private void setSpinnerListener() {

        spUnitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unitType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
              unitType="";
            }
        });

        spBeds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                beds = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                beds = "";
            }
        });

    }
}