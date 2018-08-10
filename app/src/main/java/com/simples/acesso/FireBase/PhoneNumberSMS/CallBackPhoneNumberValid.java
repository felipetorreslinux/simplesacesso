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

    public CallBackPhoneNumberValid(View view){
        this.view = view;
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        String code = phoneAuthCredential.getSmsCode();
        Snackbar.make(view, "Código SMS: "+code, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            //Numero inválido
            Snackbar.make(view, "Número de telefone inválido", Snackbar.LENGTH_LONG).show();
        } else if (e instanceof FirebaseTooManyRequestsException) {
            //Quantidade excedida
            Snackbar.make(view, "Tentativas excedidas.\nTente após 30 minutos", Snackbar.LENGTH_LONG).show();
        }
    }
}
