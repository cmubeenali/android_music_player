package com.casualmill.musicplayer.models;

/**
 * Created by faztp on 08-Aug-17.
 */

public class Album {
    public final long id;
    public final String title;
    public final String artistName;
    public final int songCount;
    public final int year;
    public final long artistId;

    public Album(long _id, String _title, String _artistName, int _songCount, int _year, long _artistId) {
        this.id = _id;
        this.title = _title;
        this.artistName = _artistName;
        this.songCount = _songCount;
        this.year = _year;
        this.artistId = _artistId;
    }
}
