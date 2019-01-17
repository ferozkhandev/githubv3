package com.githubv3api.meesn.githubv3api;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.githubv3api.meesn.githubv3api.apprepository.AppRepository;
import com.githubv3api.meesn.githubv3api.model.User;
import com.githubv3api.meesn.githubv3api.service.UserClient;
import com.githubv3api.meesn.githubv3api.viewmodel.AppViewModel;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    //Views Variables
    EditText email, password;
    Button btn_login;
    TextView signup;
    View parentLayout;

    private boolean loginSuccess = false;
    private String userLoginName;
    private InternetCheck internetCheck;
    private AppViewModel appViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.githubactionbar);
        parentLayout = findViewById(android.R.id.content);
        internetCheck = new InternetCheck(Login.this);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        //Declare Views
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin();
            }
        });
        signup = findViewById(R.id.link_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/join?source=experiment-header-dropdowns-home");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void tryLogin()
    {
        if (internetCheck.netCheck())
        {
            login();
        }
        else
        {
            Snackbar snackbar = Snackbar
                    .make(parentLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tryLogin();
                        }
                    });
            snackbar.show();
        }
    }

    private void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        btn_login.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(Login.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // TODO: Implement your own authentication logic here.
        appViewModel.loginUser(this.email.getText().toString().trim(), password.getText().toString().trim(), getApplicationContext());

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        loginSuccess = appViewModel.getLoginStatus();
                        userLoginName = appViewModel.getUserLoginName();
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (loginStatus()) {
                            writesp(userLoginName);
                            onLoginSuccess();
                            Intent intent = new Intent(Login.this, HomePage.class);
                            intent.putExtra("userLoginName", userLoginName);
                            startActivity(intent);

                        } else {
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    private void writesp(String userLoginName) {

        SharedPreferences sharedPref = this.getSharedPreferences("MY_PREFS",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String username = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String base = username + ":" + password;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        editor.putBoolean("alreadylogin", true);
        editor.putString("userloginname", userLoginName);
        editor.putString("Authorization", authHeader);
        editor.apply();
    }

    private boolean loginStatus() {
        return loginSuccess;
    }

    public void onLoginSuccess() {
        btn_login.setEnabled(true);
        Login.this.finish();
    }

    public void onLoginFailed() {
        btn_login.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String emails = email.getText().toString();
        String passwords = password.getText().toString();

        if (emails.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {
            email.setError("Please enter a valid email address.");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwords.isEmpty() || password.length() < 6 || password.length() > 35) {
            password.setError("Please enter a valid password.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

}
