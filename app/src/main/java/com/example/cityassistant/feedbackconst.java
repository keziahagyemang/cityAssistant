package com.example.cityassistant;

public class feedbackconst {

    String feedback;
    String userId;
    String date;

    public feedbackconst(){

    }

    public feedbackconst(String feedback, String userId, String date){
        this.feedback = feedback;
        this.userId = userId;
        this.date = date;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
