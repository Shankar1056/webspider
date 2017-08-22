package apextechies.etodo.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apextechies.etodo.R;
import apextechies.etodo.activity.HomeSubCategory;
import apextechies.etodo.adapter.HomeGridAdapter;
import apextechies.etodo.common.ExpandableHeightGridView;
import apextechies.etodo.network.Utilz;
import apextechies.etodo.model.HomeCategory;
import apextechies.etodo.webservices.WebServices;

/**
 * Created by Shankar on 12/29/2016.
 */
public class HomeFragmentNew extends Fragment{

    private ArrayList<HomeCategory> homeCategories = new ArrayList<>();
    private ExpandableHeightGridView categoryGrid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        domapping(view);

        return view;
    }

    private void domapping(View view) {


            categoryGrid = (ExpandableHeightGridView) view.findViewById(R.id.categoryGrid);


            categoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   /* Intent intent = new Intent(getActivity(), ProductList.class);
                    intent.putExtra("menuid", homeCategories.get(position).getCat_id());
                    intent.putExtra("name", homeCategories.get(position).getCat_name());
                    startActivity(intent);*/

                    Intent intent = new Intent(getActivity(), HomeSubCategory.class);
                    intent.putExtra("id", homeCategories.get(position).getCat_id());
                    intent.putExtra("name", homeCategories.get(position).getCat_name());
                    startActivity(intent);
                }
            });
        new GetCategory().execute(WebServices.CATEGORY);

        }




    /*public void sendata() {
        Intent intent = new Intent(getActivity(), TaxiBookingForm.class);
        startActivity(intent);
    }*/





    private class GetCategory extends AsyncTask<String, Void, String> {
        String result1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                            }

                        }

                        HomeGridAdapter homeGridAdapter = new HomeGridAdapter(getActivity(), homeCategories);
                        categoryGrid.setAdapter(homeGridAdapter);
                        categoryGrid.setExpanded(true);


                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }


}
