package com.example.nick_.testapp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dropbox.core.android.Auth;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button SignInButton = (Button) findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //If access without requiring a login is needed then use the second line instead.
                //Auth.startOAuth2Authentication(LoginActivity.this, getString(R.string.APP_KEY)); //this line:
                // Context appContext = this.getApplicationContext(); must go, and instead you use
                // a pointer to the activity you're in (probably this).
                //use if not using first line.
                getAccessToken();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccessToken();
    }

    public void getAccessToken() {
        //If access without requiring a login is needed then use the second line instead.
        //String accessToken = Auth.getOAuth2Token(); //generate Access Token
        String accessToken = getString(R.string.ACCESS_TOKEN);
        if (accessToken != null) {
            //Store accessToken in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("com.example.nick_.testapp2", Context.MODE_PRIVATE);
            prefs.edit().putString("access-token", accessToken).apply();

            //Proceed to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
