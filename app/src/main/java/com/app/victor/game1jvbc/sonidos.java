package com.app.victor.game1jvbc;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class sonidos {

    private AudioAttributes audioAttributes;
    private static SoundPool soundPool;
    final int SOUND_POOL_MAX = 2;

    private static int gopearSound;
    private static int finSonido;
    private static int menosVida;

    public sonidos(Context context){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();

        }else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }
        gopearSound = soundPool.load(context, R.raw.comer_puntos, 1);
        finSonido = soundPool.load(context, R.raw.explosion, 1);
        menosVida = soundPool.load(context, R.raw.menos_vida, 1);
    }

    public void playGolpeSonido(){
        soundPool.play(gopearSound, 1.0f,1.0f, 1,0,1.0f);
    }
    public void playFinSonido(){
        soundPool.play(finSonido, 1.0f,1.0f, 1,0,1.0f);
    }
    public void playMenosVidaSonido(){
        soundPool.play(menosVida, 1.0f,1.0f, 1,0,1.0f);
    }
}
