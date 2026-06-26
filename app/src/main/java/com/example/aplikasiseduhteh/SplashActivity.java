package com.example.aplikasiseduhteh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imgLogo  = findViewById(R.id.imgLogo);
        TextView tvAppName = findViewById(R.id.tvAppName);
        TextView tvTagline = findViewById(R.id.tvTagline);

        // Daun
        ImageView daun1 = findViewById(R.id.daun1);
        ImageView daun2 = findViewById(R.id.daun2);
        ImageView daun3 = findViewById(R.id.daun3);
        ImageView daun4 = findViewById(R.id.daun4);
        ImageView daun5 = findViewById(R.id.daun5);
        ImageView daun6 = findViewById(R.id.daun6);

        // Loading dots
        View dot1 = findViewById(R.id.dot1);
        View dot2 = findViewById(R.id.dot2);
        View dot3 = findViewById(R.id.dot3);
        Animation f1 = AnimationUtils.loadAnimation(this, R.anim.anim_float_daun);
        Animation f2 = AnimationUtils.loadAnimation(this, R.anim.anim_float_daun2);
        Animation f3 = AnimationUtils.loadAnimation(this, R.anim.anim_float_daun);
        Animation f4 = AnimationUtils.loadAnimation(this, R.anim.anim_float_daun2);
        Animation f5 = AnimationUtils.loadAnimation(this, R.anim.anim_float_daun);
        Animation f6 = AnimationUtils.loadAnimation(this, R.anim.anim_float_daun2);

        f1.setStartOffset(0);
        f2.setStartOffset(700);
        f3.setStartOffset(1400);
        f4.setStartOffset(350);
        f5.setStartOffset(1050);
        f6.setStartOffset(200);

        daun1.startAnimation(f1);
        daun2.startAnimation(f2);
        daun3.startAnimation(f3);
        daun4.startAnimation(f4);
        daun5.startAnimation(f5);
        daun6.startAnimation(f6);

        // animasi logo
        AnimationSet logoAnim = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(
                0.5f, 1f, 0.5f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(800);
        AlphaAnimation fadeLogo = new AlphaAnimation(0f, 1f);
        fadeLogo.setDuration(800);
        logoAnim.addAnimation(scale);
        logoAnim.addAnimation(fadeLogo);
        logoAnim.setFillAfter(true);
        imgLogo.startAnimation(logoAnim);

        // animasi teks
        AlphaAnimation nameAnim = new AlphaAnimation(0f, 1f);
        nameAnim.setDuration(600);
        nameAnim.setStartOffset(500);
        nameAnim.setFillAfter(true);
        tvAppName.startAnimation(nameAnim);

        AlphaAnimation taglineAnim = new AlphaAnimation(0f, 1f);
        taglineAnim.setDuration(600);
        taglineAnim.setStartOffset(750);
        taglineAnim.setFillAfter(true);
        tvTagline.startAnimation(taglineAnim);

        // animasi loading
        animateDot(dot1, 1000);
        animateDot(dot2, 1200);
        animateDot(dot3, 1400);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SessionManager session = new SessionManager(SplashActivity.this);
            Intent intent;
            if (!session.isloggedin()) {
                intent = new Intent(SplashActivity.this, login.class);
            } else if (session.isAdmin()) {
                intent = new Intent(SplashActivity.this, DaftarPenggunaActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }, 3000);
    }

    private void animateDot(View dot, long delay) {
        AlphaAnimation anim = new AlphaAnimation(0f, 1f);
        anim.setDuration(400);
        anim.setStartOffset(delay);
        anim.setFillAfter(true);
        dot.startAnimation(anim);
    }
}
