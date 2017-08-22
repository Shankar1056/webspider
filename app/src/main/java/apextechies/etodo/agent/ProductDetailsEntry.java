package apextechies.etodo.agent;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apextechies.etodo.R;
import apextechies.etodo.network.Utilz;
import apextechies.etodo.model.HomeCategory;
import apextechies.etodo.webservices.WebServices;

/**
 * Created by Shankar on 5/14/2017.
 */

public class ProductDetailsEntry extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private EditText vendorname,vendorcontactpersonname,vendorpersoncontactnumber,vendoremail,vendorwebsiteurllink,vendorlocation;
    private EditText productname,producsortdesc,produclondesc,coupancodename;
    private SaveData saveData;
    private AutoCompleteTextView autoTextView;
    private ArrayList<HomeCategory> homeCategories = new ArrayList<>();
    private final ArrayList<String> stringlist = new ArrayList<>();
    private String cat_id;
    private GetCategory getCategory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.productdetails_entry);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ProductDetailsEntry.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(ProductDetailsEntry.this, R.color.colorPrimaryDark));

        }
        domapgping();
    }

    private void domapgping() {
        try
        {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            TextView actionbar_title = (TextView)findViewById(R.id.actionbar_title);
            actionbar_title.setText(getResources().getString(R.string.submitvendordetails));
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

            vendorname = (EditText)findViewById(R.id.vendorname);
            vendorcontactpersonname = (EditText)findViewById(R.id.vendorcontactpersonname);
            vendorpersoncontactnumber = (EditText)findViewById(R.id.vendorpersoncontactnumber);
            vendoremail = (EditText)findViewById(R.id.vendoremail);
            vendorwebsiteurllink = (EditText)findViewById(R.id.vendorwebsiteurllink);
            vendorlocation = (EditText)findViewById(R.id.vendorlocation);
            productname = (EditText)findViewById(R.id.productname);
            producsortdesc = (EditText)findViewById(R.id.producsortdesc);
            produclondesc = (EditText)findViewById(R.id.produclondesc);
            coupancodename = (EditText)findViewById(R.id.coupancodename);
            findViewById(R.id.submit).setOnClickListener(this);

            autoTextView = (AutoCompleteTextView) findViewById(R.id.autocompleteEditTextView);
            autoTextView.setOnItemClickListener(this);
            autoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    autoTextView.showDropDown();
                }
            });

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetailsEntry.this.finish();
                }
            });



            if (homeCategories.size() == 0) {
                getCategory = new GetCategory();
                getCategory.execute(WebServices.CATEGORY);
                }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.submit:

                if (vendorname.getText().toString().trim().equalsIgnoreCase(""))
                {
                    Toast.makeText(this, "Vendor name is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (vendorlocation.getText().toString().trim().equalsIgnoreCase(""))
                {
                    Toast.makeText(this, "Vendor location is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    if (Utilz.isInternetConnected(ProductDetailsEntry.this)) {
                        saveData = new SaveData();
                        saveData.execute(WebServices.SUBMITPRODUCTDETAILS, vendorname.getText().toString().trim(), vendorcontactpersonname.getText().toString().trim(),
                                vendorpersoncontactnumber.getText().toString().trim(), vendoremail.getText().toString().trim(), vendorwebsiteurllink.getText().toString().trim(),
                                vendorlocation.getText().toString().trim(), productname.getText().toString().trim(), producsortdesc.getText().toString().trim(),
                                produclondesc.getText().toString().trim(), coupancodename.getText().toString().trim(), cat_id);
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {

              //  searchedItem.add("" + parent.getItemAtPosition(position));
           String cat = ""+parent.getItemAtPosition(position);
            for (int i=0;i<homeCategories.size();i++)
            {
                if (homeCategories.get(i).getCat_name().equalsIgnoreCase(cat))
                {
                    cat_id = homeCategories.get(i).getCat_id();
                }
            }

        } catch (Exception e) {
            Log.e("Location Toast", e.getMessage());
        }
    }
    private class GetCategory extends AsyncTask<String, Void, String> {
        String result1;
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(ProductDetailsEntry.this);
                mProgressDialog.setMessage(getString(R.string.loading_location));
                mProgressDialog.setIndeterminate(true);
            }

            mProgressDialog.show();


    }

        @Override
        protected String doInBackground(String... params) {


            try {
                result1 = Utilz.executeHttpGet(params[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                String cat_id = null, cat_name = null, cat_image = null, cat_status = null;

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String success = jsonObject.getString("success");
                    String success_msg = jsonObject.getString("success_message");
                    if (success.equalsIgnoreCase("true")) {
                        homeCategories.clear();
                        stringlist.clear();

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1.has("cat_id")) {
                                cat_id = jsonObject1.getString("cat_id");

                            }
                            if (jsonObject1.has("cat_name")) {
                                cat_name = jsonObject1.getString("cat_name");

                            }
                            if (jsonObject1.has("cat_image")) {
                                cat_image = jsonObject1.getString("cat_image");

                            }

                            if (jsonObject1.has("cat_status")) {
                                cat_status = jsonObject1.getString("cat_status");
                            }
                            if (cat_status.equalsIgnoreCase("1")) {
                                homeCategories.add(new HomeCategory(cat_id, cat_name, cat_image, cat_status,false,R
                                    .mipmap.red_back,0,R.mipmap.white_back));
                                stringlist.add(cat_name);
                            }

                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ProductDetailsEntry.this,
                                android.R.layout.select_dialog_item, stringlist);


                        autoTextView.setThreshold(0);
                        autoTextView.setAdapter(arrayAdapter);
                        mProgressDialog.dismiss();



                    } else {
                        mProgressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                }

            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            getCategory=null;
            mProgressDialog.dismiss();
        }
    }

    public class SaveData extends AsyncTask<String, Void, String> {
        private ProgressDialog mProgressDialog;
        String result = null;
        ArrayList<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(ProductDetailsEntry.this);
                mProgressDialog.setMessage(getString(R.string.loading));
                mProgressDialog.setIndeterminate(true);
            }

            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            namevaluepairs.add(new BasicNameValuePair("vendor_name",params[1] ));
            namevaluepairs.add(new BasicNameValuePair("vendor_contact_person_name", params[2]));
            namevaluepairs.add(new BasicNameValuePair("vendor_contact", params[3]));
            namevaluepairs.add(new BasicNameValuePair("vendor_email", params[4]));
            namevaluepairs.add(new BasicNameValuePair("vender_url", params[5]));
            namevaluepairs.add(new BasicNameValuePair("Vendor_Location", params[6]));
            namevaluepairs.add(new BasicNameValuePair("product_name", params[7]));
            namevaluepairs.add(new BasicNameValuePair("product_shortdesc", params[8]));
            namevaluepairs.add(new BasicNameValuePair("Product_LongDesc", params[9]));
            namevaluepairs.add(new BasicNameValuePair("coupancode_name", params[10]));
            namevaluepairs.add(new BasicNameValuePair("cat_id", params[11]));
            namevaluepairs.add(new BasicNameValuePair("product_visibility_status","0"));

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
                        if (jsonObject.getString("success").equalsIgnoreCase("true"))
                        {
                            Toast.makeText(ProductDetailsEntry.this, ""+jsonObject.getString("success_msg"), Toast.LENGTH_SHORT).show();
                            vendorname.setText("");
                            vendorcontactpersonname.setText("");
                            vendorpersoncontactnumber.setText("");
                            vendoremail.setText("");
                            vendorwebsiteurllink.setText("");
                            vendorlocation.setText("");
                            productname.setText("");
                            producsortdesc.setText("");
                            produclondesc.setText("");
                            coupancodename.setText("");
                            mProgressDialog.dismiss();
                        }
                        else
                        {
                            mProgressDialog.dismiss();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    mProgressDialog.cancel();
                }

            }
            saveData = null;


        }

        @Override
        protected void onCancelled() {
            saveData = null;
            mProgressDialog.dismiss();
        }

    }
}
