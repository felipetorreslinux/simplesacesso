package com.simples.acesso.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import com.simples.acesso.R;

public class PreLoads {

    static ProgressDialog progressDialog;

    public static void open (Activity activity, String title, String message, boolean cancel){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancel);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = new ProgressBar(activity).getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimaryDark),
                    PorterDuff.Mode.SRC_IN);
            progressDialog.setIndeterminateDrawable(drawable);
        }
        progressDialog.show();
    }

    public static void close (){
        progressDialog.dismiss();
    }
}
