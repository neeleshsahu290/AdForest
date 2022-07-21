package com.scriptsbundle.adforest.messages;

import static android.view.View.GONE;
import static com.scriptsbundle.adforest.utills.SettingsMain.getMainColor;
import static com.scriptsbundle.adforest.utills.SettingsMain.savePopupSettings;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myloadingbutton.MyLoadingButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.scriptsbundle.adforest.Notification.Config;
import com.scriptsbundle.adforest.R;
import com.scriptsbundle.adforest.ad_detail.Ad_detail_activity;
import com.scriptsbundle.adforest.home.HomeActivity;
import com.scriptsbundle.adforest.messages.adapter.ChatAdapter;
import com.scriptsbundle.adforest.modelsList.ChatMessage;
import com.scriptsbundle.adforest.utills.AnalyticsTrackers;
import com.scriptsbundle.adforest.utills.Network.RestService;
import com.scriptsbundle.adforest.utills.SettingsMain;
import com.scriptsbundle.adforest.utills.UrlController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment implements View.OnClickListener, ChatBottomSheet.ChatInterface {

    ArrayList<ChatMessage> chatlist;
    ChatAdapter chatAdapter;
    ListView msgListView;
    int nextPage = 1;
    boolean hasNextPage = false;
    String adId, senderId, recieverId, type, is_Block;
    SettingsMain settingsMain;
    TextView adName, adPrice, adDate, tv_typing;
    Button block_button;
    SwipeRefreshLayout swipeRefreshLayout;
    RestService restService;
    String userName;
    String typingText;
    private Boolean is_blocked_by_another_user, new_para;
    TextView tv_chatTtile, tv_online;
    //    Boolean is_BlockBool;
    private EditText msg_edittext;
    TextView BlockedTextMessage;
    LinearLayout MessageContainer;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ImageButton sendButton;
    boolean calledFromSplash;
    MyLoadingButton myLoadingButton;
    Boolean isBlockSer;
    ImageView pickerView;
    ProgressDialog dialog;
    BroadcastReceiver receiver;
    public static long downloadId = -1;
    AttachmentModel attachmentModel;
    ProgressBar progressBar;
    Boolean sentmsg = false;
    int blocktype;


    public ChatFragment() {

    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_layout, container, false);


        settingsMain = new SettingsMain(getActivity());
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Uploading...");
        dialog.setMessage("Files are uploading");
        progressBar = view.findViewById(R.id.attachmentProgress);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            adId = bundle.getString("adId", "0");
            senderId = bundle.getString("senderId", "0");
            recieverId = bundle.getString("recieverId", "0");
            userName = bundle.getString("name", " ");
            Log.d("bundleparams","senderid=> "+senderId+ "recieverId=>"+recieverId+ "name"+userName+ "adid=>"+adId);
            getActivity().setTitle(userName);

            // type = bundle.getString("type", "0");
            calledFromSplash = bundle.getBoolean("calledFromSplash", false);
            //   is_Block = bundle.getString("is_block", "");
        }

        BlockedTextMessage = view.findViewById(R.id.BlockedTextMessage);
        BlockedTextMessage.setText(settingsMain.getUserUnblock("Unblock_M"));
        MessageContainer = view.findViewById(R.id.messageContainer);
        adDate = view.findViewById(R.id.verified);
        adName = view.findViewById(R.id.post_title);
        adName.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
       // adName.setTextColor(R.color.colorPrimary);
        adPrice = view.findViewById(R.id.ad_price);
//        circularProgressButton = view.findViewById(R.id.circular_btn);
        myLoadingButton = view.findViewById(R.id.my_loading_button);
        pickerView = view.findViewById(R.id.pickerView);
        pickerView.setOnClickListener(this);

        tv_typing = view.findViewById(R.id.tv_typing);
        block_button = view.findViewById(R.id.block_btn);
        block_button.setBackgroundColor(Color.parseColor(getMainColor()));
//        myLoadingButton.setMyButtonClickListener(new MyLoadingButton.MyLoadingButtonClick() {
//            @Override
//            public void onMyLoadingButtonClick() {
//                myLoadingButton.setAnimationDuration(300)
//                        .setButtonColor(R.color.colorAccent) // Set MyLoadingButton custom background color.
//                        .setButtonLabel("Label") // Set MyLoadingButton button label text.
//                        .setButtonLabelSize(20) // Set button label size in integer.
//                        .setProgressLoaderColor(Color.WHITE) // Set Color att for loader color.
//                        .setButtonLabelColor(R.color.white) // Set button label/text color.
//                        .setProgressDoneIcon(getResources().getDrawable(R.drawable.ic_progress_done)) // Set MyLoadingButton done icon drawable.
//                        .setNormalAfterError(false); // Button animate to normal state if its in error state ,by default true.
//
//            }
//        });


        adName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Ad_detail_activity.class);
                intent.putExtra("adId", adId);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });


        block_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isBlockSer != null) {

                    if (isBlockSer) {
                        adforest_UnBlockChat();
                    } else {
                        adforest_BlockChat();
                    }
                }
            }

        });


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        msg_edittext = view.findViewById(R.id.messageEditText);
        msgListView = view.findViewById(R.id.msgListView);
        tv_chatTtile = getActivity().findViewById(R.id.tv_chatTtile);
        tv_online = getActivity().findViewById(R.id.tv_online);
        sendButton = view.findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.fieldradius);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(getMainColor()));
        sendButton.setBackground(wrappedDrawable);
/*        Drawable drawable = getResources().getDrawable(R.drawable.fieldradius).mutate();

        sendButton.setColorFilter(Color.parseColor(SettingsMain.getMainColor()), PorterDuff.Mode.SRC_IN);
sendButton.setBackground(drawable);*/
//        sendButton.setBackgroundColor(Color.parseColor(getMainColor()));
        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);
        restService = UrlController.createServiceNoTimeoutUP(RestService.class, settingsMain.getUserEmail(), settingsMain.getUserPassword(), getActivity());
       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                final Handler handler = new Handler();

                handler.postDelayed(() -> {
                    //Do something after 100ms

                    refreshChat();
                    swipeRefreshLayout.setRefreshing(false);
//                                        circularProgressButton.setVisibility(GONE);
                }, 2000);
                swipeRefreshLayout.setRefreshing(true);


            }
        });

        Log.d("currentuseremail", settingsMain.getUserEmail());
        Log.d("currentuserpass", settingsMain.getUserPassword());

        chatlist = new ArrayList<>();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    Toast.makeText(getActivity(), "there", Toast.LENGTH_SHORT).show();
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    Log.d("InstantMessage", "true");
                    refreshChat();

                  /*  String date = intent.getStringExtra("date");
                    String img = intent.getStringExtra("img");
                    String text = intent.getStringExtra("text");
                    String type = intent.getStringExtra("type");
                    String recieverIdCheck;
                    String senderIdCheck;

                    String adIdCheck = intent.getStringExtra("adIdCheck");
                    if (type.equals("sent")) {
                        senderIdCheck = intent.getStringExtra("recieverIdCheck");
                        recieverIdCheck = intent.getStringExtra("senderIdCheck");
                    } else {
                        recieverIdCheck = intent.getStringExtra("recieverIdCheck");
                        senderIdCheck = intent.getStringExtra("senderIdCheck");
                    }

                    if (adId.equals(adIdCheck) && recieverId.equals(recieverIdCheck)
                            && senderId.equals(senderIdCheck)) {
                        Log.d("Instant Message", "true");
                        ChatMessage item = new ChatMessage();
                        item.setImage(img);
                        item.setBody(text);
                        item.setDate(date);
                        item.setMine(type.equals("message"));

                        chatlist.add(item);
                        msgListView.setAdapter(chatAdapter);
                        chatAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("Instant Message", adIdCheck + recieverIdCheck + senderIdCheck);
                        Log.d("Instant Message", adId + senderId + recieverId);

                    }*/
                }
            }
        };

        attachmenttype();

        adforest_getChat();

      /*  receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                    Cursor c = downloadManager.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            @SuppressLint("Range") String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            //TODO : Use this local uri and launch intent to open file
                            Toast.makeText(context, "File saved in Downloads", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(receiver, filter);*/

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (calledFromSplash) {
                        Intent i = new Intent(getActivity(), HomeActivity.class);
                        i.putExtra("calledFromChat", true);
                        startActivity(i);
                        getActivity().finish();
                    } else {
                        getActivity().onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });
    }


    private void adforest_UnBlockChat() {
        if (SettingsMain.isConnectingToInternet(getActivity())) {
            SettingsMain.showDilog(getActivity());

            JsonObject params = new JsonObject();

                params.addProperty("sender_id", senderId);
                params.addProperty("recv_id", recieverId);



            Log.d("infoBlockChatobject", "" + params.toString());

            Call<ResponseBody> myCall = restService.postUserUnBlock(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    if (responseObj.isSuccessful()) {
                        Log.d("infoBlockresponce", "" + responseObj.toString());

                        if (isBlockSer == true) {
                            isBlockSer = false;
                        }
                        try {


                            block_button.setText("Block");

                            if (blocktype == 3||blocktype==2){
                              BlockedTextMessage.setText("you blocked by user unable to send msg");

                            }else {
                                sendButton.setVisibility(View.VISIBLE);
                                msg_edittext.setVisibility(View.VISIBLE);
                                if (attachmentModel != null) {
                                    if (!attachmentModel.attachment_allow) {
                                        pickerView.setVisibility(GONE);
                                    }
                                }
                                BlockedTextMessage.setVisibility(GONE);
                            }



                            //Post it in a handler to make sure it gets called if coming back from a lifecycle method.
                           /* new Handler().post(new Runnable() {

                                @Override
                                public void run() {
                                    try {


                                        Intent intent = getActivity().getIntent();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                                | Intent.FLAG_ACTIVITY_NO_ANIMATION);

                                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                                        startActivity(intent);
                                    } catch (Exception e) {
                                        SettingsMain.hideDilog();
                                        Log.d("exception", e.toString());
                                    }
                                }
                            });*/
                        } catch (Throwable throwable) {
                        }
                    }
                    SettingsMain.hideDilog();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.getLocalizedMessage();

                }
            });
        }
    }


    private void adforest_BlockChat() {
        if (SettingsMain.isConnectingToInternet(getActivity())) {
            SettingsMain.showDilog(getActivity());
            JsonObject params = new JsonObject();

                params.addProperty("sender_id",senderId);
                params.addProperty("recv_id", recieverId);

            Log.d("infoBlockChatobject", "" + params.toString());

            Call<ResponseBody> myCall = restService.postUserBlock(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    if (responseObj.isSuccessful()) {
                        if (isBlockSer == false) {
                            isBlockSer = true;
                        }
                        Log.d("infoBlockresponce", "" + responseObj.toString());


                        block_button.setText("Unblock");
                        msg_edittext.setVisibility(GONE);
                        sendButton.setVisibility(GONE);
                        msg_edittext.setVisibility(GONE);
                        pickerView.setVisibility(GONE);
                        BlockedTextMessage.setVisibility(View.VISIBLE);

                        //Post it in a handler to make sure it gets called if coming back from a lifecycle method.
                       /* try {
                            new Handler().post(new Runnable() {

                                @Override
                                public void run() {
                                    Intent intent = getActivity().getIntent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    //   intent.putExtra("sent" ,true);


                                    getActivity().overridePendingTransition(0, 0);
                                    startActivity(intent);
                                }
                            });
                        } catch (Exception e) {
                            SettingsMain.hideDilog();
                            e.printStackTrace();
                        }
*/
                        // Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_LONG).show();
                        // SettingsMain.hideDilog();
                    }
                    SettingsMain.hideDilog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }
    private void adforest_getChat() {
        if (SettingsMain.isConnectingToInternet(getActivity())) {
            SettingsMain.showDilog(getActivity());
            JsonObject params = new JsonObject();

            params.addProperty("ad_id", adId);
            params.addProperty("receiver_id", recieverId);

            Log.d("infosendChatobject", "" + params.toString());

            Call<ResponseBody> myCall = restService.postGetChatORLoadMore(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        Log.d("infoChatResp", "" + responseObj.toString());
                        String str = responseObj.body().string();
                        Log.d("infoChatobject", "" + str);
                        if (responseObj.isSuccessful()) {


                            JSONObject response = new JSONObject(str);
                            if (response.getString("success").equals("true")) {
                                String mystring = response.getJSONObject("ad_data").getString("post_title");
                                SpannableString content = new SpannableString(mystring);
                                content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                                adName.setText(content);
                              //  adName.setText(response.getJSONObject("ad_data").getString("post_title"));
                                adPrice.setText(response.getJSONObject("ad_data").getString("ad_price"));
                                blocktype = response.getJSONObject("ad_data").getInt("is_block");
                                blockcheck(blocktype);
                                adforest_intList(response.getJSONArray("chat"));
                                Log.d("chatlist", response.getJSONArray("chat").toString());
                                chatAdapter = new ChatAdapter(getActivity(), chatlist);
                                msgListView.setAdapter(chatAdapter);

                            } else {


                                Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_SHORT).show();
                            }
                        }
                        SettingsMain.hideDilog();
                    } catch (JSONException e) {
                        SettingsMain.hideDilog();
                        Log.d("jsonexception", e.toString());
                    } catch (IOException e) {
                        SettingsMain.hideDilog();
                        Log.d("Ioexception", e.toString());
                    }catch (Exception e){
                        Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                        Log.d("Exceptioninfocgat", e.toString());
                        SettingsMain.hideDilog();

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
                        Log.d("info Chat Exception ", "NullPointert Exception" + t.getLocalizedMessage());
                        settingsMain.hideDilog();
                    } else {
                        SettingsMain.hideDilog();
                        Log.d("info Chat error", String.valueOf(t));
                        Log.d("info Chat error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            SettingsMain.hideDilog();
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

    private void attachmenttype(){

        attachmentModel = new AttachmentModel();
        attachmentModel.attachment_allow = true;
        if (attachmentModel.attachment_allow) {
            pickerView.setVisibility(View.VISIBLE);
        } else {
            pickerView.setVisibility(GONE);
        }
        attachmentModel.attachment_type = "both";
        attachmentModel.image_size = "819200";
        attachmentModel.attachment_size = "1048576";
        attachmentModel.upload_txt = "What do you like to upload";
        attachmentModel.image_limit_txt = "Image size limit is 800kb";
        attachmentModel.doc_limit_txt = "Documents size limit is  1MB";
        attachmentModel.doc_format_txt = "Allowed formats are  pdf, doc, docx, txt, zip,";
        attachmentModel.upload_image = "Upload Images";
        attachmentModel.upload_doc = "Upload Document";

        List<String> allowedFormats = new ArrayList<String>();
        allowedFormats.add("pdf");
        allowedFormats.add("doc");
        allowedFormats.add("docx");
        allowedFormats.add("txt");
        allowedFormats.add("zip");



        attachmentModel.attachment_format = new ArrayList<>();
        attachmentModel.attachment_format.addAll(allowedFormats);



    }

    private void blockcheck(int blocktype){
        if (blocktype==1 || blocktype==3){
            isBlockSer = true;
            block_button.setText("Unblock");
        }else {
            isBlockSer = false;
            block_button.setText("Block");

        }
        if (blocktype!=0){
            if (blocktype==1 || blocktype==3){
                BlockedTextMessage.setText("Unblock to Send message");
            }
            if (blocktype==2){
                BlockedTextMessage.setText("you blocked by user unable to send msg");
            }
            pickerView.setVisibility(View.GONE);
            sendButton.setVisibility(View.GONE);
            msg_edittext.setVisibility(GONE);
            BlockedTextMessage.setVisibility(View.VISIBLE);
        }else {
            if (pickerView.getVisibility()==GONE){
            pickerView.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);
            msg_edittext.setVisibility(View.VISIBLE);
            BlockedTextMessage.setVisibility(GONE);}
        }
    }
    private void refreshChat() {
        if (SettingsMain.isConnectingToInternet(getActivity())) {
           // SettingsMain.showDilog(getActivity());
            JsonObject params = new JsonObject();

            params.addProperty("ad_id", adId);
            params.addProperty("receiver_id", recieverId);

            Log.d("infosendChatobject", "" + params.toString());

            Call<ResponseBody> myCall = restService.postGetChatORLoadMore(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("infoChatResp", "" + responseObj.toString());
                            String str = responseObj.body().string();
                            Log.d("infoChatobject", "" + str);

                            JSONObject response = new JSONObject(str);
                            if (response.getString("success").equals("true")) {
                                blocktype = response.getJSONObject("ad_data").getInt("is_block");
                                blockcheck(blocktype);
                                adforest_intList(response.getJSONArray("chat"));
                                Log.d("chatlist", response.getJSONArray("chat").toString());
                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        SettingsMain.hideDilog();
                    } catch (JSONException e) {
                        SettingsMain.hideDilog();
                        Log.d("jsonexception", e.toString());
                    } catch (IOException e) {
                        SettingsMain.hideDilog();
                        Log.d("Ioexception", e.toString());
                    }
                   // SettingsMain.hideDilog();
                }



                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                    //    settingsMain.hideDilog();
                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                      //  settingsMain.hideDilog();
                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info Chat Exception ", "NullPointert Exception" + t.getLocalizedMessage());
                       // settingsMain.hideDilog();
                    } else {
                       // SettingsMain.hideDilog();
                        Log.d("info Chat error", String.valueOf(t));
                        Log.d("info Chat error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                    }
                }
            });
        } else {
            SettingsMain.hideDilog();
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }
    }

/*   try {


                                    JSONObject messageSettings = response.getJSONObject("data").getJSONObject("message_setting");

                                    attachmentModel = new AttachmentModel();
                                    attachmentModel.attachment_allow = messageSettings.getBoolean("attachment_allow");
                                    if (attachmentModel.attachment_allow) {
                                        pickerView.setVisibility(View.VISIBLE);
                                    } else {
                                        pickerView.setVisibility(GONE);
                                    }
                                    attachmentModel.attachment_type = messageSettings.getString("attachment_type");
                                    attachmentModel.image_size = messageSettings.getString("image_size");
                                    attachmentModel.attachment_size = messageSettings.getString("attachment_size");
                                    attachmentModel.upload_txt = messageSettings.getString("upload_txt");
                                    attachmentModel.image_limit_txt = messageSettings.getString("image_limit_txt");
                                    attachmentModel.doc_limit_txt = messageSettings.getString("doc_limit_txt");
                                    attachmentModel.doc_format_txt = messageSettings.getString("doc_format_txt");
                                    attachmentModel.upload_image = messageSettings.getString("upload_image");
                                    attachmentModel.upload_doc = messageSettings.getString("upload_doc");
                                    List<String> allowedFormats = new ArrayList<>();
                                    for (int i = 0; i < messageSettings.getJSONArray("attachment_format").length(); i++) {
                                        allowedFormats.add(messageSettings.getJSONArray("attachment_format").getString(i));
                                    }
                                    attachmentModel.attachment_format = new ArrayList<>();
                                    attachmentModel.attachment_format.addAll(allowedFormats);

                                    getActivity().setTitle("");
                                    block_button.setText(response.getJSONObject("data").getString("btn_text"));
                                    tv_chatTtile.setText(response.getJSONObject("data").getString("page_title"));
                                    userName = response.getJSONObject("data").getString("page_title");

                                    //   JSONObject jsonObjectPagination = response.getJSONObject("data").getJSONObject("pagination");

                                    adPrice.setText(response.getJSONObject("data").getJSONObject("ad_price").getString("price"));
                                    String mystring = response.getJSONObject("data").getString("ad_title");
                                    SpannableString content = new SpannableString(mystring);
                                    content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                                    adName.setText(content);
                                    adName.setTextColor(Color.parseColor(getMainColor()));
                                    adDate.setText(response.getJSONObject("data").getString("ad_date"));
                                    typingText = response.getJSONObject("data").getString("is_typing");


                                    // nextPage = jsonObjectPagination.getInt("next_page");
                                    // hasNextPage = jsonObjectPagination.getBoolean("has_next_page");
                                } catch (JSONException je) {
                                    Log.e("errorchatlist", je.toString());
                                }*/

    private void adforest_loadMore(int nextPag) {

        if (SettingsMain.isConnectingToInternet(getActivity())) {

            JsonObject params = new JsonObject();
            params.addProperty("ad_id", adId);
            params.addProperty("sender_id", senderId);
            params.addProperty("receiver_id", recieverId);
            params.addProperty("type", type);
//            params.addProperty("is_block", is_Block);

            params.addProperty("page_number", nextPag);

            Log.d("info LoadMore Chat", "" + params.toString());

            Call<ResponseBody> myCall = restService.postGetChatORLoadMore(params, UrlController.AddHeaders(getActivity()));
            myCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                    try {
                        if (responseObj.isSuccessful()) {
                            Log.d("info LoadChat Resp", "" + responseObj.toString());

                            JSONObject response = new JSONObject(responseObj.body().string());
                            if (response.getBoolean("success")) {
                                Log.d("infoLoadChatobject", "" + response.getJSONObject("data"));

                                JSONObject jsonObjectPagination = response.getJSONObject("data").getJSONObject("pagination");

                                nextPage = jsonObjectPagination.getInt("next_page");
                                hasNextPage = jsonObjectPagination.getBoolean("has_next_page");

                                JSONArray jsonArray = (response.getJSONObject("data").getJSONArray("chat"));

                                Collections.reverse(chatlist);

                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ChatMessage item = new ChatMessage();
                                        item.setImage(jsonArray.getJSONObject(i).getString("img"));
                                        item.setBody(jsonArray.getJSONObject(i).getString("text"));
                                        item.setDate(jsonArray.getJSONObject(i).getString("date"));
                                        item.setMine(jsonArray.getJSONObject(i).getString("type").equals("message"));
                                        if (jsonArray.getJSONObject(i).has("images")) {
                                            if (jsonArray.getJSONObject(i).getJSONArray("images").length() != 0) {
                                                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                                                JSONArray images = jsonArray.getJSONObject(i).getJSONArray("images");
                                                for (int j = 0; j < images.length(); j++) {
                                                    item.getImages().add(images.getString(i));

                                                }

                                            }
                                        }
                                        if (jsonArray.getJSONObject(i).has("files")) {
                                            if (jsonArray.getJSONObject(i).getJSONArray("files").length() != 0) {
                                                ArrayList<String> filesList = new ArrayList<>();
                                                JSONArray images = jsonArray.getJSONObject(i).getJSONArray("files");
                                                for (int j = 0; j < images.length(); j++) {
                                                    filesList.add(images.getString(j));
                                                }
                                                item.setFile(filesList);
                                            }
                                        }
                                        chatlist.add(item);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Collections.reverse(chatlist);
                                msgListView.setAdapter(chatAdapter);
                                chatAdapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(getActivity(), response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        SettingsMain.hideDilog();
                        swipeRefreshLayout.setRefreshing(false);
                    } catch (JSONException e) {
                        SettingsMain.hideDilog();
                        swipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                    } catch (IOException e) {
                        SettingsMain.hideDilog();
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    SettingsMain.hideDilog();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof TimeoutException) {
                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        SettingsMain.hideDilog();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                        Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
                        SettingsMain.hideDilog();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                        Log.d("info LoadChat Excptn ", "NullPointert Exception" + t.getLocalizedMessage());
                        SettingsMain.hideDilog();
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        SettingsMain.hideDilog();
                        Log.d("info LoadChat error", String.valueOf(t));
                        Log.d("info LoadChat error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();
        }


    }


    private void adforest_intList(JSONArray jsonArray) {
        chatlist.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                ChatMessage item = new ChatMessage();
                item.setBody(jsonArray.getJSONObject(i).getString("msg_content"));
                String type = jsonArray.getJSONObject(i).getString("type");
                item.setImage(jsonArray.getJSONObject(i).getString("sender_img"));

                if (type.equals("sent_msg")) {
                    item.setMine(true);
                } else {
                    item.setMine(false);
                }
                item.setType(type);

                 item.setmediaType(jsonArray.getJSONObject(i).getString("media_type"));
                item.setDate(jsonArray.getJSONObject(i).getString("date_time"));
                chatlist.add(item);
            }
            Collections.reverse(chatlist);
            if (chatAdapter != null) {
                chatAdapter.notifyDataSetChanged();
            }


        } catch (JSONException e) {
            Log.d("jsonerror123",e.toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    public void adforest_sendTextMessage() {

        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        String message = msg_edittext.getEditableText().toString();
        msg_edittext.setText("");
        if (!message.equalsIgnoreCase("")) {
            if (SettingsMain.isConnectingToInternet(getActivity())) {
                sendButton.setVisibility(GONE);
                myLoadingButton.setVisibility(View.VISIBLE);
                myLoadingButton.showNormalButton();
                myLoadingButton.showLoadingButton();

                JsonObject params = new JsonObject();
                params.addProperty("is_sent", 1);
                params.addProperty("ad_id", adId);
                params.addProperty("receiver_id", recieverId);
                params.addProperty("message", message);



                Log.d("infosendMessage Object", "" + params.toString());
                Call<ResponseBody> myCall = restService.postGetChatORLoadMore(params, UrlController.AddHeaders(getActivity()));
                myCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseObj) {
                        try {
                            if (responseObj.isSuccessful()) {
                                Log.d("infosendMessageResp", "" + responseObj.toString());

                                JSONObject response = new JSONObject(responseObj.body().string());
                                Log.d("infosendMessageResp", "" + responseObj.body().toString());
                                if (response.getString("success").equals("true")) {
                                    myLoadingButton.showDoneButton();
                                    adforest_intList(response.getJSONArray("chat"));
                                    blocktype = response.getJSONObject("ad_data").getInt("is_block");
                                    blockcheck(blocktype);

                                    final Handler handler = new Handler();

                                    handler.postDelayed(() -> {
                                        //Do something after 100ms
                                        myLoadingButton.setVisibility(GONE);
                                        sendButton.setVisibility(View.VISIBLE);
                                    }, 2000);



                                    msg_edittext.setText("");
                                } else {
                                      Toast.makeText(getActivity(), "response not succesful", Toast.LENGTH_LONG).show();


                                }
                            }
//                            SettingsMain.hideDilog();
                        } catch (JSONException e) {
//                            SettingsMain.hideDilog();
                            sendButton.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        } catch (IOException e) {
                            sendButton.setVisibility(View.VISIBLE);
//                            SettingsMain.hideDilog();
                            e.printStackTrace();
                        }
//                        SettingsMain.hideDilog();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (t instanceof TimeoutException) {
                            Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//                            SettingsMain.hideDilog();
                        }
                        if (t instanceof SocketTimeoutException || t instanceof NullPointerException) {

                            Toast.makeText(getActivity(), settingsMain.getAlertDialogMessage("internetMessage"), Toast.LENGTH_SHORT).show();
//                            SettingsMain.hideDilog();
                        }
                        if (t instanceof NullPointerException || t instanceof UnknownError || t instanceof NumberFormatException) {
                            Log.d("info sendMessage", "NullPointert Exception" + t.getLocalizedMessage());
//                            SettingsMain.hideDilog();
                        } else {
//                            SettingsMain.hideDilog();
                            Log.d("info sendMessage error", String.valueOf(t));
                            Log.d("info sendMessage error", String.valueOf(t.getMessage() + t.getCause() + t.fillInStackTrace()));
                        }
                    }
                });
            } else {

//                SettingsMain.hideDilog();
                Toast.makeText(getActivity(), "Internet error", Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sendMessageButton) {

            adforest_sendTextMessage();

        }
        if (v.getId() == pickerView.getId()) {
            ChatBottomSheet bottomSheet = new ChatBottomSheet(getActivity(), restService, this, attachmentModel);
            bottomSheet.show(getChildFragmentManager(), "chatSheet");
        }
    }

    @Override
    public void onResume() {
        try {
            if (settingsMain.getAnalyticsShow() && !settingsMain.getAnalyticsId().equals(""))
                AnalyticsTrackers.getInstance().trackScreenView("Chat Box");
            super.onResume();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

    }

    private void saveValue(String value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("VAL", value).apply();

    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);

        super.onPause();
    }


    int successfullyUploadedImagesCount, totalFiles;

    private void adforest_uploadImages(List<MultipartBody.Part> parts) {
        Log.d("multipartimg", parts.toString());


       /* JsonObject params = new JsonObject();

        params.addProperty("ad_id", adId);
        params.addProperty("receiver_id", recieverId);
        params.addProperty("is_sent",1);*/
     /*   RequestBody parambody =
                RequestBody.create(
                        MediaType.parse("application/json"), String.valueOf(params));*/

        progressBar.setVisibility(View.VISIBLE);
        pickerView.setVisibility(GONE);
        RequestBody adID =
                RequestBody.create(
                        MediaType.parse("text/plain"), adId);
        RequestBody sender = RequestBody.create(
                MediaType.parse("text/plain"), senderId);
        RequestBody receiver = RequestBody.create(
                MediaType.parse("text/plain"), recieverId);
     /*   RequestBody t = RequestBody.create(
                MediaType.parse("text/plain"), "1");*/

        Call<ResponseBody> req;
        req = restService.postUploadChatFile(adID,sender,receiver,parts, UrlController.UploadImageAddHeaders(getActivity()));
        Log.d("multipart","adid=> " +adID + "reciverid=>"+  receiver );

        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("response_image_send",response.toString()+call.toString());
               /* try {
                    Log.d("response_image_send",response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                progressBar.setVisibility(GONE);
                pickerView.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {

                    JSONObject responseobj = null;
                    try {
                        responseobj = new JSONObject(response.body().string());
                        Log.d("infoUploadImageobject", "" + responseobj.getJSONObject("data").toString());

                        if (responseobj.getBoolean("success")) {
                            Toast.makeText(getActivity(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                           // adforest_intList(responseobj.getJSONObject("data").getJSONArray("chat"));
                          /*  if (chatAdapter != null) {
                                chatAdapter.notifyDataSetChanged();
                            }*/
                            dialog.dismiss();
                            refreshChat();


                        }
                    } catch (JSONException e) {
                        dialog.dismiss();
                        Log.d("errorjson", e.toString());
                    } catch (IOException e) {
                        dialog.dismiss();
                        Log.d("errorexception", e.toString());

                    }


                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(GONE);
                pickerView.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("info_Upload_Image_Err:", t.toString());
                dialog.dismiss();
                t.getLocalizedMessage();
                t.printStackTrace();
            }
        });

    }

    @Override
    public void getFiles(List<MultipartBody.Part> parts, int count) {
        successfullyUploadedImagesCount = 0;
        totalFiles = count;
        adforest_uploadImages(parts);
    }
}
