package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemberProfile extends AppCompatActivity {


    FirebaseAuth mauth;
    ImageButton back;
    ImageView profilepic;
    String jjk;
    int numCompleted = 0;

    Integer ii=0;
    ArrayList<String> liked;
    ArrayList<String> suggests;
    Integer bol=0;

    RecyclerView rv;
    EditText u,p;

    List<ImageViewModel> ls;

    ImageViewAdapter adapter;
    TextView sgrv;
    TextView uname;
    EditText name, aboutme;
    ImageButton home,football,groups,notifications,profile;
    FirebaseFirestore db;
    String memberuid;
    List<String> dps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);

        Bundle extras = getIntent().getExtras();
        memberuid= extras.getString("uid");
        dps=new ArrayList<>(Arrays.asList());
        mauth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();


        u = findViewById(R.id.usernamee);
        p = findViewById(R.id.password);
        uname = findViewById(R.id.uname);
        sgrv = findViewById(R.id.SGrv);
        back = findViewById(R.id.back);
        rv= findViewById(R.id.rvclubs);
        profilepic = findViewById(R.id.profilepic);

        ls=new ArrayList<>();

        jjk = "";

        adapter=new ImageViewAdapter(ls,MemberProfile.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(MemberProfile.this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(lm);
//        ls.add(new ImageViewModel("https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/images%2Fimage%3A256102?alt=media&token=0e9cff61-998e-466f-a6a8-318be59e1bc3"));
        adapter.notifyDataSetChanged();
        checkclubs();
        getclubs();
        setdp();

        name = findViewById(R.id.name);
        aboutme  = findViewById(R.id.aboutme);
        getData();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }


    private void  getData(){
        DocumentReference docRef = db.collection("users").document(memberuid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve data
                    String namee = documentSnapshot.getString("name");
                    String aboutmee = documentSnapshot.getString("aboutme");
                    String una = documentSnapshot.getString("email");
                    name.setText(namee);
                    uname.setText(una.substring(0, una.length() - 10));

                    int iend = una.indexOf("@"); //this finds the first occurrence of "."
//in string thus giving you the index of where it is in the string

// Now iend can be -1, if lets say the string had no "." at all in it i.e. no "." is found.
//So check and account for it.

                    String subString;
                    if (iend != -1)
                    {
                        subString= una.substring(0 , iend); //this will give abc
                        uname.setText(subString);

                    }


                    if(!aboutmee.isEmpty())
                        aboutme.setText(aboutmee);
                    else
                        aboutme.setText("Write something here... ");


                } else {
                    // Document does not exist
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error retrieving document
                    }
                });


    }

    private void getclubs(){
        CollectionReference groupsRef = db.collection("groups");
        ArrayList<String> groupids = new ArrayList<>();
        groupsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot groupDocument : task.getResult()) {
                        CollectionReference chatMembersRef = groupDocument.getReference().collection("chatmembers");

                        Query query = chatMembersRef.whereEqualTo("uid", memberuid);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> innerTask) {
                                if (innerTask.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : innerTask.getResult()) {
                                        String groupId = groupDocument.getId();

//                                        groupids.add(groupId);
                                        // Do something with the group ID
                                        Log.d("abdabd","aaa"+groupId);
//                                        groupids.add(groupId);
                                        if(!groupId.toLowerCase().contains("club"))
                                            sgrv.setText(sgrv.getText()+ groupId+"\n");
                                        sgrv.setPaintFlags(sgrv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);




                                    }
//                                    Toast.makeText(getApplicationContext(), "hayyeee", Toast.LENGTH_SHORT).show();




                                } else {
                                    // Handle the error
                                }
                            }
                        });
                    }



                } else {
                    // Handle the error
                }
            }
        });

    }




    private void setdp(){
        CollectionReference messagesRef =  db.collection("users");
        messagesRef.document(memberuid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Log.d("TAG", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                                String img = documentSnapshot.getString("imgurl");
                                if(!img.isEmpty())
                                    Picasso.get().load(img).into(profilepic);
                                else
                                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/images%2Fimage%3A256102?alt=media&token=0e9cff61-998e-466f-a6a8-318be59e1bc3").into(profilepic);


                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.w("TAG", "Error getting document", task.getException());
                        }
                    }
                });
    }


    private void getimages(List<String> dpp){


        CollectionReference collectionRef = db.collection("clubs");



        collectionRef.whereIn("club_name", dps)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String fieldValue = document.getString("dp");
                                // Do something with the field value
                                ls.add(new ImageViewModel(fieldValue));
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            // Handle the error
                        }
                    }
                });




//        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                               if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        String fieldValue = document.getString("dp");
//                        // Do something with the field value
//                        ls.add(new ImageViewModel(fieldValue));
//                        adapter.notifyDataSetChanged();
//
//                    }
//                } else {
//                    // Handle the error
//                }
//            }
//        });

    }





    public void checkclubs(){

// Replace "groups" with the name of your top-level collection
        CollectionReference groupsRef = db.collection("groups");

// Replace "your_uid_value" with the UID value you want to query for
        String uidValue = memberuid;

        groupsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                int numIterations = querySnapshot.size();

                for (DocumentSnapshot groupDocumentSnapshot : querySnapshot) {
                    String groupId = groupDocumentSnapshot.getId();

                    // Query the chatmembers subcollection of the current group
                    CollectionReference chatMembersRef = groupDocumentSnapshot.getReference().collection("chatmembers");
                    Query query = chatMembersRef.whereEqualTo("uid", uidValue);

                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot querySnapshot) {
                            for (DocumentSnapshot chatMemberDocumentSnapshot : querySnapshot) {
                                String chatMemberId = groupDocumentSnapshot.getId();
                                // Use the group ID and chat member ID as needed
                                if(chatMemberId.toLowerCase().contains("club") && !dps.contains(chatMemberId)) {
                                    dps.add(chatMemberId);
                                    Log.d("abdulll", "done " + chatMemberId + ": ");

                                }
                            }
                            numCompleted++;
                            if (numCompleted == numIterations) {
                                if(!dps.isEmpty())
                                    getimages(dps);
                                else
                                    rv.setVisibility(View.INVISIBLE);

                            }



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "Error querying for chatmembers in group " + groupId + ": " + e);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Error querying for groups: " + e);
            }
        });

    }


}