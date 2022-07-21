package com.scriptsbundle.adforest.messages.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scriptsbundle.adforest.utills.SettingsMain;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.scriptsbundle.adforest.R;
import com.scriptsbundle.adforest.helper.SendReciveONClickListner;
import com.scriptsbundle.adforest.modelsList.messageSentRecivModel;

public class ItemSendRecMesageAdapter extends RecyclerView.Adapter<ItemSendRecMesageAdapter.CustomViewHolder> {
    private List<messageSentRecivModel> feedItemList;
    private Context mContext;
    private SendReciveONClickListner oNItemClickListener;
    private SettingsMain settingsmain;


    public ItemSendRecMesageAdapter(Context context, List<messageSentRecivModel> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sent_message, null);
        return new CustomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final messageSentRecivModel feedItem = feedItemList.get(i);

       customViewHolder.name.setText(feedItemList.get(i).getName());
        settingsmain = new SettingsMain(mContext);


        if(settingsmain.getRTL()){
            customViewHolder.name.setGravity(Gravity.RIGHT);
        }else {
            customViewHolder.name.setGravity(Gravity.LEFT);
        }
       customViewHolder.addname.setText(feedItemList.get(i).getposttitle());
       String num= feedItemList.get(i).getmsgunread();
       if (!num.equals("")){
      customViewHolder.notiftn_txt.setText(num);
      customViewHolder.notiftn_txt.setVisibility(View.VISIBLE);
       }
       boolean isblock = feedItemList.get(i).getisblockcondition();
       if (isblock){
           customViewHolder.is_block.setVisibility(View.VISIBLE);
       }else {
           customViewHolder.is_block.setVisibility(View.GONE);

       }

        Glide.with(mContext)
                .load(feedItemList.get(i).getTumbnail())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(customViewHolder.imageView);
  //      customViewHolder.topic.setText(feedItemList.get(i).getTopic());
    //    String blockstatus= feedItemList.get(i).getIsBlock();
        /*if(blockstatus !=null && blockstatus.equals("true")){
            customViewHolder.is_block.setText("Block");
        }*/
      /*  if (!feedItem.isMessageRead()) {
            customViewHolder.card_view.setCardBackgroundColor(Color.parseColor("#fffcf5"));
            customViewHolder.notification_icon.setVisibility(View.VISIBLE);
        }*/

       /* if (!TextUtils.isEmpty(feedItem.getTumbnail())) {
              Picasso.get().load(feedItem.getTumbnail())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(customViewHolder.imageView);
        }*/

        View.OnClickListener listener = v -> oNItemClickListener.onItemClick(feedItem);

        customViewHolder.linearLayout.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public SendReciveONClickListner getOnItemClickListener() {
        return oNItemClickListener;
    }

    public void setOnItemClickListener(SendReciveONClickListner onItemClickListener) {
        this.oNItemClickListener = onItemClickListener;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, topic,is_block, addname,notiftn_txt;
        LinearLayout linearLayout;
        CardView card_view;
        ImageView notification_icon;


        CustomViewHolder(View view) {
            super(view);
            this.addname=view.findViewById(R.id.txtaddname);
            this.notiftn_txt= view.findViewById(R.id.notiftn_txt);
           this.is_block=view.findViewById(R.id.txtBLock);
            this.linearLayout = view.findViewById(R.id.linear_layout_card_view);
            this.imageView = view.findViewById(R.id.image_view);
            this.name = view.findViewById(R.id.text_username);
           // this.notification_icon = view.findViewById(R.id.notification_icon);
           // this.topic = view.findViewById(R.id.loginTime);
         //   this.card_view = view.findViewById(R.id.card_view);
        }
    }
}