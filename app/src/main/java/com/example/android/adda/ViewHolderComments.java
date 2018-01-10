package com.example.android.adda;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ktubuntu on 10/1/18.
 */

public class ViewHolderComments extends RecyclerView.ViewHolder {

    View view;

    public ViewHolderComments(View itemView) {
        super(itemView);

        view=itemView;
    }

    public void setUserName(String userName){
        TextView textView=itemView.findViewById(R.id.id_userName_commentRow);
        textView.setText(userName);
    }

    public void setCommentBody(String commentBody){
        TextView textView=(TextView)itemView.findViewById(R.id.id_commentBody);
        textView.setText(commentBody);
    }
}
