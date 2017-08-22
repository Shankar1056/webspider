package apextechies.etodo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apextechies.etodo.R;
import apextechies.etodo.activity.ProductList;
import apextechies.etodo.model.ProductListModel;

/**
 * Created by Shankar on 5/5/2017.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private List<ProductListModel> applications;
    ArrayList<ProductListModel> arraylist;
    private int rowLayout;
    private ProductList mAct;
    private Context context;

    public ProductListAdapter(Context c, List<ProductListModel> applications, int rowLayout, ProductList act) {
        this.context = c;
        this.applications = applications;
        this.rowLayout = rowLayout;
        this.mAct = act;
        this.arraylist = new ArrayList<ProductListModel>();

    }


    public void clearApplications() {
        int size = this.applications.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                applications.remove(0);
                arraylist.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addApplications(List<ProductListModel> applications) {
        this.applications.addAll(applications);
        this.arraylist.addAll(applications);
        this.notifyItemRangeInserted(0, applications.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final ProductListModel appInfo = applications.get(i);
        viewHolder.vendorname.setText(appInfo.getVendor_name());
        viewHolder.vendorlocation.setText(appInfo.getVendor_Location());
       /* if (!(appInfo.getRating().equalsIgnoreCase(""))) {
            viewHolder.ratingBar.setRating(Float.parseFloat(appInfo.getRating()));
        }*/
        if (!appInfo.getProduct_image().isEmpty()) {
            Picasso.with(context).load(appInfo.getProduct_image()).into(viewHolder.productImage);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mAct.animateActivity(i);
            }
        });
        viewHolder.calllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  mAct.call(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return applications == null ? 0 : applications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView vendorname, vendorlocation;
        private ImageView productImage;
        private RelativeLayout calllayout;

        public ViewHolder(View itemView) {
            super(itemView);
            vendorname = (TextView) itemView.findViewById(R.id.vendorname);
            vendorlocation = (TextView) itemView.findViewById(R.id.vendorlocation);
            calllayout = (RelativeLayout) itemView.findViewById(R.id.calllayout);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
        }

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        applications.clear();
        if (charText.length() == 0) {
            applications.addAll(arraylist);
        } else {
            for (ProductListModel wp : arraylist) {
                if (wp.getProduct_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    applications.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}
