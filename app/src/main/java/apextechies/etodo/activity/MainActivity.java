package apextechies.etodo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;

import org.json.JSONObject;

import apextechies.etodo.R;
import apextechies.etodo.common.ClsGeneral;
import apextechies.etodo.fragment.AboutUs;
import apextechies.etodo.fragment.ComingSoon;
import apextechies.etodo.fragment.ContactUs;
import apextechies.etodo.fragment.HomeFragmentNew;
import apextechies.etodo.fragment.Privacy;
import apextechies.etodo.login.LoginActivity;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    private TextView toolbartext;

    private LinearLayout leftdrawer;
    private DrawerLayout drawer;

    //Linked
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    private ProgressDialog progress;
    private TextView user_name, user_email;
    private ImageView profile_picture;

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try
        {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        catch (Exception e)
        {

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = MainActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
        }
        domapping();

    }

    private void domapping() {
        toolbartext = (TextView) findViewById(R.id.toolbartext);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftdrawer = (LinearLayout)findViewById(R.id.leftdrawer);
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);
        
        findViewById(R.id.flightlayout).setOnClickListener(this);
        findViewById(R.id.myholidaylayout).setOnClickListener(this);
        findViewById(R.id.rechargelayout).setOnClickListener(this);
        findViewById(R.id.hotdeallayout).setOnClickListener(this);
    
        /*TextView profile = (TextView)nv.findViewById(R.id.profile);
        TextView flight = (TextView)nv.findViewById(R.id.flight);
        TextView holiday = (TextView)nv.findViewById(R.id.holiday);
        TextView share = (TextView)nv.findViewById(R.id.share);
        TextView notification = (TextView)nv.findViewById(R.id.notification);
        TextView contactus = (TextView)nv.findViewById(R.id.contactus);
        TextView about = (TextView)nv.findViewById(R.id.about);
        TextView privacy = (TextView)nv.findViewById(R.id.privacy);
        TextView termsconition = (TextView)nv.findViewById(R.id.termsconition);
        TextView cancellation = (TextView)nv.findViewById(R.id.cancellation);*/
        
        leftdrawer.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        if (ClsGeneral.getPreferences(MainActivity.this,getString(R.string.loginType)).equalsIgnoreCase(getString(R.string.linked))) {

            linkededinApiHelper();
        }
        Fragment fragment = new HomeFragmentNew();
        openfragment(getResources().getString(R.string.home), fragment);

    }

    private void openfragment(String string, Fragment fragment) {
        toolbartext.setText(string);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
        // setTitle(getResources().getString(R.string.app_name));

    }
    public void linkededinApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(MainActivity.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {

                    setprofile(result.getResponseDataAsJson());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onApiError(LIApiError error) {
            }
        });
    }

    public  void  setprofile(JSONObject response){

        try {
            JSONObject res = response;

            /*user_email.setText(response.get("emailAddress").toString());
            user_name.setText(response.get("formattedName").toString());

            Picasso.with(this).load(response.getString("pictureUrl"))
                    .into(profile_picture);
*/
        } catch (Exception e){
            e.printStackTrace();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.leftdrawer:
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
                else
                {
                    drawer.openDrawer(GravityCompat.END);
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.flightlayout:
                try {
                    String url = "http://www.musafirbazar.com/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.myholidaylayout:
                comingsoon("My Holiday");
                break;
            case R.id.rechargelayout:
                comingsoon("Recharge");
                break;
            case R.id.hotdeallayout:
                comingsoon("Hot Deal");
                break;
        }
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    
    
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                break;
            case R.id.share:
                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=apextechies.etodo&hl=en");
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Share Webspider"));
                    
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            case R.id.notification:
                comingsoon("Notification");
                break;
            case R.id.about:
                Fragment fragment = new AboutUs();
                openfragment(getResources().getString(R.string.aboutus), fragment);
                break;
            case R.id.contactus:
                Fragment fragment1 = new ContactUs();
                openfragment(getResources().getString(R.string.contactus), fragment1);
                break;
            case R.id.privacy:
                Fragment fragment2 = new Privacy();
                openfragment("Privacy", fragment2);
                break;
            case R.id.termsconition:
                Fragment fragment3 = new Privacy();
                openfragment("Terms & Conditions", fragment3);
                break;
            case R.id.cancellation:
                comingsoon("Cancellation");
                break;
            
        }
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
    
    private void comingsoon(String text) {
    
        Fragment fragment = new ComingSoon();
        openfragment(text, fragment);
    }
}
