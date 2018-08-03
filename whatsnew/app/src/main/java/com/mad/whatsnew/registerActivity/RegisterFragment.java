package com.mad.whatsnew.registerActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.mad.whatsnew.R;

/**
 * Fragment class for register
 */
public class RegisterFragment extends Fragment implements RegisterContract.View {
    private RegisterContract.Presenter mPresenter;

    @BindView(R.id.register_activity_email_et) TextInputEditText mEmailEt;
    @BindView(R.id.register_activity_password_et) TextInputEditText mPasswordEt;
    @BindView(R.id.register_activity_email_inputlayout) TextInputLayout mEmailLayout;
    @BindView(R.id.register_activity_password_inputlayout) TextInputLayout mPasswordLayout;
    @BindView(R.id.register_activity_secpass_inputlayout) TextInputLayout mSecPassLayout;
    @BindView(R.id.register_activity_secpass_et) TextInputEditText mSecPassEt;

    @BindView(R.id.register_activity_register_btn) Button mRegisterBtn;
    @BindView(R.id.register_activity_cancel_btn) Button mCancelBtn;

    /**
     * Function for return new instance
     * @return new register fragment
     */
    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_register, container, false);
        ButterKnife.bind(this, root);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.registerBtnClickedEvent(getActivity());
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        return root;
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public String getEmail() {
        return mEmailEt.getText().toString();
    }

    @Override
    public String getSecPass() {
        return mSecPassEt.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPasswordEt.getText().toString();
    }

    @Override
    public void setSecPassError() {
        mSecPassLayout.setError(getString(R.string.register_secpass_error));
    }

    @Override
    public void setPasswordError() {
        mPasswordLayout.setError(getString(R.string.register_password_error));
    }

    @Override
    public void setEmailError() {
        mEmailLayout.setError(getString(R.string.register_email_error));
    }

    @Override
    public void clearEtError() {
        mSecPassLayout.setError(null);
        mPasswordLayout.setError(null);
        mEmailLayout.setError(null);
    }

    @Override
    public void finishActivity() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void registedToast() {
        if (getContext() != null) {
            Toast toast = Toast.makeText(getContext()
                    , getString(R.string.register_success_toast), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
