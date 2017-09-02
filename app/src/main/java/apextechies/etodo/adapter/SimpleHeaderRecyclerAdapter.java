/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package apextechies.etodo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import apextechies.etodo.R;
import apextechies.etodo.model.HomeCategory;

public class SimpleHeaderRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private LayoutInflater mInflater;
    private ArrayList<HomeCategory> mItems;
    private View mHeaderView;
    private Context context;

    public SimpleHeaderRecyclerAdapter(Context context, ArrayList<HomeCategory> items, View headerView) {
        mInflater = LayoutInflater.from(context);
        mItems = items;
        mHeaderView = headerView;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null) {
            return mItems.size();
        } else {
            return mItems.size() + 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        //return (position == 0 || position == 1 || position == 2) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
        return (position ==0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        } else {
            return new ItemViewHolder(mInflater.inflate(R.layout.mainactivity_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            try {
                ((ItemViewHolder) viewHolder).cat_name.setText(mItems.get(position - 2).getCat_name());
                // Picasso.with(context).load(mItems.get())
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
           
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView cat_name;
        ImageView cat_image;

        public ItemViewHolder(View view) {
            super(view);
            cat_name = (TextView) view.findViewById(R.id.cat_name);
            cat_image = (ImageView)view.findViewById(R.id.cat_image);
        }
    }
}
