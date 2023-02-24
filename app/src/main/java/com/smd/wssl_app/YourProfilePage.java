package com.smd.wssl_app;
import java.net.URI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

//import org.brunocvcunha.instagram4j.Instagram4j;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.feed.FeedIterable;
import com.github.instagram4j.instagram4j.models.IGPayload;
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.requests.IGRequest;
import com.github.instagram4j.instagram4j.requests.feed.FeedTimelineRequest;
import com.github.instagram4j.instagram4j.requests.media.MediaGetLikersRequest;
import com.github.instagram4j.instagram4j.models.user.User;
import com.github.instagram4j.instagram4j.responses.feed.FeedTimelineResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
//
//import org.brunocvcunha.instagram4j.requests.InstagramSavedFeedRequest;
//import org.brunocvcunha.instagram4j.requests.payload.FeedMediaItem;
//import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
//import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
//import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
//import org.brunocvcunha.instagram4j.requests.payload.InstagramLoggedUser;
//import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;
//import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nullable;


import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Request;
//import facebook4j.Facebook;
//import facebook4j.FacebookException;
//import facebook4j.FacebookFactory;
//import facebook4j.Like;
//import facebook4j.Page;
//import facebook4j.ResponseList;
//import facebook4j.Version;
//import facebook4j.conf.ConfigurationBuilder;


import java.io.IOException;
import java.util.List;

public class YourProfilePage extends AppCompatActivity {
FirebaseAuth mauth;
    ImageButton logout,back;
    ImageView profilepic;
    Integer ii=0;
    Integer bol=0;

    RecyclerView rv;
    Button update;
    List<ImageViewModel> ls;
Button fb;
    ImageViewAdapter adapter;
TextView sgrv;
    EditText name, aboutme;
    ImageButton home,football,groups,notifications,profile;
    FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;
    CallbackManager callbackManager ;
    AccessToken accessToken = AccessToken.getCurrentAccessToken();




//    GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
//        @Override
//        public void onCompleted(@androidx.annotation.Nullable JSONObject jsonObject, @androidx.annotation.Nullable GraphResponse graphResponse) {
//
//        }
//    });
//
//    Bundle parameters = new Bundle();
//            parameters.putString("fields", "id,name,link");
//            request.setParameters(parameters);
//            request.executeAsync();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       db = FirebaseFirestore.getInstance();



        setContentView(R.layout.activity_your_profile_page);

        mauth = FirebaseAuth.getInstance();
        Navbarfunctions();
        logout = findViewById(R.id.logout);
        sgrv = findViewById(R.id.SGrv);
        back = findViewById(R.id.back);
        update = findViewById(R.id.update);
        rv= findViewById(R.id.rvclubs);

        ls=new ArrayList<>();


fb = findViewById(R.id.fb);
        adapter=new ImageViewAdapter(ls,YourProfilePage.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(YourProfilePage.this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(lm);
      ls.add(new ImageViewModel("https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/images%2Fimage%3A256102?alt=media&token=0e9cff61-998e-466f-a6a8-318be59e1bc3"));
adapter.notifyDataSetChanged();
getimages();
getclubs();

name = findViewById(R.id.name);
aboutme  = findViewById(R.id.aboutme);
        getData();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateprofile();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profilepic = findViewById(R.id.profilepic);
        setdp();
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {








                        String username = "chughta_i";
                        String password = "IndiuM456";

//
//Log.d("errors","working");
//                Instagram4j instagram = Instagram4j.builder().username(username).password(password).build();
//
////                instagram.setup();
//                Log.d("errors","working1");
//                InstagramLoginResult user = null;
//                try {
////                    instagram.setup();
//                    Log.d("errors","working1");
//                     user = instagram.login();
//                    Log.d("errors","working2");
//                } catch (IOException e) {
//                    Log.d("errors","1");
//                    e.printStackTrace();
//                }
//
//                Log.d("errors","working3");
//                Log.d("errors", user.toString());


//                InstagramSearchUsernameResult userResult = null;
//                try {
//                    userResult = instagram.sendRequest(new InstagramSearchUsernameRequest(username));
//                } catch (IOException e) {
//                    Log.d("errors","2");
//                    e.printStackTrace();
//                }
//
//                Log.d("errors","2"+ userResult.toString());
//                long userId = userResult.getUser().getPk();
////                List<InstagramFeedItem> likedMedia = instagram.sendRequest(new MediaLikersRequest(userId)).getItems();
//                List<FeedMediaItem> likedMedia  = null;
//                try {
//                    likedMedia = instagram.sendRequest(new InstagramSavedFeedRequest()).getItems();
//                } catch (IOException e) {
//                    Log.d("errors","3");
//                    e.printStackTrace();
//                }
//
//                Log.d("errors","3"+ likedMedia.toString());
//
//                for (FeedMediaItem media : likedMedia) {
//                    Log.d("errors","4");
//                   Log.d("ooo",media.toString());
//                }







//  Create a new Instagram client with the user's credentials
                ArrayList<String> liked = new ArrayList<String>();
int i=0;
                IGClient client = null;

                try {
                    client = IGClient.builder()
                            .username(username)
                            .password(password)
                            .login();
                } catch (IGLoginException e) {
                    Log.d("logged","logger");
                    e.printStackTrace();
                }
                Log.d("logged",client.toString());
               for (FeedTimelineResponse response: client.getActions().timeline().feed()) {
//                  liked.add(C"><":L"??")
                   Log.d("sook",response.toString());
//                   Log.d("sook",response.toString().substring(0,response.toString().indexOf("type1")));


                   liked.add(response.toString());
                   Log.d("ook2","num");
                   i++;
                   if(i==10)
                       break;
//                 for (String res :  (response.toString().split("' '"))){
//                     Log.d("feedD",res);
//                     if(res.startsWith("#"))
//                         liked.add(res.substring(1));
//
//                 }

               }
//                Log.d("resullt","fsdf");
               Log.d("resulter",liked.toString());
//                List<TimelineMedia> likedMedia = null;
//                try {
//                    likedMedia = client.actions().users().info(client.)
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                // print media info
//                for (TimelineMedia media : likedMedia) {
//                    System.out.println(media.toString());
//                }











//                // Get the authenticated user's liked media
//                List<Media> likedMedia = client.sendRequest(new FeedLikedRequest()).getItems();
//
//                // Extract the IDs of the liked media
//                for (Media media : likedMedia) {
//                    String mediaId = media.getId();
//                    System.out.println("Liked Media ID: " + mediaId);
//                }





            }

//
//                        // Extract the names of the liked pages from the response
//                        List<User> pages = likedPages().;
//                        for (User page : pages) {
//                            System.out.println("Liked page: " + page.getUsername());
//                        }


//
//                FacebookCallback<LoginResult> loginCallback = new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // Get the access token from the login result
//                        String accessToken = loginResult.getAccessToken().getToken();
//
//                        // Use the access token to make Graph API requests
//
////                        FacebookClient fbClient = new DefaultFacebookClient(accessToken, Version.VERSION_12_0);
////
////                        // Make a request to the Graph API to retrieve the user's liked pages
//                        Connection<Page> result = fbClient.fetchConnection("me/likes", Page.class);
//
//                        // Iterate over the pages and print the name and ID of each page
//                        for (List<Page> pageList : result) {
//                            for (Page page : pageList) {
//                                System.out.println(page.getName() + " - " + page.getId());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // Handle login cancellation
//                    }
//
//                    @Override
//                    public void onError(com.facebook.FacebookException error) {
//
//                    }
//
//
//                };
//
//// Start the Facebook Login process
//                LoginManager.getInstance().registerCallback(callbackManager, loginCallback);
//                LoginManager.getInstance().logInWithReadPermissions(YourProfilePage.this, Arrays.asList("public_profile", "user_likes"));





//
//                ConfigurationBuilder cb = new ConfigurationBuilder();
//                cb.setDebugEnabled(true)
//                        .setOAuthAppId("1630667134117748")
//                        .setOAuthAppSecret("63f119b8cc94cb7bd321176b1b8f6858")
//                        .setOAuthAccessToken(null)
//                        .setOAuthPermissions("user_likes");
//                FacebookFactory ff = new FacebookFactory(cb.build());
//
//                Facebook facebook = ff.getInstance();
//                facebook4j.auth.AccessToken accessToken = null;
//
//                try {
////                    String accessTokens = facebook.getOAuthAccessToken(null).getToken();
//                    Log.d("123","INSIDE");
//                    Log.d("oAuth1","lol"+accessToken.toString());
//                    accessToken = facebook.getOAuthAppAccessToken();
//                    Log.d("oAuth",accessToken.toString());
//                } catch (FacebookException e) {
//                    e.printStackTrace();
//                }
//                facebook.setOAuthAccessToken(accessToken);
//                ResponseList<Like> likes = null;
//                try {
//                    likes = facebook.likes().getUserLikes();
//                } catch (FacebookException e) {
//                    e.printStackTrace();
//                }
//                for (Like like : likes) {
//                    System.out.println(like.getName());
//                }










//                callbackManager = CallbackManager.Factory.create();
//
//                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//
//                        Log.d("loll","name1");
//                        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(@androidx.annotation.Nullable JSONObject jsonObject, @androidx.annotation.Nullable GraphResponse graphResponse) {
//                                Log.d("loll","name2");
//                            }
//                        });
//                        Bundle parameters = new Bundle();
//                        parameters.putString("fields", "id,name,link");
//                        request.setParameters(parameters);
//                        request.executeAsync();
//                    }
//
//                    @Override
//                    public void onError(@androidx.annotation.NonNull FacebookException e) {
//                        Log.d("loll","name3"+e);
//
//                        }
//
//                    @Override
//                    public void onCancel() {
//                        Log.d("loll","name4");
//                    }
//                });
//                LoginManager.getInstance().logInWithReadPermissions(YourProfilePage.this , Arrays.asList("public_profile"));
////                String name =  LoginManager.getInstance();
//                Log.d("tza","yyun");

        });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauth.signOut();
                Intent i =new Intent(getApplicationContext(),SignUpLogin.class);
                startActivity(i);
                finish();
            }
        });
        name = findViewById(R.id.name);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.requestFocus();
                name.setFocusableInTouchMode(true);

            }
        });

    }


    private void  getData(){
        DocumentReference docRef = db.collection("users").document(mauth.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve data
                    String namee = documentSnapshot.getString("name");
                        String aboutmee = documentSnapshot.getString("aboutme");
                    name.setText(namee);
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

    private void updateprofile(){

        DocumentReference docRef = db.collection("users").document(mauth.getUid());
        Map<String, Object> update = new HashMap<>();
        update.put("name", name.getText().toString());
        update.put("aboutme", aboutme.getText().toString());
        docRef.update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Update successful
                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error updating document
                    }
                });


    }

    public void Navbarfunctions(){

        home = findViewById(R.id.home);
        football = findViewById(R.id.football);
        groups = findViewById(R.id.groups);
        notifications = findViewById(R.id.notifcations);
        profile = findViewById(R.id.profile);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!this.getClass().getName().contains("HomeScreen")){
                    Intent i = new Intent(getApplicationContext(),HomeScreen.class);
                    startActivity(i);}
            }
        });

        football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!this.getClass().getName().contains("ClubsPage")){

                    Intent i = new Intent(getApplicationContext(),ClubsPage.class);
                    startActivity(i);
                }}
        });

        groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!this.getClass().getName().contains("SubjectGroups")){

                    Intent i = new Intent(getApplicationContext(),SubjectGroups.class);
                    startActivity(i);}
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!this.getClass().getName().contains("YourProfilePage")){

                    Intent i = new Intent(getApplicationContext(),YourProfilePage.class);
                    startActivity(i);}
            }
        });


    }

    private void setdp(){
        CollectionReference messagesRef =  db.collection("users");
        messagesRef.document(mauth.getUid())
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri imageUri = data.getData();
            profilepic.setImageURI(imageUri);

            // Upload the image to Firestore
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();

                            // Add the image URL to the Firestore document
                            DocumentReference docRef = db.collection("users").document(mauth.getUid());
                            docRef.update("imgurl", imageURL);
                        }
                    });
                }
            });
        }
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

                        Query query = chatMembersRef.whereEqualTo("uid", mauth.getUid());
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> innerTask) {
                                if (innerTask.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : innerTask.getResult()) {
                                        String groupId = groupDocument.getId();
//                                        groupids.add(groupId);
                                        // Do something with the group ID
                                        Log.d("abd","aaa"+groupId);
//                                        groupids.add(groupId);
                                        sgrv.setText(sgrv.getText()+ groupId+"\n");




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


    private void getimages(){


        CollectionReference collectionRef = db.collection("clubs");



        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    }


}