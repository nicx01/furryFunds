package com.nodejes.furryfunds;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicFondoService extends Service {
    private static MediaPlayer mediaPlayer;
    private MediaPlayer nextMediaPlayer;


    @Override
    public void onCreate() {
        super.onCreate();
        if (mediaPlayer == null) {  // Solo crea el MediaPlayer si no existe
            iniciarMusica();
        }
    }

    private void iniciarMusica() {
        mediaPlayer = MediaPlayer.create(this, R.raw.musica_china_fondo);
        nextMediaPlayer = MediaPlayer.create(this, R.raw.musica_china_fondo);

        // Configurar el siguiente audio antes de que termine el primero
        mediaPlayer.setNextMediaPlayer(nextMediaPlayer);

        mediaPlayer.setOnCompletionListener(mp -> {
            mediaPlayer.release();
            mediaPlayer = nextMediaPlayer;
            prepararNuevoMediaPlayer();
        });

        mediaPlayer.start();
    }

    private void prepararNuevoMediaPlayer() {
        nextMediaPlayer = MediaPlayer.create(this, R.raw.musica_china_fondo);
        mediaPlayer.setNextMediaPlayer(nextMediaPlayer);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
