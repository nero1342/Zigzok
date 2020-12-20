package com.nero.zigzok.room;

import us.zoom.sdk.MeetingActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.nero.zigzok.MainActivity;
import com.nero.zigzok.R;
import com.nero.zigzok.youtube.VideoItem;

public class MyMeetingActivity extends MeetingActivity {

	private static final int REQUEST_CODE_SEARCH_MUSIC = 0x3939;
	private Button btnLeaveZoomMeeting;
	private Button btnSwitchToNextCamera;
	private Button btnAudio;
	private Button btnParticipants;

	private TextView _txtRoomId;
	private TextView _txtPassword;

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

		initMusicSearching();
	}

	private void initRoomInfo() {
		_txtRoomId = (TextView)findViewById(R.id.txtRoomID);
		_txtPassword = (TextView)findViewById(R.id.txtPassword);
		MeetingInfo meetingInfo = MeetingInfo.getInstance();
		String roomId = meetingInfo.getMeetingId();
		String password = meetingInfo.getPassword();

		_txtRoomId.setText(roomId);
		_txtPassword.setText(password);
	}

	// 4PJ3Ye
	private void initMusicSearching() {
		Button btnSearch = (Button) findViewById(R.id.btnSearchMusic);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyMeetingActivity.this, com.nero.zigzok.youtube.MainActivity.class);
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
				Toast.makeText(this, video.getTitle(), Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
			}
		}
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
		Intent intent = new Intent(this, com.nero.zigzok.MainActivity.class);
		intent.setAction(com.nero.zigzok.MainActivity.ACTION_RETURN_FROM_MEETING);
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
