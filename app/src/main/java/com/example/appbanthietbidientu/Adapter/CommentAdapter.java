package com.example.appbanthietbidientu.Adapter;

import android.content.Context;
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
    private static List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
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

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
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
                            int commentId = (int) comments.get(position).getIdCmt();
                            commentDeleteListener.onCommentDelete(String.valueOf(commentId));
                        }
                    }
                }
            });
        }

    }
    public interface CommentDeleteListener {
        void onCommentDelete(String commentId);
    }
    private static CommentDeleteListener commentDeleteListener;

    public void setCommentDeleteListener(CommentDeleteListener listener) {
        this.commentDeleteListener = listener;
    }


}
