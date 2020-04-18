package com.abdiel.tab_music.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.abdiel.tab_music.R;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {

    Bundle songExtraData;
    ArrayList<File> songFileList;

    SeekBar mSeekBar;
    TextView mSongTitle;
    ImageView playBTN;
    ImageView nextBtn;
    ImageView prevBTN;
    TextView currentTime;
    TextView totalTime;



    static MediaPlayer mMediaPlayer; //poniendole static evitamos que se sobre pongan las canciones
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //rescatamos los componentes del xml
        mSeekBar = findViewById(R.id.musicSeekerBar);
        mSongTitle = findViewById(R.id.songTitle1);
        playBTN = findViewById(R.id.playBTN1);
        nextBtn = findViewById(R.id.nextBTN);
        prevBTN = findViewById(R.id.previusBtn1);
        currentTime = findViewById(R.id.currentTimer);
        totalTime = findViewById(R.id.totalTimer);

        //verificamos si media player es nulo o no
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
        }

        //rescatamos los datos que nos mando el fragment de allMusic
        Intent songData = getIntent();
        //alojamos los datos en el bundle
        songExtraData = songData.getExtras();



        songFileList = (ArrayList) songExtraData.getParcelableArrayList("songFileList");
         position = songExtraData.getInt("position", 0);
        initMusicPlyer(position); //mandamos a llamar al metodo para realizar los cambios

        //configuracion de play y pause

        playBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cuando el boton play pause es accionado
                play();
            }
        });

        //coniguracion del boton next

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creamos la condicion de que si la posision es mayor que el tamanio del arrelgo
                //verificamos si la posision actual de la cancion en la lista es menor al total de cacniones presentadas
                if(position<songFileList.size()-1){
                    position++;
                }else{
                    //si la posision es mayor o igual al numero de canciones en la lista
                    //insertamos la posision
                    position = 0;

                }
                //iniciamos la cancion de la lista con la posision

                initMusicPlyer(position);

            }
        });


        //configuracion boton previus

        prevBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //si la posision de la cancion en la lista es menor o igual a 0

                if(position<=0){
                    position = songFileList.size()-1;
                }else {
                    position--;
                }
                initMusicPlyer(position);
            }
        });

    }//onCreate

    private void initMusicPlyer(final int position){
        //creamos una condicion la cual si el nos permite saber si el mediaplayer esta funcionando
        //en dado caso de que no funcione lo reseete
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.reset();
        }

        //traemos los nombre de la lista de caciones, extraemos el nombre
        String name = songFileList.get(position).getName();
        //se lo pegamos al songtittle
        mSongTitle.setText(name);

        //traemos las canciones del path de la sd

        Uri songResourseUri =  Uri.parse(songFileList.get(position).toString());

        //creamos a media player

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), songResourseUri);

        //ahora podemos iniciar las canciones para usarlas

        mMediaPlayer.start();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //set seekbar maximun duration
                mSeekBar.setMax(mMediaPlayer.getDuration());

                //agregamos el maximo de duracion de la cancion
                String totTime = createTimerLabel(mMediaPlayer.getDuration());
                totalTime.setText(totTime);

                //iniciamos la cancion
                mMediaPlayer.start();

                //insertamos el icono de pausa
                playBTN.setImageResource(R.drawable.ic_pause);
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //cuando la cancion este finalizada
                //playBTN.setImageResource(R.drawable.ic_play);

                int currentSongPosition = position;

                if(currentSongPosition < songFileList.size()-1){
                    currentSongPosition++;
                }else {
                    currentSongPosition = 0;
                }
                initMusicPlyer(currentSongPosition);
            }
        });

        //configuracion seekbar

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //cuando el seeekbar tiene cambios
                if(fromUser){
                    mMediaPlayer.seekTo(progress); //seek de la cancion
                    mSeekBar.setProgress(progress); // set

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //configuracion del seekbar para que cambie con la duracion de la cancion

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null){
                    try {
                        if(mMediaPlayer.isPlaying()){
                            Message message = new Message();
                            message.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);

                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();



    }//initMusic

    //creamos el handler para el proceso
    @SuppressLint("HandlerLeak")
    private Handler handler =  new Handler(){
        @Override
        public void handleMessage(Message msg){
            //aqui insertamos el tiempo real en el avanze de la cancion
            currentTime.setText(createTimerLabel(msg.what));
            mSeekBar.setProgress(msg.what);
        }
    };//Handler

    private void play(){
        if(mMediaPlayer!= null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            playBTN.setImageResource(R.drawable.ic_play);
        }else {
            mMediaPlayer.start();
            playBTN.setImageResource(R.drawable.ic_pause);
        }
    }//play()


    //metodo para poder limpiar los timer derecho e izquierdo
    public String createTimerLabel(int duration){
        String timerLabel = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        timerLabel+= min+":";
        timerLabel += sec;

        return timerLabel;
    }
}
