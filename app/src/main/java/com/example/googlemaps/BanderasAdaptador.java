package com.example.googlemaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BanderasAdaptador extends ArrayAdapter<Banderas> {

    public BanderasAdaptador(Context context, ArrayList<Banderas> datos) {
        super (context, R.layout.lyitem, datos);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.lyitem, null);

        TextView lblNombreBandera = (TextView)item.findViewById(R.id.lblNombreBandera);
        lblNombreBandera.setText(getItem(position).getNombrebandera());

        ImageView imageView = (ImageView)item.findViewById(R.id.imgBandera);
        Glide.with(this.getContext())
                .load(getItem(position).getImagenbandera())
                .into(imageView);

        return(item);
    }

}
