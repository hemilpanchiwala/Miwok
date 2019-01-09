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

public class FamilyActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }else if (i == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            }else if (i == AudioManager.AUDIOFOCUS_LOSS) {
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

        a.add(new Word("father","әpә",R.drawable.family_father, R.raw.family_father));
        a.add(new Word("mother","әṭa",R.drawable.family_mother, R.raw.family_mother));
        a.add(new Word("son","angsi",R.drawable.family_son, R.raw.family_son));
        a.add(new Word("daughter","tune",R.drawable.family_daughter, R.raw.family_daughter));
        a.add(new Word("older brother","taachi",R.drawable.family_older_brother, R.raw.family_older_brother));
        a.add(new Word("younger brother","chalitti",R.drawable.family_younger_brother, R.raw.family_younger_brother));
        a.add(new Word("older sister","teṭe",R.drawable.family_older_sister, R.raw.family_older_sister));
        a.add(new Word("younger sister","kolliti",R.drawable.family_younger_sister, R.raw.family_younger_sister));
        a.add(new Word("grandmother","ama",R.drawable.family_grandmother, R.raw.family_grandmother));
        a.add(new Word("grandfather","paapa",R.drawable.family_grandfather, R.raw.family_grandfather));

        WordAdapter wordAdapter=new WordAdapter(this, a, R.color.category_family);
        ListView listView=(ListView)findViewById(R.id.number);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                releaseMediaPlayer();

                Word word = a.get(i);

                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                mediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getAudioResourceId() );
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
