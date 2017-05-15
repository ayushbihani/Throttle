package com.example.user.fullthrottle;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.List;
import java.util.Locale;

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
     SharedPreferences locationfile;
     SharedPreferences.Editor edit;
    private ImageView image;
    public String startlatitude,startlongitude,destlatitude,destlongitude;
    String timeString,destnString,startingString;
    private TextView selectImage;
    private EditText destn,time,starting;
    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ride_input);
        file=getApplicationContext().getSharedPreferences("Pref",MODE_PRIVATE);
        locationfile=getApplicationContext().getSharedPreferences("locationfile",MODE_PRIVATE);
        edit=locationfile.edit();
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
                timeString=time.getText().toString();
                 destnString=destn.getText().toString();
                startingString=starting.getText().toString();

                if(timeString == null || destnString ==null || startingString ==null)
                {
                    Toast.makeText(RideInputActivity.this,"Fields cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    try {
                        findlocation();
                        Log.d("locations",startlongitude+"LUL"+destlatitude);
                    }
                    catch (Exception e){}
                    Event e =new Event();
                    e.setDestn(destnString);
                    e.setDate(timeString);
                    e.setStart(startingString);
                    Gson gson=new Gson();
                    String json=gson.toJson(e);
                    editor.putString(destnString.concat(timeString).concat(startingString),json);
                    editor.apply();
                    editor.commit();
                    firebaseUpload();
                    //myRef.child("Joined").s
                    Intent i = new Intent(RideInputActivity.this,EventActivity.class);
                    startActivity(i);
                }

            }
        });
    }
    public void firebaseUpload()
    {
        DatabaseReference myRef = fbdatabase.getReference("event");
        myRef = myRef.child(startingString.concat(destnString)).push();
        String name=user_info_file.getString("Username","null");
        String phoneno = user_info_file.getString("Phone","null");
        myRef.child("startlatitude").setValue(locationfile.getString("startlat","null"));
        myRef.child("startlongitude").setValue(locationfile.getString("startlong","null"));
        myRef.child("destnlatitude").setValue(locationfile.getString("destlat","null"));

        myRef.child("destnlongitude").setValue(locationfile.getString("destlong","null"));
        myRef.child("date").setValue(timeString);
        myRef.child("destination").setValue(destnString);
        myRef.child("start").setValue(startingString);
        myRef.child("name").setValue(name);
        myRef.child("phone").setValue(phoneno);

    }

    public void findlocation() {
//        GeocodingLocation startingAddress = new GeocodingLocation();
//        startingAddress.getAddressFromLocation(starting.getText().toString(),
//                getApplicationContext(), new GeocoderHandler());
//        GeocodingLocation destnAddress = new GeocodingLocation();
//        destnAddress.getAddressFromLocation(destn.getText().toString(),
//                getApplicationContext(), new GeocoderHandler2());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String locations[] = new String[4];

        try {
            List<Address> addressList = geocoder.getFromLocationName(startingString, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);

                locations[0] = String.valueOf(address.getLatitude());
                locations[1] = String.valueOf(address.getLongitude());
                edit.putString("startlat", locations[0]);
                edit.putString("startlong", locations[1]);
                List<Address> addressList1 = geocoder.getFromLocationName(destnString, 1);
                if (addressList1 != null && addressList1.size() > 0) {
                    Address address1 = addressList1.get(0);

                    locations[2] = String.valueOf(address1.getLatitude());
                    locations[3] = String.valueOf(address1.getLongitude());
                    edit.putString("destlat", locations[2]);
                    edit.putString("destlong", locations[3]);
                    edit.commit();
                    edit.apply();

                }
            }
        } catch (Exception e) {
        }
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
    public class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    startlatitude= bundle.getString("latitude");
                    startlongitude=bundle.getString("longitude");

                    break;
                default:
                    startlatitude= null;
                    startlongitude=null;
            }

        }
    }
    public class GeocoderHandler2 extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    destlatitude= bundle.getString("latitude");
                    destlongitude=bundle.getString("longitude");
                    Log.d("DestnLoc:",destlatitude);
                    Log.d("DestnLoc:",destlongitude);
                    edit.apply();
                    break;
                default:
                    destlatitude= null;
                    destlongitude=null;
            }

        }
    }
}
