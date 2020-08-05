package com.example.roommatefinder.singletone;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.roommatefinder.R;

public class ProgressDialog {
    public static Dialog progressDialog;

    //progress loader
    public static void loaderForPagination(Context context) {

        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        progressDialog.setContentView(R.layout.progress_layout);

        progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        progressDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        progressDialog.setCancelable(false);
    }
}
