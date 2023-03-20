package com.smd.wssl_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClubsAdapter extends RecyclerView.Adapter<ClubsAdapter.MyViewHolder> {
    List<ClubModel> ls;
    Context c;
    FirebaseAuth mauth;

    public ClubsAdapter(List<ClubModel> ls, Context c) {
        this.ls = ls;
        this.c = c;
        mauth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(c).inflate(R.layout.row_clubs,parent,false);
        return new ClubsAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.name.setText(ls.get(position).getClub_name());
        String users = ls.get(position).getAmount_of_users();
        holder.no_of_members.setText((users));

        Picasso.get().load(ls.get(position).getClub_image()).into(holder.img);
        check(ls.get(position).getClub_name(),holder);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.join.getText()=="Joined"){
                Intent i = new Intent(c,ChatPage.class);
                i.putExtra("chat_name",ls.get(position).getClub_name());
                    i.putExtra("type","club");

                    c.startActivity(i);}
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.join.getText()=="Joined"){

                    Intent i = new Intent(c,ChatPage.class);
                i.putExtra("chat_name",ls.get(position).getClub_name());
                c.startActivity(i);}
            }
        });

        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if(holder.join.getText()!="Joined"){
    Log.d("taggg",ls.get(position).getClub_name().toString());

    //add user to club
    getData(ls.get(position).getClub_name());
    holder.join.setText("Joined");
    holder.join.setBackgroundResource(R.color.gray);
    inc(ls.get(position).getClub_name().toString());

}

            }
        });


    }

    public void filterlist(ArrayList<ClubModel> filteredstalls) {
        ls = filteredstalls;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

CircleImageView img;
TextView name;
TextView no_of_members;
        AppCompatButton join;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.profile_image);
            join = itemView.findViewById(R.id.join);
            name = itemView.findViewById(R.id.name);
            no_of_members  = itemView.findViewById(R.id.no_of_members);

        }
    }


    private void getData(String club_name){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("users").document(mauth.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Object user = documentSnapshot.getData();
                    Log.d("TAG", "User: " + user);

                  addmember(club_name,documentSnapshot.get("name").toString(),documentSnapshot.get("imgurl").toString());

                } else {
                    Log.d("TAG", "No such document");
                }
            }
        });
    }


    private void addmember(String club_name,String user_name,String dp){
        Map<String, Object> data = new HashMap<>();
        data.put("uid", mauth.getUid());
        data.put("dp", dp);
        data.put("name",user_name );
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference groupsRef = db.collection("groups");
        DocumentReference footballRef = groupsRef.document(club_name);




        CollectionReference messagesRef = footballRef.collection("chatmembers");

        messagesRef.add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        Log.d("TAG", "Document added with ID in chatmembers " );

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });

    }


    private void check(String club_name, ClubsAdapter.MyViewHolder holder){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference colRef = db.collection("groups").document(club_name).collection("chatmembers");
        Query query = colRef.whereEqualTo("uid", currentUserId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {

                        Log.d("TAG", "User ID not found in chatmembers collection");
//                        Toast.makeText(c, "User not found", Toast.LENGTH_SHORT).show();

                    } else {
                        holder.join.setText("Joined");
                        holder.join.setBackgroundResource(R.color.gray);
                        Log.d("TAG", "User ID found in chatmembers collection");
//                        Toast.makeText(c, "User found", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }


    private void inc(String title){
        Log.d("taggg",title);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        firestore.setFirestoreSettings(settings);

        Query query = firestore.collection("clubs")
                .whereEqualTo("club_name", title);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    for (DocumentSnapshot document : documents) {
                        DocumentReference documentRef = document.getReference();
                        firestore.runTransaction(new Transaction.Function<Void>() {
                            @Override
                            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                DocumentSnapshot snapshot = transaction.get(documentRef);
                                String fieldValue = snapshot.getString("members");
                                int fieldValueInt = Integer.parseInt(fieldValue);
                                fieldValueInt++;
                                fieldValue = Integer.toString(fieldValueInt);
                                transaction.update(documentRef, "members", fieldValue);
                                Log.d("firer",fieldValue);

                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // The transaction was successful.
                                Log.d("firer","---YES");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // The transaction failed.
                                Log.d("firer","---no");

                            }
                        });
                    }
                } else {
                    // The query failed.
                    Log.d("firer","---no");

                }
            }
        });

    }

}
