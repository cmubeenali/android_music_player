package com.casualmill.musicplayer.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casualmill.musicplayer.MusicData;
import com.casualmill.musicplayer.MusicPlayer;
import com.casualmill.musicplayer.R;
import com.casualmill.musicplayer.adapters.TrackAdapter;
import com.casualmill.musicplayer.models.Track;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TracksFragment extends Fragment {

    public View rootView;
    public long album_id=0;

    public TracksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tracks, container, false);

        // More information about RecyclerView at https://developer.android.com/training/material/lists-cards.html
        RecyclerView recyclerView = rootView.findViewById(R.id.tracks_recyclerView);
        // size wont change based on size of child items. set it true for optimization since thats our case
        recyclerView.setHasFixedSize(true);
        // we will have a linear list
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(getArguments()!=null)
            album_id=getArguments().getLong("album_id");
        this.LoadTracks();

        return rootView;
    }


    public void LoadTracks() {
        final TracksFragment _ref = this;

        if(album_id==0){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<Track> tracks = MusicData.getAllTracks(_ref.getContext());
                    MusicPlayer.setServiceTrackList(tracks);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            TrackAdapter trackAdapter = new TrackAdapter(tracks,getActivity().getApplicationContext());
                            RecyclerView recyclerView = _ref.rootView.findViewById(R.id.tracks_recyclerView);
                            recyclerView.setAdapter(trackAdapter);
                        }
                    });
                }
            });
            t.start();
        }else if(album_id!=0){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<Track> tracks = MusicData.getAllTracks(_ref.getContext(),album_id);
                    MusicPlayer.setServiceTrackList(tracks);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            TrackAdapter trackAdapter = new TrackAdapter(tracks,getActivity().getApplicationContext());
                            RecyclerView recyclerView = _ref.rootView.findViewById(R.id.tracks_recyclerView);
                            recyclerView.setAdapter(trackAdapter);
                        }
                    });
                }
            });
            t.start();
        }
    }

}
