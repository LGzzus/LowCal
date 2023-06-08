package com.example.lowca.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lowca.Models.Alimentos;
import com.example.lowca.R;

import java.util.ArrayList;

public class ListViewAlimentosAdapter extends BaseAdapter {
    Context context;
    ArrayList<Alimentos> alimentosData;
    LayoutInflater layoutInflater;
    Alimentos alimentosModel;

    public ListViewAlimentosAdapter(Context context, ArrayList<Alimentos> alimentosData){
        this.context = context;
        this.alimentosData = alimentosData;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return alimentosData.size();
    }

    @Override
    public Object getItem(int i) {
        return alimentosData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View rowView = convertView;
        if (rowView == null){
            rowView = layoutInflater.inflate(R.layout.lista_alimentos, null, true);
        }
        //enlazar vistas
        TextView alimento = rowView.findViewById(R.id.txtViewAlimento);
        TextView calorias = rowView.findViewById(R.id.txtVIewAlimentoGramos);
        TextView cantidad = rowView.findViewById(R.id.txtVIewAlimentoCantidad);
        alimentosModel = alimentosData.get(i);
        alimento.setText(alimentosModel.getAlimento());
        calorias.setText(alimentosModel.getCalorias());
        cantidad.setText(alimentosModel.getCantidad());

        return rowView;
    }
}
