package apextechies.etodo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import apextechies.etodo.R;
import apextechies.etodo.adapter.DescPagerAdpter;
import apextechies.etodo.common.ClsGeneral;
import apextechies.etodo.model.DescBannerImageModel;
import apextechies.etodo.network.Utilz;
import apextechies.etodo.webservices.WebServices;

import static apextechies.etodo.R.string.addtowishlist;
import static apextechies.etodo.R.string.like;
import static apextechies.etodo.R.string.liked;

/**
 * Created by Shankar on 5/6/2017.
 */

public class ProductDescription extends AppCompatActivity implements View.OnClickListener {
	ViewPager imgNewsImg;
	ArrayList<DescBannerImageModel> bannerNews;
	private int currentimageindex = -1;
	boolean isGoingForaward = true;
	Timer timer;
	ImageView[] circleButtons = new ImageView[6];
	private GetDescBannerImage getDescBannerImage;
	private TextView websitelink, email, liketext, wishlisttext;
	private DoLike doLike;
	private int count;
	private TextView likecount,name;
	
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy =
			    new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		setContentView(R.layout.product_description);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = ProductDescription.this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(ContextCompat.getColor(ProductDescription.this, R.color.colorAccent));
		}
		
		try {
			count = Integer.parseInt(getIntent().getStringExtra("likecount"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			count = 0;
		}
		domapping();
	}
	
	private void domapping() {
		try {
			imgNewsImg = (ViewPager) findViewById(R.id.imgNewsImage);
			bannerNews = new ArrayList<>();
			
			TextView vendorname = (TextView) findViewById(R.id.vendorname);
			likecount = (TextView) findViewById(R.id.likecount);
			TextView totalviews = (TextView) findViewById(R.id.totalviews);
			TextView phone = (TextView) findViewById(R.id.phone);
			email = (TextView) findViewById(R.id.email);
			TextView contactpersonname = (TextView) findViewById(R.id.contactpersonname);
			TextView location = (TextView) findViewById(R.id.location);
			TextView description = (TextView) findViewById(R.id.description);
			websitelink = (TextView) findViewById(R.id.websitelink);
			liketext = (TextView) findViewById(R.id.liketext);
			wishlisttext = (TextView) findViewById(R.id.wishlisttext);
			name = (TextView)findViewById(R.id.name);
			findViewById(R.id.wishlist).setOnClickListener(this);
			findViewById(R.id.back).setOnClickListener(this);
			
			String a = getIntent().getStringExtra("vendorname");
			vendorname.setText(getIntent().getStringExtra("vendorname"));
			phone.setText(getIntent().getStringExtra("vendorcontact"));
			email.setText(getIntent().getStringExtra("vendoremail"));
			contactpersonname.setText(getIntent().getStringExtra("contactperson"));
			location.setText(getIntent().getStringExtra("contactlocation"));
			websitelink.setText(getIntent().getStringExtra("websitelink"));
			description.setText(getIntent().getStringExtra("description"));
			name.setText(a);
			if (getIntent().getStringExtra("liked").equalsIgnoreCase("1")) {
				liketext.setText(getResources().getString(liked));
				liketext.setTextColor(getResources().getColor(R.color.red));
			}
			if (getIntent().getStringExtra("liked").equalsIgnoreCase("0")) {
				liketext.setText(getResources().getString(R.string.like));
				liketext.setTextColor(getResources().getColor(R.color.black));
			}
			if (getIntent().getStringExtra("wishliststatus").equalsIgnoreCase("1")) {
				wishlisttext.setText(getResources().getString(R.string.removefromwishlist));
				wishlisttext.setTextColor(getResources().getColor(R.color.red));
			}
			if (getIntent().getStringExtra("wishliststatus").equalsIgnoreCase("0")) {
				wishlisttext.setText(getResources().getString(R.string.addtowishlist));
				wishlisttext.setTextColor(getResources().getColor(R.color.black));
			}
			if (getIntent().getStringExtra("likecount").length() > 0) {
				likecount.setText(getIntent().getStringExtra("likecount"));
			}
			
			findViewById(R.id.calllayout).setOnClickListener(this);
			findViewById(R.id.share).setOnClickListener(this);
			findViewById(R.id.like).setOnClickListener(this);
			websitelink.setOnClickListener(this);
			email.setOnClickListener(this);
			
			ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
			ImageLoader.getInstance().init(configuration);
			circleButtons[0] = (ImageView) findViewById(R.id.btnfirst);
			circleButtons[1] = (ImageView) findViewById(R.id.btnsecond);
			circleButtons[2] = (ImageView) findViewById(R.id.btnThird);
			circleButtons[3] = (ImageView) findViewById(R.id.btnFourth);
			circleButtons[4] = (ImageView) findViewById(R.id.btnFifth);
			circleButtons[5] = (ImageView) findViewById(R.id.btnSixth);
			
			//categoryGrid.setNumColumns(3);
			
			
			if (Utilz.isInternetConnected(ProductDescription.this)) {
				getDescBannerImage = new GetDescBannerImage();
				getDescBannerImage.execute(WebServices.DESCRIPTIONSLIDER);
				
			} else {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ProductDescription.this, "No internet connection", Toast.LENGTH_SHORT).show();
					}
				}, 2000);
			}
			
			
			imgNewsImg.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageSelected(int arg0) {
					currentimageindex = arg0;
					Log.d("POSITION", "" + arg0);
					
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
					for (int i = 0; i < 6; i++) {
						if (i == arg0) {
							Log.d("POSITION_ new", "" + arg0);
							circleButtons[i]
							    .setImageResource(R.drawable.rounded_home_select);
							
						} else {
							circleButtons[i]
							    .setImageResource(R.drawable.rounded_home_unselect);
						}
					}
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
				}
			});
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.calllayout:
				try {
					
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:" + getIntent().getStringExtra("vendorcontact")));
					startActivity(intent);
				} catch (Exception e) {
					Toast.makeText(ProductDescription.this, "Phone facility not available on your device", Toast.LENGTH_SHORT).show();
				}
				
				break;
			case R.id.websitelink:
				try {
					String url = getIntent().getStringExtra("websitelink");
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				} catch (Exception e) {
					//Toast.makeText(this, "cant", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.email:
				try {
					Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
					    "mailto", getIntent().getStringExtra("vendoremail"), null));
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
					emailIntent.putExtra(Intent.EXTRA_TEXT, "");
					startActivity(Intent.createChooser(emailIntent, "Send email..."));
				} catch (Exception e) {
					//Toast.makeText(this, "cant", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.share:
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=apextechies.etodo&hl=en");
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
				break;
			case R.id.like:
				doLike = new DoLike();
				
				if (liketext.getText().toString().equalsIgnoreCase(getResources().getString(liked))) {
					try {
						if (count > 0) {
							count = count - 1;
						}
						
					} catch (NumberFormatException e) {
						count = 0;
					} catch (NullPointerException e) {
						count = 0;
					} catch (Exception e) {
						count = 0;
					}
					
					
					doLike.execute(WebServices.LIKEWISHLISTRATE, "0", "" + count, "1", "like");
				} else if (liketext.getText().toString().equalsIgnoreCase(getResources().getString(like))) {
					try {
						if (count > 0) {
							count = count + 1;
						}
						
					} catch (NumberFormatException e) {
						count = 0;
					} catch (NullPointerException e) {
						count = 0;
					} catch (Exception e) {
						count = 0;
					}
					doLike.execute(WebServices.LIKEWISHLISTRATE, "1", "" + count, "1", "like");
				}
				break;
			case R.id.wishlist:
				doLike = new DoLike();
				
				if (wishlisttext.getText().toString().equalsIgnoreCase(getResources().getString(R.string.removefromwishlist))) {
					doLike.execute(WebServices.LIKEWISHLISTRATE, "1", "" + count, "0", "wishlist");
				} else if (wishlisttext.getText().toString().equalsIgnoreCase(getResources().getString(R.string.addtowishlist))) {
					
					doLike.execute(WebServices.LIKEWISHLISTRATE, "1", "" + count, "1", "wishlist");
				}
				break;
			
			case R.id.back:
				finish();
				break;
		}
	}
	
	
	private class GetDescBannerImage extends AsyncTask<String, Void, String> {
		String result1;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			nameValuePairs.add(new BasicNameValuePair("product_id", getIntent().getStringExtra("product_id")));
			
			try {
				result1 = Utilz.executeHttpPost(params[0], nameValuePairs);
				//result1="{\"employee\":[{\"image\":\"http://phpautoclassifiedscript.com/images/taxi-booking-script.jpg\"}]}";
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result1;
		}
		
		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			
			if (s != null) {
				String product_id = null, product_sliderid = null, product_sliderimage = null, product_slider_visibility_status = null, product_slidercoupanid = null;
				try {
					JSONObject jsonObject = new JSONObject(s);
					String success = jsonObject.getString("success");
					String success_message = jsonObject.getString("success_message");
					if (success.equalsIgnoreCase("true")) {
						bannerNews.clear();
						
						
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						
						
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject1 = jsonArray.getJSONObject(i);
							
							if (jsonObject1.has("product_sliderid")) {
								product_sliderid = jsonObject1.getString("product_sliderid");
							}
							if (jsonObject1.has("product_id")) {
								product_id = jsonObject1.getString("product_id");
							}
							if (jsonObject1.has("product_sliderimage")) {
								product_sliderimage = jsonObject1.getString("product_sliderimage");
							}
							if (jsonObject1.has("product_slider_visibility_status")) {
								product_slider_visibility_status = jsonObject1.getString("product_slider_visibility_status");
							}
							if (jsonObject1.has("product_slidercoupanid")) {
								product_slidercoupanid = jsonObject1.getString("product_slidercoupanid");
							}
							
							if (product_slider_visibility_status.equalsIgnoreCase("1")) {
								if (i <= 5) {
									circleButtons[i].setVisibility(View.VISIBLE);
								}
								bannerNews.add(new DescBannerImageModel(product_sliderid, product_id, product_sliderimage, product_slider_visibility_status,
								    product_slidercoupanid));
							}
							
						}
						
						if (bannerNews.size() > 0) {
							DescPagerAdpter viewPagerAdapter = new DescPagerAdpter(
							    ProductDescription.this, bannerNews, ProductDescription.this);
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
								
							}, 2000, 2000);
						}
						
					} else {
						
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
	
	private class DoLike extends AsyncTask<String, Void, String> {
		String result1;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			nameValuePairs.add(new BasicNameValuePair("user_id", ClsGeneral.getPreferences(ProductDescription.this, ClsGeneral.USER_ID)));
			nameValuePairs.add(new BasicNameValuePair("email", ClsGeneral.getPreferences(ProductDescription.this, ClsGeneral.USER_EMAIL)));
			nameValuePairs.add(new BasicNameValuePair("product_id", getIntent().getStringExtra("product_id")));
			nameValuePairs.add(new BasicNameValuePair("liked", params[1]));
			nameValuePairs.add(new BasicNameValuePair("Like_count", params[2]));
			nameValuePairs.add(new BasicNameValuePair("wishlist", params[3]));
			nameValuePairs.add(new BasicNameValuePair("rate", "5"));
			nameValuePairs.add(new BasicNameValuePair("like_wishlist", params[4]));
			
			try {
				result1 = Utilz.executeHttpPost(params[0], nameValuePairs);
				//result1="{\"employee\":[{\"image\":\"http://phpautoclassifiedscript.com/images/taxi-booking-script.jpg\"}]}";
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result1;
		}
		
		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			
			if (s != null) {
				try {
					JSONObject jsonObject = new JSONObject(s);
					String success = jsonObject.getString("success");
					String success_message = jsonObject.getString("success_message");
					if (success.equalsIgnoreCase("true")) {
						if (jsonObject.has("name")) {
							if (jsonObject.getString("name").equalsIgnoreCase("like")) {
								likecount.setText("" + count);
								if (jsonObject.getString("data").equalsIgnoreCase("1")) {
									liketext.setText(getResources().getString(R.string.liked));
									liketext.setTextColor(getResources().getColor(R.color.red));
								}
								if (jsonObject.getString("data").equalsIgnoreCase("0")) {
									liketext.setText(getResources().getString(like));
									liketext.setTextColor(getResources().getColor(R.color.black));
								}
							}
							if (jsonObject.getString("name").equalsIgnoreCase("wishlist")) {
								if (jsonObject.getString("data").equalsIgnoreCase("1")) {
									wishlisttext.setText(getResources().getString(R.string.removefromwishlist));
									wishlisttext.setTextColor(getResources().getColor(R.color.red));
								}
								if (jsonObject.getString("data").equalsIgnoreCase("0")) {
									wishlisttext.setText(getResources().getString(addtowishlist));
									wishlisttext.setTextColor(getResources().getColor(R.color.black));
								}
							}
						}
						
						
					} else {
						
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
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
	
}
