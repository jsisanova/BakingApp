package com.example.android.bakingapp;

import android.util.Log;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

//  have our PlaybackStateListener implement the Player.EventListener interface.
// This is used to inform us about important player events including errors and playback state changes.
public class PlaybackStateListener implements Player.EventListener {

    private static final String TAG = PlaybackStateListener.class.getSimpleName();

    // onPlayerStateChanged is called when:
    //
    //play/pause state changes, given by the playWhenReady parameter
    //playback state changes, given by the playbackState parameter
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        String stateString;

        switch (playbackState) {
            // The player has been instantiated but has not yet been prepared with a MediaSource.
            case ExoPlayer.STATE_IDLE:
                stateString = "ExoPlayer.STATE_IDLE      -";
                break;
            // The player is not able to play from the current position because not enough data has been buffered.
            case ExoPlayer.STATE_BUFFERING:
                stateString = "ExoPlayer.STATE_BUFFERING -";
                break;
            // The player is able to immediately play from the current position.
            // This means the player will start playing media automatically if playWhenReady is true. If it is false the player is paused.
            case ExoPlayer.STATE_READY:
                stateString = "ExoPlayer.STATE_READY     -";
                break;
            // The player has finished playing the media.
            case ExoPlayer.STATE_ENDED:
                stateString = "ExoPlayer.STATE_ENDED     -";
                break;
            default:
                stateString = "UNKNOWN_STATE             -";
                break;
        }
        Log.d(TAG, "changed state to " + stateString + " playWhenReady: " + playWhenReady);
    }

    /** How do you know if your player is actually playing media? Well, all of the following conditions must be met:
     *
     * playback state is STATE_READY
     * playWhenReady is true
     * playback is not suppressed for some other reason (e.g. loss of audio focus)
     * Luckily, ExoPlayer provides a convenience method ExoPlayer.isPlaying for exactly this purpose!
     * Or, if you want to be kept informed when isPlaying changes, you can listen for onIsPlayingChanged.
     */
}