package com.nero.zikzok.room;

import us.zoom.sdk.MeetingActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nero.zikzok.MainActivity;
import com.nero.zikzok.R;
import com.nero.zikzok.youtube.VideoItem;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import static com.nero.zikzok.room.Constants.JWT_TOKEN;

public class MyMeetingActivity extends MeetingActivity {

	private static final int REQUEST_CODE_SEARCH_MUSIC = 0x3939;
	private Button btnLeaveZoomMeeting;
	private Button btnSwitchToNextCamera;
	private Button btnAudio;
	private Button btnParticipants;
	private Button btnPlay;

	private TextView _txtRoomId;
	private TextView _txtPassword;

	private List<VideoItem> _lstVideoInQueue = new ArrayList<>();

	FirebaseDatabase mFirebaseInstance;
	DatabaseReference mFirebaseDatabase;
	DatabaseReference mQueueDatabase;
	MeetingInfo meetingInfo;
	@Override
	protected int getLayout() {
		return R.layout.my_meeting_layout;
	}

	@Override
	protected boolean isAlwaysFullScreen() {
		return false;
	}
	
	@Override
	protected boolean isSensorOrientationEnabled() {
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		disableFullScreenMode();
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		setupTabs();

		btnLeaveZoomMeeting = (Button)findViewById(R.id.btnLeaveZoomMeeting);
		btnSwitchToNextCamera = (Button)findViewById(R.id.btnSwitchToNextCamera);
		btnAudio = (Button)findViewById(R.id.btnAudio);
		btnParticipants = (Button)findViewById(R.id.btnParticipants);

		btnLeaveZoomMeeting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showLeaveDialog();
			}
		});

		btnSwitchToNextCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switchToNextCamera();
			}
		});

		btnAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//doAudioAction();
				if(!isAudioConnected()) {
					connectVoIP();
				} else {
					muteAudio(!isAudioMuted());
				}
			}
		});

		btnParticipants.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showParticipants();
			}
		});

		initRoomInfo();
		initSongSearching();
		initSongQueue();

		btnPlay = findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final VideoItem topSong = _lstVideoInQueue.get(0);
				YouTubePlayerFragment youTubePlayerFragment =
						(YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubeFragment);
				youTubePlayerFragment.initialize("AIzaSyDk4ptR6D-ugBV3kOCykaSAkY9KkMifzcg", new YouTubePlayer.OnInitializedListener() {
					@Override
					public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
						youTubePlayer.loadVideo(topSong.getId());
					}

					@Override
					public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

					}
				});
			}
		});
	}

	private void initSongQueue() {
		Button btnQueue = (Button) findViewById(R.id.btnQueueSong);
		btnQueue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyMeetingActivity.this, com.nero.zikzok.youtube.QueueSong.class);
				intent.putExtra("Queue", (Serializable) _lstVideoInQueue);
				startActivity(intent);
			}
		});
	}

	private void initRoomInfo() {
		_txtRoomId = (TextView)findViewById(R.id.txtRoomID);
		_txtPassword = (TextView)findViewById(R.id.txtPassword);
		meetingInfo = MeetingInfo.getInstance();
		String roomId = meetingInfo.getMeetingId();
		final String[] password = {meetingInfo.getPassword()};

		if (password[0] == "??????") {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.url("https://api.zoom.us/v2/meetings/"+roomId)
					.get()
					.addHeader("authorization", "Bearer "+ JWT_TOKEN)
					.build();
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
						password[0] = json.getString("password");
						_txtPassword.setText(password[0]);
						meetingInfo.setPassword(password[0]);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			});
		}
		_txtRoomId.setText(roomId);
		_txtPassword.setText(password[0]);

		mFirebaseInstance = FirebaseDatabase.getInstance();

		mFirebaseDatabase = mFirebaseInstance.getReference("room").child(roomId);
		if (password[0] != "??????")
			mFirebaseDatabase.child("password").setValue(password[0]);
		else {

		}
		mQueueDatabase = mFirebaseDatabase.child("queue");
		mQueueDatabase.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				Log.e("Count " ,""+snapshot.getChildrenCount());
				ArrayList<VideoItem> newLst = new ArrayList<>();
				for (DataSnapshot postSnapshot: snapshot.getChildren()) {
            		VideoItem video = postSnapshot.getValue(VideoItem.class);
            		newLst.add(video);
					Log.e("Get Data", video.toString());
				}
				_lstVideoInQueue = newLst;
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.w("Get data", "Failed to read value.", error.toException());
			}
		});
	}

	// 4PJ3Ye
	private void initSongSearching() {
		Button btnSearch = (Button) findViewById(R.id.btnSearchSong);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyMeetingActivity.this, com.nero.zikzok.youtube.MainActivity.class);
				startActivityForResult(intent, REQUEST_CODE_SEARCH_MUSIC);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_SEARCH_MUSIC) {
			if (resultCode == Activity.RESULT_OK) {
				VideoItem video = (VideoItem) data.getSerializableExtra("VIDEO_INFO");
				addSongToQueue(video);
				Toast.makeText(this, video.getTitle(), Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void addSongToQueue(VideoItem video) {
		_lstVideoInQueue.add(video);
//		mQueueDatabase.setValue(_lstVideoInQueue);
	}

	@Override
	public void onConfigurationChanged (Configuration newConfig) {
		newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
		super.onConfigurationChanged(newConfig);
		
		disableFullScreenMode();
	}

	private void disableFullScreenMode() {
		getWindow().setFlags(~WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
	}

	private void switchToMainActivity(int tab) {
		Intent intent = new Intent(this, com.nero.zikzok.MainActivity.class);
		intent.setAction(com.nero.zikzok.MainActivity.ACTION_RETURN_FROM_MEETING);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.putExtra(MainActivity.EXTRA_TAB_ID, tab);
		
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateButtonsStatus();
		
		// disable animation
		overridePendingTransition(0, 0);
	}

	@Override
	protected void onMeetingConnected() {
		updateButtonsStatus();
	}
	
	@Override
	protected void onSilentModeChanged(boolean inSilentMode) {
		updateButtonsStatus();
	}
	
	@Override
	protected void onStartShare() {
//		btnShare.setVisibility(View.GONE);
//		btnStopShare.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onStopShare() {
//		btnShare.setVisibility(View.VISIBLE);
//		btnStopShare.setVisibility(View.GONE);
	}

	private void updateButtonsStatus() {
		
		boolean enabled = (isMeetingConnected() && !isInSilentMode());
		
		btnSwitchToNextCamera.setEnabled(enabled);
		btnAudio.setEnabled(enabled);
		btnParticipants.setEnabled(enabled);
//		btnShare.setEnabled(enabled);
//		btnMoreOptions.setEnabled(enabled);
		
//		if(isSharingOut()) {
//			btnShare.setVisibility(View.GONE);
//			btnStopShare.setVisibility(View.VISIBLE);
//		} else {
//			btnShare.setVisibility(View.VISIBLE);
//			btnStopShare.setVisibility(View.GONE);
//		}
	}
}
