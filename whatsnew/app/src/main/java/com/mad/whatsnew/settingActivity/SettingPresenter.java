package com.mad.whatsnew.settingActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.whatsnew.model.CustomLink;
import com.mad.whatsnew.model.Favorite;
import com.mad.whatsnew.util.FirebaseDBUtils;
import com.mad.whatsnew.util.FirebaseStoreUtil;

import java.io.IOException;
import java.util.ArrayList;

import static com.mad.whatsnew.mainActivity.MainActivity.FIREBASE_REF_CUSLINK;
import static com.mad.whatsnew.mainActivity.MainActivity.FIREBASE_REF_IMAGE;

/**
 * Presenter class
 * Is the presenter in MVP
 */
public class SettingPresenter implements SettingContract.Presenter{
    private SettingContract.View mView;
    private Boolean mLoggedIn = false;
    private String mReference = FIREBASE_REF_CUSLINK;
    private Uri mImageUri;
    private String mUserEmail;

    private final static String DOWNLOADIMG_TAG = "download image";

    /**
     * Constructor
     * @param view setting fragment
     */
    public SettingPresenter(SettingContract.View view) {
        mView = view;
        mView.setPresenter(this);

        //for login
        FirebaseAuth firebaseAuth;

        //check login
        FirebaseAuth.AuthStateListener authStateListener;
        //for login
        firebaseAuth = FirebaseAuth.getInstance();
        //check login
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    mLoggedIn = false;
                    mView.setContentLayoutVisible(false);
                    mView.setErrorLayoutVisible(true);
                } else {
                    mUserEmail = user.getEmail();
                    mView.setUserEmail(mUserEmail);
                    StorageReference sref = FirebaseStorage.getInstance().getReference(FIREBASE_REF_IMAGE + user.getUid());
                    sref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try {
                                mView.setUserImage(uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(DOWNLOADIMG_TAG, e.toString());
                        }
                    });
                    mView.setContentLayoutVisible(true);
                    mView.setErrorLayoutVisible(false);
                    Log.e("ref", mReference);
                }
            }
        };
        //check login
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void start() {

    }

    @Override
    public void setAdapter() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String ref = mReference + FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference cusRef = database.getReference(ref);
            cusRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<CustomLink> customLinkArrayList = new ArrayList<>();
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Log.e("postSnapshot", postSnapshot.getKey());
                        CustomLink customLink = postSnapshot.getValue(CustomLink.class);
                        customLink.setFirebaseKey(postSnapshot.getKey());
                        customLinkArrayList.add(customLink);
                        mView.setRecyclerViewContent(customLinkArrayList);
                    }
                    if (customLinkArrayList.isEmpty()) {
                        mView.setNoItemLayoutVisible(true);
                        mView.setRecyclerViewVisible(false);
                    } else {
                        mView.setNoItemLayoutVisible(false);
                        mView.setRecyclerViewVisible(true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void addNewCusLink(String title, String link) {
            String ref = mReference + FirebaseAuth.getInstance().getCurrentUser().getUid();
            CustomLink customLink = new CustomLink(link, title, null);
            FirebaseDBUtils.pushCusLinkToDb(ref, customLink);
    }

    @Override
    public void updateLogo(Uri filePath) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if (filePath != null) {
            storageReference = storageReference.child(FIREBASE_REF_IMAGE + FirebaseAuth.getInstance().getCurrentUser().getUid());
            FirebaseStoreUtil.uploadImage(storageReference, filePath);
        }
    }

    @Override
    public void logoClicked() {
        mView.toLogoClicked(mLoggedIn);
    }
}
