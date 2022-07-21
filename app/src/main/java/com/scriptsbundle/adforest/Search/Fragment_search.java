package com.scriptsbundle.adforest.Search;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.scriptsbundle.adforest.Search.SearchActivity.searchBtn;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.scriptsbundle.adforest.R;
import com.scriptsbundle.adforest.Search.adapter.ActivityCategoryActivity;
import com.scriptsbundle.adforest.Search.adapter.ItemCatgorySubListAdapter;
import com.scriptsbundle.adforest.SplashScreen;
import com.scriptsbundle.adforest.ad_detail.Ad_detail_activity;
import com.scriptsbundle.adforest.adapters.ItemSearchFeatureAdsAdapter;
import com.scriptsbundle.adforest.adapters.SpinnerAndListAdapter;
import com.scriptsbundle.adforest.helper.CatSubCatOnclicklinstener;
import com.scriptsbundle.adforest.helper.oncatItemClick;
import com.scriptsbundle.adforest.home.helper.Location_popupModel;
import com.scriptsbundle.adforest.modelsList.catSubCatlistModel;
import com.scriptsbundle.adforest.modelsList.subcatDiloglist;
import com.scriptsbundle.adforest.multispinnerfilter.KeyPairBoolData;
import com.scriptsbundle.adforest.multispinnerfilter.MultiSpinnerListener;
import com.scriptsbundle.adforest.multispinnerfilter.MultiSpinnerSearch;
import com.scriptsbundle.adforest.utills.GPSTracker;
import com.scriptsbundle.adforest.utills.NestedScroll;
import com.scriptsbundle.adforest.utills.Network.RestService;
import com.scriptsbundle.adforest.utills.SettingsMain;
import com.scriptsbundle.adforest.utills.UrlController;
import com.xw.repo.BubbleSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_search extends Fragment {
    TextView textpricelabel1, textpricelabel2;
    EditText textprice1, textprice2;
    Boolean onclick = false;
    CardView categorycard;
    TextView et5;
    CategoryAdapter categoryAdapter;
    Boolean secondcat = false;
    Boolean isCategorysearch = false;
    String search_id = "";

    ArrayList<catSubCatlistModel> searchedAdList = new ArrayList<>();
    ArrayList<catSubCatlistModel> featureAdsList = new ArrayList<>();
    RecyclerView MyRecyclerView, recyclerViewFeatured, recycler_view_category;
    // Button searchBtn;
    //EditText editTextSearch;
    SettingsMain settingsMain;
    TextView textViewTitleFeature, textViewFilterText;
    ItemCatgorySubListAdapter itemCatgorySubListAdapter;
    ItemSearchFeatureAdsAdapter itemSearchFeatureAdsAdapter;
    LinearLayout viewProductLayout, linearhide;
    List<View> allViewInstance = new ArrayList<>();
    List<View> allViewInstanceforCustom = new ArrayList<>();
    JSONObject jsonObject, jsonObjectFilterSpinner, jsonObjectforCustom, jsonObjectPagination;
    ImageView imageViewCollapse;
    LinearLayout linearLayoutCollapse, linearLayoutCustom;
    JsonArray citiesarray,townarray;
    RestService restService;
    Spinner spinnerFilter;
    //    Button spinnerFilterText;
    LinearLayout linearLayoutFilter;
    String catID;
    boolean isSort = false, ison = false;
    String sortType = "";
    RelativeLayout relativeLayoutSpiner, relativelyt123;
    NestedScrollView scrollView;
    // AutoCompleteTextView et;
    CardView cvImage;
    LinearLayout ll_image;
    //    CardView cvCity, cvTown;
    SwitchCompat scImageOption, scPriceOption;
    TextView tvShowWithImages, tvShowWithPrice; // tvCityText, tvTownText,
    //  MultiSpinnerSearch spinnerCity, spinnerTown;
    EditText mapBoxPlaces;
    ProgressBar progressBar;
    boolean isLoading = false, hasNextPage = false;
    int currentPage = 1, nextPage = 1, totalPage = 0;
    GridLayoutManager MyLayoutManager2;
    String stringCAT_keyName, ad_title, requestFrom, ad_id, locationIdHomePOpup;

    String longtitude = "", latitude = "";
    BubbleSeekBar bubbleSeekBarDistance;
    CrystalRangeSeekbar rangeSeekbar;
    ArrayList<String> places = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    private PlacesClient placesClient;
    private Boolean spinnerTouched = false;
    private Boolean spinnerTouched2 = false, checkRequest = false;
    private Calendar myCalendar = Calendar.getInstance();
    // static Boolean onLoad = false;
    double lat_by_mapbox, lon_by_mapbox;
    String address_by_mapbox, addressbyGeoCode;
    Boolean mapBox = true;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout loadingLayout;
    RelativeLayout mainRelative;

    //    ArrayList<String> cityArrayListSelected = new ArrayList<>();
    //  ArrayList<String> townArrayListSelected = new ArrayList<>();
    //   ArrayList<String> modelArrayList = new ArrayList<>();
    JSONArray customOptnList;
    JSONArray modelArray;
    JSONArray townArray;
    //private Object CategoryAdapter;

    public Fragment_search() {
        // Required empty public constructor
    }

    void callLayouts(View view) {
        //setOnItemClickListener CategoryAdapter.setOncatItemClickListener(this::onItemClick);
       /* textpricelabel1 = view.findViewById(R.id.textpricelabel1);
        textpricelabel2= view.findViewById(R.id.textpricelabel2);
        textprice1= view.findViewById(R.id.txtprice1);
        textprice2=view.findViewById(R.id.textprice2);
       // recycler_view_category=view.findViewById(R.id.recycler_view_category);
        categorycard=view.findViewById(R.id.categorycard);
*/
        //   recycler_view_category=view.findViewById(R.id.recycler_view_category);

        recycler_view_category = view.findViewById(R.id.recycler_view_category);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_view_category.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        recycler_view_category.addItemDecoration(dividerItemDecoration);
        relativelyt123 = view.findViewById(R.id.relativelyt123);
        categoryAdapter = new CategoryAdapter();
    }

    void setListners() {

    }

    void iscategory(Boolean categoryvisible) {
        if (categoryvisible) {

            recycler_view_category.setVisibility(View.VISIBLE);
            linearLayoutCollapse.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.GONE);
            //  mainRelative.setVisibility(View.GONE);
            relativelyt123.setVisibility(View.GONE);
            searchBtn.setVisibility(View.GONE);
        } else {
            recycler_view_category.setVisibility(View.GONE);
            //   mainRelative.setVisibility(View.VISIBLE);
            relativelyt123.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.VISIBLE);
            linearLayoutCollapse.setVisibility(View.VISIBLE);
            searchBtn.setVisibility(View.VISIBLE);
        }
    }

  /*  void isCategoryselected(Boolean Selectcategory){
        if (Selectcategory){
            linearLayoutCollapse.setVisibility(View.GONE);
            recycler_view_category.setVisibility(View.VISIBLE);

        }  else {
            linearLayoutCollapse.setVisibility(View.VISIBLE);
            recycler_view_category.setVisibility(View.GONE);
        }

    }*/

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        callLayouts(view);
        //   setTargetFragment(this)

        scrollView = getActivity().findViewById(R.id.scrollView);
        progressBar = view.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
        loadingLayout = view.findViewById(R.id.shimmerMain);
        mainRelative = view.findViewById(R.id.mainRelative);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ad_title = bundle.getString("ad_title", "");
            ad_id = bundle.getString("catId", "");
            requestFrom = bundle.getString("requestFrom", "");
            locationIdHomePOpup = bundle.getString("ad_country", "");
        }
        settingsMain = new SettingsMain(getActivity());

//        cvCity = view.findViewById(R.id.cvCity);
//        cvCity.setVisibility(View.GONE);
//        tvCityText = view.findViewById(R.id.tvCityText);
//        spinnerCity = view.findViewById(R.id.spinnerCity);
//
//        tvTownText = view.findViewById(R.id.tvTownText);
//        spinnerTown = view.findViewById(R.id.spinnerTown);
//        cvTown = view.findViewById(R.id.cvTown);
//        cvTown.setVisibility(View.GONE);

        cvImage = view.findViewById(R.id.cvImage);
        ll_image = view.findViewById(R.id.ll_image);
        tvShowWithImages = view.findViewById(R.id.tvShowWithImages);
        scImageOption = view.findViewById(R.id.scImageOption);
        tvShowWithPrice = view.findViewById(R.id.tvShowWithPrice);
        scPriceOption = view.findViewById(R.id.scPriceOption);
        //searchBtn = view.findViewById(R.id.send_button);
        searchBtn.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        searchBtn.setVisibility(View.GONE);

        imageViewCollapse = getActivity().findViewById(R.id.collapse);

        // linearhide = view.findViewById(R.id.linearhide);
        //  onLoad = true;
        linearLayoutCollapse = view.findViewById(R.id.linearLayout);
        linearLayoutFilter = view.findViewById(R.id.filter_layout);
        textViewFilterText = view.findViewById(R.id.textViewFilter);
        spinnerFilter = view.findViewById(R.id.spinner);
//        spinnerFilterText = view.findViewById(R.id.spinner);
//        spinnerFilterText.setBackgroundColor(Color.parseColor(SettingsMain.getMainColor()));
        relativeLayoutSpiner = view.findViewById(R.id.rel1);

        viewProductLayout = view.findViewById(R.id.customOptionLL);

        textViewTitleFeature = view.findViewById(R.id.textView6);

        MyRecyclerView = view.findViewById(R.id.recycler_view);
        MyRecyclerView.setHasFixedSize(true);
        MyRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        recyclerViewFeatured = view.findViewById(R.id.recycler_view2);
        recyclerViewFeatured.setHasFixedSize(true);
        MyLayoutManager2 = new GridLayoutManager(getActivity(), 1);
        MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerViewFeatured.setLayoutManager(MyLayoutManager2);

        scrollView.setOnScrollChangeListener(new NestedScroll() {
            @Override
            public void onScroll() {
                if (hasNextPage) {
                    if (!isLoading) {
                        isLoading = true;
                        progressBar.setVisibility(View.VISIBLE);
                        adforest_loadmore(adforest_getDataFromDynamicViews());
                        Log.d("heeeeeeelo", nextPage + "==" + totalPage);
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        relativeLayoutSpiner.setVisibility(View.GONE);


        jsonObject = new JSONObject();
        if (settingsMain.getAppOpen()) {
            restService = UrlController.createService(RestService.class);
        } else
            restService = UrlController.createService(RestService.class, settingsMain.getUserEmail(), settingsMain.getUserPassword(), getActivity());


        Log.d("requestform", requestFrom.toString());
        if (requestFrom.trim().equals("Home")) {
            spinnerFilter.setVisibility(View.GONE);
//            spinnerFilterText.setVisibility(View.GONE);
            imageViewCollapse.setVisibility(View.VISIBLE);
            searchBtn.setId(2000);
            Log.d("searchbtn", String.valueOf(searchBtn.getId()));


            /*if (searchBtn.getId() == 2000) {
                searchBtn.setVisibility(View.GONE);

            } else {
                searchBtn.setVisibility(View.VISIBLE);
            }*/
            linearLayoutCollapse.setVisibility(View.GONE);
            //  AnimationUtils.slideDown(linearLayoutCollapse);
            imageViewCollapse.setImageResource(R.drawable.ic_controls);
            JsonObject params = new JsonObject();
            params.addProperty("ad_title", ad_title);
            params.addProperty("catId", ad_id);
            params.addProperty("page_number", 1);
            params.addProperty("ad_country", locationIdHomePOpup);
            adforest_search(params);
            //adforest_adSearchLoc();
        } else {
            // loadingLayout.setVisibility(View.VISIBLE);

            linearLayoutCollapse.setVisibility(View.VISIBLE);
            //   searchBtn.setVisibility(View.VISIBLE);


            adforest_getViews();
            //  loadingLayout.setVisibility(View.GONE);

            /*shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            searchBtn.setVisibility(View.VISIBLE);*/
        }
        // adforest_adSearchLoc();

        setListners();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                onclick = true;
                linearLayoutCollapse.setVisibility(View.GONE);
                searchBtn.setVisibility(View.GONE);
                adforest_submitQuery(adforest_getDataFromDynamicViews(), "");
                //onclick=false;
            }
        });
        imageViewCollapse.setOnClickListener(view12 -> {
            Log.d("onclick", String.valueOf(onclick));

            if (requestFrom.equals("Home") && !checkRequest) {
                adforest_getViews();
                checkRequest = true;
                requestFrom = "";
                spinnerFilter.setVisibility(View.VISIBLE);
//                spinnerFilterText.setVisibility(View.VISIBLE);

            }
            scrollView.scrollTo(0, 0);
            imageViewCollapse.setVisibility(View.VISIBLE);

            if (!onclick) {
                //  searchBtn.setVisibility(View.VISIBLE);

                imageViewCollapse.setVisibility(View.VISIBLE);

                if (linearLayoutCollapse.getVisibility() == View.GONE) {
                    linearLayoutCollapse.setVisibility(View.VISIBLE);
                    //AnimationUtils.slideUp(linearLayoutCollapse);
                    imageViewCollapse.setImageResource(R.drawable.ic_remove_circle_outline);
                    relativelyt123.setVisibility(View.GONE);
                    MyRecyclerView.setVisibility(View.GONE);
                    linearLayoutFilter.setVisibility(View.GONE);
                    searchBtn.setVisibility(View.VISIBLE);


                } else {
                    linearLayoutCollapse.setVisibility(View.GONE);
                    //  AnimationUtils.slideDown(linearLayoutCollapse);
                    imageViewCollapse.setImageResource(R.drawable.ic_controls);
                    relativelyt123.setVisibility(View.VISIBLE);
                    MyRecyclerView.setVisibility(View.VISIBLE);
                    linearLayoutFilter.setVisibility(View.VISIBLE);

                    searchBtn.setVisibility(View.GONE);
                }
            } else {

                linearLayoutCollapse.setVisibility(View.GONE);
                //  AnimationUtils.slideDown(linearLayoutCollapse);
                imageViewCollapse.setImageResource(R.drawable.ic_controls);
                relativelyt123.setVisibility(View.VISIBLE);
                MyRecyclerView.setVisibility(View.VISIBLE);
                linearLayoutFilter.setVisibility(View.VISIBLE);

                searchBtn.setVisibility(View.GONE);


            }
            onclick = false;

        });

        //  adforest_GetCity();

        return view;
    }

    private void adforest_submitQuery(JsonObject params, String s) {

        if (isSort) {
            params.addProperty("sort", s);
            sortType = s;
            isSort = true;
        }


        if (adforest_getDataFromDynamicViewsForCustom() != null) {
            params.add("custom_fields", adforest_getDataFromDynamicViewsForCustom());
            if (citiesarray!=null){
                params.add("city_location",citiesarray);
            }
            if (townarray!=null){
                params.add("town_location",townarray);
            }
        }


//        if (cityArrayListSelected != null) {
//            JsonArray cityJsonArray = new JsonArray();
//            for (String user : cityArrayListSelected) {
//                cityJsonArray.add(new JsonPrimitive(user));
//            }
//            params.add("city_location",
//                    cityJsonArray);
//        }
//
//        if (townArrayListSelected != null) {
//            JsonArray townJsonArray = new JsonArray();
//            for (String user : townArrayListSelected) {
//                townJsonArray.add(new JsonPrimitive(user));
//            }
//            params.add("town_location",
//                    townJsonArray);
//        }

        if (settingsMain.getShowNearBy()) {
            params.addProperty("nearby_latitude", latitude);
            params.addProperty("nearby_longitude", longtitude);
            params.addProperty("ad_location", "");
            if (bubbleSeekBarDistance != null)
                params.addProperty("nearby_distance", Integer.toString(bubbleSeekBarDistance.getProgress()));
        }

//        if (et.getText().toString().equals("")) {
//            params.addProperty("nearby_latitude", "");
//            params.addProperty("nearby_longitude", "");
//        }
        //onclick=true;
        params.addProperty("page_number", 1);
       /* if (linearLayoutCollapse.getVisibility()==View.VISIBLE){
            linearLayoutCollapse.setVisibility(View.GONE);}*/
        onclick = true;
        adforest_search(params);
        Log.d("full_para", params.toString());
    }

    private void adforest_search(JsonObject params) {
        if (SettingsMain.isConnectingToInternet(getActivity())) {

            Log.d("info Send SearchData =", "" + params.toString());
            loadingLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            Call<ResponseBody> myCall = restService.postGetSearchNdLoadMore(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info_SearchData_Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                String veryLongString =  response.getJSONObject("data").toString();
                                int maxLogSize = 1000;
                                for (int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
                                    int start = i * maxLogSize;
                                    int end = (i + 1) * maxLogSize;
                                    end = end > veryLongString.length() ? veryLongString.length() : end;
                                    Log.d("info_SearchData_object", veryLongString.substring(start, end));
                                }
                             //   Log.d("info_SearchData_object", "" + response.getJSONObject("data"));

                                Log.d("info SearchData extra", "" + response.getJSONObject("extra"));
                                if (requestFrom.trim().equals("Home")) {
                                    getActivity().setTitle(response.getJSONObject("extra").getString("title"));
                                }
                                //Fet5 onLoad = false;
                                if (response.getJSONObject("extra").getBoolean("is_show_featured")) {
                                    textViewTitleFeature.setVisibility(View.VISIBLE);
                                } else {
                                    textViewTitleFeature.setVisibility(View.GONE);
                                }
                                jsonObjectPagination = response.getJSONObject("pagination");

                                nextPage = jsonObjectPagination.getInt("next_page");
                                currentPage = jsonObjectPagination.getInt("current_page");
                                totalPage = jsonObjectPagination.getInt("max_num_pages");

                                hasNextPage = jsonObjectPagination.getBoolean("has_next_page");

                                adforest_loadList(response.getJSONObject("data").getJSONObject("featured_ads"),
                                        response.getJSONObject("data").getJSONArray("ads"),
                                        response.getJSONObject("topbar"));

                                itemCatgorySubListAdapter = new ItemCatgorySubListAdapter(getActivity(), searchedAdList);
                                itemSearchFeatureAdsAdapter = new ItemSearchFeatureAdsAdapter(getActivity(), featureAdsList);
                                itemSearchFeatureAdsAdapter.setHorizontelAd("default");
                                recyclerViewFeatured.setAdapter(itemSearchFeatureAdsAdapter);

                                if (settingsMain.isFeaturedScrollEnable()) {
                                    adforest_recylerview_autoScroll(settingsMain.getFeaturedScroolDuration(),
                                            40, settingsMain.getFeaturedScroolLoop(),
                                            recyclerViewFeatured, MyLayoutManager2, itemSearchFeatureAdsAdapter);
                                }

                                MyRecyclerView.setAdapter(itemCatgorySubListAdapter);

                                itemSearchFeatureAdsAdapter.setOnItemClickListener(new CatSubCatOnclicklinstener() {
                                    @Override
                                    public void onItemClick(catSubCatlistModel item) {
                                        Intent intent = new Intent(getActivity(), Ad_detail_activity.class);
                                        intent.putExtra("adId", item.getId());
                                        getActivity().startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                    }

                                    @Override
                                    public void onItemTouch(catSubCatlistModel item) {

                                    }

                                    @Override
                                    public void addToFavClick(View v, String position) {
                                        adforest_addToFavourite(position);
                                    }
                                });


                                itemCatgorySubListAdapter.setOnItemClickListener(new CatSubCatOnclicklinstener() {
                                    @Override
                                    public void onItemClick(catSubCatlistModel item) {

                                        Intent intent = new Intent(getActivity(), Ad_detail_activity.class);
                                        intent.putExtra("adId", item.getId());
                                        getActivity().startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                    }

                                    @Override
                                    public void onItemTouch(catSubCatlistModel item) {

                                    }

                                    @Override
                                    public void addToFavClick(View v, String position) {

                                    }
                                });

                                adforest_showFiler();

                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.no_result_found), Toast.LENGTH_SHORT).show();
                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        // searchBtn.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        searchBtn.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    } catch (IOException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        searchBtn.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);
                    // searchBtn.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        searchBtn.setVisibility(View.VISIBLE);
                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        searchBtn.setVisibility(View.VISIBLE);

                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info SearchData ", "NullPointert Exception" + t.getLocalizedMessage());
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        searchBtn.setVisibility(View.VISIBLE);

                    } else {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        searchBtn.setVisibility(View.VISIBLE);
                        Log.d("info SearchData err", String.valueOf(t));
                        Log.d("info SearchData err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.GONE);
            searchBtn.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
        }
    }


    private void adforest_carmodel(JsonObject params) {
        if (SettingsMain.isConnectingToInternet(getActivity())) {

            Log.d("info cardata =", "" + params.toString());
            Call<ResponseBody> myCall = restService.postGetCarModel(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info SearchData Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Log.d("info SearchData object", "" + response.getJSONArray("data"));
                                JSONArray jsonArray = response.optJSONArray("data");


                                List<KeyPairBoolData> listArray1 = new ArrayList<>();
                                if (jsonArray != null) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                                        keyPairBoolData.setId(i + 1);
                                        keyPairBoolData.setName(jsonArray.getString(i));
                                        listArray1.add(keyPairBoolData);
                                    }
                                }

                                TextView customOptionsName = new TextView(getActivity());
                                customOptionsName.setTextSize(12);
                                customOptionsName.setAllCaps(true);
                                customOptionsName.setTextColor(Color.BLACK);
                                customOptionsName.setPadding(10, 15, 10, 15);
                                customOptionsName.setText("Models");

                                LinearLayout linearLayout = new LinearLayout(getActivity());
                                linearLayout.setOrientation(LinearLayout.VERTICAL);

                                linearLayout.addView(customOptionsName);


                                final MultiSpinnerSearch spinner = new MultiSpinnerSearch(getActivity());
                                spinner.setClearText(jsonObject.optJSONObject("extra").optString("clear_all"));
                                spinner.setSearchHint(jsonObject.optJSONObject("extra").optString("type_to_search"));
                                spinner.setSearchEnabled(true);

                                spinner.setShowSelectAllButton(false);

                                spinner.setItems(listArray1, new MultiSpinnerListener() {

                                    @Override
                                    public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                                        //Log.e("selected Items", selectedItems.get(0).getName().toString());
//                                        modelArrayList = new ArrayList<>();
//                                        for (int k = 0; k < selectedItems.size(); k++) {
//                                            modelArrayList.add(selectedItems.get(k).getName());
//                                        }
                                    }
                                });

                                spinner.setSelection(0, false);

                                CardView cardView = new CardView(getActivity());
                                cardView.setCardElevation(2);
                                cardView.setUseCompatPadding(true);
                                cardView.setRadius(0);
                                cardView.setContentPadding(10, 10, 10, 10);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.topMargin = 10;
                                params.bottomMargin = 10;
                                cardView.setLayoutParams(params);
                                linearLayout.addView(spinner);
                                cardView.addView(linearLayout);

                                viewProductLayout.addView(cardView);
                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info SearchData ", "NullPointert Exception" + t.getLocalizedMessage());
                    } else {
                        Log.d("info SearchData err", String.valueOf(t));
                        Log.d("info SearchData err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
        }
    }

    private void adforest_loadmore(JsonObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JsonObject();
        }
        if (isSort) {
            if (sortType != null)
                jsonObject.addProperty("sort", sortType);
        }

        if (adforest_getDataFromDynamicViewsForCustom() != null) {
            jsonObject.add("custom_fields", adforest_getDataFromDynamicViewsForCustom());
            if (citiesarray!=null){
                jsonObject.add("city_location",citiesarray);
            }
            if (townarray!=null){
                jsonObject.add("town_location",townarray);
            }
        }
        jsonObject.addProperty("page_number", nextPage);

        if (SettingsMain.isConnectingToInternet(getActivity())) {

            Log.d("info data object", "" + jsonObject.toString());
            Call<ResponseBody> myCall = restService.postGetSearchNdLoadMore(jsonObject, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info_searchLoad_Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Log.d("info searchLoadMore obj", "" + response.getJSONObject("data"));

                                isLoading = false;
                                jsonObjectPagination = response.getJSONObject("pagination");

                                nextPage = jsonObjectPagination.getInt("next_page");
                                currentPage = jsonObjectPagination.getInt("current_page");
                                totalPage = jsonObjectPagination.getInt("max_num_pages");
                                hasNextPage = jsonObjectPagination.getBoolean("has_next_page");

                                JSONArray searchAds = response.getJSONObject("data").getJSONArray("ads");

                                try {
                                    if (searchAds.length() > 0) {
                                        for (int i = 0; i < searchAds.length(); i++) {
                                            catSubCatlistModel item = new catSubCatlistModel();
                                            JSONObject object = searchAds.getJSONObject(i);
                                            item.setId(object.getString("ad_id"));
                                            item.setCardName(object.getString("ad_title"));
                                            item.setDate(object.getString("ad_date"));
                                            item.setAuthor_type(object.getString("author_type"));
                                            item.setAdViews(object.getString("ad_views"));
                                            item.setPath(object.getString("ad_cats_name"));
                                            item.setPrice(object.getJSONObject("ad_price").getString("price"));
                                            item.setImageResourceId((object.getJSONArray("images").getJSONObject(0).getString("thumb")));
                                            item.setLocation(object.getJSONObject("location").getString("address"));
                                            item.setIsturned(0);
                                            item.setIs_show_countDown(object.getJSONObject("ad_timer").getBoolean("is_show"));
                                            if (object.getJSONObject("ad_timer").getBoolean("is_show"))
                                                item.setTimer_array(object.getJSONObject("ad_timer").getJSONArray("timer"));
                                            searchedAdList.add(item);
                                        }
                                        MyRecyclerView.setAdapter(itemCatgorySubListAdapter);
                                        itemCatgorySubListAdapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        e.printStackTrace();
                    } catch (IOException e) {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);

                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info searchLoadMore ", "NullPointert Exception" + t.getLocalizedMessage());
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);


                    } else {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        Log.d("info searchLoadMore err", String.valueOf(t));
                        Log.d("info searchLoadMore err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    private void adforest_getViews() {
        //   onclick=true;


        if (SettingsMain.isConnectingToInternet(getActivity())) {
            loadingLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            Call<ResponseBody> myCall = restService.getSearchDetails(UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info_Search_Details", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                //     onLoad = false;
                                Log.d("info_search_response", response.toString());
                                adforest_setViews(response);

                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                       /* shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                       searchBtn.setVisibility(View.VISIBLE);*/


                    } catch (JSONException e) {
                        e.printStackTrace();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        searchBtn.setVisibility(View.VISIBLE);


                    } catch (IOException e) {
                        e.printStackTrace();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        searchBtn.setVisibility(View.VISIBLE);


                    }
                   /* shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);
                    searchBtn.setVisibility(View.VISIBLE);*/


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    loadingLayout.setVisibility(View.GONE);
                    searchBtn.setVisibility(View.VISIBLE);


                    Log.d("info_ProfileGet_error", String.valueOf(t));
                    Log.d("info_ProfileGet_error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                }
            });
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.GONE);
            searchBtn.setVisibility(View.VISIBLE);

            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    void adforest_loadList(JSONObject featureObject, JSONArray searchAds, JSONObject filtertext) {
        searchedAdList.clear();
        featureAdsList.clear();
        try {
            Log.d("search jsonaarry is = ", searchAds.toString());
            if (searchAds.length() > 0) {
                for (int i = 0; i < searchAds.length(); i++) {

                    catSubCatlistModel item = new catSubCatlistModel();

                    JSONObject object = searchAds.getJSONObject(i);

                    item.setId(object.getString("ad_id"));
                    item.setCardName(object.getString("ad_title"));
                    item.setDate(object.getString("ad_date"));
                    item.setAuthor_type(object.getString("author_type"));
                    item.setAdViews(object.getString("ad_views"));
                    item.setPath(object.getString("ad_cats_name"));
                    item.setPrice(object.getJSONObject("ad_price").getString("price"));
                    item.setImageResourceId((object.getJSONArray("images").getJSONObject(0).getString("thumb")));
                    item.setLocation(object.getJSONObject("location").getString("address"));
                    item.setIsturned(0);
                    item.setIs_show_countDown(object.getJSONObject("ad_timer").getBoolean("is_show"));
                    if (object.getJSONObject("ad_timer").getBoolean("is_show"))
                        item.setTimer_array(object.getJSONObject("ad_timer").getJSONArray("timer"));
                    searchedAdList.add(item);
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.no_result_found), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Log.d("feature Object is = ", featureObject.getJSONArray("ads").toString());
            textViewTitleFeature.setText(featureObject.getString("text"));
            if (featureObject.getJSONArray("ads").length() > 0)
                for (int i = 0; i < featureObject.getJSONArray("ads").length(); i++) {

                    catSubCatlistModel item = new catSubCatlistModel();
                    JSONObject object = featureObject.getJSONArray("ads").getJSONObject(i);

                    item.setAddTypeFeature(object.getJSONObject("ad_status").getString("featured_type_text"));
                    item.setId(object.getString("ad_id"));
                    item.setCardName(object.getString("ad_title"));
                    item.setDate(object.getString("ad_date"));
                    item.setAdViews(object.getString("ad_views"));
                    item.setPath(object.getString("ad_cats_name"));
                    item.setPrice(object.getJSONObject("ad_price").getString("price"));
                    item.setImageResourceId((object.getJSONArray("ad_images").getJSONObject(0).getString("thumb")));
                    item.setLocation(object.getJSONObject("ad_location").getString("address"));
                    item.setIsfav(1);
                    item.setFavBtnText(object.getJSONObject("ad_saved").getString("text"));
                    item.setIsturned(1);

                    item.setIs_show_countDown(object.getJSONObject("ad_timer").getBoolean("is_show"));
                    if (object.getJSONObject("ad_timer").getBoolean("is_show"))
                        item.setTimer_array(object.getJSONObject("ad_timer").getJSONArray("timer"));

                    featureAdsList.add(item);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            textViewFilterText.setText(filtertext.getString("count_ads"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!requestFrom.equals("Home"))
            imageViewCollapse.performClick();
        if (requestFrom.equals("Home") && checkRequest)
            imageViewCollapse.performClick();
    }

    @SuppressLint("ClickableViewAccessibility")
    void adforest_setViews(JSONObject jsonObjec) {

        try {
            jsonObject = jsonObjec;
            //    Log.d("info_Search_Data ===== ", jsonObject.toString());
            String veryLongString = jsonObject.toString();
            int maxLogSize = 1000;
            for (int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > veryLongString.length() ? veryLongString.length() : end;
                Log.d("info_Search_Data", veryLongString.substring(start, end));
            }
            JSONArray customOptnList = jsonObject.getJSONArray("data");

            getActivity().setTitle(jsonObject.getJSONObject("extra").getString("title"));
            searchBtn.setText(jsonObject.getJSONObject("extra").getString("search_btn"));
            // tvTownText.setText(jsonObject.getJSONObject("extra").getString("town_text"));
            //tvCityText.setText(jsonObject.getJSONObject("extra").getString("city_text"));
            tvShowWithPrice.setText(jsonObject.getJSONObject("extra").getString("show_with_price_text"));
            tvShowWithImages.setText(jsonObject.getJSONObject("extra").getString("show_with_images_text"));
            stringCAT_keyName = jsonObject.getJSONObject("extra").getString("field_type_name");


            for (int noOfCustomOpt = 0; noOfCustomOpt < customOptnList.length(); noOfCustomOpt++) {

                CardView cardView = new CardView(getActivity());
                cardView.setCardElevation(2);
                cardView.setUseCompatPadding(true);
                cardView.setRadius(0);
                //  cardView.setContentPadding(10, 10, 10, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = 10;
                params.bottomMargin = 10;
                cardView.setLayoutParams(params);


                CardView cardViewList = new CardView(getActivity());
                cardViewList.setCardElevation(2);
                cardViewList.setUseCompatPadding(true);
                cardViewList.setRadius(0);
                cardViewList.setContentPadding(10, 10, 10, 10);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params1.topMargin = 10;
                params1.bottomMargin = 10;
                cardViewList.setLayoutParams(params1);

                LinearLayout linearLayoutnew = new LinearLayout(getActivity());
                linearLayoutnew.setOrientation(LinearLayout.VERTICAL);
                linearLayoutnew.setBackgroundColor(getResources().getColor(R.color.white));


                final JSONObject eachData = customOptnList.getJSONObject(noOfCustomOpt);
                TextView customOptionsName = new TextView(getActivity());
                customOptionsName.setAllCaps(true);
                customOptionsName.setTextColor(Color.BLACK);
                customOptionsName.setTextSize(12);
                customOptionsName.setPadding(10, 15, 10, 15);

                customOptionsName.setText(eachData.getString("title"));


                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setPadding(5, 5, 5, 5);
                linearLayout.setBackgroundColor(getResources().getColor(R.color.card_detailing));
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                //  linearLayout.addView(customOptionsName);
                //    linearLayoutnew.addView(customOptionsName);

                View view = new View(getActivity());
                view.setBackgroundColor(getResources().getColor(R.color.card_detailing));
                LinearLayout.LayoutParams paramsview = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
                paramsview.topMargin = 10;
                paramsview.bottomMargin = 10;
                view.setLayoutParams(paramsview);
                // linearLayoutnew.addView(view);

                if (eachData.getString("field_type").equals("select")) {

                    final JSONArray dropDownJSONOpt = eachData.getJSONArray("values");

                    final ArrayList<subcatDiloglist> SpinnerOptions;
                    ArrayList<KeyPairBoolData> listArray1 = new ArrayList<>();
                    SpinnerOptions = new ArrayList<>();
                    for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                        subcatDiloglist subDiloglist = new subcatDiloglist();
                        subDiloglist.setId(dropDownJSONOpt.getJSONObject(j).getString("id"));
                        subDiloglist.setName(dropDownJSONOpt.getJSONObject(j).getString("name"));
                        try {
                            subDiloglist.setImage(dropDownJSONOpt.getJSONObject(j).getString("image"));
                        }catch (Exception e){
                            subDiloglist.setImage("");
                        }
                        subDiloglist.setHasSub(dropDownJSONOpt.getJSONObject(j).getBoolean("has_sub"));
                        subDiloglist.setHasCustom(dropDownJSONOpt.getJSONObject(j).getBoolean("has_template"));
                        //String optionString = dropDownJSONOpt.getJSONObject(j).getString("name");

                        KeyPairBoolData h = new KeyPairBoolData();
                        if (eachData.getString("field_type_name").equals("ad_town")||eachData.getString("field_type_name").equals("ad_cities")){
                           h.setId(dropDownJSONOpt.getJSONObject(j).getLong("id"));
                        }else {
                            h.setId(j + 1);

                        }
                        h.setName(dropDownJSONOpt.getJSONObject(j).getString("name"));
                        h.setSelected(false);
                        listArray1.add(h);

                        SpinnerOptions.add(subDiloglist);
                    }


                    /*  categorycard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (SpinnerOptions!=null && SpinnerOptions.size()>0){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("category",SpinnerOptions); // Put anything what you want
                            CategoryFragment fragobj = new CategoryFragment();

                            fragobj.setArguments(bundle);
                            replaceFragment2(fragobj,"hhhh");
                            //startFragment(fragobj);
                                }
                        }
                    });*/


                    final SpinnerAndListAdapter spinnerAndListAdapter;
                    spinnerAndListAdapter = new SpinnerAndListAdapter(getActivity(), SpinnerOptions, true);

                    if (eachData.getString("field_type_name").equals("ad_country")) {
                        textViewFilterText.setVisibility(View.GONE);
                        cardView.setVisibility(View.GONE);
//                        final MultiSpinnerSearch multiSpinnerSearch = new MultiSpinnerSearch(getActivity());
//                        multiSpinnerSearch.setSearchEnabled(true);
//                        multiSpinnerSearch.setShowSelectAllButton(true);
//                        multiSpinnerSearch.setItems(listArray1, new MultiSpinnerListener() {
//
//                            @Override
//                            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
//                                // Log.e("selectedItems", selectedItems.get(0).getName().toString());
//                                if (eachData.optString("title").matches("City")) {
//                                    if (selectedItems.size() == 1) {
//
//                                        if (SettingsMain.isConnectingToInternet(getActivity())) {
//                                            loadingLayout.setVisibility(View.VISIBLE);
//                                            shimmerFrameLayout.setVisibility(View.VISIBLE);
//                                            shimmerFrameLayout.startShimmer();
//                                            //for serlecting the location if location have sabLocations
//                                            try {
//                                                if (eachData.getString("field_type_name").equals("ad_country")) {
//
//                                                    JsonObject params1 = new JsonObject();
//                                                    params1.addProperty("ad_country", SpinnerOptions.get(0).getId());
//                                                    Log.d("info sendSearch Loctn", params1.toString() + eachData.getString("field_type_name"));
//
//                                                    Call<ResponseBody> myCall = restService.postGetSearcSubLocation(params1, UrlController.AddHeaders(getActivity()));
//                                                    myCall.enqueue(new Callback<ResponseBody>() {
//                                                        @Override
//                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
//                                                            try {
//                                                                if (responseObj.isSuccessful()) {
//                                                                    Log.d("info SubSearch Resp", "" + responseObj.toString());
//
//                                                                    JSONObject response = new JSONObject(responseObj.body().string());
//                                                                    if (response.getBoolean("success")) {
//                                                                        Log.d("info SearchLctn object", "" + response.getJSONObject("data"));
//                                                                        spinnerTouched = false;
//
//                                                                        adforest_ShowDialogMulti(response.getJSONObject("data"), SpinnerOptions.get(0), SpinnerOptions
//                                                                                , spinnerAndListAdapter, multiSpinnerSearch, eachData.getString("field_type_name"));
//
//                                                                    } else {
//                                                                        Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
//                                                                    }
//                                                                }
//                                                                shimmerFrameLayout.stopShimmer();
//                                                                shimmerFrameLayout.setVisibility(View.GONE);
//                                                                loadingLayout.setVisibility(View.GONE);
//                                                                searchBtn.setVisibility(View.VISIBLE);
//
//
//                                                            } catch (JSONException e) {
//                                                                shimmerFrameLayout.stopShimmer();
//                                                                shimmerFrameLayout.setVisibility(View.GONE);
//                                                                loadingLayout.setVisibility(View.GONE);
//                                                                searchBtn.setVisibility(View.VISIBLE);
//
//
//                                                                e.printStackTrace();
//                                                            } catch (IOException e) {
//                                                                shimmerFrameLayout.stopShimmer();
//                                                                shimmerFrameLayout.setVisibility(View.GONE);
//                                                                loadingLayout.setVisibility(View.GONE);
//                                                                searchBtn.setVisibility(View.VISIBLE);
//
//
//                                                                e.printStackTrace();
//                                                            }
//                                                            shimmerFrameLayout.stopShimmer();
//                                                            shimmerFrameLayout.setVisibility(View.GONE);
//                                                            loadingLayout.setVisibility(View.GONE);
//                                                            searchBtn.setVisibility(View.VISIBLE);
//
//
//                                                        }
//
//                                                        @Override
//                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                                            if (t instanceof TimeoutException) {
//                                                                Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//                                                                shimmerFrameLayout.stopShimmer();
//                                                                shimmerFrameLayout.setVisibility(View.GONE);
//                                                                loadingLayout.setVisibility(View.GONE);
//                                                                searchBtn.setVisibility(View.VISIBLE);
//
//
//                                                            }
//                                                            if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {
//
//                                                                Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//                                                                shimmerFrameLayout.stopShimmer();
//                                                                shimmerFrameLayout.setVisibility(View.GONE);
//                                                                loadingLayout.setVisibility(View.GONE);
//                                                                searchBtn.setVisibility(View.VISIBLE);
//
//
//                                                            }
//                                                            if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
//                                                                Log.d("info SearchLctn ", "NullPointert Exception" + t.getLocalizedMessage());
//                                                                shimmerFrameLayout.stopShimmer();
//                                                                shimmerFrameLayout.setVisibility(View.GONE);
//                                                                loadingLayout.setVisibility(View.GONE);
//                                                                searchBtn.setVisibility(View.VISIBLE);
//
//
//                                                            } else {
//                                                                shimmerFrameLayout.stopShimmer();
//                                                                shimmerFrameLayout.setVisibility(View.GONE);
//                                                                loadingLayout.setVisibility(View.GONE);
//                                                                searchBtn.setVisibility(View.VISIBLE);
//
//
//                                                                Log.d("info SearchLctn error", String.valueOf(t));
//                                                                Log.d("info SearchLctn error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
//                                                            }
//                                                        }
//                                                    });
//
//                                                }
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        } else {
//                                            shimmerFrameLayout.stopShimmer();
//                                            shimmerFrameLayout.setVisibility(View.GONE);
//                                            loadingLayout.setVisibility(View.GONE);
//                                            searchBtn.setVisibility(View.VISIBLE);
//
//
//                                            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
//                                        }
//                                        spinnerTouched = false;
//
//
//                                        try {
//
//                                            Log.d("true===== ", "in Main ====  " + SpinnerOptions.get(0).isHasCustom());
//
//                                            if (eachData.getBoolean("has_cat_template"))
//                                                if (SpinnerOptions.get(0).isHasCustom()) {
//                                                    linearLayoutCustom.removeAllViews();
//                                                    allViewInstanceforCustom.clear();
//                                                    catID = SpinnerOptions.get(0).getId();
//                                                    adforest_showCustom();
//                                                    ison = true;
//                                                    Log.d("true===== ", "add All");
//
//
//                                                } else {
//                                                    if (ison) {
//                                                        linearLayoutCustom.removeAllViews();
//                                                        allViewInstanceforCustom.clear();
//                                                        ison = false;
//                                                        Log.d("true===== ", "remove All");
//
//                                                    }
//                                                }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//
//
//                                    } else {
//                                        if (ison) {
//                                            linearLayoutCustom.removeAllViews();
//                                            allViewInstanceforCustom.clear();
//                                            ison = false;
//                                            Log.d("true===== ", "remove All");
//                                        }
//                                    }
//
//                                }
//
//                            }
//                        });
//
//                        allViewInstance.add(multiSpinnerSearch);
//                        allViewInstanceforCustom.add(multiSpinnerSearch);
//                        multiSpinnerSearch.setSelection(0, false);
//
//                        multiSpinnerSearch.setOnTouchListener((v, event) -> {
//                            System.out.println("Real touch felt.");
//                            spinnerTouched = true;
//                            return false;
//                        });
//
////                        multiSpinnerSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                            @Override
////                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
////                                if (spinnerTouched) {
////                                    //String variant_name = dropDownJSONOpt.getJSONObject(position).getString("name");
////
////                                }
////                                spinnerTouched = false;
////                            }
////
////                            @Override
////                            public void onNothingSelected(AdapterView<?> parentView) {
////                            }
////
////                        });
//                        linearLayout.addView(multiSpinnerSearch, 1);
//

                    } else {
                        final Spinner spinner = new Spinner(getActivity());
                        allViewInstance.add(spinner);
                        spinner.setAdapter(spinnerAndListAdapter);

                        spinner.setSelection(0, false);

                        spinner.setOnTouchListener((v, event) -> {
                            System.out.println("Real touch felt.");
                            spinnerTouched = true;
                            return false;
                        });

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                if (spinnerTouched) {
                                    //String variant_name = dropDownJSONOpt.getJSONObject(position).getString("name");
                                    if (position != 0) {
                                        final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) selectedItemView.getTag();
                                        if (subcatDiloglistitem.isHasSub()) {


                                            if (SettingsMain.isConnectingToInternet(getActivity())) {
                                                loadingLayout.setVisibility(View.VISIBLE);
                                                shimmerFrameLayout.setVisibility(View.VISIBLE);
                                                shimmerFrameLayout.startShimmer();
                                                //for serlecting the Categoreis if Categoreis have SubCategoreis
                                                try {
                                                    if (eachData.getString("field_type_name").equals("ad_cats1")) {

                                                        JsonObject params = new JsonObject();
                                                        params.addProperty("subcat", subcatDiloglistitem.getId());

                                                        Log.d("info sendSearch SubCats", "" + params.toString());

                                                        Call<ResponseBody> myCall = restService.postGetSearcSubCats(params, UrlController.AddHeaders(getActivity()));
                                                        myCall.enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                                                try {
                                                                    if (responseObj.isSuccessful()) {
                                                                        Log.d("info GetSubCats Resp", "" + responseObj.toString());

                                                                        JSONObject response = new JSONObject(responseObj.body().string());
                                                                        if (response.getBoolean("success")) {
                                                                            Log.d("info GetSubCats object", "" + response.getJSONObject("data"));
                                                                            spinnerTouched = false;

                                                                            adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, SpinnerOptions
                                                                                    , spinnerAndListAdapter, spinner, eachData.getString("field_type_name"));

                                                                        } else {
                                                                            Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                    shimmerFrameLayout.stopShimmer();
                                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                                    loadingLayout.setVisibility(View.GONE);
                                                                    searchBtn.setVisibility(View.VISIBLE);


                                                                } catch (JSONException e) {
                                                                    shimmerFrameLayout.stopShimmer();
                                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                                    loadingLayout.setVisibility(View.GONE);
                                                                    searchBtn.setVisibility(View.VISIBLE);


                                                                    e.printStackTrace();
                                                                } catch (IOException e) {
                                                                    shimmerFrameLayout.stopShimmer();
                                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                                    loadingLayout.setVisibility(View.GONE);
                                                                    searchBtn.setVisibility(View.VISIBLE);


                                                                    e.printStackTrace();
                                                                }
                                                                shimmerFrameLayout.stopShimmer();
                                                                shimmerFrameLayout.setVisibility(View.GONE);
                                                                loadingLayout.setVisibility(View.GONE);
                                                                searchBtn.setVisibility(View.VISIBLE);


                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                shimmerFrameLayout.stopShimmer();
                                                                shimmerFrameLayout.setVisibility(View.GONE);
                                                                loadingLayout.setVisibility(View.GONE);
                                                                searchBtn.setVisibility(View.VISIBLE);


                                                                Log.d("info GetAdnewPost error", String.valueOf(t));
                                                                Log.d("info GetAdnewPost error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                                            }
                                                        });
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                //for serlecting the location if location have sabLocations
                                                try {
                                                    if (eachData.getString("field_type_name").equals("ad_country")) {

                                                        JsonObject params1 = new JsonObject();
                                                        params1.addProperty("ad_country", subcatDiloglistitem.getId());
                                                        Log.d("info sendSearch Loctn", params1.toString() + eachData.getString("field_type_name"));

                                                        Call<ResponseBody> myCall = restService.postGetSearcSubLocation(params1, UrlController.AddHeaders(getActivity()));
                                                        myCall.enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                                                try {
                                                                    if (responseObj.isSuccessful()) {
                                                                        Log.d("info SubSearch Resp", "" + responseObj.toString());

                                                                        JSONObject response = new JSONObject(responseObj.body().string());
                                                                        if (response.getBoolean("success")) {
                                                                            Log.d("info SearchLctn object", "" + response.getJSONObject("data"));
                                                                            spinnerTouched = false;

                                                                            adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, SpinnerOptions
                                                                                    , spinnerAndListAdapter, spinner, eachData.getString("field_type_name"));

                                                                        } else {
                                                                            Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                    shimmerFrameLayout.stopShimmer();
                                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                                    loadingLayout.setVisibility(View.GONE);
                                                                    searchBtn.setVisibility(View.VISIBLE);


                                                                } catch (JSONException e) {
                                                                    shimmerFrameLayout.stopShimmer();
                                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                                    loadingLayout.setVisibility(View.GONE);
                                                                    searchBtn.setVisibility(View.VISIBLE);


                                                                    e.printStackTrace();
                                                                } catch (IOException e) {
                                                                    shimmerFrameLayout.stopShimmer();
                                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                                    loadingLayout.setVisibility(View.GONE);
                                                                    searchBtn.setVisibility(View.VISIBLE);


                                                                    e.printStackTrace();
                                                                }
                                                                shimmerFrameLayout.stopShimmer();
                                                                shimmerFrameLayout.setVisibility(View.GONE);
                                                                loadingLayout.setVisibility(View.GONE);
                                                                searchBtn.setVisibility(View.VISIBLE);


                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                if (t instanceof TimeoutException) {
                                                                    Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                                                    shimmerFrameLayout.stopShimmer();
                                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                                    loadingLayout.setVisibility(View.GONE);
                                                                    searchBtn.setVisibility(View.VISIBLE);


                                                                }
                                                                if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                                                                    Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                                                    shimmerFrameLayout.stopShimmer();
                                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                                    loadingLayout.setVisibility(View.GONE);
                                                                    searchBtn.setVisibility(View.VISIBLE);


                                                                }
                                                                if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                                                                    Log.d("info SearchLctn ", "NullPointert Exception" + t.getLocalizedMessage());
                                                                    shimmerFrameLayout.stopShimmer();
                                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                                    loadingLayout.setVisibility(View.GONE);
                                                                    searchBtn.setVisibility(View.VISIBLE);


                                                                } else {
                                                                    shimmerFrameLayout.stopShimmer();
                                                                    shimmerFrameLayout.setVisibility(View.GONE);
                                                                    loadingLayout.setVisibility(View.GONE);
                                                                    searchBtn.setVisibility(View.VISIBLE);


                                                                    Log.d("info SearchLctn error", String.valueOf(t));
                                                                    Log.d("info SearchLctn error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                                                }
                                                            }
                                                        });

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                shimmerFrameLayout.stopShimmer();
                                                shimmerFrameLayout.setVisibility(View.GONE);
                                                loadingLayout.setVisibility(View.GONE);
                                                searchBtn.setVisibility(View.VISIBLE);


                                                Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
                                            }
                                            spinnerTouched = false;
                                        }

                                        try {

                                            Log.d("true===== ", "in Main ====  " + subcatDiloglistitem.isHasCustom());

                                            if (eachData.getBoolean("has_cat_template"))
                                                if (subcatDiloglistitem.isHasCustom()) {
                                                    linearLayoutCustom.removeAllViews();
                                                    allViewInstanceforCustom.clear();
                                                    catID = subcatDiloglistitem.getId();
                                                    adforest_showCustom();
                                                    ison = true;
                                                    Log.d("true===== ", "add All");


                                                } else {
                                                    if (ison) {
                                                        linearLayoutCustom.removeAllViews();
                                                        allViewInstanceforCustom.clear();
                                                        ison = false;
                                                        Log.d("true===== ", "remove All");

                                                    }
                                                }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        if (ison) {
                                            linearLayoutCustom.removeAllViews();
                                            allViewInstanceforCustom.clear();
                                            ison = false;
                                            Log.d("true===== ", "remove All");
                                        }
                                    }
                                }
                                spinnerTouched = false;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                            }

                        });

                        //     cardViewList.setContentPadding(10, 20, 10, 20);


                        if (SpinnerOptions != null && SpinnerOptions.size() > 0) {
                            et5 = new TextView(getActivity());
                            et5.setHint(eachData.getString("title"));
                            et5.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_format_list_bulleted), null, ContextCompat.getDrawable(getActivity(), R.drawable.ic_arrow_down_), null);
                            et5.setPadding(10, 20, 10, 20);
                            et5.setCompoundDrawablePadding(10);
                            et5.setText(SpinnerOptions.get(0).name);
                            et5.setMaxLines(1);

                            linearLayoutnew.addView(et5);
                            //   linearLayoutnew.addView(spinner);
                            cardViewList.addView(linearLayoutnew);
                            linearLayout.addView(cardViewList, 0);


                            //  recycler_view_category.setVisibility(View.VISIBLE);
                            et5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(getActivity(), ActivityCategoryActivity.class);
                                    intent.putExtra("category", SpinnerOptions);
                                    getActivity().startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);


                                }
                            });
                        }

                    }


                    if (eachData.getString("field_type_name").equals("ad_cats1")) {
                        linearLayoutCustom = new LinearLayout(getActivity());
                        linearLayoutCustom.setPadding(5, 5, 5, 5);
                        linearLayoutCustom.setOrientation(LinearLayout.VERTICAL);
                        linearLayoutCustom.setBackgroundColor(getResources().getColor(R.color.card_detailing));
                        linearLayout.addView(linearLayoutCustom, 1);
                    }

                    cardView.setContentPadding(10, 20, 10, 20);
                    cardView.addView(linearLayout);
                    viewProductLayout.addView(cardView);

                    //new change
                    adforest_showCustom();
                }
                if (eachData.getString("field_type").equals("textfield")) {
                    // TextInputLayout til = new TextInputLayout(getActivity());
                    // til.setHint(eachData.getString("title"));
                    EditText et = new EditText(getActivity());
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    et.setHint(eachData.getString("title"));
                    et.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.border_edittext));
                    et.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.ic_search_dark), null, null, null);
                    et.setPadding(10, 20, 10, 20);
                    et.setCompoundDrawablePadding(10);
                    et.setMaxLines(1);
                    //  til.addView(et);
                    allViewInstance.add(et);
                    cardView.addView(et);
                    viewProductLayout.addView(cardView);
                }


                if (eachData.getString("field_type").equals("glocation_textfield")) {
                    TextInputLayout til = new TextInputLayout(getActivity());

                    til.setHint(eachData.getString("title"));
                    //addressbyGeoCode);

//                    if (settingsMain.getPlacesSearch()) {
//                        mapBoxPlaces = new EditText(getActivity());
//                        mapBoxPlaces.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new PlaceAutocomplete.IntentBuilder()
//                                        .accessToken(getString(R.string.access_token))
//                                        .placeOptions(PlaceOptions.builder().backgroundColor(Color.parseColor("#EEEEEE")).limit(10).build(PlaceOptions.MODE_CARDS))
//                                        .build(getActivity());
//                                startActivityForResult(intent, 35);
//                            }
//                        });
//                        mapBoxPlaces.setText(addressbyGeoCode);
//                        til.addView(mapBoxPlaces);
//                        allViewInstance.add(mapBoxPlaces);
//                        cardView.addView(til);
//                        viewProductLayout.addView(cardView);
//                    } else
//                    {
//                        et = new AutoCompleteTextView(getActivity());
//                        et.setText("");
//                        placesClient = com.google.android.libraries.places.api.Places.createClient(getContext());
//                        et.setOnItemClickListener(this);
//
//                        et.addTextChangedListener(new TextWatcher() {
//
//                            @Override
//                            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                manageAutoComplete(s.toString());
//
//                            }
//
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                            }
//
//                            @Override
//                            public void afterTextChanged(Editable s) {
//
//                            }
//                        });
//                        et.setOnItemClickListener(this);
//                        til.addView(et);
//                        allViewInstance.add(et);
//                        cardView.addView(til);
//                        viewProductLayout.addView(cardView);
//                    }
                }

                if (eachData.getString("field_type").equals("range_textfield")) {
                    String text1 = eachData.getJSONArray("data").getJSONObject(0).getString("title");
                    String text2 = eachData.getJSONArray("data").getJSONObject(1).getString("title");
                    LinearLayout linearLayout1 = new LinearLayout(getActivity());
                    linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                  /*  textpricelabel1.setText(text1);
                    textpricelabel2.setText(text2);
                    textprice1.setHint(text1);
                    textprice2.setHint(text2);
*/

                    TextInputLayout til = new TextInputLayout(getActivity());
                    TextInputLayout til2 = new TextInputLayout(getActivity());

                    til.setHint(text1);
                    til2.setHint(text2);


                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params2.weight = 1;

                    EditText et = new EditText(getActivity());
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    EditText et2 = new EditText(getActivity());
                    et2.setInputType(InputType.TYPE_CLASS_NUMBER);
                    til.addView(et);
                    til2.addView(et2);

                    til.setLayoutParams(params2);
                    til2.setLayoutParams(params2);
                    linearLayout1.addView(til);
                    linearLayout1.addView(til2);

                    linearLayout.addView(linearLayout1);
                    allViewInstance.add(linearLayout1);
                    cardView.addView(linearLayout);
                    viewProductLayout.addView(cardView);
                }
                if (settingsMain.getShowNearBy()) {
                    if (eachData.getString("field_type").equals("seekbar")) {
                        Location_popupModel Location_popupModel = settingsMain.getLocationPopupModel(getContext());
                        final BubbleSeekBar bubbleSeekBar = new BubbleSeekBar(getActivity());
                        bubbleSeekBar.getConfigBuilder()
                                .max(Location_popupModel.getSlider_number())
                                .sectionCount(Location_popupModel.getSlider_step())
                                .secondTrackColor(Color.parseColor(SettingsMain.getMainColor()))
                                .sectionTextPosition(BubbleSeekBar.TextPosition.BELOW_SECTION_MARK)
                                .showSectionMark()
                                .showThumbText()
                                .thumbTextSize(20)
                                .trackColor(Color.parseColor("#cccccc"))
                                .touchToSeek()
                                .build();
                        linearLayout.addView(bubbleSeekBar);
                        cardView.addView(linearLayout);
                        allViewInstance.add(bubbleSeekBar);
                        viewProductLayout.addView(cardView);
                        bubbleSeekBarDistance = bubbleSeekBar;
                    }
                }
            }
        } catch (JSONException e) {
            Log.d("jsonerrorsomething",e.toString());
        }

//        spinnerFilterText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////
//                Intent i = new Intent(getActivity(), SearchActivity.class);
//                startActivity(i);
//
//            }
//        });
        try {
            jsonObjectFilterSpinner = jsonObject.getJSONObject("topbar");


            final JSONArray dropDownJSONOpt = jsonObjectFilterSpinner.getJSONArray("sort_arr");
            final ArrayList<String> SpinnerOptions;
            SpinnerOptions = new ArrayList<>();
            for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                SpinnerOptions.add(dropDownJSONOpt.getJSONObject(j).getString("value"));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_medium, SpinnerOptions);
            spinnerFilter.setAdapter(adapter);

            spinnerFilter.setOnTouchListener((v, event) -> {
                System.out.println("Real touch felt.");
                onclick = true;

                spinnerTouched2 = true;
                return false;
            });

            spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (spinnerTouched2) {
                        try {
                            Log.d("Spinner_touched", "spinner");

                            //Toast.makeText(getActivity(), "" + dropDownJSONOpt.getJSONObject(i).getString("key"), Toast.LENGTH_SHORT).show();
                            isSort = true;
                            adforest_submitQuery(adforest_getDataFromDynamicViews(), dropDownJSONOpt.getJSONObject(i).getString("key"));
                            //  imageViewCollapse.performClick();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    spinnerTouched2 = false;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void list(ArrayList<subcatDiloglist> list) {
        categoryAdapter.ItemCategoryAdapter(getActivity(), list);
        recycler_view_category.setAdapter(categoryAdapter);
        categoryAdapter.setOncatItemClickListener(new oncatItemClick() {

            @Override
            public void onItemClick(subcatDiloglist item) {

                if (secondcat == false) {
                    if (!item.id.equals("")) {
                        //  mainRelative.setVisibility(View.VISIBLE);
                        secondcat = true;
                        recycler_view_category.setVisibility(View.GONE);

                        getsubcategory(item.id);


                    }
                } else {
                    secondcat = false;
                    et5.setText(item.name);
                    iscategory(false);
                    catchange(item);

                }


            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 35) {
            if (resultCode == RESULT_OK && data != null) {
                CarmenFeature feature = PlaceAutocomplete.getPlace(data);
                Point point = feature.center();
                lat_by_mapbox = point.latitude();
                lon_by_mapbox = point.longitude();
                latitude = String.valueOf(lat_by_mapbox);
                longtitude = String.valueOf(lon_by_mapbox);
                address_by_mapbox = feature.placeName();
                mapBoxPlaces.setText(address_by_mapbox);
                PlaceAutocomplete.clearRecentHistory(getActivity());

            }
          /*  if (requestCode==111){
                subcatDiloglist addID = (subcatDiloglist) data.getSerializableExtra("item");
                String addressLine=data.getStringExtra("addressLine");
                onResumegetcategory(addID);
            }*/
        }
    }

    private void manageAutoComplete(String query) {
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        FindAutocompletePredictionsRequest.Builder request = FindAutocompletePredictionsRequest.builder();

        if (SplashScreen.gmap_has_countries) {
            request.setCountry(SplashScreen.gmap_countries);
        }
        if (settingsMain.getAlertDialogMessage("location_type").equals("regions")) {
            request.setTypeFilter(TypeFilter.ADDRESS);
        } else {
            request
                    .setTypeFilter(TypeFilter.REGIONS);
        }
        request.setSessionToken(token)
                .setQuery(query);


        placesClient.findAutocompletePredictions(request.build()).addOnSuccessListener((response) -> {

            ids.clear();
            places.clear();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                places.add(prediction.getFullText(null).toString());
                ids.add(prediction.getPlaceId());
                Log.i("Places", prediction.getPlaceId());
                Log.i("Places", prediction.getFullText(null).toString());

            }
            String[] data = places.toArray(new String[places.size()]); // terms is a List<String>

            ArrayAdapter<?> adapter = new ArrayAdapter<Object>(getContext(), android.R.layout.simple_dropdown_item_1line, data);
            // et.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e("Places", "Place not found: " + apiException.getStatusCode());
            }
        });


    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        String placeId = ids.get(position);
        List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);

// Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                .build();
// Add a listener to handle the response.
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            com.google.android.libraries.places.api.model.Place place = response.getPlace();
            Log.i("Places", "Place found: " + place.getLatLng().latitude + " " + place.getLatLng().longitude);
            longtitude = Double.toString(place.getLatLng().longitude);
            latitude = Double.toString(place.getLatLng().latitude);

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
// Handle error with given status code.
                Log.e("Places", "Place not found: " + exception.getMessage());
            }
        });


    }

    private void adforest_showCustom() {

        if (linearLayoutCustom != null) {

            if (SettingsMain.isConnectingToInternet(getActivity())) {

                JsonObject params = new JsonObject();
                if (!TextUtils.isEmpty(catID)) {
                    params.addProperty("cat_id", catID);
                    Log.d("info sendSearch CatID", catID);
                } else {
                    params.addProperty("cat_id", "");
                    Log.d("info sendSearch CatID", "");
                }
                Call<ResponseBody> myCall = restService.postGetSearchDynamicFields(params, UrlController.AddHeaders(getActivity()));
                myCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                        try {
                            if (responseObj.isSuccessful()) {
                                Log.d("info_searchDynamic_Resp", "" + responseObj.toString());

                                JSONObject response = new JSONObject(responseObj.body().string());
                                if (response.getBoolean("success")) {

                                    String veryLongString = response.getJSONArray("data").toString();
                                    int maxLogSize = 1000;
                                    for (int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
                                        int start = i * maxLogSize;
                                        int end = (i + 1) * maxLogSize;
                                        end = end > veryLongString.length() ? veryLongString.length() : end;
                                        Log.d("info_searchDynamic_obj", veryLongString.substring(start, end));
                                    }
                                    adforest_setViewsForCustom(response);
                                } else {
                                    Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLayout.setVisibility(View.GONE);
                            cvImage.setVisibility(View.VISIBLE);
                            searchBtn.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLayout.setVisibility(View.GONE);
                            cvImage.setVisibility(View.VISIBLE);
                            searchBtn.setVisibility(View.VISIBLE);


                            e.printStackTrace();
                        } catch (IOException e) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLayout.setVisibility(View.GONE);
                            cvImage.setVisibility(View.VISIBLE);
                            searchBtn.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        cvImage.setVisibility(View.VISIBLE);
                        searchBtn.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (t instanceof TimeoutException) {
                            Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLayout.setVisibility(View.GONE);
                            cvImage.setVisibility(View.VISIBLE);
                            searchBtn.setVisibility(View.VISIBLE);


                        }
                        if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                            Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLayout.setVisibility(View.GONE);
                            cvImage.setVisibility(View.VISIBLE);
                            searchBtn.setVisibility(View.VISIBLE);


                        }

                        if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                            Log.d("info searchDynamic ", "NullPointert Exception" + t.getLocalizedMessage());
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLayout.setVisibility(View.GONE);
                            cvImage.setVisibility(View.VISIBLE);
                            searchBtn.setVisibility(View.VISIBLE);

                        } else {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLayout.setVisibility(View.GONE);
                            cvImage.setVisibility(View.VISIBLE);
                            searchBtn.setVisibility(View.VISIBLE);

                            Log.d("info searchDynamic err", String.valueOf(t));
                            Log.d("info searchDynamic err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void adforest_ShowDialog(JSONObject data, final subcatDiloglist main,
                                     final ArrayList<subcatDiloglist> spinnerOptionsout,
                                     final SpinnerAndListAdapter spinnerAndListAdapterout,
                                     final Spinner spinner1, final String field_type_name) {

        Log.d("info Dialog Data===== ", "adforest_ShowDialog");
        try {
            Log.d("info Dialog Data===== ", data.getJSONArray("values").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Dialog dialog = new Dialog(getActivity(), R.style.PauseDialog);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_sub_cat);

        dialog.setTitle(main.getName());
        ListView listView = dialog.findViewById(R.id.listView);

        final ArrayList<subcatDiloglist> listitems = new ArrayList<>();
        final JSONArray listArray;
        try {
            listArray = data.getJSONArray("values");
            for (int j = 0; j < listArray.length(); j++) {
                subcatDiloglist subDiloglist = new subcatDiloglist();
                subDiloglist.setId(listArray.getJSONObject(j).getString("id"));
                subDiloglist.setName(listArray.getJSONObject(j).getString("name"));
                subDiloglist.setHasSub(listArray.getJSONObject(j).getBoolean("has_sub"));
                subDiloglist.setHasCustom(listArray.getJSONObject(j).getBoolean("has_template"));
                listitems.add(subDiloglist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final SpinnerAndListAdapter spinnerAndListAdapter1 = new SpinnerAndListAdapter(getActivity(), listitems);
        listView.setAdapter(spinnerAndListAdapter1);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) view.getTag();

            //Log.d("helllo" , spinnerOptionsout.adforest_get(1).getId() + " === " + spinnerOptionsout.adforest_get(1).getName());

            if (!spinnerOptionsout.get(1).getId().equals(subcatDiloglistitem.getId())) {

                if (subcatDiloglistitem.isHasSub()) {


                    if (SettingsMain.isConnectingToInternet(getActivity())) {
                        loadingLayout.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.startShimmer();
                        if (field_type_name.equals("ad_cats1")) {
                            JsonObject params = new JsonObject();
                            params.addProperty("subcat", subcatDiloglistitem.getId());

                            Log.d("info sendDiSubCats", params.toString() + field_type_name);

                            Call<ResponseBody> myCall = restService.postGetSearcSubCats(params, UrlController.AddHeaders(getActivity()));
                            myCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                    try {
                                        if (responseObj.isSuccessful()) {
                                            Log.d("info DiSubCats Resp", "" + responseObj.toString());

                                            JSONObject response = new JSONObject(responseObj.body().string());
                                            if (response.getBoolean("success")) {
                                                Log.d("info DidSubCats object", "" + response.getJSONObject("data"));

                                                adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, spinnerOptionsout
                                                        , spinnerAndListAdapterout, spinner1, field_type_name);

                                            } else {
                                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                    } catch (JSONException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                        e.printStackTrace();
                                    }
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLayout.setVisibility(View.GONE);
                                    searchBtn.setVisibility(View.VISIBLE);

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    if (t instanceof TimeoutException) {
                                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                    }
                                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                    }
                                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                                        Log.d("info DidSubCats ", "NullPointert Exception" + t.getLocalizedMessage());
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);


                                    } else {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                        Log.d("info DiaSubCats error", String.valueOf(t));
                                        Log.d("info DiaSubCats error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                    }
                                }
                            });
                        }
                        if (field_type_name.equals("ad_country")) {

                            JsonObject params1 = new JsonObject();
                            params1.addProperty("ad_country", subcatDiloglistitem.getId());
                            Log.d("info DiSubLocation", params1.toString() + field_type_name);

                            Call<ResponseBody> myCall = restService.postGetSearcSubLocation(params1, UrlController.AddHeaders(getActivity()));
                            myCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                    try {
                                        if (responseObj.isSuccessful()) {
                                            Log.d("info DiSubLocation Resp", "" + responseObj.toString());

                                            JSONObject response = new JSONObject(responseObj.body().string());
                                            if (response.getBoolean("success")) {
                                                Log.d("info DiSubLocation obj", "" + response.getJSONObject("data"));

                                                adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, spinnerOptionsout
                                                        , spinnerAndListAdapterout, spinner1, field_type_name);

                                            } else {
                                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                    } catch (JSONException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                        e.printStackTrace();
                                    }
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLayout.setVisibility(View.GONE);
                                    searchBtn.setVisibility(View.VISIBLE);


                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    if (t instanceof TimeoutException) {
                                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                    }
                                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                    }
                                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                                        Log.d("info DiSubLocation ", "NullPointert Exception" + t.getLocalizedMessage());
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                    } else {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                        Log.d("info DiSubLocation err", String.valueOf(t));
                                        Log.d("info DiSubLocation err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                    }
                                }
                            });

                        }
                    } else {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        searchBtn.setVisibility(View.VISIBLE);

                        Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    for (int ii = 0; ii < spinnerOptionsout.size(); ii++) {
                        if (spinnerOptionsout.get(ii).getId().equals(subcatDiloglistitem.getId())) {
                            spinnerOptionsout.remove(ii);
                            Log.d("info ===== ", "else of list inner is 1st button into for loop");
                            break;
                        }
                    }
                    Log.d("info ===== ", "else of list inner is 1st button out of for loop");

                    // added here
                    spinnerOptionsout.add(1, subcatDiloglistitem);
                    spinner1.setSelection(1, false);
                    spinnerAndListAdapterout.notifyDataSetChanged();

                }

                Log.d("true===== ", "in dalog ====  " + subcatDiloglistitem.isHasCustom());

                if (subcatDiloglistitem.isHasCustom()) {
                    linearLayoutCustom.removeAllViews();
                    allViewInstanceforCustom.clear();
                    catID = subcatDiloglistitem.getId();
                    adforest_showCustom();
                    Log.d("true===== ", "inter add All");

                } else {
                    ison = false;
                    Log.d("true===== ", "inter remove All");
                }
            } else {
                spinner1.setSelection(1, false);
                Log.d("info ===== ", "else of chk is 1st button out");

            }
            dialog.dismiss();
        });

        Button Send = dialog.findViewById(R.id.send_button);
        Button Cancel = dialog.findViewById(R.id.cancel_button);
        Cancel.setVisibility(View.GONE);

        try {
            Send.setText(jsonObject.getJSONObject("extra").getString("dialog_send"));
            Cancel.setText(jsonObject.getJSONObject("extra").getString("dialg_cancel"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Send.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        Cancel.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));

        Send.setOnClickListener(v -> {

            for (int i = 0; i < spinnerOptionsout.size(); i++) {
                if (spinnerOptionsout.get(i).getId().equals(main.getId())) {
                    spinnerOptionsout.remove(i);
                    Log.d("info ===== ", "send button in");
                    break;
                }
            }

            spinnerOptionsout.add(1, main);
            spinnerAndListAdapterout.notifyDataSetChanged();
            spinner1.setSelection(1, false);
            Log.d("info ===== ", "send button out");

            dialog.dismiss();
        });

        Cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }


    private void adforest_ShowDialogMulti(JSONObject data, final subcatDiloglist main,
                                          final ArrayList<subcatDiloglist> spinnerOptionsout,
                                          final SpinnerAndListAdapter spinnerAndListAdapterout,
                                          final MultiSpinnerSearch spinner1, final String field_type_name) {

        Log.d("info Dialog Data===== ", "adforest_ShowDialog");
        try {
            Log.d("info Dialog Data===== ", data.getJSONArray("values").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Dialog dialog = new Dialog(getActivity(), R.style.PauseDialog);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_sub_cat);

        dialog.setTitle(main.getName());
        ListView listView = dialog.findViewById(R.id.listView);

        final ArrayList<subcatDiloglist> listitems = new ArrayList<>();
        final JSONArray listArray;
        try {
            listArray = data.getJSONArray("values");
            for (int j = 0; j < listArray.length(); j++) {
                subcatDiloglist subDiloglist = new subcatDiloglist();
                subDiloglist.setId(listArray.getJSONObject(j).getString("id"));
                subDiloglist.setName(listArray.getJSONObject(j).getString("name"));
                subDiloglist.setHasSub(listArray.getJSONObject(j).getBoolean("has_sub"));
                subDiloglist.setHasCustom(listArray.getJSONObject(j).getBoolean("has_template"));
                listitems.add(subDiloglist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final SpinnerAndListAdapter spinnerAndListAdapter1 = new SpinnerAndListAdapter(getActivity(), listitems);
        listView.setAdapter(spinnerAndListAdapter1);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) view.getTag();

            //Log.d("helllo" , spinnerOptionsout.adforest_get(1).getId() + " === " + spinnerOptionsout.adforest_get(1).getName());

            if (!spinnerOptionsout.get(1).getId().equals(subcatDiloglistitem.getId())) {

                if (subcatDiloglistitem.isHasSub()) {


                    if (SettingsMain.isConnectingToInternet(getActivity())) {
                        loadingLayout.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.startShimmer();
                        if (field_type_name.equals("ad_cats1")) {
                            JsonObject params = new JsonObject();
                            params.addProperty("subcat", subcatDiloglistitem.getId());

                            Log.d("info sendDiSubCats", params.toString() + field_type_name);

                            Call<ResponseBody> myCall = restService.postGetSearcSubCats(params, UrlController.AddHeaders(getActivity()));
                            myCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                    try {
                                        if (responseObj.isSuccessful()) {
                                            Log.d("info DiSubCats Resp", "" + responseObj.toString());

                                            JSONObject response = new JSONObject(responseObj.body().string());
                                            if (response.getBoolean("success")) {
                                                Log.d("info DidSubCats object", "" + response.getJSONObject("data"));

                                                adforest_ShowDialogMulti(response.getJSONObject("data"), subcatDiloglistitem, spinnerOptionsout
                                                        , spinnerAndListAdapterout, spinner1, field_type_name);

                                            } else {
                                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                    } catch (JSONException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                        e.printStackTrace();
                                    }
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLayout.setVisibility(View.GONE);
                                    searchBtn.setVisibility(View.VISIBLE);

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    if (t instanceof TimeoutException) {
                                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                    }
                                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                    }
                                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                                        Log.d("info DidSubCats ", "NullPointert Exception" + t.getLocalizedMessage());
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);


                                    } else {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                        Log.d("info DiaSubCats error", String.valueOf(t));
                                        Log.d("info DiaSubCats error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                    }
                                }
                            });
                        }
                        if (field_type_name.equals("ad_country")) {

                            JsonObject params1 = new JsonObject();
                            params1.addProperty("ad_country", subcatDiloglistitem.getId());
                            Log.d("info DiSubLocation", params1.toString() + field_type_name);

                            Call<ResponseBody> myCall = restService.postGetSearcSubLocation(params1, UrlController.AddHeaders(getActivity()));
                            myCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                    try {
                                        if (responseObj.isSuccessful()) {
                                            Log.d("info DiSubLocation Resp", "" + responseObj.toString());

                                            JSONObject response = new JSONObject(responseObj.body().string());
                                            if (response.getBoolean("success")) {
                                                Log.d("info DiSubLocation obj", "" + response.getJSONObject("data"));

                                                adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, spinnerOptionsout
                                                        , spinnerAndListAdapterout, spinner1, field_type_name);

                                            } else {
                                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                    } catch (JSONException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                        e.printStackTrace();
                                    }
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLayout.setVisibility(View.GONE);
                                    searchBtn.setVisibility(View.VISIBLE);


                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    if (t instanceof TimeoutException) {
                                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                    }
                                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                    }
                                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                                        Log.d("info DiSubLocation ", "NullPointert Exception" + t.getLocalizedMessage());
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);


                                    } else {
                                        shimmerFrameLayout.stopShimmer();
                                        shimmerFrameLayout.setVisibility(View.GONE);
                                        loadingLayout.setVisibility(View.GONE);
                                        searchBtn.setVisibility(View.VISIBLE);

                                        Log.d("info DiSubLocation err", String.valueOf(t));
                                        Log.d("info DiSubLocation err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                    }
                                }
                            });

                        }
                    } else {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        searchBtn.setVisibility(View.VISIBLE);

                        Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
                    }


                } else {

                    for (int ii = 0; ii < spinnerOptionsout.size(); ii++) {
                        if (spinnerOptionsout.get(ii).getId().equals(subcatDiloglistitem.getId())) {
                            spinnerOptionsout.remove(ii);
                            Log.d("info ===== ", "else of list inner is 1st button into for loop");
                            break;
                        }
                    }
                    Log.d("info ===== ", "else of list inner is 1st button out of for loop");

                    spinnerOptionsout.add(1, subcatDiloglistitem);
                    ArrayList<KeyPairBoolData> keyPairBoolDataArrayList = new ArrayList<>();
                    for (int k = 0; k < spinnerOptionsout.size(); k++) {
                        KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                        keyPairBoolData.setId(k + 1);
                        keyPairBoolData.setName(spinnerOptionsout.get(k).getName());
                        if (k == 1) {
                            keyPairBoolData.setSelected(true);
                        } else {
                            keyPairBoolData.setSelected(false);
                        }
                        keyPairBoolDataArrayList.add(keyPairBoolData);
                    }
                    spinner1.setItems(keyPairBoolDataArrayList, new MultiSpinnerListener() {
                        @Override
                        public void onItemsSelected(List<KeyPairBoolData> selectedItems) {

                        }
                    });
                    //spinner1.setSelection(1, false);

                    // spinner1.setSelection(1, false);
                    // spinnerAndListAdapterout.notifyDataSetChanged();

                }

                Log.d("true===== ", "in dalog ====  " + subcatDiloglistitem.isHasCustom());

                if (subcatDiloglistitem.isHasCustom()) {
                    linearLayoutCustom.removeAllViews();
                    allViewInstanceforCustom.clear();
                    catID = subcatDiloglistitem.getId();
                    adforest_showCustom();
                    Log.d("true===== ", "inter add All");

                } else {
                    ison = false;
                    Log.d("true===== ", "inter remove All");
                }
            } else {
                spinner1.setSelection(1, false);
                Log.d("info ===== ", "else of chk is 1st button out");

            }
            dialog.dismiss();
        });

        Button Send = dialog.findViewById(R.id.send_button);
        Button Cancel = dialog.findViewById(R.id.cancel_button);

        try {
            Send.setText(jsonObject.getJSONObject("extra").getString("dialog_send"));
            Cancel.setText(jsonObject.getJSONObject("extra").getString("dialg_cancel"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Send.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));
        Cancel.setBackgroundColor(Color.parseColor(settingsMain.getMainColor()));

        Send.setOnClickListener(v -> {

            for (int i = 0; i < spinnerOptionsout.size(); i++) {
                if (spinnerOptionsout.get(i).getId().equals(main.getId())) {
                    spinnerOptionsout.remove(i);
                    Log.d("info ===== ", "send button in");
                    break;
                }
            }

            spinnerOptionsout.add(1, main);
            spinnerAndListAdapterout.notifyDataSetChanged();
            spinner1.setSelection(1, false);
            Log.d("info ===== ", "send button out");

            dialog.dismiss();
        });

        Cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }


    public JsonObject adforest_getDataFromDynamicViews() {
        JsonObject optionsObj = null;
        Log.d("requestform1", requestFrom);

        if (!requestFrom.equals("Home")) {
            try {
                JSONArray customOptnList = jsonObject.getJSONArray("data");
                Log.d("customobjlist", customOptnList.toString());
                optionsObj = new JsonObject();
                for (int noOfViews = 0; noOfViews < customOptnList.length(); noOfViews++) {
                    JSONObject eachData = customOptnList.getJSONObject(noOfViews);


                    if (eachData.getString("field_type").equals("select")) {
                        Spinner spinner = (Spinner) allViewInstance.get(noOfViews);

                        subcatDiloglist subcatDiloglist1 = (subcatDiloglist) spinner.getSelectedView().getTag();
                        JSONArray dropDownJSONOpt = eachData.getJSONArray("values");
                        String variant_name = dropDownJSONOpt.getJSONObject(spinner.getSelectedItemPosition()).getString("id");
                        Log.d("value_id12345", variant_name + "");

                       /* optionsObj.addProperty(eachData.getString("field_type_name"),
                                "" + subcatDiloglist1.getId());*/
                        optionsObj.addProperty(eachData.getString("field_type_name"),
                                "" + search_id);
                        Log.d("value_id12345", eachData.getString("field_type_name") + "" + subcatDiloglist1.getId());

                    }


                    if (eachData.getString("field_type").equals("textfield")) {
                        TextView textView = (TextView) allViewInstance.get(noOfViews);
                        if (!textView.getText().toString().equalsIgnoreCase(""))
                            optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                        else
                            optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                        Log.d("variant_  name", textView.getText().toString() + "");
                    }

                    if (eachData.getString("field_type").equals("glocation_textfield")) {
                        TextView textView = (TextView) allViewInstance.get(noOfViews);
                        if (!textView.getText().toString().equalsIgnoreCase(""))
                            optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                        else
                            optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                        Log.d("variant_name", textView.getText().toString() + "");
                    }
                    if (eachData.getString("field_type").equals("range_textfield")) {
                        LinearLayout linearLayout = (LinearLayout) allViewInstance.get(noOfViews);

                        TextInputLayout textView = (TextInputLayout) linearLayout.getChildAt(0);
                        TextInputLayout textView2 = (TextInputLayout) linearLayout.getChildAt(1);

                        if (textView.getEditText() != null && textView2.getEditText() != null)
                            optionsObj.addProperty(eachData.getString("field_type_name"), textView.getEditText().getText().toString() + "-" +
                                    textView2.getEditText().getText().toString());
                    }

                }

                hideSoftKeyboard();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d("array_us", (optionsObj != null ? optionsObj.toString() : null) + " ==== size====  " + allViewInstance.size());
        }
        return optionsObj;
    }




    public JsonObject adforest_getDataFromDynamicViewsForCustom() {
        JsonObject optionsObj = null;

        if (jsonObjectforCustom != null) {
            try {
                JSONArray customOptnList = jsonObjectforCustom.getJSONArray("data");

                optionsObj = new JsonObject();

                for (int noOfViews = 0; noOfViews < customOptnList.length(); noOfViews++) {

                    JSONObject eachData = customOptnList.getJSONObject(noOfViews);
                    Log.d("jsonarray123", eachData.toString());

                    if (eachData.getString("field_type").equals("select")) {
                        MultiSpinnerSearch spinner = (MultiSpinnerSearch) allViewInstanceforCustom.get(noOfViews);


                        // subcatDiloglist subcatDiloglist1 = (subcatDiloglist) spinner.getSelectedView().getTag();
                        JSONArray dropDownJSONOpt = eachData.getJSONArray("values");
                        String variant_name = dropDownJSONOpt.getJSONObject(spinner.getSelectedItemPosition()).getString("id");
                        Log.d("value id", variant_name + "");

                        List<String> subcatDiloglistList = new ArrayList<>();
                        for (int i = 0; i < spinner.getSelectedItems().size(); i++) {

                            if (eachData.getString("field_type_name").equals("ad_cities")  || eachData.getString("field_type_name").equals("ad_town")){

                                JSONObject obj = (JSONObject) spinner.getSelectedItems().get(i).getObject();
                                Log.d("objecttt",obj.toString());
                                if (spinner.getSelectedItems().get(i).getName()=="All"){

                                }else {
                                    subcatDiloglistList.add(obj.getString("id"));

                                    //    subcatDiloglistList.add("");
                                    Log.d("spinnername", String.valueOf(spinner.getSelectedItems().get(i).getId()));

                                }


                            }else {
                                subcatDiloglistList.add(spinner.getSelectedItems().get(i).getName());
                            }
                            Log.d("spinnerid", String.valueOf(spinner.getSelectedItems().get(i).getName()));
                        }

                        JsonArray userJsonArray = new JsonArray();

                        for (String user : subcatDiloglistList) {
                            userJsonArray.add(new JsonPrimitive(user));
                        }

                        if(eachData.getString("field_type_name").equals("ad_cities")&&eachData.getString("field_type_name").equals("ad_town")){
                            if (eachData.getString("field_type_name").equals("ad_cities")){
                                citiesarray=userJsonArray;

                            }
                            if (eachData.getString("field_type_name").equals("ad_town")){
                                townarray=userJsonArray;

                            }
                        }
                        else if
                        (eachData.getString("field_type_name").equals("ad_cities")){

                            citiesarray=userJsonArray;
                        }  else if( eachData.getString("field_type_name").equals("ad_town") ) {
                            townarray=userJsonArray;

                        } else {
                            optionsObj.add(eachData.getString("field_type_name"),
                                    userJsonArray);
                        }


                        Log.d("userarray12345", eachData.getString("field_type_name") + "    " +
                                userJsonArray);
                    }


//                    if (modelArrayList.size() > 0) {
//                        JsonArray userJsonArray = new JsonArray();
//
//                        for (String user : modelArrayList) {
//                            userJsonArray.add(new JsonPrimitive(user));
//                        }
//
//                        optionsObj.add("modele",
//                                userJsonArray);
//                    }

                    if (eachData.getString("field_type").equals("textfield")) {
                        TextView textView = (TextView) allViewInstanceforCustom.get(noOfViews);
                        if (!textView.getText().toString().equalsIgnoreCase(""))
                            optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                        else
                            optionsObj.addProperty(eachData.getString("field_type_name"), textView.getText().toString());
                        Log.d("variant_name", textView.getText().toString() + "");
                    }

                    if (eachData.getString("field_type").equals("radio")) {
                        RadioGroup radioGroup = (RadioGroup) allViewInstanceforCustom.get(noOfViews);
                        RadioButton selectedRadioBtn = getActivity().findViewById(radioGroup.getCheckedRadioButtonId());
                        if (selectedRadioBtn != null) {
                            Log.d("variant_name", selectedRadioBtn.getTag().toString() + "");
                            optionsObj.addProperty(eachData.getString("field_type_name"),
                                    selectedRadioBtn.getTag().toString());
                        }
                    }
                    if (eachData.getString("field_type").equals("checkbox")) {
                        LinearLayout linearLayout = (LinearLayout) allViewInstanceforCustom.get(noOfViews);
                        Log.d("info if", "checkbox" + linearLayout);
                        JSONArray checkBoxJSONOpt = eachData.getJSONArray("values");
                        String values = "";
                        for (int j = 0; j < checkBoxJSONOpt.length(); j++) {
                            Log.d("info if", "for");
                            CheckBox chk = (CheckBox) linearLayout.getChildAt(j);
                            if (chk.isChecked()) {
                                Log.d("info if", "iffff");
                                values = values.concat("," + chk.getTag());
                            }
                        }
                        optionsObj.addProperty(eachData.getString("field_type_name"), values);
                    }
                    if (eachData.getString("field_type").equals("textfield_date")) {
                        LinearLayout linearLayout = (LinearLayout) allViewInstanceforCustom.get(noOfViews);
                        TextInputLayout textInputLayout = (TextInputLayout) linearLayout.getChildAt(0);
                        TextInputLayout textInputLayout1 = (TextInputLayout) linearLayout.getChildAt(1);
                        if (textInputLayout.getEditText() != null && textInputLayout1.getEditText() != null)
                            optionsObj.addProperty(eachData.getString("field_type_name"), textInputLayout.getEditText().getText().toString() + "|" +
                                    textInputLayout1.getEditText().getText().toString());

                    }
                    if (eachData.getString("field_type").equals("radio_color")) {
                        RadioGroup radioGroup = (RadioGroup) allViewInstanceforCustom.get(noOfViews);
                        RadioButton selectedRadioBtn = getActivity().findViewById(radioGroup.getCheckedRadioButtonId());
                        if (selectedRadioBtn != null) {
                            Log.d("variant_name", selectedRadioBtn.getTag().toString() + "");
                            optionsObj.addProperty(eachData.getString("field_type_name"),
                                    selectedRadioBtn.getTag().toString());
                        }
                    }
                    if (eachData.getString("field_type").equals("number_range")) {
                        LinearLayout linearLayout = (LinearLayout) allViewInstanceforCustom.get(noOfViews);
                        TextInputLayout textInputLayout = (TextInputLayout) linearLayout.getChildAt(0);
                        TextInputLayout textInputLayout1 = (TextInputLayout) linearLayout.getChildAt(1);
                        if (textInputLayout.getEditText() != null && textInputLayout1.getEditText() != null)
                            optionsObj.addProperty(eachData.getString("field_type_name"), textInputLayout.getEditText().getText().toString() + "-" +
                                    textInputLayout1.getEditText().getText().toString());
                    }
                    if (eachData.getString("field_type").equals("range_textfield")) {
                        LinearLayout linearLayout = (LinearLayout) allViewInstanceforCustom.get(noOfViews);

                        TextInputLayout textView = (TextInputLayout) linearLayout.getChildAt(0);
                        TextInputLayout textView2 = (TextInputLayout) linearLayout.getChildAt(1);

                        if (textView.getEditText() != null && textView2.getEditText() != null)
                            optionsObj.addProperty(eachData.getString("field_type_name"), textView.getEditText().getText().toString() + "-" +
                                    textView2.getEditText().getText().toString());
                    }

                    if (scImageOption.isChecked()) {
                        optionsObj.addProperty("ctm_image_toggle", "on");
                    }

                    if (scPriceOption.isChecked()) {
                        optionsObj.addProperty("ctm_image_price", "on");
                    }

                }


                hideSoftKeyboard();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("array us custom", (optionsObj != null ? optionsObj.toString() : null) + " ==== size====  " + allViewInstanceforCustom.size());

        return optionsObj;
    }

    @SuppressLint("ClickableViewAccessibility")
    void adforest_setViewsForCustom(JSONObject jsonObjec) {

        try {
            jsonObjectforCustom = jsonObjec;
            Log.d("Custom_data ===== ", jsonObjectforCustom.toString());
            customOptnList = jsonObjectforCustom.getJSONArray("data");

            for (int noOfCustomOpt = 0; noOfCustomOpt < customOptnList.length(); noOfCustomOpt++) {
                CardView cardView = new CardView(getActivity());
                cardView.setCardElevation(2);
                cardView.setUseCompatPadding(true);
                cardView.setRadius(0);
                cardView.setContentPadding(10, 10, 10, 10);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params1.topMargin = 10;
                params1.bottomMargin = 10;
                cardView.setLayoutParams(params1);

                JSONObject eachData = customOptnList.getJSONObject(noOfCustomOpt);
                TextView customOptionsName = new TextView(getActivity());
                customOptionsName.setTextSize(12);
                customOptionsName.setAllCaps(true);
                customOptionsName.setTextColor(Color.BLACK);
                customOptionsName.setPadding(10, 15, 10, 15);

                customOptionsName.setText(eachData.getString("title"));

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                linearLayout.addView(customOptionsName);


                View view1 = new View(getActivity());
                view1.setBackgroundColor(getResources().getColor(R.color.card_detailing));
                LinearLayout.LayoutParams paramsview = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
                paramsview.topMargin = 10;
                paramsview.bottomMargin = 10;
                view1.setLayoutParams(paramsview);
                linearLayout.addView(view1);

                if (eachData.getString("field_type").equals("select")) {

                    final JSONArray dropDownJSONOpt = eachData.getJSONArray("values");

                    if (eachData.getString("field_type_name").equals("modele")) {
                        modelArray = eachData.getJSONArray("values");
                    }

                    if (eachData.getString("field_type_name").equals("ad_town")) {
                        townArray = eachData.getJSONArray("values");
                    }


                    final List<KeyPairBoolData> listArray1 = new ArrayList<>();
                    final ArrayList<subcatDiloglist> SpinnerOptions;
                    SpinnerOptions = new ArrayList<>();
                    for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                        subcatDiloglist subDiloglist = new subcatDiloglist();
                        subDiloglist.setId(dropDownJSONOpt.getJSONObject(j).getString("id"));
                        subDiloglist.setName(dropDownJSONOpt.getJSONObject(j).getString("name"));
                        subDiloglist.setHasSub(dropDownJSONOpt.getJSONObject(j).getBoolean("has_sub"));
                        //String optionString = dropDownJSONOpt.getJSONObject(j).getString("name");
                        KeyPairBoolData h = new KeyPairBoolData();

                        h.setObject(dropDownJSONOpt.getJSONObject(j));



                            h.setId(j + 1);
                        h.setName(dropDownJSONOpt.getJSONObject(j).getString("name"));
                        h.setSelected(false);
                        if (dropDownJSONOpt.getJSONObject(j).getString("name")!="All"){
                        listArray1.add(h);

                        SpinnerOptions.add(subDiloglist);}
                    }
                    final SpinnerAndListAdapter spinnerAndListAdapter;

                    spinnerAndListAdapter = new SpinnerAndListAdapter(getActivity(), SpinnerOptions);

                    final MultiSpinnerSearch spinner = new MultiSpinnerSearch(getActivity());
                    spinner.setSearchEnabled(true);
                    spinner.setClearText(jsonObject.optJSONObject("extra").optString("clear_all"));
                    spinner.setSearchHint(jsonObject.optJSONObject("extra").optString("type_to_search"));
                    spinner.setShowSelectAllButton(false);
                    spinner.setEmptyTitle(getString(R.string.no_result_found));
                    //allViewInstanceforCustom.add(spinner);
                     // spinner.setAdapter(spinnerAndListAdapter);
                    List<subcatDiloglist> subcatDiloglistList = new ArrayList<>();
                    spinner.setItems(listArray1, new MultiSpinnerListener() {

                        @Override
                        public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                          //   Log.e("selectedItems", selectedItems.get(0).getName().toString());
                            if (eachData.optString("main_title").matches("Marque")) {
                                if (selectedItems.size() == 1) {

                                    ArrayList<KeyPairBoolData> keyPairBoolDataArrayList = new ArrayList<>();
                                    for (int i = 0; i < modelArray.length(); i++) {
                                        JSONObject jsonObject = modelArray.optJSONObject(i);
                                        Log.d("marqueobj",jsonObject.toString());
                                        if (jsonObject.optString("id").matches(selectedItems.get(0).getName().toString())) {
                                            KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                                            keyPairBoolData.setId(i + 1);
                                            keyPairBoolData.setName(jsonObject.optString("name"));
                                            keyPairBoolDataArrayList.add(keyPairBoolData);
                                        }
                                    }
                                    if (keyPairBoolDataArrayList.size() > 0) {
                                        CardView cardView1 = (CardView) linearLayoutCustom.getChildAt(4);
                                        cardView1.setVisibility(View.VISIBLE);
                                        MultiSpinnerSearch spinner = (MultiSpinnerSearch) allViewInstanceforCustom.get(4);
                                        spinner.setClearText(jsonObject.optJSONObject("extra").optString("clear_all"));
                                        spinner.setSearchHint(jsonObject.optJSONObject("extra").optString("type_to_search"));
                                        spinner.setSearchEnabled(true);
                                        spinner.setImageLeft(true);

                                        spinner.setShowSelectAllButton(false);
                                        spinner.setEmptyTitle(getString(R.string.no_result_found));
                                        spinner.setItems(keyPairBoolDataArrayList, new MultiSpinnerListener() {
                                            @Override
                                            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {

                                            }
                                        });
                                    } else {
                                        CardView cardView1 = (CardView) linearLayoutCustom.getChildAt(4);
                                        cardView1.setVisibility(View.GONE);
                                    }

                                } else {
                                    CardView cardView1 = (CardView) linearLayoutCustom.getChildAt(4);
                                    cardView1.setVisibility(View.GONE);
                                }
                            } else if (eachData.optString("field_type_name").matches("ad_cities")) {
                                if (selectedItems.size() == 1) {

                                    ArrayList<KeyPairBoolData> keyPairBoolDataArrayList = new ArrayList<>();
                                    for (int i = 0; i < townArray.length(); i++) {
                                        JSONObject jsonObject = townArray.optJSONObject(i);
                                        if (jsonObject.optString("city_id").matches(selectedItems.get(0).getName().toString())) {
                                            KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                                            keyPairBoolData.setId(i + 1);
                                            keyPairBoolData.setName(jsonObject.optString("name"));
                                            keyPairBoolData.setObject(jsonObject);
                                            keyPairBoolDataArrayList.add(keyPairBoolData);
                                        }
                                    }
                                    if (keyPairBoolDataArrayList.size() > 0) {
                                        CardView cardView1 = (CardView) linearLayoutCustom.getChildAt(1);
                                        cardView1.setVisibility(View.VISIBLE);
                                        MultiSpinnerSearch spinner = (MultiSpinnerSearch) allViewInstanceforCustom.get(1);
                                        spinner.setClearText(jsonObject.optJSONObject("extra").optString("clear_all"));
                                        spinner.setSearchHint(jsonObject.optJSONObject("extra").optString("type_to_search"));
                                        spinner.setSearchEnabled(true);
                                        spinner.setShowSelectAllButton(false);
                                        spinner.setEmptyTitle(getString(R.string.no_result_found));
                                        spinner.setItems(keyPairBoolDataArrayList, new MultiSpinnerListener() {
                                            @Override
                                            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {


                                            }
                                        });

                                    } else {
                                        CardView cardView1 = (CardView) linearLayoutCustom.getChildAt(1);
                                        cardView1.setVisibility(View.GONE);
                                    }

                                } else {
                                    CardView cardView1 = (CardView) linearLayoutCustom.getChildAt(1);
                                    cardView1.setVisibility(View.GONE);
                                }
                            }

                        }
                    });

                    spinner.setSelection(0, false);

                    spinner.setOnTouchListener((v, event) -> {
                        spinnerTouched = true;
                        return false;
                    });

//                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                            if (spinnerTouched) {
//                                //String variant_name = dropDownJSONOpt.getJSONObject(position).getString("name");
//                                if (position != 0) {
//                                    final subcatDiloglist subcatDiloglistitem = (subcatDiloglist) selectedItemView.getTag();
//
////                                    Toast.makeText(getActivity(), subcatDiloglistitem.getName(), Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parentView) {
//                        }
//
//                    });

                    allViewInstanceforCustom.add(spinner);

                    linearLayout.addView(spinner);

                    cardView.addView(linearLayout);

                    if (eachData.getString("field_type_name").equals("modele")) {
                        cardView.setVisibility(View.GONE);
                    } else if (eachData.getString("field_type_name").equals("ad_town")) {
                        cardView.setVisibility(View.GONE);
                    }

                    linearLayoutCustom.addView(cardView);
                }
                if (eachData.getString("field_type").equals("textfield")) {
                    TextInputLayout til = new TextInputLayout(getActivity());
                    til.setHint(eachData.getString("title"));
                    EditText et = new EditText(getActivity());
                    til.addView(et);
                    allViewInstanceforCustom.add(et);
                    linearLayoutCustom.addView(til);
                }

                if (eachData.getString("field_type").equals("radio")) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 3;
                    params.bottomMargin = 3;

                    final JSONArray radioButtonJSONOpt = eachData.getJSONArray("values");
                    RadioGroup rg = new RadioGroup(getActivity()); //create the RadioGroup
                    for (int j = 0; j < radioButtonJSONOpt.length(); j++) {

                        RadioButton rb = new RadioButton(getActivity());

                        if (j == 0)
//                            rb.setChecked(true);
                            rb.setLayoutParams(params);
                        rb.setTag(radioButtonJSONOpt.getJSONObject(j).getString("id"));
                        rb.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        rb.setPadding(10, 10, 10, 10);
                        String optionString = radioButtonJSONOpt.getJSONObject(j).getString("name");
                        rb.setText(optionString);
                        rg.setOnCheckedChangeListener((group, checkedId) -> {
                            View radioButton = group.findViewById(checkedId);
                            String variant_name = radioButton.getTag().toString();
//                                Toast.makeText(getActivity(), variant_name + "", Toast.LENGTH_LONG).show();
                        });

                        rg.addView(rb, params);

                    }
                    allViewInstanceforCustom.add(rg);
                    linearLayout.addView(rg, params);
                    linearLayoutCustom.addView(linearLayout);
                }
                if (eachData.getString("field_type").equals("checkbox")) {
                    Log.d("info add", noOfCustomOpt + "");
                    LinearLayout linearLayout1 = new LinearLayout(getActivity());
                    linearLayout1.setOrientation(LinearLayout.VERTICAL);

                    JSONArray checkBoxJSONOpt = eachData.getJSONArray("values");

                    for (int j = 0; j < checkBoxJSONOpt.length(); j++) {

                        CheckBox checkBox = new CheckBox(getContext());
                        checkBox.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        checkBox.setTag(checkBoxJSONOpt.getJSONObject(j).getString("id"));
                        checkBox.setFocusable(true);
                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params2.topMargin = 3;
                        params2.bottomMargin = 3;
                        String optionString = checkBoxJSONOpt.getJSONObject(j).getString("name");
                        checkBox.setText(optionString);
                        linearLayout1.addView(checkBox, params2);
                    }

                    allViewInstanceforCustom.add(linearLayout1);
                    linearLayout.addView(linearLayout1);
                    cardView.addView(linearLayout);
                    linearLayoutCustom.addView(cardView);
                }
                if (eachData.getString("field_type").equals("textfield_date")) {
                    LinearLayout linearLayoutdate = new LinearLayout(getActivity());
                    linearLayoutdate.setOrientation(LinearLayout.HORIZONTAL);
                    TextInputLayout til1 = new TextInputLayout(getActivity());
                    TextInputLayout till = new TextInputLayout(getActivity());
                    LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params3.weight = 1;
                    til1.setHint(eachData.getString("title"));
                    till.setHint(eachData.getString("title"));
                    EditText et = new EditText(getActivity());
                    EditText et2 = new EditText(getActivity());
                    til1.setLayoutParams(params3);
                    till.setLayoutParams(params3);
                    et.setTextSize(14);
                    et2.setTextSize(14);
                    et2.setFocusable(true);
                    et.setFocusable(true);
                    Drawable img = getResources().getDrawable(R.drawable.ic_calendar);
                    et.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, img, null);
                    et2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, img, null);
                    til1.addView(et);
                    till.addView(et2);

                    linearLayoutdate.addView(til1);
                    linearLayoutdate.addView(till);
                    cardView.setContentPadding(10, 20, 10, 20);
                    et.setClickable(false);
                    et.setFocusable(false);
                    et2.setClickable(false);
                    et2.setFocusable(false);

                    final EditText editText = et;
                    final EditText editText1 = et2;
                    editText1.setOnClickListener(view -> adforest_showDate(editText1));
                    editText.setOnClickListener(v -> adforest_showDate(editText));

                    linearLayout.addView(cardView);
                    allViewInstanceforCustom.add(linearLayoutdate);
                    cardView.addView(linearLayoutdate);
                    linearLayoutCustom.addView(linearLayout);
                }
                if (eachData.getString("field_type").equals("radio_color")) {
                    HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 3;
                    params.bottomMargin = 3;
                    horizontalScrollView.setLayoutParams(params);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.topMargin = 3;
                    layoutParams.bottomMargin = 3;
                    layoutParams.setMarginEnd(5);

                    final JSONArray radioButtonJSONOpt = eachData.getJSONArray("values");
                    RadioGroup rg = new RadioGroup(getActivity()); //create the RadioGroup
                    rg.setOrientation(LinearLayout.HORIZONTAL);
                    for (int j = 0; j < radioButtonJSONOpt.length(); j++) {

                        RadioButton rb = new RadioButton(getActivity());
                        rg.addView(rb, layoutParams);
//                        rb.setChecked(true);
                        rb.setLayoutParams(layoutParams);
//                        rb.setHint(radioButtonJSONOpt.getJSONObject(j).getString("title"));

                        rb.setTag(radioButtonJSONOpt.getJSONObject(j).getString("id"));
                        rb.setBackgroundColor(Color.parseColor("#FFFFFF"));

                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{
                                        new int[]{-android.R.attr.state_enabled}, //disabled
                                        new int[]{android.R.attr.state_enabled} //enabled
                                },
                                new int[]{
                                        Color.BLACK //disabled
                                        , Color.parseColor(radioButtonJSONOpt.getJSONObject(j).getString("id")) //enabled
                                }
                        );


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            rb.setButtonTintList(colorStateList);//set the color tint list
                        }

                        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                View radioButton = group.findViewById(checkedId);
//                                String variant_name = radioButton.getTag().toString();
//                                Toast.makeText(getActivity(), variant_name + "", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    allViewInstanceforCustom.add(rg);
                    horizontalScrollView.addView(rg);
                    linearLayout.addView(horizontalScrollView, params);
                    linearLayoutCustom.addView(linearLayout);
                }
                if (eachData.getString("field_type").equals("number_range")) {
                    // get seekbar from view
                    final CrystalRangeSeekbar rangeSeekbar = new CrystalRangeSeekbar(getActivity());
                    rangeSeekbar.setMinValue(Float.valueOf(eachData.getJSONObject("values").getString("min_val")));
                    rangeSeekbar.setMaxValue(Float.valueOf(eachData.getJSONObject("values").getString("max_val")));

                    rangeSeekbar.setBarColor(Color.parseColor("#eeeeee")).apply();
                    rangeSeekbar.setBarHighlightColor(Color.parseColor(SettingsMain.getMainColor()));
                    rangeSeekbar.setLeftThumbColor(Color.parseColor(SettingsMain.getMainColor())).apply();
                    rangeSeekbar.setRightThumbColor(Color.parseColor(SettingsMain.getMainColor())).apply();
                    rangeSeekbar.setRightThumbHighlightColor(Color.parseColor(SettingsMain.getMainColor()));
                    rangeSeekbar.setLeftThumbHighlightColor(Color.parseColor(SettingsMain.getMainColor()));
                    rangeSeekbar.setGap(Float.valueOf(eachData.getJSONObject("values").getString("steps")));
                    rangeSeekbar.setCornerRadius(5);
                    rangeSeekbar.apply();
                    LinearLayout linearLayoutHori = new LinearLayout(getActivity());
                    linearLayoutHori.setOrientation(LinearLayout.HORIZONTAL);
                    TextInputLayout til = new TextInputLayout(getActivity());
                    TextInputLayout til2 = new TextInputLayout(getActivity());

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params2.weight = 1;
                    final EditText et = new EditText(getActivity());
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et.setText(eachData.getJSONObject("values").getString("min_val"));
                    final EditText et2 = new EditText(getActivity());
                    et2.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et2.setText(eachData.getJSONObject("values").getString("max_val"));
                    til.addView(et);
                    til2.addView(et2);

                    til.setLayoutParams(params2);
                    til2.setLayoutParams(params2);
                    linearLayoutHori.addView(til);
                    linearLayoutHori.addView(til2);
//                    rangeSeekbar.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
//                        et.setText(String.valueOf(minValue));
//                        et2.setText(String.valueOf(maxValue));
//                    });
                    // rangeSeekbar.setOnRangeSeekbarFinalValueListener((minValue, maxValue) -> Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue)));
                    allViewInstanceforCustom.add(linearLayoutHori);
                    rangeSeekbar.setVisibility(View.GONE);
                    linearLayout.addView(rangeSeekbar);
                    linearLayout.addView(linearLayoutHori);
                    cardView.addView(linearLayout);
                    linearLayoutCustom.addView(cardView);
                }
                if (eachData.getString("field_type").equals("range_textfield")) {
                    LinearLayout linearLayout1 = new LinearLayout(getActivity());
                    linearLayout1.setOrientation(LinearLayout.HORIZONTAL);

                    TextInputLayout til = new TextInputLayout(getActivity());
                    TextInputLayout til2 = new TextInputLayout(getActivity());

                    til.setHint(eachData.getJSONArray("data").getJSONObject(0).getString("title"));
                    til2.setHint(eachData.getJSONArray("data").getJSONObject(1).getString("title"));

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params2.weight = 1;

                    EditText et = new EditText(getActivity());
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    EditText et2 = new EditText(getActivity());
                    et2.setInputType(InputType.TYPE_CLASS_NUMBER);
                    til.addView(et);
                    til2.addView(et2);

                    til.setLayoutParams(params2);
                    til2.setLayoutParams(params2);
                    linearLayout1.addView(til);
                    linearLayout1.addView(til2);

                    linearLayout.addView(linearLayout1);
                    allViewInstanceforCustom.add(linearLayout1);
                    cardView.addView(linearLayout);
                    linearLayoutCustom.addView(cardView);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void adforest_showDate(final EditText editText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, java.util.Locale.getDefault());
                if (editText != null)
                    editText.setText(sdf.format(myCalendar.getTime()));
            }
        }, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }


//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Toast.makeText(getActivity(),
//                "Google Places API connection failed with error code:" +
//                        connectionResult.getErrorCode(),
//                Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//    }

    @Override
    public void onStop() {
        super.onStop();

        // adforest_showFiler();
    }

    @Override
    public void onStart() {
        super.onStart();
        // onResumegetcategory();

        //  adforest_showFiler();
    }

    @Override
    public void onResume() {
        super.onResume();
        //   adforest_showFiler();

        if (settingsMain.getcateryfound()) {
            Log.d("categoryfound123", SettingsMain.getcateryfound().toString());
            subcatDiloglist item = settingsMain.getcategry();

            et5.setText(item.name);
            settingsMain.setcateryfound(false);


            if (item.isHasCustom()) {
                isCategorysearch = true;
                search_id = item.getId();
                linearLayoutCustom.removeAllViews();
                allViewInstanceforCustom.clear();
                catID = item.getId();
                adforest_showCustom();
                ison = true;
                Log.d("true===== ", "add All");


            } else {
                if (ison) {
                    linearLayoutCustom.removeAllViews();
                    allViewInstanceforCustom.clear();
                    ison = false;
                    Log.d("true===== ", "remove All");

                }
            }
        }
        Log.d("categoryfound", SettingsMain.getcateryfound().toString());

        //  onResumegetcategory();
    }

    @Override
    public void onPause() {
        super.onPause();
        // adforest_showFiler();
        // onResumegetcategory();

    }

    private void onResumegetcategory(subcatDiloglist list) {
        try {
            Boolean catfound;
            catfound = SettingsMain.getcateryfound();

            //  subcatDiloglist list = SettingsMain.getcategry(getActivity());
            if (list.isHasCustom()) {
                linearLayoutCustom.removeAllViews();
                allViewInstanceforCustom.clear();
                catID = list.getId();
                adforest_showCustom();
                ison = true;
                Log.d("true===== ", "add All");


            } else {
                if (ison) {
                    linearLayoutCustom.removeAllViews();
                    allViewInstanceforCustom.clear();
                    ison = false;
                    Log.d("true===== ", "remove All");

                }
            }


        } catch (Exception e) {

        }
    }

    public void replaceFragment(Fragment someFragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out, R.anim.left_enter, R.anim.right_out);
        transaction.replace(R.id.frameContainer1, someFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();

        featureAdsList.clear();
        searchedAdList.clear();

        nextPage = 1;
        currentPage = 1;
        totalPage = 0;

        allViewInstance.clear();
    }

    public void replaceFragment2(Fragment someFragment, String tag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_out, R.anim.left_enter, R.anim.right_out);
        transaction.add(R.id.frameContainer1, someFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();


    }

    void adforest_showFiler() {

        if (MyRecyclerView.getAdapter() != null && MyRecyclerView.getAdapter().getItemCount() >= 0) {
            linearLayoutFilter.setVisibility(View.VISIBLE);
            if (MyRecyclerView.getAdapter().getItemCount() == 0) {
                relativeLayoutSpiner.setVisibility(View.GONE);
            } else
                relativeLayoutSpiner.setVisibility(View.VISIBLE);

        } else
            linearLayoutFilter.setVisibility(View.GONE);
    }

    void adforest_addToFavourite(String Id) {

        if (SettingsMain.isConnectingToInternet(getActivity())) {

            SettingsMain.showDilog(getActivity());

            JsonObject params = new JsonObject();
            params.addProperty("ad_id", Id);
            Log.d("info sendFavourite", Id);
            Call<ResponseBody> myCall = restService.postAddToFavourite(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info AdToFav Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        SettingsMain.hideDilog();
                    } catch (JSONException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                    }
                    SettingsMain.hideDilog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        settingsMain.hideDilog();
                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        settingsMain.hideDilog();
                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info AdToFav ", "NullPointert Exception" + t.getLocalizedMessage());
                        settingsMain.hideDilog();
                    } else {
                        SettingsMain.hideDilog();
                        Log.d("info AdToFav error", String.valueOf(t));
                        Log.d("info AdToFav error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            SettingsMain.hideDilog();
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    public void adforest_adSearchLoc() {
        double lat = 0, lon = 0;
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        if (!gpsTracker.canGetLocation())
            gpsTracker.showSettingsAlert();
        else {
            Geocoder geocoder;
            List<Address> addresses1 = null;
            try {
                addresses1 = new Geocoder(getActivity(), Locale.getDefault()).getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder result = new StringBuilder();
            if (addresses1 != null)
                if (addresses1.size() > 0) {
                    Address address = addresses1.get(0);
                    int maxIndex = address.getMaxAddressLineIndex();
                    for (int x = 0; x <= maxIndex; x++) {
                        result.append(address.getAddressLine(x));
                        lat = address.getLatitude();
                        lon = address.getLongitude();
                        //result.append(",");
                    }
                }
            try {
                addressbyGeoCode = result.toString();
                latitude = String.valueOf(lat);
                latitude = String.valueOf(lon);
                Log.d("addressbyGeoCode", addressbyGeoCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void adforest_recylerview_autoScroll(final int duration, final int pixelsToMove, final int delayMillis,
                                                final RecyclerView recyclerView, final GridLayoutManager gridLayoutManager
            , final ItemSearchFeatureAdsAdapter adapter) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;

            @Override
            public void run() {
                try {
                    if (count < adapter.getItemCount()) {
                        if (count == adapter.getItemCount() - 1) {
                            flag = false;
                        } else if (count == 0) {
                            flag = true;
                        }
                        if (flag) count++;
                        else count--;

                        recyclerView.smoothScrollToPosition(count);
                        handler.postDelayed(this, duration);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

            }
        };

        handler.postDelayed(runnable, delayMillis);
    }

    /* @Override
     public void onItemClick(subcatDiloglist item) {
         if (secondcat==false){
         if (!item.id.equals("")) {
           //  mainRelative.setVisibility(View.VISIBLE);
             secondcat =true;
             getsubcategory(item.id);

         }}
         else {
             secondcat=false;
         et5.setText(item.name);
         iscategory(false);
             catchange(item);

         }



     }*/
    void getsubcategory(String id) {


        if (SettingsMain.isConnectingToInternet(getActivity())) {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            loadingLayout.setVisibility(View.VISIBLE);
            //for serlecting the Categoreis if Categoreis have SubCategoreis
            try {


                JsonObject params = new JsonObject();
                params.addProperty("subcat", id);

                Log.d("info sendSearch SubCats", "" + params.toString());

                Call<ResponseBody> myCall = restService.postGetSearcSubCats(params, UrlController.AddHeaders(getActivity()));
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
                                    Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLayout.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLayout.setVisibility(View.GONE);


                            e.printStackTrace();
                        } catch (IOException e) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            loadingLayout.setVisibility(View.GONE);


                            e.printStackTrace();
                        }
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);


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

            //for serlecting the location if location have sabLocations
              /*  try {
                    if (eachData.getString("field_type_name").equals("ad_country")) {

                        JsonObject params1 = new JsonObject();
                        params1.addProperty("ad_country", subcatDiloglistitem.getId());
                        Log.d("info sendSearch Loctn", params1.toString() + eachData.getString("field_type_name"));

                        Call<ResponseBody> myCall = restService.postGetSearcSubLocation(params1, UrlController.AddHeaders(getActivity()));
                        myCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                                try {
                                    if (responseObj.isSuccessful()) {
                                        Log.d("info SubSearch Resp", "" + responseObj.toString());

                                        JSONObject response = new JSONObject(responseObj.body().string());
                                        if (response.getBoolean("success")) {
                                            Log.d("info SearchLctn object", "" + response.getJSONObject("data"));

                                            adforest_ShowDialog(response.getJSONObject("data"), subcatDiloglistitem, SpinnerOptions
                                                    , spinnerAndListAdapter, spinner, eachData.getString("field_type_name"));

                                        } else {
                                            Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLayout.setVisibility(View.GONE);
                                    searchBtn.setVisibility(View.VISIBLE);


                                } catch (JSONException e) {
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLayout.setVisibility(View.GONE);
                                    searchBtn.setVisibility(View.VISIBLE);


                                    e.printStackTrace();
                                } catch (IOException e) {
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLayout.setVisibility(View.GONE);
                                    searchBtn.setVisibility(View.VISIBLE);


                                    e.printStackTrace();
                                }
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                loadingLayout.setVisibility(View.GONE);
                                searchBtn.setVisibility(View.VISIBLE);


                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                if (t instanceof TimeoutException) {
                                    Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLayout.setVisibility(View.GONE);
                                    searchBtn.setVisibility(View.VISIBLE);


                                }
                                if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                                    Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLayout.setVisibility(View.GONE);
                                    searchBtn.setVisibility(View.VISIBLE);


                                }
                                if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                                    Log.d("info SearchLctn ", "NullPointert Exception" + t.getLocalizedMessage());
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLayout.setVisibility(View.GONE);
                                    searchBtn.setVisibility(View.VISIBLE);


                                } else {
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                    loadingLayout.setVisibility(View.GONE);
                                    searchBtn.setVisibility(View.VISIBLE);


                                    Log.d("info SearchLctn error", String.valueOf(t));
                                    Log.d("info SearchLctn error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                                }
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
        } else {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);


            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }

    }

    private void getArraylist(JSONArray jsonArray) {
        ArrayList<subcatDiloglist> subcategorylist = new ArrayList();
        subcategorylist.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                subcatDiloglist catDiloglist = new subcatDiloglist();
                catDiloglist.setId(jsonArray.getJSONObject(i).getString("id"));
                catDiloglist.setName(jsonArray.getJSONObject(i).getString("name"));
                catDiloglist.setHasSub(jsonArray.getJSONObject(i).getBoolean("has_sub"));

                subcategorylist.add(catDiloglist);

            }

            categoryAdapter.ItemCategoryAdapter(getActivity(), subcategorylist);
            recycler_view_category.setAdapter(categoryAdapter);
            recycler_view_category.setVisibility(View.VISIBLE);
        } catch (JSONException je) {
            Log.d("errorjson1234", je.toString());

        }

    }

    void catchange(subcatDiloglist item) {

        try {

            Log.d("true===== ", "in Main ====  " + item.isHasCustom());

            // if (eachData.getBoolean("has_cat_template"))
            if (item.isHasCustom()) {
                linearLayoutCustom.removeAllViews();
                allViewInstanceforCustom.clear();
                catID = item.getId();
                adforest_showCustom();
                ison = true;
                Log.d("true===== ", "add All");


            } else {
                if (ison) {
                    linearLayoutCustom.removeAllViews();
                    allViewInstanceforCustom.clear();
                    ison = false;
                    Log.d("true===== ", "remove All");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private void adforest_GetCity() {
//        if (SettingsMain.isConnectingToInternet(getActivity())) {
//
//            Call<ResponseBody> myCall = restService.postGetCity(UrlController.AddHeaders(getActivity()));
//            myCall.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
//                    try {
//                        if (responseObj.isSuccessful()) {
//                            Log.d("info postGetCity", "" + responseObj.toString());
//
//                            JSONObject response = new JSONObject(responseObj.body().string());
//                            if (response.getBoolean("success")) {
//                                Log.d("info postGetCity", "" + response.optJSONObject("data"));
//                                JSONObject data = response.optJSONObject("data");
//
//                                ArrayList<CityModel> cityArray = new ArrayList<>();
//
//                                JSONArray all = data.optJSONArray("all");
//                                //Iterating JSON array
//                                for (int i = 0; i < all.length(); i++) {
//                                    //Adding each element of JSON array into ArrayList
//                                    JSONObject jsonObject = all.getJSONObject(i);
//                                    CityModel cityModel = new CityModel();
//                                    cityModel.setCity_id(jsonObject.getString("city_id"));
//                                    cityModel.setName(jsonObject.getString("name"));
//                                    cityArray.add(cityModel);
//                                }
//
//                                JSONArray popular_citys = data.optJSONArray("popular_citys");
//                                //Iterating JSON array
//                                for (int i = 0; i < popular_citys.length(); i++) {
//                                    //Adding each element of JSON array into ArrayList
//                                    JSONObject jsonObject = popular_citys.getJSONObject(i);
//                                    CityModel cityModel = new CityModel();
//                                    cityModel.setCity_id(jsonObject.getString("city_id"));
//                                    cityModel.setName(jsonObject.getString("name"));
//                                    cityArray.add(cityModel);
//                                }
//
//                                JSONArray other_citys = data.optJSONArray("other_citys");
//                                //Iterating JSON array
//                                for (int i = 0; i < other_citys.length(); i++) {
//                                    JSONObject jsonObject = other_citys.getJSONObject(i);
//                                    CityModel cityModel = new CityModel();
//                                    cityModel.setCity_id(jsonObject.getString("city_id"));
//                                    cityModel.setName(jsonObject.getString("name"));
//                                    cityArray.add(cityModel);
//                                }
//
//                                List<KeyPairBoolData> listArray1 = new ArrayList<>();
//                                if (cityArray != null) {
//                                    for (int i = 0; i < cityArray.size(); i++) {
//                                        KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
//                                        keyPairBoolData.setId(i + 1);
//                                        keyPairBoolData.setName(cityArray.get(i).getName());
//                                        listArray1.add(keyPairBoolData);
//                                    }
//                                }
//                                cvCity.setVisibility(View.VISIBLE);
//
//                                spinnerCity.setSearchEnabled(true);
//                                spinnerCity.setSearchHint(getResources().getString(R.string.type_to_search));
//                                spinnerCity.setEmptyTitle(getString(R.string.no_result_found));
//                                spinnerCity.setShowSelectAllButton(true);
//
//                                spinnerCity.setItems(listArray1, new MultiSpinnerListener() {
//
//                                    @Override
//                                    public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
//                                        //Log.e("selectedItems", selectedItems.get(0).getName().toString());
//                                        cvTown.setVisibility(View.GONE);
//
//                                        ArrayList<String> cityNames = new ArrayList<>();
//                                        for (int i = 0; i < selectedItems.size(); i++) {
//                                            cityNames.add(selectedItems.get(i).getName());
//                                        }
//
//                                        cityArrayListSelected = new ArrayList<>();
//                                        for (int k = 0; k < cityArray.size(); k++) {
//                                            if (cityNames.contains(cityArray.get(k).getName())) {
//                                                cityArrayListSelected.add(cityArray.get(k).getCity_id());
//                                            }
//                                        }
//
//                                        if (cityNames.size() == 1) {
//
//                                            JsonObject params = new JsonObject();
//                                            for (int j = 0; j < cityArray.size(); j++) {
//                                                if (cityNames.get(0).matches(cityArray.get(j).getName())) {
//                                                    params.addProperty("city_id", cityArray.get(j).getCity_id());
//                                                }
//                                            }
//
//                                            adforest_GetTown(params);
//                                        }
//                                    }
//                                });
//
//                            } else {
//                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    if (t instanceof TimeoutException) {
//                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//                    }
//                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {
//
//                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//                    }
//                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
//                        Log.d("info SearchData ", "NullPointert Exception" + t.getLocalizedMessage());
//                    } else {
//                        Log.d("info SearchData err", String.valueOf(t));
//                        Log.d("info SearchData err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
//                    }
//                }
//            });
//        } else {
//            Toast.makeText(getContext(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void adforest_GetTown(JsonObject jsonObject) {
//        if (SettingsMain.isConnectingToInternet(getActivity())) {
//
//            Call<ResponseBody> myCall = restService.postGetTown(jsonObject, UrlController.AddHeaders(getActivity()));
//            myCall.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
//                    try {
//                        if (responseObj.isSuccessful()) {
//                            Log.d("info postGetTown", "" + responseObj.toString());
//
//                            JSONObject response = new JSONObject(responseObj.body().string());
//                            if (response.getBoolean("success")) {
//                                Log.d("info postGetTown", "" + response.optJSONArray("data"));
//                                JSONArray data = response.optJSONArray("data");
//
//                                if (data != null) {
//                                    ArrayList<TownModel> townModelArrayList = new ArrayList<>();
//
//                                    for (int p = 0; p < data.length(); p++) {
//                                        JSONObject jsonObject1 = data.optJSONObject(p);
//                                        TownModel townModel = new TownModel();
//                                        townModel.setTown_id(jsonObject1.optString("town_id"));
//                                        townModel.setName(jsonObject1.optString("name"));
//                                        townModelArrayList.add(townModel);
//                                    }
//
//
//                                    ArrayList<KeyPairBoolData> keyPairBoolDataArrayList = new ArrayList<>();
//                                    for (int i = 0; i < townModelArrayList.size(); i++) {
//                                        KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
//                                        keyPairBoolData.setId(i + 1);
//                                        keyPairBoolData.setName(townModelArrayList.get(i).getName());
//                                        keyPairBoolDataArrayList.add(keyPairBoolData);
//                                    }
//
//                                    cvTown.setVisibility(View.VISIBLE);
//                                    spinnerTown.setSearchEnabled(true);
//                                    spinnerTown.setSearchHint(getResources().getString(R.string.type_to_search));
//                                    spinnerTown.setEmptyTitle(getString(R.string.no_result_found));
//                                    spinnerTown.setShowSelectAllButton(true);
//
//                                    spinnerTown.setItems(keyPairBoolDataArrayList, new MultiSpinnerListener() {
//
//                                        @Override
//                                        public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
//                                            //Log.e("selectedItems", selectedItems.get(0).getName().toString());
//
//                                            ArrayList<String> townNames = new ArrayList<>();
//                                            for (int i = 0; i < selectedItems.size(); i++) {
//                                                townNames.add(selectedItems.get(i).getName());
//                                            }
//
//                                            townArrayListSelected = new ArrayList<>();
//                                            for (int k = 0; k < townNames.size(); k++) {
//                                                if (townNames.contains(townModelArrayList.get(k).getName())) {
//                                                    townArrayListSelected.add(townModelArrayList.get(k).getTown_id());
//                                                }
//                                            }
//                                        }
//                                    });
//                                }
//
//                            } else {
//                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    if (t instanceof TimeoutException) {
//                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//                    }
//                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {
//
//                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//                    }
//                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
//                        Log.d("info SearchData ", "NullPointert Exception" + t.getLocalizedMessage());
//                    } else {
//                        Log.d("info SearchData err", String.valueOf(t));
//                        Log.d("info SearchData err", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
//                    }
//                }
//            });
//        } else {
//            Toast.makeText(getContext(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//        }
//    }
}
