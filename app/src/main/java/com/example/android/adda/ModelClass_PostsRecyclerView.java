package com.example.android.adda;

/**
 * Created by ktubuntu on 7/1/18.
 */

public class ModelClass_PostsRecyclerView {


    String imageLink, userId, postText, heading, postId, userName,totalLikes,totalComments;

    ModelClass_PostsRecyclerView() {


    }

    public ModelClass_PostsRecyclerView(String imageLink, String userId, String postText, String heading, String postId, String userName,String totalLikes,String totalComments) {
        this.imageLink = imageLink;
        this.userId = userId;
        this.postText = postText;
        this.heading = heading;
        this.postId = postId;
        this.userName = userName;
        this.totalLikes=totalLikes;
        this.totalComments=totalComments;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostText() {
        return postText;
    }

    public String getHeading() {
        return heading;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserName() {

        return userName;
    }

    public String getTotalComments() {
        return totalComments;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
    }
}
