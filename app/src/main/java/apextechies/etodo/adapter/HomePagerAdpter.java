package apextechies.etodo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apextechies.etodo.R;
import apextechies.etodo.model.BannerImageModel;

/**
 * Created by Chandu on 10/23/2015.
 */
public class HomePagerAdpter extends PagerAdapter {
        Context context;
        LayoutInflater inflater;
        ArrayList<BannerImageModel> bannerNews;

public HomePagerAdpter(Context context, ArrayList<BannerImageModel> bannerNews) {
        this.context = context;
        this.bannerNews = bannerNews;

        }

@Override
public int getCount() {
        // TODO Auto-generated method stub
        return bannerNews.size();
        }

@Override
public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);

        }

@Override
public Object instantiateItem(ViewGroup container, final int position) {
        // Declare Variables
        ImageView imgNewsImage;
        ImageButton play_bt;

        inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.home_viewpager_adapter, container,
        false);

        imgNewsImage = (ImageView) itemView.findViewById(R.id.imgNewsImage);

        imgNewsImage.setTag(bannerNews.get(position));
                Picasso.with(context)
                        .load(bannerNews.get(position).getImage()).into(imgNewsImage);

        imgNewsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     //   homeFragment.sendata();
                }
        });

        ((ViewPager) container).addView(itemView);







        return itemView;
        }



        @Override
public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

        }

        }
