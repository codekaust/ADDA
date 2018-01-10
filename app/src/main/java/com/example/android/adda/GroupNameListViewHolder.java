package com.example.android.adda;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ktubuntu on 5/1/18.
 */

public class GroupNameListViewHolder extends RecyclerView.ViewHolder {

    View view;

    private GroupNameListViewHolder.ClickListener mClickListener;

    public GroupNameListViewHolder(View itemView) {
        super(itemView);
        view=itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

    }

    public interface ClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnClickListener(GroupNameListViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public void setGroupName(String groupName){
        TextView textView=(TextView)itemView.findViewById(R.id.id_groupName_textView);
        textView.setText(groupName);
    }
}
