package com.example.appbanthietbidientu.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private String userUsername; // Username retrieved from SharedPreferences
    private CommentDeleteListener commentDeleteListener; // Non-static listener

    public CommentAdapter(List<Comment> comments, String userUsername) {
        this.comments = comments;
        this.userUsername = userUsername;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.textViewUserId.setText(comment.getUserId());
        holder.textViewCommentText.setText(comment.getCommentText());
        // Convert timestamp to a readable date/time format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(new Date(comment.getTimestamp()));
        holder.textViewTimestamp.setText(formattedDate);

        // Check if the user has admin privileges (role = 1) or if the current comment's username matches the user's username
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(holder.itemView.getContext());
        String role = sharedPreferences.getString("role", "");
        if (role.equals("1") || comment.getUserId().equals(userUsername)) {
            holder.buttonDelete.setVisibility(View.VISIBLE);
        } else {
            holder.buttonDelete.setVisibility(View.GONE);
        }

        // Set onClickListener for the delete button
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentDeleteListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String commentId = String.valueOf(comments.get(position).getIdCmt());
                        commentDeleteListener.onCommentDelete(commentId);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserId;
        TextView textViewCommentText;
        TextView textViewTimestamp;
        ImageView buttonDelete;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserId = itemView.findViewById(R.id.textviewUserId);
            textViewCommentText = itemView.findViewById(R.id.textviewCommentText);
            textViewTimestamp = itemView.findViewById(R.id.textviewTimestamp);
            buttonDelete = itemView.findViewById(R.id.btn_Delete);

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commentDeleteListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            String commentId = String.valueOf(comments.get(position).getIdCmt());
                            commentDeleteListener.onCommentDelete(commentId);
                        }
                    }
                }
            });
        }
    }

    public interface CommentDeleteListener {
        void onCommentDelete(String commentId);
    }

    public void setCommentDeleteListener(CommentDeleteListener listener) {
        this.commentDeleteListener = listener;
    }
}
