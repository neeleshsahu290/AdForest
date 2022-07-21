package com.scriptsbundle.adforest.Search.adapter;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonObject;
import com.scriptsbundle.adforest.R;
import com.scriptsbundle.adforest.Search.CategoryAdapter;
import com.scriptsbundle.adforest.helper.oncatItemClick;
import com.scriptsbundle.adforest.modelsList.subcatDiloglist;
import com.scriptsbundle.adforest.utills.Network.RestService;
import com.scriptsbundle.adforest.utills.SettingsMain;
import com.scriptsbundle.adforest.utills.UrlController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCategoryActivity extends AppCompatActivity {
    RecyclerView recycler_view_category;
    Boolean subcategory = false;
    SettingsMain settingsMain;
    ShimmerFrameLayout shimmerFrameLayout;
    RestService restService;
    String subcatid;
    ArrayList<subcatDiloglist> subcategorylist;
    CategoryAdapter categoryAdapter;
    public static Activity activityCategoryActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle("Category");
        activityCategoryActivity=this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(settingsMain.getMainColor()));
            toolbar.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        }




        settingsMain = new SettingsMain(this);


        //   if (settingsMain.getAppOpen()) {
        restService = UrlController.createService(RestService.class);

        /*} else{
            restService = UrlController.createService(RestService.class, settingsMain.getUserEmail(), settingsMain.getUserPassword(), getActivity());}*/
        recycler_view_category = findViewById(R.id.recycler_view_category);

        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(this);
        recycler_view_category.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                ((LinearLayoutManager) layoutManager).getOrientation());
        recycler_view_category.addItemDecoration(dividerItemDecoration);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
       // Bundle bundle = this.getArguments();


        categoryAdapter = new CategoryAdapter();



            //  subcategory = bundle.getBoolean("subcategory");

            subcategorylist = (ArrayList<subcatDiloglist>) getIntent().getSerializableExtra("category");

            categoryAdapter.ItemCategoryAdapter(this, subcategorylist);
            recycler_view_category.setAdapter(categoryAdapter);
            recycler_view_category.setVisibility(View.VISIBLE);
        categoryAdapter.setOncatItemClickListener(new oncatItemClick(){

            @Override
            public void onItemClick(subcatDiloglist item) {
                if(item.isHasSub()) {

                    Intent intent = new Intent(ActivityCategoryActivity.this, CatsecondActivity.class);
                    intent.putExtra("subcat", item);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                    //    finish();
                }else {
                    settingsMain.setcategry(item);
                    settingsMain.setcateryfound(true);
                    finish();
                }

               /* if (secondcat==false){
                    if (!item.id.equals("")) {
                        //  mainRelative.setVisibility(View.VISIBLE);
                        secondcat =true;
                        recycler_view_category.setVisibility(View.GONE);

                        getsubcategory(item.id);


                    }}
                else {
                    secondcat=false;
                    et5.setText(item.name);
                    iscategory(false);
                    catchange(item);

                }*/



            }
        } );







    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}