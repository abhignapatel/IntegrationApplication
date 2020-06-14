package com.jaydonga.integrationapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView profile_image;
    TextView name;
    TextView email_id;
    TextView id;
    Button signoutBtn;
    LinearLayout linearLayout;
    SignInButton signInButton;

    GoogleSignInClient googleSignInClient;

   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       signInButton = findViewById(R.id.sign_in_button);
        profile_image = findViewById(R.id.imageView);
        name = findViewById(R.id.name);
        email_id = findViewById(R.id.email_id);
        id = findViewById(R.id.id);
        signoutBtn = findViewById(R.id.sign_out_btn);
        linearLayout = findViewById(R.id.linearlayout);
        linearLayout.setVisibility(View.GONE);//don't show the layout

        signInButton.setOnClickListener(this);
        signoutBtn.setOnClickListener(this);


        GoogleSignInOptions signInOptions = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)//Google Sign-In to request users' ID and basic profile information, create a GoogleSignInOptions object with the DEFAULT_SIGN_IN parameter.
            .requestEmail()//To request users' email addresses as well, create the GoogleSignInOptions object with the requestEmail option.
            .build();

        googleSignInClient = GoogleSignIn.getClient(this,signInOptions);


    }
    @Override
    public void onClick(View v) {
           switch (v.getId()){
               case R.id.sign_in_button:
                   signIn();
                   break;
               case R.id.sign_out_btn:
                   signOut();
                   break;
           }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleSignInClient.asGoogleApiClient()).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 5);

    }

    private void  updateUI(boolean isLogin){
        if (isLogin){
            linearLayout.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==5){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }
    private void handleSignInResult(GoogleSignInResult result){

        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();

            name.setText(account.getDisplayName());
            email_id.setText(account.getEmail());
            id.setText(account.getId());

            Glide.with(getApplicationContext()).load(account.getPhotoUrl()).into(profile_image);

            updateUI(true);
        }else {
            updateUI(false);
        }
    }
}