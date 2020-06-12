package com.flourmillco.flourmill_1.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.flourmillco.flourmill_1.Model.Bakery;
import com.flourmillco.flourmill_1.Model.User;
import com.flourmillco.flourmill_1.R;
import com.flourmillco.flourmill_1.SingletonUser.UserSingle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private static final int ADDRESS_PICKER_REQUEST = 1020;

    private TextView t2;
    private Button b2;
    private Button b1;
    private int mYear;
    private int mMonth;
    private int mDay;
    private User user2;
    private String myFinalDate = "";
    private EditText na;
    private EditText em;
    private EditText pass;
    private EditText myBirth;
    private EditText MyNational;
    private EditText e1;
    private EditText e2;
    private Retrofit retrofit2;
    private String namec = "";
    private String emailc = "";
    private String passowrd1 = "";
    private UserClient userClientAPI2;
    private String job = "";
    private String phone = "";
    private String userid;
    private String address;
    private SharedPreferences pref3;
    private SharedPreferences pref4;
    private SharedPreferences.Editor editor4;
    private SharedPreferences.Editor editor;

    private double currentLatitude;
    private double currentLongitude;
    private ProgressBar mProgressBar;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        mProgressBar = findViewById(R.id.progressBar);
        pref3 = getApplicationContext().getSharedPreferences("secretcode", MODE_PRIVATE);
        pref4 = getApplicationContext().getSharedPreferences("userinfo", MODE_PRIVATE);
        editor4 = pref4.edit();
        editor = pref3.edit();

        mDb = FirebaseFirestore.getInstance();
        MapUtility.apiKey = getResources().getString(R.string.google_map_api_key);
        hideSoftKeyboard();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging).
                        connectTimeout(5, TimeUnit.MINUTES).
                        writeTimeout(5, TimeUnit.MINUTES).
                        readTimeout(5, TimeUnit.MINUTES)
                .build();

        Retrofit.Builder builder2 = new Retrofit.Builder().baseUrl("https://floutmill-jo.azurewebsites.net/").
                addConverterFactory(GsonConverterFactory.create()).client(client);
        retrofit2 = builder2.build();
        userClientAPI2 = retrofit2.create(UserClient.class);

        setTitle("SignUpActivity");
        b1 = findViewById(R.id.birthbutton);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        t2 = findViewById(R.id.signup);
        b2 = findViewById(R.id.upBtn);
        na = findViewById(R.id.name);
        em = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        myBirth = findViewById(R.id.birth);
        MyNational = findViewById(R.id.national);
        e1 = findViewById(R.id.job);
        e2 = findViewById(R.id.phone);

        Toasty.info(getApplicationContext(), "Before you register account you must select your store location.", Toasty.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage("Do you want to select your bakery location")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent intent = new Intent(SignUpActivity.this, LocationPickerActivity.class);
                        startActivityForResult(intent, ADDRESS_PICKER_REQUEST);


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


        SetSpan(t2);
        b2.setOnClickListener(this);
        b1.setOnClickListener(this);
    }

    private void SetSpan(TextView text1) {
        SpannableString spanUp = new SpannableString("Already a member? LoginActivity");
        ClickableSpan clickableSpan = new ClickableSpan() {

            public void onClick(View textView) {
                Intent myIntet = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(myIntet);
            }

            public void updateDrawState(TextPaint textP) {
                super.updateDrawState(textP);
                textP.setUnderlineText(true);
                int myColor = ContextCompat.getColor(getApplicationContext(), R.color.myYellow);

                textP.setColor(myColor);
            }
        };
        spanUp.setSpan(clickableSpan, 18, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text1.setText(spanUp);
        text1.setMovementMethod(LinkMovementMethod.getInstance());
        int myColor = ContextCompat.getColor(getApplicationContext(), R.color.myYellow);
        //  text1.setHighlightColor(myColor);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent myInter2 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(myInter2);
                return true;
        }
        return true;
    }

    private boolean checkname() {
        String na1 = na.getText().toString().trim();
        if (na1.isEmpty()) {
            na.setError("This field can not be blank");
            return false;
        } else {
            na.setError(null);

            return true;
        }
    }

    private boolean checkemail() {
        String ea1 = em.getText().toString().trim();
        if (ea1.isEmpty()) {
            em.setError("This field can not be blank");
            return false;
        } else {
            em.setError(null);

            return true;
        }
    }

    private boolean checkpass() {
        String passc = pass.getText().toString().trim();
        if (passc.isEmpty()) {
            pass.setError("This field can not be blank");
            return false;
        } else {
            pass.setError(null);

            return true;
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.upBtn: {
                if (!checkname() | !checkemail() | !checkpass()) {
                    return;
                }

                try {


                    namec = na.getText().toString();
                    emailc = em.getText().toString();
                    passowrd1 = pass.getText().toString();
                    job = e1.getText().toString();
                    phone = e2.getText().toString();
                    showDialog();

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailc, passowrd1)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                                        user2 = new User();
                                        user2.setEmail(emailc);
                                        user2.setUsername(emailc.substring(0, emailc.indexOf("@")));
                                        user2.setUser_id(FirebaseAuth.getInstance().getUid());
                                        userid = user2.getUser_id();
                                        Bakery myBakery = new Bakery(namec, passowrd1, emailc, myFinalDate, Long.parseLong(MyNational.getText().toString()), phone, job, address, currentLatitude, currentLongitude);
                                        Call<JsonElement> call = userClientAPI2.register(myBakery);
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
                                                    Log.d("TAG", value + "value");
                                                    Log.d("TAG", userId + "id");
                                                    Log.d("TAG", name + "name");


                                                    GoToLogIn();
                                                    Toast.makeText(getApplicationContext(), "Account Created successfully", Toast.LENGTH_SHORT).show();
                                                } else {

                                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                                    firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                Log.d("TAG", "User deleted" + "");


                                                            }

                                                        }
                                                    });


                                                    Log.d("TAG", response.code() + "");
                                                    //          Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<JsonElement> call, Throwable t) {

                                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Log.d("TAG", "User deleted" + "");


                                                        }

                                                    }
                                                });
                                                Log.d("TAG", t.getMessage() + "");

                                                Toast.makeText(SignUpActivity.this, "Error in request", Toast.LENGTH_LONG).show();

                                            }
                                        });

                                        hideDialog();

                                    } else {
                                        View parentLayout = findViewById(android.R.id.content);
                                        Snackbar.make(parentLayout, "Something went wrong.", Snackbar.LENGTH_SHORT).show();
                                        hideDialog();
                                    }

                                }
                            });
                } catch (Exception e) {
                    Log.d(TAG, "error");
                }
                break;
            }
            case R.id.birthbutton: {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                myFinalDate = mYear + "-" + mMonth + "-" + mDay;

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                myBirth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


                break;
            }
        }
    }

    private void GoToLogIn() {

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()

                .build();
        mDb.setFirestoreSettings(settings);

        DocumentReference newUserRef = mDb
                .collection("Users")
                .document(FirebaseAuth.getInstance().getUid());


        newUserRef.set(user2).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideDialog();

                if (task.isSuccessful()) {
                    DocumentReference userRef = mDb.collection("Users")
                            .document(FirebaseAuth.getInstance().getUid());


                    userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: successfully set the user client.");
                                com.flourmillco.flourmill_1.Location.User user = task.getResult().toObject(com.flourmillco.flourmill_1.Location.User.class);

                                ((UserSingle) (getApplicationContext())).setUser(user);
                            }
                        }
                    });

                    redirectLoginScreen();
                } else {
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Something went wrong.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void redirectLoginScreen() {
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra("address") != null) {
                    address = data.getStringExtra("address");
                    currentLatitude = data.getDoubleExtra("lat", 0.0);
                    currentLongitude = data.getDoubleExtra("long", 0.0);
                    Log.d("Address", address);
                    Log.d("corrdiantes", "latt" + currentLatitude + "  Long:" + currentLongitude);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
