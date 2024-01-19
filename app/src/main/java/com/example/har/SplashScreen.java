package com.example.har;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static final long SPLASH_DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main activity after the delay
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the splash activity
            }
        }, SPLASH_DELAY);
    }

}
