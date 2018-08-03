package com.mad.whatsnew.settingActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.util.Attributes;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;
import com.mad.whatsnew.R;
import com.mad.whatsnew.adapter.CustomLinkAdapter;
import com.mad.whatsnew.loginActivity.LoginActivity;
import com.mad.whatsnew.model.CustomLink;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Setting fragment
 * Is the View in MVP
 */
public class SettingFragment extends Fragment implements SettingContract.View {
    private SettingContract.Presenter mPresenter;

    public static final int PICK_IMAGE = 1;

    @BindView(R.id.setting_activity_content_layout) RelativeLayout mContentLayout;
    @BindView(R.id.setting_activity_error_Layout) LinearLayout mErrorLayout;
    @BindView(R.id.setting_activity_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.setting_activity_rss_layout) LinearLayout mRssLayout;
    @BindView(R.id.setting_activity_add_fab) FloatingActionButton mAddFab;
    @BindView(R.id.setting_activity_emptyList_tv) TextView mNoItemTv;
    @BindView(R.id.setting_activity_login_btn) Button mLoginBtn;
    @BindView(R.id.setting_activity_user_logo) ImageView mUserLogoIv;
    @BindView(R.id.setting_activity_email_tv) TextView mUserEmailTv;

    private final static String START_STR = "www.";
    private final static String END_STR = ".xml";

    private final static String IMAGE_TYPE= "image/*";
    private final static String IMAGE_SELECT = "Select Picture";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_setting, container, false);
        ButterKnife.bind(this, root);
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                View view = getLayoutInflater().inflate(R.layout.add_link_frag, null);
                final TextInputEditText titleTv = (TextInputEditText)  view.findViewById(R.id.add_link_title_et);
                final TextInputEditText urlTv = (TextInputEditText) view.findViewById(R.id.add_link_url_et);
                final TextInputLayout titleLt = (TextInputLayout) view.findViewById(R.id.add_link_title_tl);
                final TextInputLayout urlLt = (TextInputLayout) view.findViewById(R.id.add_link_url_tl);
                TextView confirmTv = (TextView) view.findViewById(R.id.add_link_confirm_tv);
                dialog.setContentView(view);
                dialog.show();
                confirmTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean valid = true;
                        if (titleTv.getText().toString().equals("")) {
                            titleLt.setError(getString(R.string.setting_title_error));
                            valid = false;
                        }
                        if (urlTv.getText().toString().equals("")) {
                            urlLt.setError(getString(R.string.setting_url_error));
                            valid = false;
                        }
                        if (urlTv.getText().toString().trim().endsWith(END_STR)
                                && urlTv.getText().toString().startsWith(START_STR)) {
                        } else {
                            urlLt.setError(getString(R.string.setting_url_inva));
                            valid = false;
                        }
                        if (valid) {
                            mPresenter.addNewCusLink(titleTv.getText().toString().trim(), urlTv.getText().toString().trim());
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mUserLogoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, IMAGE_SELECT), PICK_IMAGE);
            }
        });

        mPresenter.setAdapter();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if (data.getData() != null) {
                Uri selectImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectImageUri);
                    mUserLogoIv.setImageBitmap(bitmap);
                    mPresenter.updateLogo(selectImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get new instance
     * @return setting fragment
     */
    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setRecyclerViewContent(ArrayList<CustomLink> customLinks) {
        RecyclerView.Adapter adapter;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CustomLinkAdapter(getContext(), customLinks);
        ((CustomLinkAdapter) adapter).setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setErrorLayoutVisible(Boolean visible) {
        if (visible) {
            mErrorLayout.setVisibility(View.VISIBLE);
        } else {
            mErrorLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setContentLayoutVisible(Boolean visible) {
        if (visible) {
            mContentLayout.setVisibility(View.VISIBLE);
        } else {
            mContentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setNoItemLayoutVisible(Boolean visible) {
        if (visible) {
            mNoItemTv.setVisibility(View.VISIBLE);
        } else {
            mNoItemTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void setRecyclerViewVisible(Boolean visible) {
        if (visible) {
            mRssLayout.setVisibility(View.VISIBLE);
        } else {
            mRssLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUserEmail(String userEmail) {
        mUserEmailTv.setText(userEmail);
    }

    @Override
    public void setUserImage(Uri imageUri) {
        new UpdateLogo().execute(imageUri.toString());
    }

    /**
     * AsyncTask for download logo
     */
    private class UpdateLogo extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap logo = null;
            try {
                InputStream in = new URL(strings[0]).openStream();
                logo = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mUserLogoIv.setImageBitmap(bitmap);
        }
    }

    @Override
    public void toLogoClicked(Boolean loggedIn) {
        if (loggedIn) {
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }
}
