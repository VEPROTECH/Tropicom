package com.tropi.dvjl.tropicom;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.tropi.dvjl.tropicom.SqliteData.SessionManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SpashScreen  extends Activity implements Animation.AnimationListener  {

    Animation animation;
    SessionManager sessionManager;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        sessionManager=new SessionManager(getApplicationContext());
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
        }

        animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_screen);

        animation.setAnimationListener(this);
        linearLayout=findViewById(R.id.layout_linear);

        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(animation);

        //  Hey hash code = 3gsS9CGUQQ4iFWOVxJG56BCPoeM=
        try {
            PackageInfo info=getPackageManager().getPackageInfo("com.tropi.dvjl.tropicom", PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest md= MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(sessionManager.isLogged()){
            Intent intent=new Intent(SpashScreen.this,Interface.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(SpashScreen.this,WelcomActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

}
