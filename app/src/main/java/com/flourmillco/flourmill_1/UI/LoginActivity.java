package com.flourmillco.flourmill_1.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.flourmillco.flourmill_1.API_Calls.UserClient;
import com.flourmillco.flourmill_1.Location.User;
import com.flourmillco.flourmill_1.Model.AuchDriverLoginFirebase;
import com.flourmillco.flourmill_1.Model.Bakery;
import com.flourmillco.flourmill_1.Model.Login;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.SingletonUser.UserSingle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private TextView t1;
    private Button b1;
    private EditText passwordx1;
    private EditText emaila;
    private SharedPreferences pref3;
    private SharedPreferences.Editor editor;
    private Login truckDriverLogin;

    private Retrofit retrofit;
    private SharedPreferences pref4;
    private SharedPreferences.Editor editor4;
    private UserClient userClientAPI;
    private AuchDriverLoginFirebase firebase;
    private ProgressBar mProgressBar;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        pref3 = getApplicationContext().getSharedPreferences("secretcode", MODE_PRIVATE);
        editor = pref3.edit();
        mProgressBar = findViewById(R.id.progressBar);
        pref4 = getApplicationContext().getSharedPreferences("userinfo", MODE_PRIVATE);
        editor4 = pref4.edit();
        hideSoftKeyboard();
        setupRetrofit();
        setupFirebaseAuth();
        setTitle("Log in");
        t1 = findViewById(R.id.signup);
        b1 = findViewById(R.id.loginBtn);
        passwordx1 = findViewById(R.id.passwordLog);
        emaila = findViewById(R.id.emailLog);

        setSpan2(t1);
        setupFirebaseAuth();
       /* shared1 = getSharedPreferences("Account", 0);
        String token = pref3.getString("token", "");
        if (!token.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();

        } else {

        }
*/
        b1.setOnClickListener(this);

    }

    private void setSpan2(TextView text2) {
        SpannableString spanIn = new SpannableString("No account yet? Create one");
        ClickableSpan clickableSpan = new ClickableSpan() {

            public void onClick(View textView) {
                Intent myIntet = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(myIntet);
            }

            public void updateDrawState(TextPaint textP) {
                super.updateDrawState(textP);
                textP.setUnderlineText(true);
                textP.setUnderlineText(true);
                int myColor = ContextCompat.getColor(getApplicationContext(), R.color.myYellow);
                textP.setColor(myColor);


            }
        };
        spanIn.setSpan(clickableSpan, 16, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        text2.setText(spanIn);
        text2.setMovementMethod(LinkMovementMethod.getInstance());
        int myColor = ContextCompat.getColor(getApplicationContext(), R.color.myYellow);
        // text2.setHighlightColor(myColor);
    }

    private boolean checkpass() {
        String passc = passwordx1.getText().toString().trim();
        if (passc.isEmpty()) {
            passwordx1.setError("This field can not be blank");
            return false;
        } else {
            passwordx1.setError(null);

            return true;
        }
    }

    private boolean checkemail() {
        String ea1 = emaila.getText().toString().trim();
        if (ea1.isEmpty()) {
            emaila.setError("This field can not be blank");
            return false;
        } else {
            emaila.setError(null);

            return true;
        }
    }

    @Override
    public void onClick(View v) {
        String emailc1 = "", passowrd1 = "";
        String prefEmail = "", prefPass;
        switch (v.getId()) {
            case R.id.loginBtn: {
                if (!checkemail() | !checkpass()) {
                    return;
                } else {

                    login();
                }
                break;
            }
        }
    }

    private void login() {


        String v1 = emaila.getText().toString();
        String v2 = passwordx1.getText().toString();

        truckDriverLogin = new Login(v1, v2);

        Call<JsonElement> call = userClientAPI.login(truckDriverLogin);
        firebase = new AuchDriverLoginFirebase(v1, v2);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {

                    JsonObject json_results = response.body().getAsJsonObject().get("user").getAsJsonObject();

                    Gson g = new Gson();

                    Bakery p = g.fromJson(json_results, Bakery.class);

                    editor4.putInt("id", p.getId());
                    editor4.putString("destination", p.getAddress());
                    editor4.putFloat("lat", (float) p.getLatitude());
                    editor4.putFloat("lon", (float) p.getLongitude());
                    editor4.putString("phone", p.getPhoneNumber());
                    editor4.apply();

                    //    ((UserSingleAPI) (getApplicationContext())).setBakery(p);
                    Log.d("TAG", p.getAddress() + "addressnew");
                    Log.d("TAG", p.getId() + "idnew");
                    Log.d("TAG", response.message() + "messagenew");
                    Log.d("TAG", response.code() + "");

                    String value = response.body().getAsJsonObject().get("token").getAsString();

                    JWT jwt = new JWT(value);

                    Claim userId = jwt.getClaim("nameid");
                    Claim name = jwt.getClaim("unique_name");

                    String myNameId = userId.asString();
                    editor.putString("token", value);
                    editor.putString("nameid", myNameId);
                    editor.putString("name", name.asString());
                    editor.apply();


                } else {
                    Log.d("TAG", response.code() + "");
                    Toast.makeText(LoginActivity.this, "Log in information Not Correct", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("TAG", t.getMessage() + "");

                Toast.makeText(LoginActivity.this, "Error In Request", Toast.LENGTH_LONG).show();


            }
        });


        signInFirebase();
    }

    private void signInFirebase() {

        Log.d(TAG, "onClick: attempting to authenticate.");

        showDialog();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(firebase.getEmail(),
                firebase.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        hideDialog();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });

    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(LoginActivity.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    DocumentReference userRef = db.collection("Users")
                            .document(user.getUid());


                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: successfully set the user client.");
                                User user = task.getResult().toObject(User.class);

                                ((UserSingle) (getApplicationContext())).setUser(user);
                            }
                        }
                    });

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }


    private void setupRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging).
                        connectTimeout(5, TimeUnit.MINUTES).
                        writeTimeout(5, TimeUnit.MINUTES).
                        readTimeout(5, TimeUnit.MINUTES)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://floutmill-jo.azurewebsites.net/").
                addConverterFactory(GsonConverterFactory.create()).client(client);
        retrofit = builder.build();
        userClientAPI = retrofit.create(UserClient.class);


    }
}
