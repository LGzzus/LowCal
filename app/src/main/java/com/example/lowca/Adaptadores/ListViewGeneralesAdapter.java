package com.example.lowca.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lowca.Models.Ejercicios;
import com.example.lowca.Models.Generales;
import com.example.lowca.R;

import java.util.ArrayList;

public class ListViewGeneralesAdapter extends BaseAdapter {

    Context context;
    ArrayList<Generales> generalesData;
    LayoutInflater layoutInflater;
    Generales generalesModel;

    public ListViewGeneralesAdapter(Context context, ArrayList<Generales> generalesData) {
        this.context = context;
        this.generalesData = generalesData;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return generalesData.size();
    }

    @Override
    public Object getItem(int i) {
        return generalesData.get(i);
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
        TextView nombre = rowView.findViewById(R.id.txtEjercicio);
        TextView calxmin = rowView.findViewById(R.id.txtCalorias);
        TextView repeticiones = rowView.findViewById(R.id.txtDuracion);
        TextView sets = rowView.findViewById(R.id.txtSets);

        generalesModel = generalesData.get(i);
        nombre.setText(generalesModel.getNombre());
        calxmin.setText(generalesModel.getCalxmin()+" kcal/min");
        repeticiones.setText(generalesModel.getRepeticiones()+" R / ");
        sets.setText(generalesModel.getSets()+" S");
        return rowView;
    }
}
