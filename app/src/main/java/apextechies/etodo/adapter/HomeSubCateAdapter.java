package apextechies.etodo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apextechies.etodo.R;
import apextechies.etodo.activity.HomeSubCategory;
import apextechies.etodo.model.HomeSubCategoryModel;

/**
 * Created by Shankar on 7/16/2017.
 */
public class HomeSubCateAdapter  extends RecyclerView.Adapter<HomeSubCateAdapter.ViewHolder> {

    private List<HomeSubCategoryModel> applications;
    ArrayList<HomeSubCategoryModel> arraylist;
    private int rowLayout;
    private HomeSubCategory mAct;
    private Context context;

    public HomeSubCateAdapter(Context c, List<HomeSubCategoryModel> applications, int rowLayout, HomeSubCategory act) {
        this.context = c;
        this.applications = applications;
        this.rowLayout = rowLayout;
        this.mAct = act;
        this.arraylist = new ArrayList<HomeSubCategoryModel>();

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

    public void addApplications(List<HomeSubCategoryModel> applications) {
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
        final HomeSubCategoryModel appInfo = applications.get(i);
        viewHolder.name.setText(appInfo.getHeading());
        if (!appInfo.getImages().isEmpty()) {
            Picasso.with(context).load(appInfo.getImages()).into(viewHolder.image);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAct.animateActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return applications == null ? 0 : applications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        applications.clear();
        if (charText.length() == 0) {
            applications.addAll(arraylist);
        } else {
            for (HomeSubCategoryModel wp : arraylist) {
                if (wp.getHeading().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    applications.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


}
