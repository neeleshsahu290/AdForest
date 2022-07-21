package com.scriptsbundle.adforest.Search.adapter;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class CatsecondActivity extends AppCompatActivity {
    RecyclerView recycler_view_category;
    Boolean subcategory = false;
    SettingsMain settingsMain;
    RelativeLayout relativeLayout;
    ShimmerFrameLayout shimmerFrameLayout;
    RestService restService;
    String subcatid;
    ArrayList<subcatDiloglist> subcategorylist;
    CategoryAdapter categoryAdapter;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catsecond);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(settingsMain.getMainColor()));
            toolbar.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        subcategorylist= new ArrayList();


        settingsMain = new SettingsMain(this);
        relativeLayout =findViewById(R.id.relativelyt12345);
        textView = findViewById(R.id.texttop);
        setTitle("Category");



        //   if (settingsMain.getAppOpen()) {
        restService = UrlController.createService(RestService.class);
       // searchBtn.setVisibility(View.GONE);

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
        subcatDiloglist item= (subcatDiloglist) getIntent().getSerializableExtra("subcat");
        textView.setText(item.name);
        getsubcategory(item.id);
        textView.setBackground(getDrawable(R.drawable.ic_button_selector));
        textView.setOnClickListener(v -> {

            settingsMain.setcategry(item);
            settingsMain.setcateryfound(true);
            try {
                ActivityCategoryActivity.activityCategoryActivity.finish();

            }catch (Exception e){

            }
            finish();

        });


    }

    void getsubcategory(String id) {


        if (SettingsMain.isConnectingToInternet(this)) {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            //for serlecting the Categoreis if Categoreis have SubCategoreis
            try {


                JsonObject params = new JsonObject();
                params.addProperty("subcat", id);

                Log.d("info sendSearch SubCats", "" + params.toString());

                Call<ResponseBody> myCall = restService.postGetSearcSubCats(params, UrlController.AddHeaders(this));
                myCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                        try {
                            if (responseObj.isSuccessful()) {
                                Log.d("info GetSubCats Resp", "" + responseObj.toString());

                                JSONObject response = new JSONObject(responseObj.body().string());
                                if (response.getBoolean("success")) {
                                    Log.d("info_GetSubCats_object", "" + response.getJSONObject("data"));

                                         /*   adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, SpinnerOptions
                                                    , spinnerAndListAdapter, spinner, eachData.getString("field_type_name"));*/

                                    JSONArray myarray = response.getJSONObject("data").getJSONArray("values");
                                    getArraylist(myarray);


                                } else {
                                    //  Toast.makeText(this, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            //  loadingLayout.setVisibility(View.GONE);



                        } catch (JSONException e) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            // loadingLayout.setVisibility(View.GONE);


                            e.printStackTrace();
                        } catch (IOException e) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            //  loadingLayout.setVisibility(View.GONE);


                            e.printStackTrace();
                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        //  loadingLayout.setVisibility(View.GONE);


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);


                        Log.d("info GetAdnewPost error", String.valueOf(t));
                        Log.d("info GetAdnewPost error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);


            Toast.makeText(this, "Internet error", Toast.LENGTH_SHORT).show();
        }

    }



    private void getArraylist(JSONArray jsonArray) {
//        subcategorylist.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                subcatDiloglist catDiloglist = new subcatDiloglist();
                catDiloglist.setId(jsonArray.getJSONObject(i).getString("id"));
                catDiloglist.setName(jsonArray.getJSONObject(i).getString("name"));
                catDiloglist.setHasSub(jsonArray.getJSONObject(i).getBoolean("has_sub"));
                try {
                    catDiloglist.setImage(jsonArray.getJSONObject(i).getString("img"));
                    Log.d("loggg",jsonArray.getJSONObject(i).getString("img"));

                }catch (Exception e){
                    catDiloglist.setImage("");
                }


                catDiloglist.setHasCustom(jsonArray.getJSONObject(i).getBoolean("has_template"));

                subcategorylist.add(catDiloglist);

            }
            categoryAdapter.ItemCategoryAdapter(this, subcategorylist);
            recycler_view_category.setAdapter(categoryAdapter);
            relativeLayout.setVisibility(View.VISIBLE);
            categoryAdapter.setOncatItemClickListener(new oncatItemClick(){

                @Override
                public void onItemClick(subcatDiloglist item) {
                    if(item.isHasSub()) {

                        Intent intent = new Intent(CatsecondActivity.this, CatsecondActivity.class);
                        intent.putExtra("subcat", item);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                        // finish();
                    }else {

                        settingsMain.setcategry(item);
                        settingsMain.setcateryfound(true);
                        try {
                            ActivityCategoryActivity.activityCategoryActivity.finish();

                        }catch (Exception e){

                        }
                        finish();
                    }

                   /* settingsMain.setcategry(item);
                    settingsMain.setcateryfound(true);
                    finish();*/




                }
            } );
        } catch (JSONException je) {
            Log.d("errorjson1234",je.toString());

        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}