package com.perfei.taolu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.maxleap.MLLog;
import com.maxleap.MLUser;
import com.maxleap.MLUserManager;
import com.maxleap.SignUpCallback;
import com.maxleap.exception.MLException;
import com.perfei.taolu.utils.BaseActivity;


public class SignupActivity extends BaseActivity {

    public static final String TAG = SignupActivity.class.getName();

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpToolbar();
        setTitle("注册");
        mEmailEditText = (EditText) findViewById(R.id.signup_email_edit_text);
        mEmailTextInputLayout = (TextInputLayout) findViewById(R.id.signup_email_input_layout);
        mPasswordEditText = (EditText) findViewById(R.id.signup_password_edit_text);
        mPasswordTextInputLayout = (TextInputLayout) findViewById(R.id.signup_password_input_layout);
        mEmailTextInputLayout.setErrorEnabled(true);
        mPasswordTextInputLayout.setErrorEnabled(true);

        findViewById(R.id.signup_button_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (validate()) {
                    MLUser user = new MLUser();
                    user.setUserName(mEmailEditText.getText().toString());
                    user.setPassword(mPasswordEditText.getText().toString());

                    MLUserManager.signUpInBackground(user, new SignUpCallback() {
                        @Override
                        public void done(final MLException e) {
                            if (e != null) {
                                MLLog.e(TAG, e);
                                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(SignupActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                            MLUser.logOut();
                            //startActivity(new Intent(mContext, LoginActivity.class));
                            finish();
                        }
                    });
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean validate() {
        mEmailTextInputLayout.setError("");
        mPasswordTextInputLayout.setError("");
        if (TextUtils.isEmpty(mEmailEditText.getText().toString())) {
            mEmailTextInputLayout.setError(getString(R.string.err_require_email));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText().toString()).matches()) {
            mEmailTextInputLayout.setError(getString(R.string.err_invalid_email));
            return false;
        }
        if (TextUtils.isEmpty(mPasswordEditText.getText().toString())) {
            mPasswordTextInputLayout.setError(getString(R.string.err_require_password));
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Signup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.perfei.taolu/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Signup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.perfei.taolu/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
