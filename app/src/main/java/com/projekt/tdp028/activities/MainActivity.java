package com.projekt.tdp028.activities;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projekt.tdp028.R;
import com.projekt.tdp028.utility.LocalStore;
import com.projekt.tdp028.viewmodels.LoginViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static com.facebook.FacebookSdk.getApplicationContext;


public class MainActivity extends BaseActivity implements LoginViewModel.OnLoginListener {

    private int counter = 0;
    private int RC_SIGN_IN = 717;
    private LoginViewModel loginViewModel;
    private CallbackManager mCallbackManager;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loginViewModel.autoLogin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        loginViewModel = new LoginViewModel(this, this);
        setupFacebook();


        Button voteBtn  = (Button) findViewById(R.id.login_button);
        Button gmailBtn = (Button) findViewById(R.id.gmail_login_button);

        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMagic((Button) v);
                enterDashboard();
            }
        });

        gmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithGmail();
            }
        });

    }


    private void setupFacebook() {
        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("!FFB", "facebook:onSuccess:" + loginResult);
                loginViewModel.handleFacebookAccessToken(MainActivity.this, loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("!FFB", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("!FFB", "facebook:onError", error);
                // ...
            }
        });

    }




    private void doMagic(Button btn) {
        counter += 1;
        TextView text = (TextView) findViewById(R.id.welcome_text_view);
        text.setText("Apa är " + counter + " år gammal");
        btn.setBackgroundColor(0xff99cc00);
    }

    private void enterDashboard() {
        EditText usernameEditText = (EditText) findViewById(R.id.username_text_input);
        EditText passwordEditText = (EditText) findViewById(R.id.password_text_input);

        String password = passwordEditText.getText().toString();
        String username = usernameEditText.getText().toString();

        Intent intent = new Intent(this, Dashboard.class);
        intent.putExtra("name", "bamse");
        startActivity(intent);
        finish();
        if (username == "Apa") {}
    }

    private void loginWithGmail() {
        List<AuthUI.IdpConfig> providers =
                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userID = user.getUid();
                loginViewModel.loginUser(userID);
            } else {
                Log.d("FAILED TO LOGIN", "Failed to login");
            }
        } else { // FACEBOOK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void didLoginUser() {
        enterDashboard();
    }

    @Override
    public void didCancelLogin() {}
}
