package net.randomail.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import net.randomail.RandomailApplication;

import net.randomail.R;

public class SplashScreenActivity extends Activity {
    private ImageView mLogoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mLogoImageView = (ImageView) findViewById(R.id.randomail_logo);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Drawable drawable = mLogoImageView.getDrawable();

        ScaleAnimation animation = new ScaleAnimation(  0.0f, 1.0f,
                0.0f, 1.0f,
                drawable.getIntrinsicWidth() / 2.0f,
                drawable.getIntrinsicHeight() / 2.0f);
        animation.setInterpolator(this, android.R.anim.bounce_interpolator);
        animation.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mLogoImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (RandomailApplication.mAuthToken == null || RandomailApplication.mUserId == null) {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(SplashScreenActivity.this, EmailsActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        mLogoImageView.startAnimation(animation);
    }
}
