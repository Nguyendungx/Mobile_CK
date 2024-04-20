package com.example.appbanthietbidientu.model;

public class Comment {
    private int idSp;
    private String userId;
    private String commentText;
    private long timestamp;


    private long idCmt;
    private String status; // Thuộc tính trạng thái

    public Comment(int idSp, String userId, String commentText, long timestamp, int idCmt, String status) {
        this.idSp = idSp;
        this.userId = userId;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.idCmt = idCmt;
        this.status = status; // Khởi tạo thuộc tính trạng thái
    }

    public Comment() {
        // Constructor không tham số
    }
    public long getIdCmt() {
        return idCmt;
    }

    public void setIdCmt(long idCmt) {
        this.idCmt = idCmt;
    }


    public int getIdSp() {
        return idSp;
    }

    public void setIdSp(int idSp) {
        this.idSp = idSp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "idSp=" + idSp +
                ", userId='" + userId + '\'' +
                ", commentText='" + commentText + '\'' +
                ", timestamp=" + timestamp +
                ", status='" + status + '\'' +
                '}';
    }
}
