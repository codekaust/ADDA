package com.example.android.adda;

/**
 * Created by ktubuntu on 10/1/18.
 */

public class ModelClassForComments {

    String userName,commentBody;

    ModelClassForComments(){}

    public ModelClassForComments(String userName, String commentBody) {
        this.userName = userName;
        this.commentBody = commentBody;
    }

    public String getUserName() {
        return userName;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

}
