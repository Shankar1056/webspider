package apextechies.etodo.login;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.iid.FirebaseInstanceId;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import apextechies.etodo.R;
import apextechies.etodo.activity.AllState;
import apextechies.etodo.activity.MainActivity;
import apextechies.etodo.common.ClsGeneral;
import apextechies.etodo.network.Utilz;
import apextechies.etodo.webservices.WebServices;

import static apextechies.etodo.R.id.loginButton;
import static apextechies.etodo.common.ClsGeneral.OTP;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, OnClickListener {
    private UserLoginTask mAuthTask = null;
    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView hideshowpassword;
    private String deviceId = null;

    //Linked
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String PACKAGE = "apextechies.etodo";
    TextView login_linkedin_btn;
    private ProgressDialog progress;

    //G+ login
    SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    //fb Login
    CallbackManager callbackManager;
    LoginButton login_button;
    private TextView fb;
    String profile_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = LoginActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.colorAccent));
        }
        domapping();

    }

    private void domapping() {
        if (ClsGeneral.getPreferences(LoginActivity.this, ClsGeneral.DEVICE_ID).equalsIgnoreCase("")) {
            deviceId = FirebaseInstanceId.getInstance().getToken();
            ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.DEVICE_ID, deviceId);
        } else {
            deviceId = ClsGeneral.getPreferences(LoginActivity.this, ClsGeneral.DEVICE_ID);
        }
        fb = (TextView) findViewById(R.id.fb);
        login_button = (LoginButton) findViewById(R.id.login_button);
        login_button.setReadPermissions("email");
        login_linkedin_btn = (TextView) findViewById(loginButton);
        findViewById(R.id.gplus).setOnClickListener(this);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        LinearLayout mEmailSignInButton = (LinearLayout) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        hideshowpassword = (TextView) findViewById(R.id.hideshowpassword);
        hideshowpassword.setOnClickListener(this);
        findViewById(R.id.gotosignup).setOnClickListener(this);
        login_linkedin_btn.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        fb.setOnClickListener(this);


        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    hideshowpassword.setVisibility(View.VISIBLE);
                } else {
                    hideshowpassword.setVisibility(View.GONE);
                }
            }
        });

        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkstatus();
        linkededinApiHelper();
    }

    public void linkededinApiHelper(){
        LISessionManager.getInstance(getApplicationContext()).clearSession();
    }
    private void checkstatus() {

        LoginManager.getInstance().logOut();
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Utilz.isValidEmail1(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if (Utilz.isInternetConnected(LoginActivity.this)) {
                ClsGeneral.setPreferences(LoginActivity.this,getString(R.string.loginType),getString(R.string.normal));
                mAuthTask = new UserLoginTask();
                mAuthTask.execute(WebServices.LOGIN, "", mEmailView.getText().toString(), mPasswordView.getText().toString(), deviceId, "normal");
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hideshowpassword:
                if (hideshowpassword.getText().toString().equalsIgnoreCase(getResources().getString(R.string.show))) {
                    hideshowpassword.setText(getResources().getString(R.string.hide));
                    mPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    hideshowpassword.setText(getResources().getString(R.string.show));
                    mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.gotosignup:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
                break;
            case loginButton:
                login_linkedin();
                break;
            case R.id.gplus:
                signInButton.performClick();
                signIn();
                break;
            case R.id.fb:
                ClsGeneral.setPreferences(LoginActivity.this,getString(R.string.loginType),getString(R.string.fb));
                login_button.performClick();
                fblogin();
                break;
            default:
                break;
        }
    }

    private void login_linkedin() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {

                // Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAuthError(LIAuthError error) {

                Toast.makeText(getApplicationContext(), "failed " + error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this,
                requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else {


            String host = "api.linkedin.com";
            String topCardUrl = "https://" + host + "/v1/people/~:" +
                    "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
            linkededinApiHelper(topCardUrl);
       /* Intent intent = new Intent(LoginActivity.this, PhoneNumberVarification.class);
        startActivity(intent);*/
        }
    }

    // This method is used to make permissions to retrieve data from linkedin

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    public void linkededinApiHelper(String topCardUrl) {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(LoginActivity.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {

                    setprofile(result.getResponseDataAsJson());
                    progress.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());

            }
        });
    }

    public void setprofile(JSONObject response) {

        try {
            JSONObject res = response;
            ClsGeneral.setPreferences(LoginActivity.this,getString(R.string.loginType),getString(R.string.linked));
            mAuthTask = new UserLoginTask();
            mAuthTask.execute(WebServices.LOGIN,response.get("formattedName").toString(), response.get("emailAddress").toString(), mPasswordView.getText().toString(), deviceId, getString(R.string.social));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //Google Plus


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    // [START onActivityResult]

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            try {
                Log.i("getDisplayName", acct.getDisplayName());
                Log.i("getEmail", acct.getEmail());
                Log.i("sign in from", acct.getFamilyName());
                Log.i("getGivenName", acct.getGivenName());
                Log.i("getId", acct.getId());
                Log.i("getPhotoUrl", String.valueOf(acct.getPhotoUrl()));

                ClsGeneral.setPreferences(LoginActivity.this,"gotid",getString(R.string.yes));

            }
            catch (Exception e)
            {
                Log.i("error", e.getMessage());
            }

            //  savacredentials(acct.getId(),acct.getDisplayName(),acct.getEmail(),"","",String.valueOf(acct.getPhotoUrl()));
            ClsGeneral.setPreferences(LoginActivity.this,"id", acct.getId());
            ClsGeneral.setPreferences(LoginActivity.this,"profile_pic",  String.valueOf(acct.getPhotoUrl()));
            if (ClsGeneral.getPreferences(LoginActivity.this,"gotid").equalsIgnoreCase(getString(R.string.yes))) {
                ClsGeneral.setPreferences(LoginActivity.this,getString(R.string.loginType),getString(R.string.gplus));
                mAuthTask = new UserLoginTask();
                mAuthTask.execute(WebServices.LOGIN,acct.getDisplayName(), acct.getEmail(), mPasswordView.getText().toString(), deviceId, getString(R.string.social));
               // new Login().execute(WebServices.SIGNUP, acct.getDisplayName(), acct.getEmail(), "", "", getString(R.string.gplus));
            }
        } else {
            ClsGeneral.setPreferences(LoginActivity.this,getString(R.string.is_signin),getString(R.string.no));
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

//FB LoGIN

    private void fblogin() {
        login_button.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        login_button.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {@Override
                public void onSuccess(LoginResult loginResult) {



                    System.out.println("onSuccess");

                    String accessToken = loginResult.getAccessToken()
                            .getToken();
                    Log.i("accessToken", accessToken);

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {@Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {

                                Log.i("LoginActivity",
                                        response.toString());
                                try {
                                    String name = null,email=null,gender=null,birthday=null;
                                    String  id = object.getString("id");
                                    try {
                                        profile_pic = String.valueOf(new URL(
                                                "http://graph.facebook.com/" + id + "/picture?type=small"));
                                        Log.i("profile_pic",
                                                profile_pic + "");

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    if (object.has("name")) {
                                        name = object.getString("name");
                                    }
                                    if (object.has("email")) {
                                        email = object.getString("email");
                                    }
                                    if (object.has("gender")) {
                                        gender = object.getString("gender");
                                    }
                                    if (object.has("birthday")) {
                                        birthday = object.getString("birthday");
                                    }

                                    ClsGeneral.setPreferences(LoginActivity.this,"fbid", id);
                                    ClsGeneral.setPreferences(LoginActivity.this,"profile_pic",  profile_pic);

                                    ClsGeneral.setPreferences(LoginActivity.this,getString(R.string.loginType),getString(R.string.linked));
                                    mAuthTask = new UserLoginTask();
                                    mAuthTask.execute(WebServices.LOGIN,name, email, mPasswordView.getText().toString(), deviceId, getString(R.string.social));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields",
                            "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        //  Log.v("LoginActivity", exception.getCause().toString());
                    }
                });
    }

    public class UserLoginTask extends AsyncTask<String, Void, String> {
         ProgressDialog mProgressDialog;
        String result = null;
        ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setMessage(getString(R.string.loading));
                mProgressDialog.setIndeterminate(true);
            }

            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            namevaluepairs.add(new BasicNameValuePair("user_name", params[1]));
            namevaluepairs.add(new BasicNameValuePair("user_email", params[2]));
            namevaluepairs.add(new BasicNameValuePair("user_password", params[3]));
            namevaluepairs.add(new BasicNameValuePair("device_id", params[4]));
            namevaluepairs.add(new BasicNameValuePair("logintype", params[5]));

            try {
                result = Utilz.executeHttpPost(params[0], namevaluepairs);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                String success = null, user_id = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.has("success")) {
                        ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.SUCCESS, jsonObject.getString("success"));
                        if (jsonObject.getString("success").equalsIgnoreCase(ClsGeneral.TRUE)) {
                            JSONArray jsonArray = jsonObject.getJSONArray("last_record");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            if (jsonObject1.has("user_id")) {
                                ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.USER_ID, jsonObject1.getString("user_id"));
                            }
                            if (jsonObject1.has("user_name")) {
                                ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.USER_NAME, jsonObject1.getString("user_name"));
                            }
                            if (jsonObject1.has("user_email")) {
                                ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.USER_EMAIL, jsonObject1.getString("user_email"));
                            }
                            if (jsonObject1.has("user_phonenumber")) {
                                ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.USER_PHONENUMBER, jsonObject1.getString("user_phonenumber"));
                            }
                            if (jsonObject1.has("user_location")) {
                                ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.USER_LOCATION, jsonObject1.getString("user_location"));
                            }
                            if (jsonObject1.has("user_dob")) {
                                ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.USER_DOB, jsonObject1.getString("user_dob"));
                            }
                            if (jsonObject1.has("user_gender")) {
                                ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.USER_GENDER, jsonObject1.getString("user_gender"));
                            }
                            if (jsonObject1.has("user_profilepic")) {
                                ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.USER_PROFILEPIC, jsonObject1.getString("user_profilepic"));
                            }
                            if (jsonObject1.has("user_status")) {
                                ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.USER_STATUS, jsonObject1.getString("user_status"));
                            }
                            if (jsonObject1.has("user_type")) {
                                ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.USER_TYPE, jsonObject1.getString("user_type"));
                            }
                            if (jsonObject1.has("otp")) {
                                ClsGeneral.setPreferences(LoginActivity.this, OTP, jsonObject1.getString("otp"));
                            }
                            if (jsonObject1.has("device_id")) {
                                ClsGeneral.setPreferences(LoginActivity.this, ClsGeneral.DEVICE_ID, jsonObject1.getString("device_id"));
                            }
                            /*if (ClsGeneral.getPreferences(LoginActivity.this, ClsGeneral.USER_PHONENUMBER).equalsIgnoreCase("")) {
                                startActivity(new Intent(LoginActivity.this, PhoneNumberVarification.class));
                                finish();
                            } */


                                startActivity(new Intent(LoginActivity.this, AllState.class));
                                finish();
                                mProgressDialog.cancel();


                        } else {
                            mProgressDialog.cancel();
                            Toast.makeText(LoginActivity.this, "" + jsonObject.getString("success_msg"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mProgressDialog.cancel();
                }
            }
            mAuthTask = null;
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

    }
}

