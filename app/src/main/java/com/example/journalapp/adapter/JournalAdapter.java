package com.example.journalapp.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.journalapp.Model.Journal;
import com.example.journalapp.R;

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {

    private Context context;
    private List<Journal> journalList;

    public JournalAdapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_journal_item, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Journal journal = journalList.get(position);
        String imageUrl;

        holder.title.setText(journal.getTitle());
        holder.description.setText(journal.getDescription());
        holder.name.setText(journal.getUsername());
        imageUrl = journal.getImageUrl();

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(
                journal.getTimestamp()
                        .getSeconds()*1000);
        holder.dateAdded.setText(timeAgo);

        Glide.with(context)
                .load(imageUrl)
                //.placeholder()
                .fitCenter()
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    // viewholder
    public class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView title, description, dateAdded, name;
        public ImageView image;
        public ImageView shareButton;
        String userId;
        String username;


        public ViewHolder(@NonNull View itemView, Context cntx) {
            super(itemView);

            context = cntx;
            title = itemView.findViewById(R.id.journal_title_list);
            description = itemView.findViewById(R.id.journal_description_list);
            dateAdded = itemView.findViewById(R.id.journal_timestamp_list);

            image = itemView.findViewById(R.id.journal_image_list);
            name = itemView.findViewById(R.id.journal_row_username);

            shareButton = itemView.findViewById(R.id.journal_row_share_button);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Sharing the Post!!
                }
            });
        }
    }



}