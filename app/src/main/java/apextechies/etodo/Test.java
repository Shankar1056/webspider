package apextechies.etodo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apextechies.etodo.activity.BaseActivity;
import apextechies.etodo.adapter.SimpleHeaderRecyclerAdapter;
import apextechies.etodo.model.HomeCategory;
import apextechies.etodo.network.Utilz;
import apextechies.etodo.webservices.WebServices;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 02 Sep 2017 at 1:43 PM
 */

public class Test extends BaseActivity implements ObservableScrollViewCallbacks {
	private View mHeaderView;
	private View mToolbarView;
	private ObservableRecyclerView mRecyclerView;
	private int mBaseTranslationY;
	private ArrayList<HomeCategory> homeCategories = new ArrayList<>();
	private View headerView;
	private boolean scroll = false;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stickyheaderrecyclerview);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
		
		mHeaderView = findViewById(R.id.header);
		ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
		mToolbarView = findViewById(R.id.toolbar);
		
		mRecyclerView = (ObservableRecyclerView) findViewById(R.id.recycler);
		mRecyclerView.setScrollViewCallbacks(this);
		mRecyclerView.setLayoutManager(new  GridLayoutManager(this, 2));
		mRecyclerView.setHasFixedSize(false);
		headerView = LayoutInflater.from(this).inflate(R.layout.recycler_header, null);
		//setDummyDataWithHeader(mRecyclerView, headerView);
//		mRecyclerView.setAdapter(new SimpleHeaderRecyclerAdapter(this, getDummyData(), headerView));
		
		findViewById(R.id.sticky).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(Test.this, "clicked", Toast.LENGTH_SHORT).show();
			}
		});
		
		new GetCategory().execute(WebServices.CATEGORY);
	}
	
	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
		if (dragging) {
			if (scroll) {
				int toolbarHeight = mToolbarView.getHeight();
				if (firstScroll) {
					float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
					if (-toolbarHeight < currentHeaderTranslationY) {
						mBaseTranslationY = scrollY;
					}
				}
				float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
				ViewPropertyAnimator.animate(mHeaderView).cancel();
				ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
			}
			scroll = dragging;
		}
		
	}
	
	@Override
	public void onDownMotionEvent() {
	}
	
	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {
		mBaseTranslationY = 0;
		
		if (scrollState == ScrollState.DOWN) {
			showToolbar();
		} else if (scrollState == ScrollState.UP) {
			int toolbarHeight = mToolbarView.getHeight();
			int scrollY = mRecyclerView.getCurrentScrollY();
			if (toolbarHeight <= scrollY) {
				hideToolbar();
			} else {
				showToolbar();
			}
		} else {
			// Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
			if (!toolbarIsShown() && !toolbarIsHidden()) {
				// Toolbar is moving but doesn't know which to move:
				// you can change this to hideToolbar()
				showToolbar();
			}
		}
	}
	
	private boolean toolbarIsShown() {
		float a = ViewHelper.getTranslationY(mHeaderView);
		return ViewHelper.getTranslationY(mHeaderView) == 0;
	}
	
	private boolean toolbarIsHidden() {
		float b = ViewHelper.getTranslationY(mHeaderView);
		float c = -mToolbarView.getHeight();
		return ViewHelper.getTranslationY(mHeaderView) == -mToolbarView.getHeight();
	}
	
	private void showToolbar() {
		float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
		if (headerTranslationY != 0) {
			ViewPropertyAnimator.animate(mHeaderView).cancel();
			ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
		}
	}
	
	private void hideToolbar() {
		float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
		int toolbarHeight = mToolbarView.getHeight();
		if (headerTranslationY != -toolbarHeight) {
			ViewPropertyAnimator.animate(mHeaderView).cancel();
			ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
		}
	}
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
						
						mRecyclerView.setAdapter(new SimpleHeaderRecyclerAdapter(Test.this,
						    homeCategories, headerView));
						
						
					} else {
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		}
		
	}
}
