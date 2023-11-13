package com.example.lowca.dieta.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lowca.dietaAsignada.view.Dieta2;
import com.example.lowca.dieta.modelo.ListDietas;
import com.example.lowca.R;

import java.util.List;


public class ListAdapterDietas extends RecyclerView.Adapter<ListAdapterDietas.ViewHolder> {
    private List<ListDietas> mData;
    public ListDietas listDietasModel;
    private LayoutInflater mInflater;
    public Context context;
    public Context cont;
    private OnItemClickListener mClickListener;

    private String[] mDatosStrings;
    Bundle pasarDatos;
    private Dieta mDietaFragment;

   // private int selectedItemPosition = RecyclerView.NO_POSITION;
    private int selectedItemPosition = -1;




    public ListAdapterDietas(List<ListDietas> itemList, Context context){
        this.mInflater= LayoutInflater.from(context);
        this.context=context;
        this.mData=itemList;
    }
    @Override
    public int getItemCount(){return mData.size();}


   public void setOnItemClickListener(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }



    public interface OnItemClickListener {
       void onItemClick(int position);

   }
    public void setDietaFragment(Dieta dietaFragment) {
        mDietaFragment = dietaFragment;
    }


    @Override
    public ListAdapterDietas.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = mInflater.inflate(R.layout.list_dieta, null);
        return new ListAdapterDietas.ViewHolder(view);
    }

    /**  Revisar estos metodos */

    public void setSelectedItem(int position) {
        selectedItemPosition = position;
        notifyDataSetChanged();
    }
    public void setSelectedItemPosition(int position) {
        selectedItemPosition = position;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(final ListAdapterDietas.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));


        if (selectedItemPosition == position) {
            holder.layoutDietas.setBackgroundColor(Color.BLUE); // Cambia el color de fondo deseado

        } else {
            holder.layoutDietas.setBackgroundColor(Color.WHITE); // Cambia el color de fondo deseado
        }

    }
    //hasta aca llega


    public  void setItems(List<ListDietas>items){mData=items;}

   /* public  void setDataToSend(String [] datosString){
        mDatosStrings=datosString;
    }*/
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewDieta;
        TextView dieta,calorias;
       LinearLayout layoutDietas;

        ViewHolder(View itemView){
            super(itemView);
            //imageViewDieta=itemView.findViewById(R.id.imageViewDieta);
            dieta=itemView.findViewById(R.id.nombreDieta);
            calorias=itemView.findViewById(R.id.caloriasDieta);
            layoutDietas=itemView.findViewById(R.id.layoutDietas);

            layoutDietas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mClickListener.onItemClick(position);
                            Context contexto=view.getContext();
                            Intent intent= new Intent(contexto, Dieta2.class);

                            if (mDietaFragment != null) {
                                // Obtener el elemento de la lista en la posición seleccionada
                                ListDietas dieta = mData.get(position);
                                // Pasar los datos al fragment Dieta
                                mDietaFragment.recibirDatos(dieta.getTipoDieta(), dieta.getCalorias(), dieta.getInfoDieta(),dieta.getId_dieta(),dieta.getDieta());
                            }


                        }
                    }
                }
            });


        }


       void  bindData(final ListDietas item){
            dieta.setText(item.getDieta());
            calorias.setText(item.getCalorias());
        }


    }

}
