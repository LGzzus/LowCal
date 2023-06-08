package com.example.lowca.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lowca.Models.Ejercicios;
import com.example.lowca.R;

import java.util.ArrayList;

public class ListViewEjerciciosAdapter extends BaseAdapter {

    Context context;
    ArrayList<Ejercicios> ejerciciosData;
    LayoutInflater layoutInflater;
    Ejercicios ejerciciosModel;

    public ListViewEjerciciosAdapter(Context context, ArrayList<Ejercicios> ejerciciosData) {
        this.context = context;
        this.ejerciciosData = ejerciciosData;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return ejerciciosData.size();
    }

    @Override
    public Object getItem(int i) {
        return ejerciciosData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View rowView = convertView;
        if (rowView == null){
            rowView = layoutInflater.inflate(R.layout.lista_ejercicios, null, true);
        }
        //enlazar vistas
        TextView ejercicio = rowView.findViewById(R.id.txtEjercicio);

        ejerciciosModel = ejerciciosData.get(i);
        ejercicio.setText(ejerciciosModel.getEjercicio());
        return rowView;    }
}
