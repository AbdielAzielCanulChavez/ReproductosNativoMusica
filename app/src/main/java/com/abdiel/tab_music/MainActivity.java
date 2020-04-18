package com.abdiel.tab_music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.abdiel.tab_music.Controlllerpager.PagerController;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    //reservamos espacios en memoria

    Toolbar mToolbar;
    TabLayout mTabLayout;
    TabItem curMusic;
    TabItem allMusic;
    TabItem playList;
    ViewPager mPager;

    //instanciasmos el pagerController

    PagerController mPagerController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //rescatamos el valor del toolbar
        mToolbar = findViewById(R.id.toolbar);
        //insertamos el soporte de actionbar para darle el valor del toolbar
        setSupportActionBar(mToolbar);
        //traemos el soporte y le ponemos el titulo que queramos
        getSupportActionBar().setTitle("Tab Music");

        //rescatamos los demas valores
        mTabLayout = findViewById(R.id.tabLayout);
        curMusic = findViewById(R.id.currentMusic);
       // allMusic = findViewById(R.id.allMusic);
        //playList = findViewById(R.id.playList);
        mPager = findViewById(R.id.viewpager);

        //creamos un nuevo objeto de tipo pagerController
        //se agregamos el soporte de fragmentos y le agregamos los tabLayouts
        //activamos el que nos traiga el get de los tabs para saber el numero de tab clickedo
        mPagerController = new PagerController(getSupportFragmentManager(), mTabLayout.getTabCount());

        //le agregamos el adapter al mpager
        mPager.setAdapter(mPagerController);

        //creamos las opciones para que el TabLayout pueda escuchar
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //hacemos que mpager escuche el cambio de pager
        //creamos un objeto de tipo TabLayout para saber en que layout estamos
        //y le aderimos el mTabLayout para saber e cual esta y cambiar a la vista que le corresponde
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));



    }

}
