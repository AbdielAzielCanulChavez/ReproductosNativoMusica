package com.abdiel.tab_music.Controlllerpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.abdiel.tab_music.Fragmentos.AllMusic;
import com.abdiel.tab_music.Fragmentos.Albums;
import com.abdiel.tab_music.Fragmentos.PlayLists;

public class PagerController  extends FragmentPagerAdapter {


    int tabCounts; //contador de los tabs

    public PagerController( FragmentManager fm, int tabCounts) {
        super(fm);
        this.tabCounts = tabCounts;  //lo instanciamos en el constructor

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //creamos un switch para saber la eleccion del usario y nos mande ese parametro para poder visualizarlo y trabajarlo
        switch (position){
            case 0:
                return new AllMusic();
            case 1:
                return new Albums();
            case 2:
                return new PlayLists();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return tabCounts;
    }
}
