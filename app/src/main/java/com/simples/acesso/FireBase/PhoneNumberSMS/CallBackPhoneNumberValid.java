package com.simples.acesso.FireBase.PhoneNumberSMS;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class CallBackPhoneNumberValid extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {

    View view;
    public static String code = null;

    public CallBackPhoneNumberValid(View view){
        this.view = view;
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        code = phoneAuthCredential.getSmsCode();
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            code = null;
        } else if (e instanceof FirebaseTooManyRequestsException) {
            code = null;
        }
    }
}
