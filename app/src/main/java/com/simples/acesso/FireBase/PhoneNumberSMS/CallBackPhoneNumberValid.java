package com.simples.acesso.FireBase.PhoneNumberSMS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.print.PrintJob;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class CallBackPhoneNumberValid extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {

    Activity activity;
    FloatingActionButton button;
    EditText editText;

    public CallBackPhoneNumberValid(Activity activity, EditText editText, FloatingActionButton button){
        this.activity = activity;
        this.button = button;
        this.editText = editText;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        if(!phoneAuthCredential.getSmsCode().isEmpty()){
            button.setVisibility(View.VISIBLE);
            editText.setText(phoneAuthCredential.getSmsCode());
        }else{
            button.setVisibility(View.GONE);
            editText.setText("1111111");
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onVerificationFailed(FirebaseException e) {
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            button.setVisibility(View.VISIBLE);
            editText.setText("1111111");
        } else if (e instanceof FirebaseTooManyRequestsException) {
            button.setVisibility(View.VISIBLE);
            editText.setText("1111111");
        }
    }
}
