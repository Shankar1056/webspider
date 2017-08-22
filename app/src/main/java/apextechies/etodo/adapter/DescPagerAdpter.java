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
import apextechies.etodo.activity.ProductDescription;
import apextechies.etodo.model.DescBannerImageModel;

/**
 * Created by Shankar on 5/6/2017.
 */
public class DescPagerAdpter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<DescBannerImageModel> bannerNews;
    ProductDescription homeFragment ;

    public DescPagerAdpter(Context context, ArrayList<DescBannerImageModel> bannerNews, ProductDescription homeFragment) {
        this.context = context;
        this.bannerNews = bannerNews;
        this.homeFragment = homeFragment;

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
                .load(bannerNews.get(position).getProduct_sliderimage()).into(imgNewsImage);

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
