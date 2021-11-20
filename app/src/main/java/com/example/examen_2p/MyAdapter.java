package com.example.examen_2p;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {

    private ArrayList<Contactos> arrayContacts;


    public MyAdapter(ContactsActivity contactsActivity, ArrayList<Contactos> arrayProducts) {
        this.arrayContacts = arrayProducts;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        //customViewHolder.imvPhoto.setImageBitmap(arrayContacts.get(i).getImg());
        customViewHolder.tvName.setText(arrayContacts.get(i).getName().toString());
    }

    @Override
    public int getItemCount() {
        return (null != arrayContacts ? arrayContacts.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView imvPhoto;

        public CustomViewHolder(View view) {
            super(view);
            //imvPhoto = view.findViewById(R.id.imvPhoto);
            tvName = view.findViewById(R.id.tvName);
        }
    }
}
