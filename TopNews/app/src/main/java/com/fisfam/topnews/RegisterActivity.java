package com.fisfam.topnews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fisfam.topnews.utils.LoginTool;
import com.fisfam.topnews.utils.UiTools;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICTURE = 500;
    private FirebaseAuth mAuth;
    private ImageView mAvatar;
    private Bitmap mBitmap = null;
    private String old_password = null;

    private View mParentView;
    private Button mRegisterButton;
    private ProgressBar mProgressBar;
    private EditText mNameText, mEmailText, mPasswordText;

    public static void open(final Context context) {
        Intent i = new Intent(context, RegisterActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        initUiComponents();
        UiTools.setSmartSystemBar(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // get the picture user picked for profile icon
        if (requestCode == REQUEST_CODE_PICTURE && resultCode == RESULT_OK) {
            Uri image_uri = data.getData();
            //mBitmap = AvatarUtils.getBitmapFormUri(this, image_uri);
            if (mBitmap != null) {
                //UiTools.displayImageCircle(this, mAvatar, mBitmap);
            }
        }
    }

    private void initUiComponents() {
        mParentView = findViewById(android.R.id.content);
        mAvatar = findViewById(R.id.avatar);
        mRegisterButton = findViewById(R.id.btn_register);
        mProgressBar = findViewById(R.id.progress_bar);
        mNameText = findViewById(R.id.et_name);
        mEmailText = findViewById(R.id.et_email);
        mPasswordText = findViewById(R.id.et_password);

        findViewById(R.id.lyt_term).setVisibility(View.VISIBLE);
        findViewById(R.id.lyt_login).setVisibility(View.VISIBLE);
        mRegisterButton.setText(R.string.form_action_register);
        TextView pageTitle = findViewById(R.id.page_title);
        pageTitle.setText(R.string.form_title_register);

        TextView login = findViewById(R.id.login);
        login.setOnClickListener(v -> {
            finish();
            LoginActivity.open(this);
        });

        RelativeLayout avatarLayout = findViewById(R.id.lyt_avatar);
        avatarLayout.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_PICTURE);
        });

        ImageButton showPassButton = findViewById(R.id.show_pass);
        showPassButton.setOnClickListener(v -> {
            v.setActivated(!v.isActivated());
            if (v.isActivated()) {
                mPasswordText.setTransformationMethod(null);
            } else {
                mPasswordText.setTransformationMethod(new PasswordTransformationMethod());
            }
            mPasswordText.setSelection(mPasswordText.getText().length());
        });

        // TODO: Terms and Policy link

        mRegisterButton.setOnClickListener(
                v -> processRegister(
                        mNameText.getText().toString().trim(),
                        mEmailText.getText().toString().trim(),
                        mPasswordText.getText().toString().trim()
                ));
    }

    private void processRegister(final String name, final String email, final String password) {

        if (!isValueValid(name, email, password)) return;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        showDialogSuccess(getString(R.string.register_success));

                        // update user name after user successfully created
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        // TODO: do we need Complete/Success Listener?
                        mAuth.getCurrentUser().updateProfile(profileUpdate);

                    } else {
                        showDialogFailed(getString(R.string.failed_text));
                    }

                    showLoading(false);

                });


    }

    private boolean isValueValid(final String name, final String email, final String password) {

        if (name.isEmpty()) {
            Snackbar.make(mParentView, R.string.profile_name_empty, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Snackbar.make(mParentView, R.string.profile_email_empty, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (!LoginTool.isEmailValid(email)) {
            Snackbar.make(mParentView, R.string.profile_email_invalid, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Snackbar.make(mParentView, R.string.profile_password_empty, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        if (!LoginTool.isPasswordValid(password)) {
            Snackbar.make(mParentView, R.string.profile_password_invalid, Snackbar.LENGTH_SHORT).show();
            return false;
        }

        showLoading(true);
        return true;
    }

    private void showLoading(final boolean show) {
        mRegisterButton.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void showDialogSuccess(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.OK, (dialogInterface, i) -> {
            finish();
            LoginActivity.open(this);
            });
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
