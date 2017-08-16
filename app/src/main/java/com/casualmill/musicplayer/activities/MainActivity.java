package com.casualmill.musicplayer.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.casualmill.musicplayer.MusicPlayer;
import com.casualmill.musicplayer.R;
import com.casualmill.musicplayer.adapters.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 899;

    // for
    private boolean isPaused;
    private Handler handler = new Handler();
    private SeekBar seekBar;
    Runnable onEverySecond = new Runnable() {

        @Override
        public void run() {
            if(!isPaused){
                seekBar.setProgress(MusicPlayer.getProgress());
                handler.postDelayed(onEverySecond, 600);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permission has to be given by the user.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        } else
            init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay!
                init();
            } else {
                // I QUIT
                // viewPager, it doesnt matter anyway. read the docs
                Snackbar.make(findViewById(R.id.viewPager), "Permission Denied. Bye Bye",
                        Snackbar.LENGTH_LONG)
                        .show();

                // close the app after 1second
                Handler h = new Handler();
                h.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                finishAffinity();
                            }
                        }, 1000);
            }
        }
    }

    private void init() {
        // setup ViewPager
        ViewPager pager = (ViewPager)findViewById(R.id.viewPager);
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        //TabLayout
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);

        // Play Button
        Button playButton = (Button)findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean playing = MusicPlayer.playPause();
                // change the picture based on playing bool
            }
        });

        Button prevButton = (Button)findViewById(R.id.button_prev);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayer.playPrevious();
            }
        });

        Button nextButton = (Button)findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayer.playNext();
            }
        });

        // Seeker
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // default [0,100]
                if (b) // user changed
                    MusicPlayer.seekToPosition(i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        MusicPlayer.init(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        handler.postDelayed(onEverySecond, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }
}
