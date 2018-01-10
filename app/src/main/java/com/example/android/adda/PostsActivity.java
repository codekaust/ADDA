package com.example.android.adda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.BoringLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.like.LikeButton;

import java.util.Map;
import java.util.Set;

public class PostsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private EditText editNewPostText, editNewPostHeading;
    private TextView addPost_TextView;
    private ImageView newPostImage;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private AlertDialog.Builder builderForComments;
    private AlertDialog dialogComments;
    private View newPostView;
    private final int NEW_POST_GALLERY_REQ = 2;
    private Uri uri;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerList;
    private View commentsView;
    private RecyclerView recyclerViewComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(HomeActivity.clickedGroupGroup).child(HomeActivity.clickedGroup);
        storageReference = FirebaseStorage.getInstance().getReference().child(HomeActivity.clickedGroupGroup).child(HomeActivity.clickedGroup);

        newPostView = getLayoutInflater().inflate(R.layout.add_post_layout, null);

        builder = new AlertDialog.Builder(PostsActivity.this);
        builder.setView(newPostView);
        dialog = builder.create();

        builderForComments = new AlertDialog.Builder(PostsActivity.this);

        editNewPostHeading = (EditText) newPostView.findViewById(R.id.id_edit_new_postHeading);
        editNewPostText = (EditText) newPostView.findViewById(R.id.id_editNewPostText);
        addPost_TextView = (TextView) newPostView.findViewById(R.id.id_addPost_textView);
        newPostImage = (ImageView) newPostView.findViewById(R.id.id_imageNewPost);

        recyclerList = (RecyclerView) findViewById(R.id.id_postList_recyclerView);
        recyclerList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PostsActivity.this);
        recyclerList.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        ///newPostAdditionToCurrentGroup
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.id_addPost_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        //newPostAdditionToCurrentGroup
        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, NEW_POST_GALLERY_REQ);
            }
        });

        ///newPostAdditionToCurrentGroup
        addPost_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference newPostDatabaseRef = databaseReference.push();

                if (!TextUtils.isEmpty(editNewPostHeading.getText().toString()) && !TextUtils.isEmpty(editNewPostText.getText().toString())) {

                    progressDialog.setMessage("Uploading");

                    if (uri != null) {

                        progressDialog.show();

                        storageReference.child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                newPostDatabaseRef.child("heading").setValue(editNewPostHeading.getText().toString().trim());
                                newPostDatabaseRef.child("postText").setValue(editNewPostText.getText().toString().trim());
                                newPostDatabaseRef.child("userId").setValue(firebaseAuth.getCurrentUser().getUid().toString());
                                newPostDatabaseRef.child("imageLink").setValue(taskSnapshot.getDownloadUrl().toString());
                                newPostDatabaseRef.child("postId").setValue(newPostDatabaseRef.getKey().toString());
                                newPostDatabaseRef.child("totalLikes").setValue("0");
                                newPostDatabaseRef.child("usersWhichLiked").child(firebaseAuth.getCurrentUser().getUid().toString()).setValue("false");
                                newPostDatabaseRef.child("totalComments").setValue("0");

                                FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid().toString()).child("Username").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        newPostDatabaseRef.child("userName").setValue(dataSnapshot.getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                progressDialog.dismiss();
                                dialog.dismiss();

                                editNewPostHeading.setText(null);
                                editNewPostText.setText(null);
                                newPostImage.setImageResource(R.drawable.addimageimage);
                            }
                        });
                    } else {
                        newPostDatabaseRef.child("heading").setValue(editNewPostHeading.getText().toString());
                        newPostDatabaseRef.child("postText").setValue(editNewPostText.getText().toString());
                        newPostDatabaseRef.child("userId").setValue(firebaseAuth.getCurrentUser().getUid().toString());
                        newPostDatabaseRef.child("postId").setValue(newPostDatabaseRef.getKey().toString());
                        newPostDatabaseRef.child("totalLikes").setValue("0");
                        newPostDatabaseRef.child("usersWhichLiked").child(firebaseAuth.getCurrentUser().getUid().toString()).setValue("false");
                        newPostDatabaseRef.child("totalComments").setValue("0");

                        progressDialog.dismiss();
                        dialog.dismiss();

                        editNewPostHeading.setText(null);
                        editNewPostText.setText(null);
                        newPostImage.setImageResource(R.drawable.addimageimage);
                    }

                } else {
                    Snackbar.make(newPostView, "Please fill all text fields.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }

    //newPostAdditionToCurrentGroup
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_POST_GALLERY_REQ && resultCode == RESULT_OK) {
            uri = data.getData();
            newPostImage.setImageURI(uri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_posts_activity, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.id_logout_PostActy) {
            firebaseAuth.signOut();
            startActivity(new Intent(PostsActivity.this, MainActivity.class));
        }

        if (id == R.id.id_changeUsername_PostActy) {
            startActivity(new Intent(PostsActivity.this, SetUpUsernameActivity.class));
        }

        if (id == R.id.id_goToHomeButton_PostsActy) {
            startActivity((new Intent(PostsActivity.this, HomeActivity.class)));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<ModelClass_PostsRecyclerView, PostsListViewHolder> FBRAposts = new FirebaseRecyclerAdapter<ModelClass_PostsRecyclerView, PostsListViewHolder>(

                ModelClass_PostsRecyclerView.class,
                R.layout.row_post_layout,
                PostsListViewHolder.class,
                databaseReference

        ) {
            @Override
            protected void populateViewHolder(PostsListViewHolder viewHolder, ModelClass_PostsRecyclerView model, int position) {

                viewHolder.setPostId(model.getPostId());
                viewHolder.setHeading(model.getHeading());
                viewHolder.setPostText(model.getPostText());
                viewHolder.setUserName(model.getUserName());
                viewHolder.setImage(PostsActivity.this, model.getImageLink());
                viewHolder.setTotalLikes(model.getTotalLikes());
                viewHolder.setTotalComments(model.getTotalComments());

                final LikeButton likeButton = (LikeButton) viewHolder.itemView.findViewById(R.id.id_likeStar_button);


                if (model.getPostId() != null) {
                    DatabaseReference mRef = databaseReference.child(model.getPostId()).child("usersWhichLiked");

                    final ValueEventListener listen = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                Map<String, String> map = (Map) dataSnapshot.getValue();
                                if (!TextUtils.isEmpty(map.get(firebaseAuth.getCurrentUser().getUid()))) {
                                    if ((map.get(firebaseAuth.getCurrentUser().getUid())).equals("true")) {
                                        likeButton.setLiked(true);
                                    } else {
                                        likeButton.setLiked(false);
                                    }
                                }
                            } else {
                                likeButton.setLiked(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    mRef.addValueEventListener(listen);
                }
            }


            @Override
            public PostsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final PostsListViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                final TextView postId = (TextView) viewHolder.itemView.findViewById(R.id.id_postIdStorageTextView);
                final TextView totalLikes = (TextView) viewHolder.itemView.findViewById(R.id.id_totalLikes);
                final TextView totalComments = (TextView) viewHolder.itemView.findViewById(R.id.id_totalNoOfComments);

                viewHolder.setOnClickListener(new PostsListViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        commentsView = getLayoutInflater().inflate(R.layout.comment_popup_layout, null);

                        final EditText editComment = (EditText) commentsView.findViewById(R.id.id_addCommentEditText);
                        ImageButton imageButton = (ImageButton) commentsView.findViewById(R.id.id_sendImageButton);

                        imageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DatabaseReference datref = databaseReference.child(postId.getText().toString()).child("comments").push();
                                datref.child("userId").setValue(firebaseAuth.getCurrentUser().getUid().toString());
                                datref.child("commentBody").setValue(editComment.getText().toString());

                                editComment.setText(null);

                                int j = Integer.parseInt(totalComments.getText().toString());
                                databaseReference.child(postId.getText().toString()).child("totalComments").setValue(Integer.toString(j + 1));
                            }
                        });

                        recyclerViewComments = (RecyclerView) commentsView.findViewById(R.id.id_listComments_RecyclerView);
                        recyclerViewComments.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManagerforComments = new LinearLayoutManager(PostsActivity.this);
                        recyclerViewComments.setLayoutManager(linearLayoutManagerforComments);
                        linearLayoutManagerforComments.setReverseLayout(true);
                        linearLayoutManagerforComments.setStackFromEnd(true);

                        builderForComments.setView(commentsView);
                        dialogComments = builderForComments.create();
                        dialogComments.show();


                        FirebaseRecyclerAdapter<ModelClassForComments, ViewHolderComments> FBRAComments = new FirebaseRecyclerAdapter<ModelClassForComments, ViewHolderComments>(

                                ModelClassForComments.class,
                                R.layout.row_comment,
                                ViewHolderComments.class,
                                databaseReference.child(postId.getText().toString()).child("comments")

                        ) {
                            @Override
                            protected void populateViewHolder(ViewHolderComments viewHolder, ModelClassForComments model, int position) {

                                viewHolder.setCommentBody(model.getCommentBody());
                                viewHolder.setUserName(model.getUserName());

                            }
                        };

                        recyclerViewComments.setAdapter(FBRAComments);
                    }

                    public void onLiked(View view, int position) {

                        databaseReference.child(postId.getText().toString()).child("usersWhichLiked").child(firebaseAuth.getCurrentUser().getUid()).setValue("true");
                        int i = Integer.parseInt(totalLikes.getText().toString());

                        databaseReference.child(postId.getText().toString()).child("totalLikes").setValue(Integer.toString((i + 1)));
                    }

                    public void onUnliked(View view, int position) {

                        databaseReference.child(postId.getText().toString()).child("usersWhichLiked").child(firebaseAuth.getCurrentUser().getUid()).setValue("false");

                        int i = Integer.parseInt(totalLikes.getText().toString());

                        databaseReference.child(postId.getText().toString()).child("totalLikes").setValue(Integer.toString((i - 1)));
                    }
                });

                return viewHolder;
            }
        };
        recyclerList.setAdapter(FBRAposts);
    }
}