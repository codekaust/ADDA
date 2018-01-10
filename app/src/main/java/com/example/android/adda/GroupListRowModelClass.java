package com.example.android.adda;

/**
 * Created by ktubuntu on 5/1/18.
 */

public class GroupListRowModelClass {
    String groupName;

    public GroupListRowModelClass(){
    }

    public GroupListRowModelClass(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
