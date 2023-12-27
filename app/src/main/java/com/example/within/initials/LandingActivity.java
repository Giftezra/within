package com.example.within.initials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.within.R;
import com.example.within.user.LoginActivity;
import com.example.within.user.RegistrationActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        Button login_button = findViewById(R.id.loginButton);
        Button sign_up_button = findViewById(R.id.sign_upButton);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });


    }
}
