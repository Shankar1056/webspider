package apextechies.etodo.splash;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import apextechies.etodo.R;
import apextechies.etodo.activity.AllState;
import apextechies.etodo.common.ClsGeneral;
import apextechies.etodo.login.LoginActivity;

/**
 * Created by Shankar on 5/11/2017.
 */

public class SplashScreen extends AppCompatActivity {
    Handler h = new Handler();
    int delay = 3000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.splash_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = SplashScreen.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(SplashScreen.this, R.color.colorAccent));
        }
        generateHashkey();

        h.postDelayed(new Runnable(){
            public void run(){
                if (ClsGeneral.getPreferences(SplashScreen.this, ClsGeneral.USER_ID).equalsIgnoreCase("")) {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }
              /*  else if (ClsGeneral.getPreferences(SplashScreen.this, ClsGeneral.USER_PHONENUMBER).equalsIgnoreCase("")) {
                    startActivity(new Intent(SplashScreen.this, PhoneNumberVarification.class));
                    finish();
                }*/
                else {

                        startActivity(new Intent(SplashScreen.this, AllState.class));
                        finish();

                }


            }
        }, delay);


    }

    private void generateHashkey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "apextechies.etodo",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String a =Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
