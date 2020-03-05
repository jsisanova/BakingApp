package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.utils.Constants;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class RecipeInstructionFragment extends Fragment {

    private ArrayList<Step> steps;
    private Step step;

    private TextView stepDescriptionTV;
    private Button previousButton;
    private Button nextButton;

    // Source: https://codelabs.developers.google.com/codelabs/exoplayer-intro/#0
    // variables for ExoPlayer
    private PlaybackStateListener playbackStateListener;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private int position;

    // Define a new interface OnStepSelectedListener that triggers a callback in the host activity
    RecipeDetailFragment.OnStepSelectedListener mCallback;

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (RecipeDetailFragment.OnStepSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepSelectedListener");
        }
    }

    // Mandatory empty constructor for the fragment manager to instantiate the fragment
    public RecipeInstructionFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the object in onCreate();
        steps = getArguments().getParcelableArrayList(Constants.STEPS_KEY);
        step = getArguments().getParcelable(Constants.STEP_KEY);

        if(((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(step.getStepShortDescription());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View rootview = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);

        // Instantiate the Listener
        playbackStateListener = new PlaybackStateListener();
        playerView = rootview.findViewById(R.id.stepVideoPlayerView);

        setupButtonsAndViews(rootview, step);

        previousButton.setOnClickListener(v -> {
            if (step.getStepId() == 0) {
                setupButtonsAndViews(rootview, step);
            } else {
                mCallback.onStepSelected(steps, (step.getStepId()) - 1);
            }
        });

        nextButton.setOnClickListener(v -> {
            if (step.getStepId() == (steps.size() - 1)) {
                setupButtonsAndViews(rootview, step);
            } else {
                mCallback.onStepSelected(steps, (step.getStepId()) + 1);
            }
        });

        if(savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean("playWhenReady");
            currentWindow = savedInstanceState.getInt("currentWindow");
            playbackPosition = savedInstanceState.getLong("playBackPosition");
        }

        // Return rootview
        return rootview;
    }

    // basic setup of buttons and views
    public void setupButtonsAndViews(View view, Step step) {
        stepDescriptionTV = view.findViewById(R.id.stepsDescriptionTextView);
        previousButton = view.findViewById (R.id.previousButton);
        nextButton = view.findViewById (R.id.nextButton);

        stepDescriptionTV.setText(step.getStepDescription());

        String stepUrl = step.getStepVideoUrl();
        if (stepUrl != null) {
            initializePlayer(stepUrl);
        } else {
            playerView.setVisibility(View.GONE);
        }

        if(step.getStepId() == 0) {
            previousButton.setVisibility(View.GONE);
        } else if(step.getStepId() == steps.size() - 1) {
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.GONE);
        } else {
            previousButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        }
    }



    // Code for Exoplayer
    // Starting with API level 24 Android supports multiple windows. As our app can be visible but not active in split window mode,
    // we need to initialize the player in onStart.
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(step.getStepVideoUrl());
        }
    }

    // Before API level 24 we wait as long as possible until we grab resources, so we wait until onResume before initializing the player.
    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(step.getStepVideoUrl());
        }
        player.setPlayWhenReady(true);
    }

    // Before API Level 24 there is no guarantee of onStop being called. So we have to release the player as early as possible in onPause.
    // Starting with API Level 24 (which brought multi and split window mode) onStop is guaranteed to be called.
    // In the paused state our activity is still visible so we wait to release the player until onStop.
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer(String stepUrl) {
        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            // To play streaming media we need to create an ExoPlayer object
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        }

        // Bind the Player to corresponding view
        playerView.setPlayer(player);

        // to avoid NullPointerException
        String str = (stepUrl == null) ? "NA" : stepUrl;
        Uri uri = Uri.parse(str);
        MediaSource mediaSource = buildMediaSource(uri);

        // Supply the state information we saved in releasePlayer to our player during initialization
        //
        // tells the player whether to start playing as soon as all resources for playback have been acquired.
        // Since playWhenReady is initially true, playback will start automatically the first time the app is run
        player.setPlayWhenReady(playWhenReady);
        // tells the player to seek to a certain position within a specific window.
        // Both currentWindow and playbackPosition were initialized to zero so that playback starts from the very start the first time the app is run.
        player.seekTo(currentWindow, playbackPosition);
        // tells the player to acquire all the resources for the given mediaSource,
        // and additionally tells it not to reset the position or state, since we already set these in the two previous lines.
        player.prepare(mediaSource, false, false);

        // register our playbackStateListener with the player
        player.addListener(playbackStateListener);
        player.prepare(mediaSource, false, false);
    }

    // Add some content to play
    // This method takes a Uri as its parameter, containing the URI of a media file
    //
    // There are many different types of MediaSource, but we'll start by creating a ExtractorMediaSource
    private MediaSource buildMediaSource(Uri uri) {
        //  First, we create a DefaultDataSourceFactory, specifying our context and the user-agent string which will be used when making the HTTP request for the media file.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), "exoplayer-codelab");
        // Next we pass our newly created dataSourceFactory to ExtractorMediaSource.Factory.
        // Before the video and/or audio data can be played it must be extracted from the container.
        // ExoPlayer is capable of extracting data from many different container formats using a variety of Extractor classes.
        // Finally, we call createMediaSource, supplying our uri, to obtain a MediaSource object
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        return mediaSource;
    }

    // Release resources with releasePlayer - frees up the player's resources and destroys it
    private void releasePlayer() {
        if (player != null) {
            // Before we release and destroy the player we store the following information:
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            // This will allow us to resume playback from where the user left off. All we need to do is supply this state information when we initialize our player.

            // we need to tidy up to avoid dangling references from the player which could cause a memory leak.
            player.removeListener(playbackStateListener);

            player.release();
            player = null;
        }
    }
}