package com.mad.whatsnew.util;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.whatsnew.settingActivity.SettingActivity;

/**
 * Fireabse storage utils
 */
public class FirebaseStoreUtil {
    /**
     * Upload image to storage
     * @param storageReference storage ref
     * @param filePath the path
     */
    public static void uploadImage(StorageReference storageReference, Uri filePath) {
        storageReference.putFile(filePath);
    }
}
