package com.example.lowca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private ViewPager view1;

    private LinearLayout inicio;
    private LinearLayout coach;
    private LinearLayout add;
    private LinearLayout dieta;
    private LinearLayout perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view1= (ViewPager) findViewById(R.id.view);
        view1.setAdapter(new AdminPpaginaAdapter());
    }
    class AdminPpaginaAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position){
            View paginaActual = null;
            switch (position){
                case 0:
                    if (inicio == null){
                        inicio = (LinearLayout)
                                LayoutInflater.from(MainActivity.this).inflate(R.layout.inicio,null);
                    }
                    paginaActual = inicio;
                    break;
                case 1:
                    if(coach == null){
                        coach = (LinearLayout)
                                LayoutInflater.from(MainActivity.this).inflate(R.layout.coach,null);
                    }
                    paginaActual = coach;
                    break;
                case 2:
                    if(add == null){
                        add = (LinearLayout)
                                LayoutInflater.from(MainActivity.this).inflate(R.layout.agregarmas,null);
                    }
                    paginaActual = add;
                    break;
                case 3:
                    if(dieta == null){
                        dieta = (LinearLayout)
                                LayoutInflater.from(MainActivity.this).inflate(R.layout.dieta,null);
                    }
                    paginaActual = dieta;
                    break;
                case 4:
                    if (perfil == null){
                        perfil = (LinearLayout)
                                LayoutInflater.from(MainActivity.this).inflate(R.layout.perfil, null);
                    }
                    paginaActual = perfil;
                    break;
            }
            ViewPager vp = (ViewPager) collection;
            vp.addView(paginaActual, 0);
            return paginaActual;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView((View) view);
        }

    }
    public void irPagina1(View v) { view1.setCurrentItem(0); }
    public void irPagina2(View v) { view1.setCurrentItem(1); }
    public void irPagina3(View v) { view1.setCurrentItem(2); }
    public void irPagina4(View v) { view1.setCurrentItem(3); }
    public void irPagina5(View v) { view1.setCurrentItem(4); }


}