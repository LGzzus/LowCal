package com.example.lowca.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lowca.Models.Rutinas;
import com.example.lowca.R;

import java.util.ArrayList;

public class ListViewRutinasAdapter extends BaseAdapter {
    Context context;
    ArrayList<Rutinas> rutinasData;
    LayoutInflater layoutInflater;
    Rutinas rutinasModel;

    public ListViewRutinasAdapter(Context context, ArrayList<Rutinas> rutinasData){
        this.context = context;
        this.rutinasData = rutinasData;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return rutinasData.size();
    }

    @Override
    public Object getItem(int i) {
        return rutinasData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Aqui
        View rowView = view;

        if (rowView == null){
            rowView = layoutInflater.inflate(R.layout.lista_rutinas, null, true);
        }

        TextView areas = rowView.findViewById(R.id.txtAreas);
        TextView calQuemadas = rowView.findViewById(R.id.txtCalQuemadas);
        TextView diaAsignado = rowView.findViewById(R.id.txtDiaAsignado);
        TextView nombre = rowView.findViewById(R.id.txtNombre);

        TextView nombreEje1 = rowView.findViewById(R.id.txtNombreEje1);
        TextView nombreEje2 = rowView.findViewById(R.id.txtNombreEje2);
        TextView nombreEje3 = rowView.findViewById(R.id.txtNombreEje3);

        TextView repeEje1 = rowView.findViewById(R.id.txtRepeEje1);
        TextView repeEje2 = rowView.findViewById(R.id.txtRepeEje2);
        TextView repeEje3 = rowView.findViewById(R.id.txtRepeEje3);

        TextView setsEje1 = rowView.findViewById(R.id.txtSetsEje1);
        TextView setsEje2 = rowView.findViewById(R.id.txtSetsEje2);
        TextView setsEje3 = rowView.findViewById(R.id.txtSetsEje3);

        rutinasModel = rutinasData.get(i);

        areas.setText(rutinasModel.getAreas());
        calQuemadas.setText(rutinasModel.getCalQuemadas());
        diaAsignado.setText(rutinasModel.getDiaAsignado());
        nombre.setText(rutinasModel.getNombre());

        nombreEje1.setText(rutinasModel.getNombreEje1());
        nombreEje2.setText(rutinasModel.getNombreEje2());
        nombreEje3.setText(rutinasModel.getNombreEje3());

        repeEje1.setText(rutinasModel.getRepeEje1());
        repeEje2.setText(rutinasModel.getRepeEje2());
        repeEje3.setText(rutinasModel.getRepeEje3());

        setsEje1.setText(rutinasModel.getSetsEje1());
        setsEje2.setText(rutinasModel.getSetsEje2());
        setsEje3.setText(rutinasModel.getSetsEje3());

        return rowView;
    }
}
