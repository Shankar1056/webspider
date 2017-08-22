package apextechies.etodo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apextechies.etodo.R;
import apextechies.etodo.adapter.HomeSubCateAdapter;
import apextechies.etodo.common.CustomItemAnimator;
import apextechies.etodo.model.HomeSubCategoryModel;
import apextechies.etodo.network.Utilz;
import apextechies.etodo.webservices.WebServices;

/**
 * Created by Shankar on 7/15/2017.
 */

public class HomeSubCategory extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<HomeSubCategoryModel> homesubCategories = new ArrayList<>();
    private RecyclerView subcat_rec;
    private HomeSubCateAdapter categoryListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory);

        mapping();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = HomeSubCategory.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(HomeSubCategory.this, R.color.colorAccent));
        }
    }

    private void mapping() {
        TextView name = (TextView) findViewById(R.id.name);
        subcat_rec = (RecyclerView) findViewById(R.id.subcat_rec);
        findViewById(R.id.back).setOnClickListener(this);
        subcat_rec.setLayoutManager(new GridLayoutManager(this,2));
        subcat_rec.setItemAnimator(new CustomItemAnimator());
        categoryListAdapter = new HomeSubCateAdapter(HomeSubCategory.this, new ArrayList<HomeSubCategoryModel>(), R.layout.subcategory_item, HomeSubCategory.this);
        subcat_rec.setAdapter(categoryListAdapter);

        name.setText(getIntent().getStringExtra("name"));

        callapi();

    }

    private void callapi() {
        new GetCategory().execute(WebServices.HOMESUBCATEGORY);
    }

    public void animateActivity(int i) {
        homesubCategories.get(i).getId();
        startActivity(new Intent(HomeSubCategory.this,ProductList.class).putExtra("subcat_id",homesubCategories.get(i).getId()).putExtra("name",homesubCategories.get(i).getHeading()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
        }
    }

    private class GetCategory extends AsyncTask<String, Void, String> {
        String result1;
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            nameValuePairs.add(new BasicNameValuePair("cat_id", getIntent().getStringExtra("id")));

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
            LinearLayout nodatafound = (LinearLayout)findViewById(R.id.nodatafound);
            if (s != null) {
                String id = null, cat_id = null, heading = null, images = null, status = null;

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String success = jsonObject.getString("success");
                    String success_msg = jsonObject.getString("success_message");
                    if (success.equalsIgnoreCase("true")) {
                        if (homesubCategories.size() > 0) {
                            homesubCategories.clear();
                        }
                        categoryListAdapter.clearApplications();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length()>0) {
                            nodatafound.setVisibility(View.GONE);
                            subcat_rec.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (jsonObject1.has("id")) {
                                    id = jsonObject1.getString("id");
            
                                }
                                if (jsonObject1.has("cat_id")) {
                                    cat_id = jsonObject1.getString("cat_id");
            
                                }
                                if (jsonObject1.has("heading")) {
                                    heading = jsonObject1.getString("heading");
            
                                }
        
                                if (jsonObject1.has("images")) {
                                    images = jsonObject1.getString("images");
                                }
                                if (jsonObject1.has("status")) {
                                    status = jsonObject1.getString("status");
                                }
                                if (status.equalsIgnoreCase("1")) {
                                    homesubCategories.add(new HomeSubCategoryModel(id, cat_id, heading, images, status));
            
                                }
        
                            }
                            categoryListAdapter.addApplications(homesubCategories);
                            categoryListAdapter.notifyDataSetChanged();
    
                        }
                        else {
                            nodatafound.setVisibility(View.VISIBLE);
                            subcat_rec.setVisibility(View.GONE);
                            
                        }
                    } else {
                        nodatafound.setVisibility(View.VISIBLE);
                        subcat_rec.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
