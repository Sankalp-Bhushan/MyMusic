package com.example.mymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button b1,b2,b3,b4;
    private ImageView iv;
    private ArrayList<MediaPlayer> mediaPlayer;
    private int index = 0;

    private int count = 0;
    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;

    public static int oneTimeOnly = 0;

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button)findViewById(R.id.button3);
        b4 = (Button)findViewById(R.id.button4);
        iv = (ImageView)findViewById(R.id.imageView);

        tx1 = (TextView)findViewById(R.id.textView2);
        tx2 = (TextView)findViewById(R.id.textView3);
        tx3 = (TextView)findViewById(R.id.textView4);

        mediaPlayer = new ArrayList<>();
        mediaPlayer.add(MediaPlayer.create(this, R.raw.alan_walker_faded));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.apne));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.army));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.aye_watan));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.banggtown));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.chan_chan));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.dillagi_rahat_fatehi));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.lambiyaan_si_judaiyaan));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.let_me_love_you));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.teri_mitti_kesari));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.the_chainsmokers_closer));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.the_script_hall_of_fame));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.the_wakhra_swag_hard));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.thokar_mp3_punjabi_song));
        mediaPlayer.add(MediaPlayer.create(this,R.raw.yaar_tera_chetak_par_chale_tane_chaska_red_frarika));

        tx3.setText("Song is playing");

        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setClickable(true);

        // used to set ability of any button or text to response any touches
       // b2.setEnabled(false);

        b2.setText(R.string.back);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    count = 1;

                    b2.setText(R.string.pause);
                    Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                    // change here
                    mediaPlayer.get(index).start();
                    // change here
                    finalTime = mediaPlayer.get(index).getDuration();
                    startTime = mediaPlayer.get(index).getCurrentPosition();

                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }

                    tx2.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime)))
                    );

                    tx1.setText(String.format("%d :, %d :",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            startTime)))
                    );

                    seekbar.setProgress((int) startTime);
                    myHandler.postDelayed(UpdateSongTime, 100);

                } else {
                    count = 0;
                    b2.setText(R.string.back);
                    Toast.makeText(getApplicationContext(), "Pausing sound",Toast.LENGTH_SHORT).show();

                    //change here
                    mediaPlayer.get(index).pause();

                }
            }
        });

        /*
        * to start new song after completion of first song
        */
        mediaPlayer.get(index).setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                index++;
                if(index >= mediaPlayer.size()) index = 0;
                count = 0;
                b2.setText(R.string.back);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.get(index).release();
                index++;
                if(index >= mediaPlayer.size()) index = 0;
                count = 0;
                b2.setText(R.string.back);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;

                    // change here
                    mediaPlayer.get(index).seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp-backwardTime)>0){
                    startTime = startTime - backwardTime;

                    //change here
                    mediaPlayer.get(index).seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void jump(View view){
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mediaPlayer.get(index).seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected void onStop(){
        super.onStop();
        myHandler.removeCallbacks(UpdateSongTime);
        mediaPlayer.get(index).stop();
        finish();
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {

            // change here
            startTime = mediaPlayer.get(index).getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

}