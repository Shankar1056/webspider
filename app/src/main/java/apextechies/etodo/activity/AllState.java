package apextechies.etodo.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import apextechies.etodo.R;
import apextechies.etodo.adapter.HomePagerAdpter;
import apextechies.etodo.adapter.StateGridAdapter;
import apextechies.etodo.common.ClsGeneral;
import apextechies.etodo.common.ExpandableHeightGridView;
import apextechies.etodo.model.BannerImageModel;
import apextechies.etodo.model.HomeCategory;
import apextechies.etodo.model.LocationModel;
import apextechies.etodo.network.Download_web;
import apextechies.etodo.network.OnTaskCompleted;
import apextechies.etodo.network.Utilz;
import apextechies.etodo.webservices.WebServices;

/**
 * Created by Shankar on 7/7/2017.
 */

public class AllState extends AppCompatActivity implements AdapterView.OnItemClickListener {
	ViewPager imgNewsImg;
	ArrayList<BannerImageModel> bannerNews = new ArrayList<>();
	public int currentimageindex = -1;
	boolean isGoingForaward = true;
	Timer timer;
	private ArrayList<HomeCategory> homeCategories = new ArrayList<>();
	private ExpandableHeightGridView categoryGrid;
	ArrayList<LocationModel> locationModellist = new ArrayList<>();
	ArrayList<String> stringlist = new ArrayList<>();
	private String loc_id = "1", stateid;
	private boolean doubleBackToExitPressedOnce = false;
	private StateGridAdapter homeGridAdapter;
	private AutoCompleteTextView autoTextView;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_state);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = AllState.this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(ContextCompat.getColor(AllState.this, R.color.colorAccent));
		}
		domapping();
	}
	
	private void domapping() {
		imgNewsImg = (ViewPager) findViewById(R.id.imgNewsImage);
		categoryGrid = (ExpandableHeightGridView) findViewById(R.id.categoryGrid);
		
		categoryGrid.setOnItemClickListener(this);
		
		
		imgNewsImg.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				currentimageindex = arg0;
				Log.d("POSITION", "" + arg0);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
				for (int i = 0; i < bannerNews.size(); i++) {
					if (i == arg0) {
						Log.d("POSITION_ new", "" + arg0);
						
					} else {
					}
				}
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		
		if (Utilz.isInternetConnected(AllState.this)) {
			
			getbannerimage();
			
		} else {
			Toast.makeText(AllState.this, "No internet connection", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void getcity() {
		Download_web web = new Download_web(this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				closeDialog();
				if (!response.equals("")) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						String status = jsonObject.getString("success");
						if (status.equalsIgnoreCase("true")) {
							locationModellist.clear();
							stringlist.clear();
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject1 = jsonArray.getJSONObject(i);
								String id = jsonObject1.getString("id");
								String state_id = jsonObject1.getString("state_id");
								String name = jsonObject1.getString("name");
								
								LocationModel locationModel = new LocationModel(id, state_id, name);
								locationModellist.add(locationModel);
								stringlist.add(name);
							}
							
							ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AllState.this,
							    android.R.layout.select_dialog_item, stringlist);
							
							
							autoTextView.setThreshold(0);
							autoTextView.setAdapter(arrayAdapter);
							
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
		showDailog();
		web.setReqType(true);
		web.execute(WebServices.CITY);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(this, "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
		
		stateid = homeCategories.get(position).getCat_id();
		ClsGeneral.setPreferences(AllState.this, "stateid", stateid);
		showcitydialog();
	}
	
	private void showcitydialog() {
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
		dialogBuilder.setView(dialogView);
		final AlertDialog b = dialogBuilder.create();
		getcity();
		autoTextView = (AutoCompleteTextView) dialogView.findViewById(R.id.autocompleteEditTextView);
		
		autoTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				autoTextView.showDropDown();
			}
		});
		
		autoTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				loc_id = locationModellist.get(position).getCity_id();
				for (int i = 0; i < locationModellist.size(); i++) {
					if (locationModellist.get(i).getLocation().equalsIgnoreCase(autoTextView.getText().toString())) {
						loc_id = locationModellist.get(i).getCity_id();
					}
				}
				ClsGeneral.setPreferences(AllState.this, "cityid", loc_id);
				startActivity(new Intent(AllState.this, MainActivity.class));
				b.dismiss();
			}
		});
		
		
		b.show();
		
	}
	
	
	private void getstate() {
		Download_web web = new Download_web(this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				closeDialog();
				if (!response.equals("")) {
					String cat_id = null, cat_name = null, cat_image = null, cat_status = null;
					
					try {
						JSONObject jsonObject = new JSONObject(response);
						String success = jsonObject.getString("success");
						String success_msg = jsonObject.getString("success_message");
						if (success.equalsIgnoreCase("true")) {
							homeCategories.clear();
							
							
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject1 = jsonArray.getJSONObject(i);
								if (jsonObject1.has("id")) {
									cat_id = jsonObject1.getString("id");
									
								}
								if (jsonObject1.has("name")) {
									cat_name = jsonObject1.getString("name");
									
								}
								if (jsonObject1.has("image")) {
									cat_image = jsonObject1.getString("image");
									
								}
								
								if (jsonObject1.has("cat_status")) {
									cat_status = jsonObject1.getString("cat_status");
								}
								homeCategories.add(new HomeCategory(cat_id, cat_name, cat_image, cat_status, false, R
								    .mipmap.red_back, 0, R.mipmap.white_back));
								
							}
							
							homeGridAdapter = new StateGridAdapter(AllState.this, homeCategories,
							    AllState.this);
							categoryGrid.setAdapter(homeGridAdapter);
							categoryGrid.setExpanded(true);
							
							
						} else {
							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
		showDailog();
		web.setReqType(true);
		web.execute(WebServices.STATE);
		
	}
	
	private void getbannerimage() {
		Download_web web = new Download_web(this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				closeDialog();
				if (!response.equals("")) {
					String image = null, slider_id = null, slider_status = null, slider_coupanid = null;
					try {
						JSONObject jsonObject = new JSONObject(response);
						String success = jsonObject.getString("success");
						String success_message = jsonObject.getString("success_message");
						if (success.equalsIgnoreCase("true")) {
							bannerNews.clear();
							
							
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							
							
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject1 = jsonArray.getJSONObject(i);
								
								if (jsonObject1.has("slider_id")) {
									slider_id = jsonObject1.getString("slider_id");
								}
								if (jsonObject1.has("slider_image")) {
									image = jsonObject1.getString("slider_image");
								}
								if (jsonObject1.has("slider_status")) {
									slider_status = jsonObject1.getString("slider_status");
								}
								if (jsonObject1.has("slider_coupanid")) {
									slider_coupanid = jsonObject1.getString("slider_coupanid");
								}
								
								if (slider_status.equalsIgnoreCase("1")) {
									bannerNews.add(new BannerImageModel(slider_id, image, slider_status, slider_coupanid));
								}
								
							}
							
							if (bannerNews.size() > 0) {
								HomePagerAdpter viewPagerAdapter = new HomePagerAdpter(
								    AllState.this, bannerNews);
								imgNewsImg.setAdapter(viewPagerAdapter);
								// pageSwitcher(2);
								final Handler mHandler = new Handler();
								
								final Runnable mUpdateResults = new Runnable() {
									public void run() {
										AnimateandSlideShow();
									}
								};
								timer = new Timer();
								timer.scheduleAtFixedRate(new TimerTask() {
									public void run() {
										
										mHandler.post(mUpdateResults);
									}
									
								}, 5000, 5000);
							}
							
						} else {
							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				getstate();
			}
		});
		//  showDailog();
		web.setReqType(true);
		web.execute(WebServices.BANNERSLIDER);
		
	}
	
	
	private void AnimateandSlideShow() {
		
		if (isGoingForaward) {
			currentimageindex++;
			
			if (currentimageindex >= (bannerNews.size() - 1)) {
				isGoingForaward = false;
				currentimageindex = -1;
			}
		} else if (!isGoingForaward) {
			currentimageindex--;
			if (currentimageindex <= 0) {
				isGoingForaward = true;
			}
		}
		
		Log.d("POSITION", "" + currentimageindex);
		imgNewsImg.setCurrentItem(currentimageindex);
	}
	
	@Override
	public void onBackPressed() {
		
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
		}
		
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 3000);
	}
	
	ProgressDialog dialog;
	
	void showDailog() {
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	void closeDialog() {
		if (dialog != null)
			dialog.cancel();
	}
	
	public void setpos() {
		homeGridAdapter.notifyDataSetChanged();
	}
	
	
}
