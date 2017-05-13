package com.example.user.fullthrottle;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

/**
 * Created by User on 4/10/2017.
 */

/*
* Affan Here is where we'll have to add firebase database code to upload all the ride details onto firebase.
* so we clicks the host button in the form we need to add whatever input hes given to firebase.
* This is just for adding to the firebase.
* Retrieving all of them and displaying them as card will be done in EventActivity using recyler view adapter
* So firebase part lets be done by this weekend
* S0 next weekend we can start with location and search queries for data structure part
* */
public class RideInputActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button host;
    SharedPreferences.Editor editor;
    SharedPreferences file;
    SharedPreferences user_info_file;
    FirebaseDatabase fbdatabase;
    private int year, month, day;
    private ImageView image;
    private TextView selectImage;
    private EditText destn,time,starting;
    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ride_input);
        file=getApplicationContext().getSharedPreferences("Pref",MODE_PRIVATE);
        editor=file.edit();
        image=(ImageView)findViewById(R.id.imageview);
        selectImage = (TextView)findViewById(R.id.image);
        user_info_file = getApplicationContext().getSharedPreferences("Personal_info",MODE_PRIVATE);
        host=(Button)findViewById(R.id.host);
        destn=(EditText)findViewById(R.id.destinationinput);
        starting=(EditText)findViewById(R.id.startinput);
        time=(EditText)findViewById(R.id.dateinput);
        auth=FirebaseAuth.getInstance();
        fbdatabase = FirebaseDatabase.getInstance();
        if(fbdatabase == null)
        {
            Toast.makeText(RideInputActivity.this,"Error",Toast.LENGTH_SHORT).show();
            return;
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(RideInputActivity.this, LoginActivity.class));
            finish();
        }

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();

            }
        });
        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeString=time.getText().toString();
                String destnString=destn.getText().toString();
                String startingString=starting.getText().toString();

                if(timeString == null || destnString ==null || startingString ==null)
                {
                    Toast.makeText(RideInputActivity.this,"Fields cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    Event e =new Event();
                    e.setDestn(destnString);
                    e.setDate(timeString);
                    e.setStart(startingString);
                    Gson gson=new Gson();
                    String json=gson.toJson(e);
                    editor.putString(destnString.concat(timeString).concat(startingString),json);
                    editor.apply();
                    editor.commit();
                    DatabaseReference myRef = fbdatabase.getReference("event");
                    myRef = myRef.child(startingString.concat(destnString)).push();
                    String name=user_info_file.getString("Username","null");
                    String phoneno = user_info_file.getString("Phone","null");
                    myRef.child("date").setValue(timeString);
                    myRef.child("destination").setValue(destnString);
                    myRef.child("start").setValue(startingString);
                    myRef.child("name").setValue(name);
                    myRef.child("phone").setValue(phoneno);
                    //myRef.child("Joined").s
                    Intent i = new Intent(RideInputActivity.this,EventActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    time.setText(arg1+"/"+arg2+"/"+arg3);
                }
            };

    private void openGallery() {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            Uri imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

        public boolean checksession()
        {
            if(FirebaseAuth.getInstance().getCurrentUser() ==null)
            {
                return true;
            }
            return false;
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.logout)
        {
            auth.signOut();
            boolean sess=checksession();
            if(sess)
            {
                Intent i =new Intent(RideInputActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
            else
                Toast.makeText(RideInputActivity.this,"Error in logging out",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
