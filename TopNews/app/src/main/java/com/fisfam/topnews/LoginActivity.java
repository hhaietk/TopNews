package com.fisfam.topnews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fisfam.topnews.pojo.User;
import com.fisfam.topnews.utils.LoginTool;
import com.fisfam.topnews.utils.NetworkCheck;
import com.fisfam.topnews.utils.UiTools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    public static void open(final Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private UserPreference mUserPref;

    private View mParentView;
    private EditText mEmailText, mPasswordText;
    private Button mLoginButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mUserPref = new UserPreference(this);

        initUiComponents();
        UiTools.setSmartSystemBar(this);
    }

    private void initUiComponents() {
        mParentView = findViewById(android.R.id.content);
        mProgressBar = findViewById(R.id.progress_bar);
        mLoginButton = findViewById(R.id.btn_login);
        mEmailText = findViewById(R.id.et_email);
        mPasswordText = findViewById(R.id.et_password);

        ImageButton showPassButton = findViewById(R.id.show_pass);
        showPassButton.setOnClickListener(v -> {
            // invert the "eye" icon after click
            v.setActivated(!v.isActivated());
            if (v.isActivated()) {
                // show password
                mPasswordText.setTransformationMethod(null);
            } else {
                // hide password
                mPasswordText.setTransformationMethod(new PasswordTransformationMethod());
            }
            mPasswordText.setSelection(mPasswordText.getText().length());
        });

        mLoginButton.setOnClickListener(
                v -> validateValue(mEmailText.getText().toString().trim(), mPasswordText.getText().toString().trim()));

        TextView registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(v -> {
            finish();
            RegisterActivity.open(this);
        });

        //TODO: forgot password?
        (findViewById(R.id.forgot_password)).setOnClickListener(v -> showDialogForgotPassword());
    }

    private void validateValue(final String email, final String password) {

        if (email.trim().isEmpty()) {
            Snackbar.make(mParentView, R.string.login_email_warning, Snackbar.LENGTH_SHORT).show();
            return;
        } else if (!LoginTool.isEmailValid(email)) {
            Snackbar.make(mParentView, R.string.login_email_invalid, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (password.trim().isEmpty()) {
            Snackbar.make(mParentView, R.string.login_password_warning, Snackbar.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        processLogin(email, password);
    }

    private void showLoading(final boolean show) {
        mLoginButton.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void processLogin(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showDialogSuccess(getString(R.string.login_success_info));
                        UserPreference pref = new UserPreference(this);
                        FirebaseUser user = mAuth.getCurrentUser();
                        pref.setUser(user.getDisplayName());
                        String uid = user.getUid();
                        saveUserFirestore(uid, user.getDisplayName(), email, mUserPref.getCountry());
                    } else {
                        showDialogFailed(getString(R.string.failed_text));
                    }
                    showLoading(false);
                });
    }

    private void saveUserFirestore(final String uid, final String name, final String email, final String country) {
        User user = new User(name, email, country);
        mFirestore.collection("users").document(uid).set(user);
    }

    private void showDialogForgotPassword() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forgot_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        final View content = dialog.findViewById(R.id.lyt_content);
        final View loadingView = dialog.findViewById(R.id.progress_loading);
        final EditText emailEditText = dialog.findViewById(R.id.et_email);
        final TextView errorMessage = dialog.findViewById(R.id.tv_message);

        errorMessage.setVisibility(View.GONE);

        (dialog.findViewById(R.id.bt_submit)).setOnClickListener(v -> {

            String email = emailEditText.getText().toString();

            if (email.equals("")) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText(R.string.profile_email_empty);
                return;
            } else if (!LoginTool.isEmailValid(email)) {
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText(R.string.profile_email_invalid);
                return;
            }

            content.setVisibility(View.INVISIBLE);
            loadingView.setVisibility(View.VISIBLE);

            mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(aVoid -> {
                        showDialogSuccess(getString(R.string.email_sent_info));
                        loadingView.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                    })
                    .addOnFailureListener(e -> {

                        Log.e(TAG, "showDialogForgotPassword: ", e);

                        if (NetworkCheck.isNetworkAvailable(this)) {
                            errorMessage.setText(R.string.failed_text);
                        } else {
                            errorMessage.setText(R.string.no_internet_text);
                        }

                        loadingView.setVisibility(View.GONE);
                        errorMessage.setVisibility(View.VISIBLE);
            });
        });

        (dialog.findViewById(R.id.btn_close)).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showDialogSuccess(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.OK, (dialogInterface, i) -> finish());
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showDialogFailed(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.OK, null);
        dialog.setCancelable(true);
        dialog.show();
    }
}
