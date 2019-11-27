package com.fisfam.topnews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fisfam.topnews.utils.LoginTool;
import com.fisfam.topnews.utils.UiTools;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    public static void open(final Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }

    private FirebaseAuth mAuth;
    private View mParentView;
    private EditText mEmailText, mPasswordText;
    private Button mLoginButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
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
        //(findViewById(R.id.forgot_password)).setOnClickListener(v -> showDialogForgotPassword());
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
                        pref.setUser(mAuth.getCurrentUser().getEmail());
                    } else {
                        showDialogFailed(getString(R.string.failed_text));
                    }

                    showLoading(false);
                });
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
