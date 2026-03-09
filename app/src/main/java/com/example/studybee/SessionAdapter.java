package com.example.studybee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder>{

    List<Session> sessions;
    DatabaseHelper dbHelper;

    public SessionAdapter(List<Session> sessions, DatabaseHelper dbHelper){
        this.sessions = sessions;
        this.dbHelper = dbHelper;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView dateText;
        TextView startText;
        TextView endText;
        TextView durationText;
        ImageButton deleteButton;

        public ViewHolder(View itemView){
            super(itemView);

            dateText = itemView.findViewById(R.id.dateText);
            startText = itemView.findViewById(R.id.startText);
            endText = itemView.findViewById(R.id.endText);
            durationText = itemView.findViewById(R.id.durationText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        Session session = sessions.get(position);

        holder.dateText.setText("Date: " + session.date);
        holder.startText.setText("Start: " + session.start);
        holder.endText.setText("End: " + session.end);
        holder.durationText.setText("Duration: " + session.duration + " sec");

        holder.deleteButton.setOnClickListener(v -> {

            dbHelper.deleteSession(session.id);

            sessions.remove(position);

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, sessions.size());

        });
    }

    @Override
    public int getItemCount(){
        return sessions.size();
    }
}