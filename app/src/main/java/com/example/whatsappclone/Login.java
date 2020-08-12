package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText emailEdt, passwordEdt;
    private Button loginBtn, signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdt = findViewById(R.id.edtEmailLogin);
        passwordEdt = findViewById(R.id.edtPasswordLogin);
        loginBtn = findViewById(R.id.loginbtnlogin);
        signupBtn = findViewById(R.id.singupbtnLogin);

        loginBtn.setOnClickListener(this);
        signupBtn.setOnClickListener(this);

        passwordEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(loginBtn);
                }

                return false;
            }
        });

        if(ParseUser.getCurrentUser() != null) {
            //ParseUser.getCurrentUser().logOut();
            transitionToSocialMedia();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.loginbtnlogin:
                if(emailEdt.getText().toString().equals("") || passwordEdt.getText().toString().equals("")){
                    FancyToast.makeText(Login.this, "the email and the password are required",
                            FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                }else {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.show();
                    ParseUser.logInInBackground(emailEdt.getText().toString(), passwordEdt.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                FancyToast.makeText(Login.this, user.getEmail() + "is signed up",
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            } else {
                                FancyToast.makeText(Login.this, e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                            progressDialog.dismiss();
                            transitionToSocialMedia();
                        }
                    });
                }
                break;
            case R.id.singupbtnLogin:
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
                break;
        }

    }
    public void loginLayoutTapped(View view){
        try {
            InputMethodManager inputMethodManager =  (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void transitionToSocialMedia(){
        Intent intent = new Intent(Login.this, WhatsAppUsers.class);
        startActivity(intent);
        finish();
    }

}