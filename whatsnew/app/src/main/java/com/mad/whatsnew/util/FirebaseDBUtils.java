package com.mad.whatsnew.util;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.whatsnew.model.CustomLink;
import com.mad.whatsnew.model.News;

/**
 * Firebase Database utils
 */
public class FirebaseDBUtils {

    /**
     * Write to database
     * @param reference database ref
     * @param value value to wrote
     */
    public static void writeToDb(String reference, String value) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference(reference);
        myref.setValue(value);
    }

    /**
     * Push to database
     * @param reference database ref
     * @param news news to push
     */
    public static void pushToDb(String reference, News news) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference newRef = database.child(reference).push();
        newRef.setValue(news);
    }

    /**
     * Push custom link to database
     * @param reference database ref
     * @param customLink custom link to push
     */
    public static void pushCusLinkToDb(String reference, CustomLink customLink) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference newRef = database.child(reference).push();
        newRef.setValue(customLink);
    }

    /**
     * Delete item from database
     * @param itemId item id to delete
     */
    public static void deleteItemDb(String itemId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String reference = "Favorite/" +  user.getUid() + "/" + itemId;
        DatabaseReference deleteRef = database.getReference(reference);
        deleteRef.setValue(null);
    }

    /**
     * Delete custom link from database
     * @param itemId item id to delete
     */
    public static void deleteCusLinkDb(String itemId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String reference = "CustomLink/" +  user.getUid() + "/" + itemId;
        DatabaseReference deleteRef = database.getReference(reference);
        deleteRef.setValue(null);
    }
}
