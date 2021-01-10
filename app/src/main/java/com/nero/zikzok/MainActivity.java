package com.nero.zikzok;

import us.zoom.sdk.InviteOptions;
import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.MeetingViewsOptions;
import us.zoom.sdk.StartMeetingOptions;
import us.zoom.sdk.StartMeetingParamsWithoutLogin;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;
import us.zoom.sdk.ZoomSDKRawDataMemoryMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nero.zikzok.room.MeetingInfo;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static com.nero.zikzok.room.Constants.SDK_KEY;
import static com.nero.zikzok.room.Constants.SDK_SECRET;
import static com.nero.zikzok.room.Constants.WEB_DOMAIN;
import static us.zoom.sdk.MeetingError.MEETING_ERROR_SUCCESS;
import static us.zoom.sdk.MeetingService.USER_TYPE_ZOOM;

public class MainActivity extends AppCompatActivity implements MeetingServiceListener, ZoomSDKInitializeListener {

    private final static String TAG = "Zikzok";

    DatabaseReference accountReference;
    private static String USER_ID;
    private static String JWT_TOKEN;

    public final static String ACTION_RETURN_FROM_MEETING = "us.zoom.sdkexample2.action.ReturnFromMeeting";
    public final static String EXTRA_TAB_ID = "tabId";
    private final static String LAST_ROOM_ID_KEY = "LAST_ROOM_ID";
    private final static String LAST_USERNAME_KEY = "LAST_USERNAME";

    public final static int TAB_HOME = 1;
    public final static int TAB_CREATEROOM = 2;
    public final static int TAB_JOINROOM  = 3;
    public final static int TAB_SETTING  = 3;

    private final static int STYPE = MeetingService.USER_TYPE_API_USER;
    private final static String DISPLAY_NAME = "Zikzok";

    private View _viewHome;
    private View _viewCreateRoom;
    private View _viewJoinRoom;
    private View _viewSettings;

    private ImageButton _btnCreateRoom;
    private ImageButton _btnJoinRoom;
    private ImageButton _btnSettings;

    private Button _btnJoinRoomFinal;
    private Button _btnCreateRoomFinal;

    FirebaseDatabase mFirebaseInstance;

    ZoomSDK zoomSDK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        zoomSDK = ZoomSDK.getInstance();
        if (savedInstanceState == null)
            initZoomSdk();

        if(zoomSDK.isInitialized())
            registerMeetingServiceListener();

        mFirebaseInstance = FirebaseDatabase.getInstance();
        initComponents();
    }

    private void initZoomSdk() {
        ZoomSDKInitParams initParams = new ZoomSDKInitParams();
        initParams.appKey = SDK_KEY;
        initParams.appSecret = SDK_SECRET;
        initParams.domain = WEB_DOMAIN;
        //initParams.videoRawDataMemoryMode = ZoomSDKRawDataMemoryMode.ZoomSDKRawDataMemoryModeStack;
        zoomSDK.initialize(getApplicationContext(), this, initParams);
    }

    private void initComponents() {
        // Home's buttons
        _btnCreateRoom = (ImageButton) findViewById(R.id.btnCreateRoom);
        _btnJoinRoom = (ImageButton) findViewById(R.id.btnJoinRoom);
        _btnSettings = (ImageButton) findViewById(R.id.btnSettings);

        initCreateRoom();
        initJoinRoom();
        initSettings();
    }


    private void initSettings() {
        _viewSettings = LayoutInflater.from(this).inflate(R.layout.settings, null);
        _btnSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // create the popup window
                int width = (int) pxToDp(321);
                int height = (int) pxToDp(360);
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(_viewSettings, width, height, focusable);
                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
    }

    private void initJoinRoom() {
        final SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        _viewJoinRoom = LayoutInflater.from(this).inflate(R.layout.join_room, null);

        final EditText _textRoomID = _viewJoinRoom.findViewById(R.id.txtRoomID);
        final EditText _textUsername = _viewJoinRoom.findViewById(R.id.txtUsername);
        final EditText _textPassword = _viewJoinRoom.findViewById(R.id.txtPassword);

        _textRoomID.setText(sharedPreferences.getString(LAST_ROOM_ID_KEY, ""));
        _textUsername.setText(sharedPreferences.getString(LAST_USERNAME_KEY, ""));

        _btnJoinRoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // create the popup window
                int width = (int) pxToDp(321);
                int height = (int) pxToDp(360);
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(_viewJoinRoom, width, height, focusable);
                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });

        _btnJoinRoomFinal = (Button) _viewJoinRoom.findViewById(R.id.btnJoinRoomFinal);
        _btnJoinRoomFinal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String _roomID = String.valueOf(_textRoomID.getText());
                String _username = String.valueOf(_textUsername.getText());
                String _password = String.valueOf(_textPassword.getText());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LAST_ROOM_ID_KEY, _roomID);
                editor.putString(LAST_USERNAME_KEY, _username);
                editor.apply();
                joinRoom(_roomID, _username, _password);
            }
        });
    }

    private double pxToDp(int px) {
        return px * getResources().getDisplayMetrics().density;
    }

    private void initCreateRoom() {
        final SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        _viewCreateRoom = LayoutInflater.from(this).inflate(R.layout.create_room, null);
        final EditText _textUsername = _viewCreateRoom.findViewById(R.id.txtUsername);
        _textUsername.setText(sharedPreferences.getString(LAST_USERNAME_KEY, ""));

        _btnCreateRoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // create the popup window
                int width = (int) pxToDp(321);
                int height = (int) pxToDp(360);
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(_viewCreateRoom, width, height, focusable);
                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
        _btnCreateRoomFinal = _viewCreateRoom.findViewById(R.id.btnCreateRoomFinal);
        _btnCreateRoomFinal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _username = String.valueOf(_textUsername.getText());
                sharedPreferences.edit().putString(LAST_USERNAME_KEY, _username).apply();
                mFirebaseInstance.getReference("zoom/accounts").orderByChild("in_use").equalTo(false).limitToFirst(1).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot result = task.getResult();
                        if (result.exists() && result.getChildrenCount() > 0) {
                            for (DataSnapshot data : result.getChildren()) {
                                accountReference = data.getRef();
                                USER_ID = (String) data.child("user_id").getValue();
                                JWT_TOKEN = (String) data.child("jwt_token").getValue();
                                Log.d("[FIREBASE]", "User_id = " + USER_ID);
                                Log.d("[FIREBASE]", "jwt_token = " + JWT_TOKEN);
                                createRoomStage1(_username);
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Out of Zoom account to create room", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // disable animation
        overridePendingTransition(0,0);

        String action = intent.getAction();

        if(ACTION_RETURN_FROM_MEETING.equals(action)) {
//            int tabId = intent.getIntExtra(EXTRA_TAB_ID, TAB_HOME);
//            selectTab(tabId);
            Log.d("[ANDROID]", "Returned from meeting");
        }
    }

    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);

        if(errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show();

            registerMeetingServiceListener();
        }
    }

    private void registerMeetingServiceListener() {
        MeetingService meetingService = zoomSDK.getMeetingService();
        if (meetingService != null) {
            meetingService.addListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        if(zoomSDK.isInitialized()) {
            MeetingService meetingService = zoomSDK.getMeetingService();
            meetingService.removeListener(this);
        }
        super.onDestroy();
    }

    private void joinRoom(String roomId, String username, String _password) {
        MeetingInfo meetingInfo = MeetingInfo.getInstance();
        meetingInfo.setMeetingId(roomId);
        meetingInfo.setPassword(_password);
        zoomSDK = ZoomSDK.getInstance();

        if(!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        if(roomId == null) {
            Toast.makeText(this, "roomID can not be NULL", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();
//
        JoinMeetingOptions opts = new JoinMeetingOptions();
        opts.no_driving_mode = true;
        opts.no_titlebar = true;
        opts.no_bottom_toolbar = true;
        opts.no_invite = true;
        opts.no_video = true;
        opts.no_meeting_end_message = true;
        JoinMeetingParams params = new JoinMeetingParams();
        params.meetingNo = roomId;
        params.displayName = username;
        params.password = _password;

        int ret = meetingService.joinMeetingWithParams(getApplicationContext(), params, opts);

        Log.i(TAG, "onClickBtnStartMeeting, ret=" + ret);
    }

    private void createRoomStage1(final String username) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.zoom.us/v2/users/"+USER_ID+"/token?type=zak")
                .get()
                .addHeader("authorization", "Bearer "+ JWT_TOKEN)
                .build();

        final ProgressDialog _progressDialog = new ProgressDialog(this);
        _progressDialog.setTitle("Creating room...");
        _progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _progressDialog.setMessage("Wait a few seconds...");
        _progressDialog.show();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Error","Failed to connect: "+e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // Log.d(TAG, response.body().string());
                String x = response.body().string();
                try {
                    JSONObject json = new JSONObject(x);
                    String zak = json.getString("token");
                    MeetingInfo meetingInfo = MeetingInfo.getInstance();
                    meetingInfo.setZak(zak);
                    createRoomStage2(username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                _progressDialog.dismiss();
            }
        });
    }

    public void createRoomStage2(final String username) {
        Log.i("[ZOOM]", "Creating room");
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url("https://api.zoom.us/v2/users/" + USER_ID + "/meetings")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + JWT_TOKEN)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("[ZOOM]", "Error: Failed to connect: " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // Log.d(TAG, response.body().string());
                String x = response.body().string();
                try {
                    JSONObject json = new JSONObject(x);
                    String roomID = json.getString("id");
                    String password = json.getString("password");
                    createRoomStage3(username, roomID, password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createRoomStage3(String username, String roomId, String password) {
        MeetingInfo meetingInfo = MeetingInfo.getInstance();
        meetingInfo.setMeetingId(roomId);
        meetingInfo.setPassword(password);

        if(!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        if(roomId == null) {
            Toast.makeText(this, "MEETING_ID in Constants can not be NULL", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();

        JoinMeetingOptions opts = new JoinMeetingOptions();
        opts.no_driving_mode = true;
        opts.no_titlebar = true;
        opts.no_bottom_toolbar = true;
        opts.no_invite = true;
        opts.no_video = true;

        StartMeetingParamsWithoutLogin params = new StartMeetingParamsWithoutLogin();
//        params.userId = "_ghh0YajRNuV-ciJ9JciVQ";
        params.userId = USER_ID;
        params.userType = USER_TYPE_ZOOM;
        params.meetingNo = roomId;
        params.displayName = username;
        params.zoomAccessToken = MeetingInfo.getInstance().getZak();
        Log.d("[ZOOM]", "User ID = " + USER_ID);
        MainViewModel.USER_ID = USER_ID;
        int ret = meetingService.startMeetingWithParams(getApplicationContext(), params, opts);
        if (ret == MEETING_ERROR_SUCCESS)
            accountReference.child("in_use").setValue(true);
        Log.d("[ZOOM]", "Create room ret = " + ret);
    }

    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode,
                                       int internalErrorCode) {

        if(meetingStatus == meetingStatus.MEETING_STATUS_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
            Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show();
        }

        if(meetingStatus == MeetingStatus.MEETING_STATUS_IDLE || meetingStatus == MeetingStatus.MEETING_STATUS_FAILED) {
//            selectTab(TAB_WELCOME);
        }
    }

    @Override
    public void onZoomAuthIdentityExpired() {

    }
}