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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private Button signUpBtn, loginBtn;
    private EditText emailEdt, userNameEdt, passwordEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        emailEdt = findViewById(R.id.edtEmail);
        userNameEdt = findViewById(R.id.edtUserName);
        passwordEdt = findViewById(R.id.edtPassword);

        signUpBtn = findViewById(R.id.signupbtn);
        loginBtn = findViewById(R.id.loginbtn);

        passwordEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(signUpBtn);
                }

                return false;
            }
        });



        signUpBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null) {
            //ParseUser.getCurrentUser().logOut();
            transitionToSocialMedia();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginbtn:
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
                break;

            case R.id.signupbtn:
                if(emailEdt.getText().toString().equals("") ||
                        userNameEdt.getText().toString().equals("") ||
                        passwordEdt.getText().toString().equals("")){

                    FancyToast.makeText(SignUp.this,  " email, username and password are required  ",
                            FancyToast.LENGTH_LONG, FancyToast.INFO,true).show();

                }else{
                    final ParseUser parseUser = new ParseUser();
                    parseUser.setEmail(emailEdt.getText().toString());
                    parseUser.setUsername(userNameEdt.getText().toString());
                    parseUser.setPassword(passwordEdt.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.show();

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                FancyToast.makeText(SignUp.this,  " signed up",
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS,true).show();
                                transitionToSocialMedia();
                            }else{
                                FancyToast.makeText(SignUp.this, e.getMessage(),
                                        FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;
        }
    }
    public void rootLayouttapped(View view){
        try {
            InputMethodManager inputMethodManager =  (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void transitionToSocialMedia(){
        Intent intent = new Intent(SignUp.this, WhatsAppUsers.class);
        startActivity(intent);
        finish();
    }

}