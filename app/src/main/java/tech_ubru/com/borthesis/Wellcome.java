package tech_ubru.com.borthesis;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

public class Wellcome extends AppCompatActivity {
    private static int SPLAH_TIME_OUT = 4000;
    ImageView imv_book,imv_ubru,imv_itech;
    TextView tv_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        imv_book = findViewById(R.id.imv_book);
        imv_itech = findViewById(R.id.imv_itech);
        imv_ubru = findViewById(R.id.imv_ubru);
        tv_welcome = findViewById(R.id.tv_welcome);
        fadeOutAndHideImage(imv_book);
        fadeOutAndHideImage(imv_itech);
        fadeOutAndHideImage(imv_ubru);
        fadeOutAndHideTextView(tv_welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(Wellcome.this,LoginActivity.class);
                startActivity(mainIntent);

                finish();
            }
        },SPLAH_TIME_OUT);
    }
    private void fadeOutAndHideImage(final ImageView img)
    {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(SPLAH_TIME_OUT);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeOut);


    }
    private void fadeOutAndHideTextView(final TextView img)
    {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(SPLAH_TIME_OUT);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeOut);


    }
}
