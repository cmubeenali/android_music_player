package com.casualmill.musicplayer.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.casualmill.musicplayer.MusicData;
import com.casualmill.musicplayer.R;
import com.casualmill.musicplayer.adapters.AlbumAdapter;
import com.casualmill.musicplayer.adapters.TrackAdapter;
import com.casualmill.musicplayer.models.Album;
import com.casualmill.musicplayer.models.Track;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsFragment extends Fragment {

    public View rootView;

    public AlbumsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_albums, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.albums_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

        LoadAlbums();
        return rootView;
    }


    public void LoadAlbums() {
        final AlbumsFragment _ref = this;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Album> albums = MusicData.getAllAlbums(_ref.getContext());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        AlbumAdapter albumAdapter = new AlbumAdapter(albums);
                        RecyclerView recyclerView = _ref.rootView.findViewById(R.id.albums_recyclerView);
                        recyclerView.setAdapter(albumAdapter);
                    }
                });
            }
        });
        t.start();
    }

}
