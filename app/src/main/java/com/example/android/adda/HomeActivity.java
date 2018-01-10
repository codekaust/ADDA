package com.example.android.adda;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2;

    private Toolbar toolbar;
    private TextView gItextView;
    private TextView oGtextView;
    private RecyclerView generalListrecyclerView;
    private RecyclerView otherListrecyclerView;

    public static String clickedGroup;
    public static String clickedGroupGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseReference1=FirebaseDatabase.getInstance().getReference().child("GeneralInterestList");
        databaseReference2=FirebaseDatabase.getInstance().getReference().child("OtherGroupsList");

        gItextView=(TextView)findViewById(R.id.id_generalInterests_Home_Activity);
        oGtextView=(TextView)findViewById(R.id.id_otherGroups_Home_Activity);

        gItextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =new AlertDialog.Builder(HomeActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.general_interests_popup,null);

                generalListrecyclerView=(RecyclerView)mView.findViewById(R.id.id_general_groupNameList_RecyclerView);
                generalListrecyclerView.setHasFixedSize(true);
                generalListrecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));


                FirebaseRecyclerAdapter<GroupListRowModelClass,GroupNameListViewHolder> FBRA=new FirebaseRecyclerAdapter<GroupListRowModelClass, GroupNameListViewHolder>(

                        GroupListRowModelClass.class,
                        R.layout.row_list_select_grp_layout,
                        GroupNameListViewHolder.class,
                        databaseReference1

                ) {

                    @Override
                    protected void populateViewHolder(GroupNameListViewHolder viewHolder, GroupListRowModelClass model, int position) {

                        Log.v("HomeActivity",model.getGroupName());
                        viewHolder.setGroupName(model.getGroupName());

                    }

                    @Override
                    public GroupNameListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        GroupNameListViewHolder viewHolder=super.onCreateViewHolder(parent, viewType);

                        viewHolder.setOnClickListener(new GroupNameListViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView textView=(TextView)view.findViewById(R.id.id_groupName_textView);
                                clickedGroup=textView.getText().toString();
                                clickedGroupGroup="GeneralInterests";
                                startActivity(new Intent(HomeActivity.this,PostsActivity.class));
                                Log.v("HomeActivity","Viewholder clicked");
                            }
                        });

                        return viewHolder;
                    }
                };

                generalListrecyclerView.setAdapter(FBRA);
                builder.setView(mView);
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        oGtextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =new AlertDialog.Builder(HomeActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.other_groups_popup,null);
                builder.setView(mView);
                AlertDialog dialog=builder.create();
                dialog.show();

                otherListrecyclerView=(RecyclerView)mView.findViewById(R.id.id_other_groupNameList_RecyclerView);
                otherListrecyclerView.setHasFixedSize(true);
                otherListrecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

                FirebaseRecyclerAdapter<GroupListRowModelClass,GroupNameListViewHolder> FBRA=new FirebaseRecyclerAdapter<GroupListRowModelClass, GroupNameListViewHolder>(

                        GroupListRowModelClass.class,
                        R.layout.row_list_select_grp_layout,
                        GroupNameListViewHolder.class,
                        databaseReference2

                ) {
                    @Override
                    protected void populateViewHolder(GroupNameListViewHolder viewHolder, GroupListRowModelClass model, int position) {

                        viewHolder.setGroupName(model.getGroupName());

                    }

                    @Override
                    public GroupNameListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        GroupNameListViewHolder viewHolder=super.onCreateViewHolder(parent, viewType);

                        viewHolder.setOnClickListener(new GroupNameListViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView textView=(TextView)view.findViewById(R.id.id_groupName_textView);
                                clickedGroup=textView.getText().toString();
                                clickedGroupGroup="OtherGroups";
                                startActivity(new Intent(HomeActivity.this,PostsActivity.class));
                            }
                        });

                        return viewHolder;
                    }
                };

                otherListrecyclerView.setAdapter(FBRA);

            }
        });

        firebaseAuth=FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_activity,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if(id==R.id.id_logout_HomeActy){
            firebaseAuth.signOut();
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
        }

        if (id==R.id.id_addGroup_HomeActy){
            AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
            View view=getLayoutInflater().inflate(R.layout.add_group_layout,null);
            builder.setView(view);
            final AlertDialog alertDialog=builder.create();
            alertDialog.show();

            final EditText editNewGroupName=(EditText)view.findViewById(R.id.id_editNewGroupName);
            Button addGrp=(Button)view.findViewById(R.id.id_addNewGroupBtn);
            addGrp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("OtherGroupsList").push().child("groupName");
                    databaseReference.setValue(editNewGroupName.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            alertDialog.dismiss();
                        }
                    });

                }
            });

            if(id==R.id.id_changeUsername_HomeActy){
                startActivity(new Intent(HomeActivity.this,SetUpUsernameActivity.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
