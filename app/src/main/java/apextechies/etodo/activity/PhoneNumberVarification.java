package apextechies.etodo.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apextechies.etodo.R;
import apextechies.etodo.common.ClsGeneral;
import apextechies.etodo.common.Readsms;
import apextechies.etodo.network.Utilz;
import apextechies.etodo.webservices.WebServices;

/**
 * Created by Shankar on 5/2/2017.
 */

public class PhoneNumberVarification extends AppCompatActivity implements View.OnClickListener {
    private EditText phonenumer, otp;
    private IntentFilter intentFilter;
    private Readsms readsms;
    private String serverActivationCode;
    private RelativeLayout enterotplayout;
    private TextView whyText, send_otp;
    private SendOtp sendOtp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.phonenumbervarification);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = PhoneNumberVarification.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(PhoneNumberVarification.this, R.color.colorAccent));
        }



        send_otp = (TextView) findViewById(R.id.send_otp);
        whyText = (TextView) findViewById(R.id.why);
        findViewById(R.id.resendotp).setOnClickListener(this);
        findViewById(R.id.otplayout).setOnClickListener(this);
        phonenumer = (EditText) findViewById(R.id.phonenumer);
        otp = (EditText) findViewById(R.id.otp);
        enterotplayout = (RelativeLayout) findViewById(R.id.enterotplayout);
        whyText.setOnClickListener(this);

       /* readsms = new Readsms() {
            @Override
            protected void onSmsReceived(String s) {
                if (s != null && s.length() > 0) {
                    if (s.contains("SpotSoon")) {
                        String splitmsg[] = s.split(" ");
                        otp.setText(splitmsg[0]);
                    }
                }
            }
        };*/



        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (otp.getText().toString().length() == 6) {

                    if (serverActivationCode.equalsIgnoreCase(otp.getText().toString().trim())) {

                        hideSoftKeyboard();
                        ClsGeneral.setPreferences(PhoneNumberVarification.this,ClsGeneral.USER_PHONENUMBER,phonenumer.getText().toString());
                        startActivity(new Intent(PhoneNumberVarification.this,MainActivity.class));
                        finish();

                    } else {
                        Toast.makeText(PhoneNumberVarification.this, "Enter Correct OTP", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();
        sendOtp.cancel(true);


    }
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.otplayout:
                if (send_otp.getText().toString().equalsIgnoreCase(getResources().getString(R.string.sentotp))) {
                    if (phonenumer.getText().toString().trim().length() == 10) {
                        sendOtp = new SendOtp();
                        sendOtp.execute(WebServices.SENDOTP, phonenumer.getText().toString());
                    } else {
                        Toast.makeText(PhoneNumberVarification.this, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                    }
                } else if (send_otp.getText().toString().equalsIgnoreCase(getResources().getString(R.string.submit))) {
                    if (otp.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        Toast.makeText(this, "Enter otp", Toast.LENGTH_SHORT).show();
                    }
                    else if (!(otp.getText().toString().trim().equalsIgnoreCase(serverActivationCode)))
                    {
                        Toast.makeText(this, "Enter correct otp", Toast.LENGTH_SHORT).show();
                    }
                    else if (serverActivationCode.equalsIgnoreCase(otp.getText().toString().trim())) {
                        ClsGeneral.setPreferences(PhoneNumberVarification.this, ClsGeneral.USER_PHONENUMBER, phonenumer.getText().toString());
                        startActivity(new Intent(PhoneNumberVarification.this, MainActivity.class));
                        finish();
                    }
                }
                break;
            case R.id.why:
                if (whyText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.why))) {
                    dilao();
                } else if (whyText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.change))) {
                    enterotplayout.setVisibility(View.GONE);
                    phonenumer.setClickable(true);
                    phonenumer.setFocusableInTouchMode(true);
                    send_otp.setText(getResources().getString(R.string.sentotp));
                    whyText.setText(getResources().getString(R.string.why));

                }
                break;
            case R.id.resendotp:
                if (phonenumer.getText().toString().trim().length() == 10) {
                    sendOtp = new SendOtp();
                    sendOtp.execute(WebServices.SENDOTP, phonenumer.getText().toString());
                } else {
                    Toast.makeText(PhoneNumberVarification.this, "Enter Valid Number", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void dilao() {
        final Dialog dialog = new Dialog(PhoneNumberVarification.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.why_dialog);
        dialog.show();

        TextView okgotit = (TextView) dialog.findViewById(R.id.okgotit);
        okgotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private class SendOtp extends AsyncTask<String, Void, String> {
        String result1;
        ProgressDialog progressDialog;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(PhoneNumberVarification.this, "Please wait...",
                    "");
            progressDialog.setCancelable(false);
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                nameValuePairs.add(new BasicNameValuePair("user_email", ClsGeneral.getPreferences(PhoneNumberVarification.this, ClsGeneral.USER_EMAIL)));
                nameValuePairs.add(new BasicNameValuePair("user_phonenumber", params[1]));
                result1 = Utilz.executeHttpPost(params[0], nameValuePairs);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return result1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }


            if (s != null) {
                try {

                    JSONObject jsonObject = new JSONObject(s);


                    String isSuccess = jsonObject.getString("success");
                    String msg = jsonObject.getString("success_message");


                    if (isSuccess.equalsIgnoreCase("true")) {

                        serverActivationCode = jsonObject.getString("otp");
                        enterotplayout.setVisibility(View.VISIBLE);
                        whyText.setText(getResources().getString(R.string.change));
                        phonenumer.setClickable(false);
                        phonenumer.setFocusable(false);
                        send_otp.setText(getResources().getString(R.string.submit));

                    } else {

                        Toast.makeText(PhoneNumberVarification.this, "" + msg, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

