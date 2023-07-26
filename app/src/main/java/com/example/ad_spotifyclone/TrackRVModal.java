package com.example.ad_spotifyclone;

public class TrackRVModal {
    // on below line creating variables
    // for track name, track artist and id.
    private String trackName;
    private String trackArtist;
    private String id;

    // on below line creating
    // constructors, getters and setters.
    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackArtist() {
        return trackArtist;
    }

    public void setTrackArtist(String trackArtist) {
        this.trackArtist = trackArtist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TrackRVModal(String trackName, String trackArtist, String id) {
        this.trackName = trackName;
        this.trackArtist = trackArtist;
        this.id = id;
    }
}