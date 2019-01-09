package com.example.android.miwok;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if(i == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT || i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }else if(i == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            }else if(i == AudioManager.AUDIOFOCUS_LOSS) {
                mediaPlayer.stop();
                releaseMediaPlayer();
            }
        }
    };

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> a = new ArrayList<Word>();


        a.add(new Word("one","lutti",R.drawable.number_one, R.raw.number_one));
        a.add(new Word("two","otiiko",R.drawable.number_two, R.raw.number_two));
        a.add(new Word("three","tolookosu",R.drawable.number_three, R.raw.number_three));
        a.add(new Word("four","oyyisa",R.drawable.number_four, R.raw.number_four));
        a.add(new Word("five","massokka",R.drawable.number_five, R.raw.number_five));
        a.add(new Word("six","temmokka",R.drawable.number_six, R.raw.number_six));
        a.add(new Word("seven","kenekaku",R.drawable.number_seven, R.raw.number_seven));
        a.add(new Word("eight","kawinta",R.drawable.number_eight, R.raw.number_eight));
        a.add(new Word("nine","wo'e",R.drawable.number_nine, R.raw.number_nine));
        a.add(new Word("ten","na'aacha",R.drawable.number_ten, R.raw.number_ten));

        WordAdapter wordAdapter=new WordAdapter(this, a, R.color.category_numbers);
        ListView listView=(ListView)findViewById(R.id.number);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                releaseMediaPlayer();

                Word word = a.get(i);

                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback


                mediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getAudioResourceId() );
                mediaPlayer.start();

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Toast.makeText(getApplicationContext(), "I'm done", Toast.LENGTH_SHORT).show();
                    }
                });

            }}
        });
    }
    }
