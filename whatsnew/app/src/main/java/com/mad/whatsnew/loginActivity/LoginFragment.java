package com.mad.whatsnew.loginActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.whatsnew.R;
import com.mad.whatsnew.registerActivity.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Fragment class for login activity
 * Is the view of MVP
 */
public class LoginFragment extends Fragment implements LoginContract.View {
    private LoginContract.Presenter mPresenter;
    private final static String TAG = "loginFragment";
    private final static String INTERNET_TAG = "internet";

    @BindView(R.id.login_activity_email_et) TextInputEditText mEmailEt;
    @BindView(R.id.login_activity_password_et) TextInputEditText mPasswordEt;
    @BindView(R.id.login_activity_email_inputlayout) TextInputLayout mEmailLayout;
    @BindView(R.id.login_activity_password_inputlayout) TextInputLayout mPasswordLayout;

    @BindView(R.id.login_activity_login_btn) Button mLoginBtn;
    @BindView(R.id.login_activity_register_btn) Button mRegisterBtn;
    @BindView(R.id.login_activity_cancel_btn) Button mCancelBtn;
    @BindView(R.id.nointernet_Tv) TextView mNoInternetTv;
    @BindView(R.id.login_activity_content_layout) LinearLayout mContentLayout;

    /**
     * Function for return new instance if there are no fragment manager
     * @return is the new login fragment object
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_login, container, false);
        ButterKnife.bind(this, root);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loginClickEvent(getActivity());
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                finishActivity();
                startActivity(intent);
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        if (isNetworkConnected()) {
            Log.e(INTERNET_TAG, "connected");
        } else {
            Log.e(INTERNET_TAG, "not connected");
            mNoInternetTv.setVisibility(View.VISIBLE);
            mContentLayout.setVisibility(View.GONE);
        }

        return root;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public String getEmail() {
        return mEmailEt.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPasswordEt.getText().toString();
    }

    @Override
    public void showLoginResult(Boolean loggedIn) {

    }

    @Override
    public void setEmailError(Boolean hasError) {
        if (hasError) {
            mEmailLayout.setError(getString(R.string.invalid_email));
        } else {
            mEmailLayout.setError(null);
        }
    }

    @Override
    public void setPasswordError(Boolean hasError) {
        if (hasError) {
            mPasswordLayout.setError(getString(R.string.invalid_password));
        } else {
            mPasswordLayout.setError(null);
        }
    }

    @Override
    public void finishActivity() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void showLogoutToast() {
        if (getActivity() != null) {
            Toast toast = Toast.makeText(getActivity()
                    , getString(R.string.login_toast), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Function to check network status
     * @return is the state of network, true means connected, false means no network
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext()
                .getSystemService(getContext().CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
