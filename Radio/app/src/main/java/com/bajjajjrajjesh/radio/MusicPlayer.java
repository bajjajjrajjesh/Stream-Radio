package com.bajjajjrajjesh.radio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;

/**
 * Created by User on 2014.07.03..
 */
public class MusicPlayer {
    public static boolean playerStatus = false;
    private static ExoPlayer mediaPlayer;
    private ConnectivityManager  cm;
    private NetworkInfo netInfo;
    private RadioListElement radioListElement;
    private static String urlUpdater="default";
    private static String location;
    private DataSource dataSource;

    public static String getUrl() {
        return urlUpdater;
    }

    public MusicPlayer(final Context context) {
        mediaPlayer = ExoPlayer.Factory.newInstance(1);
        dataSource = new DefaultUriDataSource(context, getDefaultUserAgent());
    }

    public static ExoPlayer getMediaPlayer() {
        return mediaPlayer;
    }


    public static void stopRealMediaPlayer(){
        mediaPlayer.stop();
    }

    public static void playRealMediaPlayer(){
        mediaPlayer.setPlayWhenReady(true);
    }

    public void play(RadioListElement rle) {
        radioListElement=rle;
        playMusic(radioListElement.getUrl());
    }

    public static String getLocation() {
        return location;
    }

    public void playMusic(final String url){

        location = radioListElement.getFrequency();
        MainActivity.setViewPagerSwitch();
        MainActivity.startBufferingAnimation();
        MainActivity.getStartOrStopBtn().setImageResource(R.drawable.pause);
        try {
            mediaPlayer.stop();

            Uri uri = Uri.parse(url);
            ExtractorSampleSource extractorSampleSource = new ExtractorSampleSource(uri, dataSource, new DefaultAllocator(64 * 1024), 64 * 1024 * 256);
            MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(extractorSampleSource, MediaCodecSelector.DEFAULT);
            mediaPlayer.prepare(audioRenderer);
            mediaPlayer.setPlayWhenReady(true);

            mediaPlayer.addListener(new ExoPlayer.Listener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    //System.out.println("ELSOOOOOOOOOOOO"+playWhenReady+" "+playbackState);
                    if(playbackState==4){
                        MainActivity.stopBufferingAnimation();
                        playerStatus = true;
                    }else{
                        playerStatus = false;
                    }
                }

                @Override
                public void onPlayWhenReadyCommitted() {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    MainActivity.stopBufferingAnimation();
                    MainActivity.getRadioListLocation().setText("The radio is probably offline.");
                    location = "The radio is probably offline.";
                    MainActivity.getStartOrStopBtn().setImageResource(R.drawable.play);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getDefaultUserAgent() {
        StringBuilder result = new StringBuilder(64);
        result.append("Dalvik/");
        result.append(System.getProperty("java.vm.version")); // such as 1.1.0
        result.append(" (Linux; U; Android ");

        String version = Build.VERSION.RELEASE; // "1.0" or "3.4b5"
        result.append(version.length() > 0 ? version : "1.0");

        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ");
                result.append(model);
            }
        }
        String id = Build.ID; // "MASTER" or "M4-rc20"
        if (id.length() > 0) {
            result.append(" Build/");
            result.append(id);
        }
        result.append(")");
        return result.toString();
    }
}
