package com.mad.whatsnew.favoriteActivity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.whatsnew.adapter.NewsAdapter;
import com.mad.whatsnew.model.Favorite;
import com.mad.whatsnew.model.News;

import java.util.ArrayList;

import static com.mad.whatsnew.mainActivity.MainActivity.FIREBASE_REF_FAV;

/**
 * Presenter class for favorite activity
 * Is the presenter in MVP
 */
public class FavoritePresenter implements FavoriteContract.Presenter{
    private FavoriteContract.View mView;
    private Boolean mLoggedIn = false;
    private String mReference = FIREBASE_REF_FAV;

    /**
     * Constructor of favorite presenter
     * @param view is the fragment class of favorite activity
     */
    public FavoritePresenter(FavoriteContract.View view) {
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
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    mLoggedIn = false;
                    mView.setContentLayoutVisible(false);
                    mView.setErrorLayoutVisible(true);
                } else {
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
            DatabaseReference favRef = database.getReference(ref);

            favRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Favorite> favList = new ArrayList<>();

                    // Get all data from firebase and convert them into Favorite model
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Favorite favorite = postSnapshot.getValue(Favorite.class);
                        favorite.setFirebaseKey(postSnapshot.getKey());
                        favList.add(favorite);
                        mView.setRecyclerViewContent(favList);
                    }
                    if (favList.isEmpty()) {
                        mView.setNoItemTvVisible(true);
                        mView.setContentLayoutVisible(false);
                    } else {
                        mView.setNoItemTvVisible(false);
                        mView.setContentLayoutVisible(true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void refresh() {
        setAdapter();
    }

    @Override
    public void logoClicked() {
        mView.toLogoClicked(mLoggedIn);
    }
}
