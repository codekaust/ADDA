package com.example.android.adda;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

/**
 * Created by ktubuntu on 7/1/18.
 */

public class PostsListViewHolder extends RecyclerView.ViewHolder {

    View view;
    private DatabaseReference databaseReference;
    private PostsListViewHolder.ClickListener mClickListener;
    private ImageButton commentImageButton;
    private LikeButton likeButton;
    private FirebaseAuth firebaseAuth;

    public interface ClickListener{
        public void onItemClick(View view, int position);
        public void onLiked(View view, int position);
        public void onUnliked(View view, int position);
    }

    public PostsListViewHolder(View itemView) {
        super(itemView);

        view=itemView;

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");

        commentImageButton=(ImageButton)view.findViewById(R.id.id_commentImageButton);
        likeButton=(LikeButton)view.findViewById(R.id.id_likeStar_button);

        commentImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                mClickListener.onUnliked(likeButton, getAdapterPosition());
            }
        });

        likeButton.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {
                mClickListener.onLiked(likeButton, getAdapterPosition());
            }
        });
    }



    public void setOnClickListener(PostsListViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public void setHeading(String heading){
        TextView textView=(TextView)itemView.findViewById(R.id.id_postHeading);
        textView.setText(heading);
    }

    public void setTotalLikes(String totalLikes){
        TextView textView=(TextView)itemView.findViewById(R.id.id_totalLikes);
        textView.setText(totalLikes);
    }

    public void setPostId(String postId){
        TextView textView=(TextView)itemView.findViewById(R.id.id_postIdStorageTextView);
        textView.setText(postId);
        }

    public void setPostText(String postText){
        TextView textView=(TextView)itemView.findViewById(R.id.id_postTextPost);
        textView.setText(postText);
    }
    public void setUserName(String userName){
        TextView textView=(TextView)itemView.findViewById(R.id.id_postingUsername);
        textView.setText(userName);
    }

    public void setTotalComments(String totalComments){
        TextView textView=(TextView)itemView.findViewById(R.id.id_totalNoOfComments);
        Log.v("Check no of comments","test");
        textView.setText(totalComments);
        Log.v("Check no of comments",textView.getText().toString());

    }

    public void setImage(Context ctx,String imageLink){
        ImageView imageView=(ImageView)itemView.findViewById(R.id.id_imagePost);
        if(imageLink!=null){
            Picasso.with(ctx).load(imageLink).into(imageView);
        }
        else {
            imageView.setPadding(0,0,0,0);
        }
    }
}
