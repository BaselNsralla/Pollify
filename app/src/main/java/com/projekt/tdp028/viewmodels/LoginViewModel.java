package com.projekt.tdp028.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projekt.tdp028.Constants.FirebaseKeys;
import com.projekt.tdp028.models.firebase.User;
import com.projekt.tdp028.utility.LocalStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.firebase.ui.auth.AuthUI.TAG;

public class LoginViewModel extends ViewModel implements FacebookCallback<LoginResult> {
    private DatabaseReference mDatabase;
    private OnLoginListener onLoginListener;
    private FirebaseAuth mAuth;
    private Context context;

    public LoginViewModel(Context context, OnLoginListener listener){
        onLoginListener = listener;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public void loginUser(final String uid) {
        Query checkQuery = mDatabase.child(FirebaseKeys.USERS);
        checkQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid)) {
                    completeLogin(uid);
                } else {
                    User user = new User(uid, new ArrayList<String>(), new HashMap<>());
                    Task createUserTask = mDatabase.child(FirebaseKeys.USERS).child(uid).setValue(user);
                    createUserTask.addOnCompleteListener(task -> { completeLogin(uid); });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void completeLogin(String uid) {
        onLoginListener.didLoginUser();
        LocalStore.saveUserId(uid, context);
    }


    public void handleFacebookAccessToken(Activity activity, AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginUser(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Toast.makeText(getApplicationContext().this, "Authentication failed.",
                            //        Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }

    public void autoLogin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            completeLogin(currentUser.getUid());
        }
    }


    @Override
    public void onSuccess(LoginResult loginResult) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    public interface OnLoginListener {
        public void didLoginUser();
        public void didCancelLogin();
    }
}
