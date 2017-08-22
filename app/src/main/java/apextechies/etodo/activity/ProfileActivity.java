package apextechies.etodo.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import apextechies.etodo.R;
import apextechies.etodo.common.ClsGeneral;
import apextechies.etodo.network.Utilz;
import apextechies.etodo.login.LoginActivity;
import apextechies.etodo.webservices.WebServices;

/**
 * Created by Shankar on 5/15/2017.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name, email, contact;
    private RadioGroup genderGroup;
    private RadioButton genderButton;
    private TextView logoutupdate,dob;
    String gender = "";
    private MyProfile myProfile;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.profile_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ProfileActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(ProfileActivity.this, R.color.colorAccent));
        }
        domapping();


    }

    private void domapping() {
        try

        {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            TextView actionbar_title = (TextView) findViewById(R.id.actionbar_title);
            actionbar_title.setText(getResources().getString(R.string.contactus));
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            name = (EditText) findViewById(R.id.name);
            email = (EditText) findViewById(R.id.email);
            contact = (EditText) findViewById(R.id.contact);
            dob = (TextView) findViewById(R.id.dob);
            logoutupdate = (TextView) findViewById(R.id.logoutupdate);


            name.setClickable(false);
            email.setClickable(false);
            contact.setClickable(false);
            dob.setClickable(false);
            name.setFocusable(false);
            email.setFocusable(false);
            contact.setFocusable(false);
            dob.setFocusable(false);

            findViewById(R.id.email_sign_in_button).setOnClickListener(this);
            findViewById(R.id.edit).setOnClickListener(this);
            dob.setOnClickListener(this);

            genderGroup = (RadioGroup) findViewById(R.id.radioGroup1);
            RadioButton radio0 = (RadioButton) findViewById(R.id.radio0);
            RadioButton radio1 = (RadioButton) findViewById(R.id.radio1);

            actionbar_title.setText(getResources().getString(R.string.myprofile));
            name.setText(ClsGeneral.getPreferences(ProfileActivity.this, ClsGeneral.USER_NAME));
            email.setText(ClsGeneral.getPreferences(ProfileActivity.this, ClsGeneral.USER_EMAIL));
            contact.setText(ClsGeneral.getPreferences(ProfileActivity.this, ClsGeneral.USER_PHONENUMBER));
            dob.setText(ClsGeneral.getPreferences(ProfileActivity.this, ClsGeneral.USER_DOB));
            if (ClsGeneral.getPreferences(ProfileActivity.this, ClsGeneral.USER_GENDER).equalsIgnoreCase("Male")) {
                radio0.setChecked(true);
            }
            if (ClsGeneral.getPreferences(ProfileActivity.this, ClsGeneral.USER_GENDER).equalsIgnoreCase("Female")) {
                radio1.setChecked(true);
            }

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileActivity.this.finish();
                }
            });

            genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (logoutupdate.getText().toString().equalsIgnoreCase(getResources().getString(R.string.update))) {
                        genderButton = (RadioButton) findViewById(checkedId);
                        // This puts the value (true/false) into the variable
                        boolean isChecked = genderButton.isChecked();

                        if (isChecked) {
                            gender = genderButton.getText().toString().trim();
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        getDate();
    }

    private void getDate() {
        try {

            myCalendar = Calendar.getInstance();

            date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel(myCalendar);
                }

            };


        } catch (Exception e) {

        }

    }

    private void updateLabel(Calendar myCalendar) {

        try {
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            dob.setText(sdf.format(myCalendar.getTime()));
        } catch (Exception e) {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                if (logoutupdate.getText().toString().equalsIgnoreCase(getResources().getString(R.string.logout_big))) {
                    ClsGeneral.setPreferences(ProfileActivity.this, ClsGeneral.USER_ID, "");
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                } else if (logoutupdate.getText().toString().equalsIgnoreCase(getResources().getString(R.string.update))) {
                    myProfile = new MyProfile();
                    myProfile.execute(WebServices.UPDATEUSERPROFILE, name.getText().toString(), contact.getText().toString(),
                            email.getText().toString(), dob.getText().toString(), gender);
                }

                break;
            case R.id.edit:
                name.setClickable(true);
                dob.setClickable(true);
                name.setFocusableInTouchMode(true);
                dob.setFocusableInTouchMode(true);
                logoutupdate.setText(getResources().getString(R.string.update));
                break;
            case R.id.dob:
                if (logoutupdate.getText().toString().equalsIgnoreCase(getResources().getString(R.string.update))) {
                    new DatePickerDialog(ProfileActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    break;
                }
                break;

        }
    }

    private class MyProfile extends AsyncTask<String, Void, String> {
        private ProgressDialog mProgressDialog;
        String result1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(ProfileActivity.this);
                mProgressDialog.setMessage("Updating your profile");
                mProgressDialog.setIndeterminate(true);
            }

            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            nameValuePairs.add(new BasicNameValuePair("user_id", ClsGeneral.getPreferences(ProfileActivity.this, ClsGeneral.USER_ID)));
            nameValuePairs.add(new BasicNameValuePair("user_name", params[1]));
            nameValuePairs.add(new BasicNameValuePair("user_email", params[3]));
            nameValuePairs.add(new BasicNameValuePair("user_phonenumber", params[2]));
            nameValuePairs.add(new BasicNameValuePair("user_dob", params[4]));
            nameValuePairs.add(new BasicNameValuePair("user_gender", params[5]));
            try {
                result1 = Utilz.executeHttpPost(params[0], nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return result1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("success");
                    String success_message = jsonObject.getString("success_message");

                    if (status.equalsIgnoreCase("true")) {
                        logoutupdate.setText(getResources().getString(R.string.logout_big));
                        name.setClickable(false);
                        email.setClickable(false);
                        contact.setClickable(false);
                        dob.setClickable(false);
                        name.setFocusable(false);
                        email.setFocusable(false);
                        contact.setFocusable(false);
                        dob.setFocusable(false);

                        ClsGeneral.setPreferences(ProfileActivity.this, ClsGeneral.USER_NAME, name.getText().toString());
                        ClsGeneral.setPreferences(ProfileActivity.this, ClsGeneral.USER_EMAIL, email.getText().toString());
                        ClsGeneral.setPreferences(ProfileActivity.this, ClsGeneral.USER_PHONENUMBER, contact.getText().toString());
                        ClsGeneral.setPreferences(ProfileActivity.this, ClsGeneral.USER_GENDER, gender);
                        ClsGeneral.setPreferences(ProfileActivity.this, ClsGeneral.USER_DOB, dob.getText().toString());

                    } else {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
