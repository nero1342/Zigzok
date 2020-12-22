package com.nero.zikzok.youtube;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nero.zikzok.R;

import java.util.ArrayList;
import java.util.List;

public class QueueSong extends Activity {
    private VideoItemAdapter videoItemAdapter;

    private RecyclerView mRecyclerView;

    private List<VideoItem> queueSongs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.queue_song);

        mRecyclerView = (RecyclerView) findViewById(R.id.videos_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        queueSongs = (ArrayList<VideoItem>) intent.getSerializableExtra("Queue");
        videoItemAdapter = new VideoItemAdapter(getApplicationContext(), queueSongs, this, "queue");

        mRecyclerView.setAdapter(videoItemAdapter);
    }
}

