package com.casualmill.musicplayer.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.casualmill.musicplayer.MusicData;
import com.casualmill.musicplayer.R;
import com.casualmill.musicplayer.activities.MainActivity;
import com.casualmill.musicplayer.fragments.AlbumsFragment;
import com.casualmill.musicplayer.fragments.TracksFragment;
import com.casualmill.musicplayer.models.Album;

import java.util.ArrayList;
import android.support.v4.app.FragmentActivity;

/**
 * Created by faztp on 08-Aug-17.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder>{

    private ArrayList<Album> albums;
    public Context ctx;

    public AlbumAdapter(ArrayList<Album> _albums, Context ctx) {
        this.albums = _albums;
        this.ctx=ctx;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        Holder hl = new Holder(v);
        return hl;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Album tr = albums.get(position);

        holder.albumName.setText(tr.title);
        holder.artist.setText(tr.artistName);
        holder.albumArt.setImageBitmap(MusicData.getAlbumCoverArt(holder.itemView.getContext(), tr.id));
    }

    @Override
    public int getItemCount() { return albums == null ? 0 : albums.size();  }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView albumName, artist;
        protected ImageView albumArt;

        public Holder(View itemView) {
            super(itemView);
            this.albumName = itemView.findViewById(R.id.item_album_albumName);
            this.artist = itemView.findViewById(R.id.item_album_artist);
            this.albumArt = itemView.findViewById(R.id.item_album_albumArt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.ActivateTrackFragment(albums.get(getAdapterPosition()).id);
        }

        public void ActivateTrackFragment(long album_id)
        {
            android.support.v4.app.FragmentManager fragManager = ((FragmentActivity)ctx).getSupportFragmentManager();
            TracksFragment fragTracks=new TracksFragment();
            Bundle data=new Bundle();
            data.putLong("album_id",album_id);
            fragTracks.setArguments(data);
            FragmentTransaction fragmentTransaction=fragManager.beginTransaction();
            fragmentTransaction.replace(android.R.id.content,fragTracks);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }

    }

}
