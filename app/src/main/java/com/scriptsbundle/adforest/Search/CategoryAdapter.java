package com.scriptsbundle.adforest.Search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.scriptsbundle.adforest.R;
import com.scriptsbundle.adforest.helper.oncatItemClick;
import com.scriptsbundle.adforest.modelsList.subcatDiloglist;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CustomViewHolder> {
    private List<subcatDiloglist> ItemList;
    private Context mContext;
    private oncatItemClick oNItemClickListener;


    public void ItemCategoryAdapter(Context context, List<subcatDiloglist> ItemList1) {
        this.ItemList = ItemList1;
        this.mContext = context;
        //  this.oNItemClickListener=oNItemClickListener1;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_category_single, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final subcatDiloglist list = ItemList.get(i);

        customViewHolder.text1.setText(list.name);
        String imageurl = list.getImage();
        Log.d("imgurl",imageurl.toString());
        if (imageurl != "") {
            Glide.with(mContext)
                    .load(imageurl)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(customViewHolder.img);
            customViewHolder.img.setVisibility(View.VISIBLE);
        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oNItemClickListener.onItemClick(list);
            }
        };
        //   holder.linearLayoutMain.setOnClickListener(listener);


        // View.OnClickListener listener = v -> oNItemClickListener.onItemClick(list);
      /*  customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/
        customViewHolder.itemView.setOnClickListener(listener);

    }

    public oncatItemClick getOncatItemClickListener() {
        return oNItemClickListener;
    }

    public void setOncatItemClickListener(oncatItemClick onItemClickListener) {
        this.oNItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        ImageView img;
        //  ConstraintLayout linearLayout;


        CustomViewHolder(View view) {
            super(view);
            this.text1 = view.findViewById(R.id.alertTextView1);
            this.img = view.findViewById(R.id.cat_drawable);
            //   this.linearLayout=view.findViewById(R.id.lyt);


        }
    }

}
