package com.example.studybee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Doeski_Adapter extends RecyclerView.Adapter<Doeski_Adapter.ViewHolder>{

    List<Dosezek> doseski;
    DatabaseHelper dbHelper;

    public Doeski_Adapter(List<Dosezek> doseski, DatabaseHelper dbHelper){
        this.doseski = doseski;
        this.dbHelper = dbHelper;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView description;
        TextView date;

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.discription);
            date = itemView.findViewById(R.id.date);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.achievement_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        Dosezek dosezek = doseski.get(position);

        holder.name.setText(dosezek.name);
        holder.description.setText(dosezek.description);
        holder.date.setText(dosezek.date);

        if(dosezek.date == null){
            holder.date.setText("Locked");
        }else{
            holder.date.setText("Unlocked: " + dosezek.date);
        }

        if(!dosezek.hidden){
            holder.description.setText("???????????");
            holder.date.setText("hidden trophy");
        }

    }

    @Override
    public int getItemCount(){
        return doseski.size();
    }
}