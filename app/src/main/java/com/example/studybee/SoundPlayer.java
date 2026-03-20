package com.example.studybee;


import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

public class SoundPlayer {
    private MediaPlayer mediaPlayer;

    public void playSound(Context c, int soundNumber) {

        // če že nekaj igra → ustavi (prepreči overlap)
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        int resId;

        switch (soundNumber) {
            case 1:
                resId = R.raw.soundeffect_1;
                break;
            case 2:
                resId = R.raw.soundeffect_2;
                break;
            case 3:
                resId = R.raw.soundeffect_3;
                break;
            case 4:
                resId = R.raw.soundeffect_4;
                break;
            case 5:
                resId = R.raw.soundeffect_5;
                break;
            case 6:
                resId = R.raw.soundeffect_6;
                break;
            default:
                return; // invalid številka → nič ne naredi
        }

        mediaPlayer = MediaPlayer.create(c, resId);
        mediaPlayer.start();

        // auto cleanup da ne leak-a memory
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mediaPlayer = null;
        });
    }


    public void playBgSound(Context c, int soundNumber) {

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        int resId;

        switch (soundNumber) {
            case 1:
                resId = R.raw.fireplace;
                break;
            case 2:
                resId = R.raw.rain;
                break;
            case 3:
                resId = R.raw.oceanwaves;
                break;
            case 4:
                resId = R.raw.coffeshop;
                break;
            case 5:
                resId = R.raw.graynoise;
                break;
            default:
                return; // invalid številka → nič ne naredi
        }

        mediaPlayer = MediaPlayer.create(c, resId);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void turnOffPlyer(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
