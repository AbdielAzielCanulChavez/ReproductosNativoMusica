package com.abdiel.tab_music.Fragmentos;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.abdiel.tab_music.Actividades.Player;
import com.abdiel.tab_music.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterActivity;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMusic extends Fragment {

    //instanciamos el listview del xml
    ListView allMusicList;
    //creamos un arreglo que es donde se alojaran el adapter para las canciones
    ArrayAdapter<String> musicArrayAdapter;
    //creamos un array string que sera para el nombre de las canciones
    String songs[];

    ArrayList<File> musics;


    public AllMusic() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view=inflater.inflate(R.layout.fragment_all_music, container, false);
      //rescatamos el list view del xml
        allMusicList = view.findViewById(R.id.musicList);

        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                //muestra la lista de musica
                musics = findMusicFiles(Environment.getExternalStorageDirectory());
                //guardamos en un arrayList
                songs = new String[musics.size()];

                //validamos si existen datos en el arayGuardados
                for(int i = 0; i < musics.size(); i++){
                    //rescatamos las cancines su id y el nombre de la cancion que tiene
                    songs[i] = musics.get(i).getName();
                }
                //traemos la lista para hacerle set con el adaptador
                musicArrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,songs);
                allMusicList.setAdapter(musicArrayAdapter);

                //handle song click on list
                allMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent player = new Intent(getActivity(), Player.class);
                        //enviamos el detalle de la cacncion
                        player.putExtra("songFileList", musics);
                        player.putExtra("position", position);
                        startActivity(player);
                    }
                });


            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
               //aqui pregunta sobre los permisos que se le daran a la app
                token.continuePermissionRequest();
            }
        }).check();

      return view;
    }

    //cresmos una clase para buscar los archivos de musica
    private ArrayList<File> findMusicFiles(File file){
        //creamos un objeto para los archivos
        ArrayList<File> allMusicFilesObject = new ArrayList<>();
        File[] files = file.listFiles();

        for(File currentFile: files){
            //validamos si existen archivos en el direcctorio y si son ocultos o no
            if(currentFile.isDirectory() && !currentFile.isHidden()){
               //agregamos todos al array que los almacenara
                allMusicFilesObject.addAll(findMusicFiles(currentFile));
            }else{

                //creamos la condicion que solo nos traiga archivos con .mp3 y las opciones que queramos
                if(currentFile.getName().endsWith(".mp3") || currentFile.getName().endsWith(".mp4a") || currentFile.getName().endsWith(".wav")){
                    allMusicFilesObject.add(currentFile);
                }

            }
        }
        //retornamos la lista llena
        return allMusicFilesObject;
    }

}
