package com.example.user.numericalsecurity;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.format.Formatter;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public  class Main2Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,SensorEventListener, SharedPreferences.OnSharedPreferenceChangeListener, TextToSpeech.OnInitListener{
    int sto=6000;
    TextView t19,t18,t17,t20;
    public GoogleApiClient mApiClient;
    ArrayAdapter arrayAdapter ;
    ArrayList<DetectedActivity> lv = new ArrayList<>();
    ListView listView;
    TextView textView,textView7;
    BroadcastReceiver broadcastReceiver;
    private ActivityRecognitionClient activityRecognitionClient;

    private Context mContext;
    private BroadcastReceiver receiver;
    public static final String MOST_P = ".MOST DETECTED_ACTIVITY";
    public static final String DETECTED_ACTIVITY = ".DETECTED_ACTIVITY";
    String FileName = "myFile";

    private SensorManager sensorMan;
    private Sensor accelerometer;

    private float[] mGravity;
    private static final  int CAMERA_REQUEST = 150;

    private float mAccel;

    private float mAccelCurrent;

    private float mAccelLast;

//Define an ActivityRecognitionClient//

    private ActivityRecognition mActivityRecognitionClient;
    private ActivitiesAdapter mAdapter;
    FusedLocationProviderClient client;
    IntentFilter filter;
    EditText editText,e2;
     Switch sw2;

     TextToSpeech mTTS = null;
    private final int ACT_CHECK_TTS_DATA = 1000;
    int TAKE_PHOTO_CODE = 100;
    public static int count = 0 ;


    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver;
    private  static final int REQUEST_LOCATION = 102 ;




    @Override
    public void onBackPressed() {
        // Intent intt = new Intent(MainActivity.this,LoginActivity.class);
        //     startActivity(intt);




        Intent inten = new Intent(Intent.ACTION_MAIN);
        inten.addCategory(Intent.CATEGORY_HOME);
        inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inten);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        final TextView textView = (TextView) findViewById(R.id.textView18);
        setSupportActionBar(toolbar);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView3);
        editText = (EditText) findViewById(R.id.editText);
        e2 = (EditText) findViewById(R.id.editText2);

         sw2 = (Switch) findViewById(R.id.switch2);

        Switch sw4 = (Switch) findViewById(R.id.switch4);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce1);
        ImageView im5 = (ImageView) findViewById(R.id.image);
        im5.startAnimation(animation);

        Button button = (Button) findViewById(R.id.button2);
        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mAccel = 0.00f;

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"Please grant the permission discover nearby devices", Toast.LENGTH_SHORT);
            askForPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_LOCATION);
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        View bottomSheet = findViewById(R.id.designbottomsheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);



        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        mAccelCurrent = SensorManager.GRAVITY_EARTH;

        mAccelLast = SensorManager.GRAVITY_EARTH;

        mContext = this;



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile();

                Toast.makeText(getApplicationContext(), "L'outil Distance est activé", Toast.LENGTH_LONG).show();


                final Handler handler1 = new Handler();
                final Handler handler = new Handler();
                final int delay = 1000;
                final int delay1 = 3600000;
                mBluetoothAdapter.enable();

                _turnBluetoothDiscoverabilityOn();
                _startDeviceDiscovery();

                SharedPreferences sharedPreferences = getSharedPreferences(FileName,Context.MODE_PRIVATE);
                String defaultValue = "DefaultName";
                String defaultValue2 = "DefaultName";
                String number2 = sharedPreferences.getString("number2", defaultValue2);

                String soldname = mBluetoothAdapter.getName();
                if(soldname.equalsIgnoreCase(number2) == false){
                    mBluetoothAdapter.setName(number2);
                }




            }
        });
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {



                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    // If it's already paired, skip it, because it's been listed already
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                        short deviceRssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                        if(deviceRssi>-60 ){



                        }else if (deviceRssi>-60 ){

                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(1000);
                        }



                    }

                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {




                }
            }
        };


        readFile();


        sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            Calendar rightnow = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
                            String strDate = sdf.format(rightnow.getTime());
                            SharedPreferences sharedPreferences = getSharedPreferences(FileName,Context.MODE_PRIVATE);
                            String defaultValue2 = "DefaultName";
                            String number2 = sharedPreferences.getString("number2", defaultValue2);
                            String v = strDate + "    " + intent.getStringExtra("activity") + " " + "Confidence : " + intent.getExtras().getInt("confidence") + "%" + "\n";
                            if("Still".equals(intent.getStringExtra("activity"))){
                                imageView.setImageResource(R.drawable.ic_accessibility_black_24dp);

                            }else if ("On Foot".equals(intent.getStringExtra("activity"))){
                                imageView.setImageResource(R.drawable.ic_directions_walk_black_24dp);

                            }else if ("In Vehicle".equals(intent.getStringExtra("activity"))){
                                imageView.setImageResource(R.drawable.ic_local_taxi_black_24dp);
                                if (number2.isEmpty()){
                                    saySomething("Salut, vous êtes dans un véhicules verifiez que vous n'avez rien oublié en sortant", 0);
                                }else {
                                    saySomething("Hello user, You are in a vehicle make sure you don't forget anything", 0);
                                }
                            }else if ("Running".equals(intent.getStringExtra("activity"))){
                                imageView.setImageResource(R.drawable.baseline_directions_run_black_18dp);


                            }else if ("Unknown Activity".equals(intent.getStringExtra("activity"))){
                                imageView.setImageResource(R.drawable.ic_location_searching_black_24dp);
                            }

                            textView.setText(v);








                            if("In Vehicle".equals(intent.getStringExtra("activity"))){
                                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                wifiManager.setWifiEnabled(true);
                                Toast.makeText(getApplicationContext(), "Wifi is enabled", Toast.LENGTH_LONG).show();


                                getWifi();



                            }else{

                                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                wifiManager.setWifiEnabled(false);


                            }


                        }
                    };

                    IntentFilter filter = new IntentFilter();
                    filter.addAction("ImActive");
                    registerReceiver(receiver, filter);



                } else {


                }
            }
        });



        Intent intent =new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, ACT_CHECK_TTS_DATA);














        ListView detectedActivitiesListView = (ListView) findViewById(R.id.Listview);

        ArrayList<DetectedActivity> detectedActivities = ActivityIntentService.detectedActivitiesFromJson(
                PreferenceManager.getDefaultSharedPreferences(this).getString(
                        DETECTED_ACTIVITY, ""));

//Bind the adapter to the ListView//
        mAdapter = new ActivitiesAdapter(this, detectedActivities);
        detectedActivitiesListView.setAdapter(mAdapter);
        activityRecognitionClient = new ActivityRecognitionClient(this);


        setSupportActionBar(toolbar);
        mApiClient = new GoogleApiClient.Builder(Main2Activity.this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(Main2Activity.this)
                .addOnConnectionFailedListener(Main2Activity.this)
                .build();
        mApiClient.connect();


        Switch sw = (Switch) findViewById(R.id.switch1);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    Toast.makeText(getApplicationContext(), "Wifi is enabled", Toast.LENGTH_LONG).show();


                } else {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(false);
                    Toast.makeText(getApplicationContext(), "Wifi is disabled", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void readFile() {
        SharedPreferences sharedPreferences = getSharedPreferences(FileName,Context.MODE_PRIVATE);
        String defaultValue = "DefaultName";
        String defaultValue2 = "DefaultName";
        String number1 = sharedPreferences.getString("number1", defaultValue);
        String number2 = sharedPreferences.getString("number2", defaultValue2);
        editText.setText(number1);
        e2.setText(number2);

    }
    private void saveFile() {
        String strNumber = editText.getText().toString();
        String  strNumber2 = e2.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences(FileName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("number1", strNumber);
        editor.putString("number2", strNumber2);
        editor.commit();


    }



    public void requestUpdatesHandler(View view) {
//Set the activity detection interval. I’m using 3 seconds//
        Task<Void> task = activityRecognitionClient.requestActivityUpdates(
                2000,
                getActivityDetectionPendingIntent());
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                updateDetectedActivitiesList();
            }
        });

    }




        //Get a PendingIntent//
    private PendingIntent getActivityDetectionPendingIntent() {
//Send the activity data to our DetectedActivitiesIntentService class//
        Intent intent = new Intent(this, ActivityIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }
    //Process the list of activities//
    protected void updateDetectedActivitiesList() {
        ArrayList<DetectedActivity> detectedActivities = ActivityIntentService.detectedActivitiesFromJson(
                PreferenceManager.getDefaultSharedPreferences(mContext)
                        .getString(DETECTED_ACTIVITY, ""));

        mAdapter.updateActivities(detectedActivities);
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACT_CHECK_TTS_DATA) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {  mTTS = new TextToSpeech(this, this);
            } else {
            Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
                 }
       }

    }




            @Override
            protected void onResume() {
                super.onResume();
                PreferenceManager.getDefaultSharedPreferences(this)
                        .registerOnSharedPreferenceChangeListener(this);
                updateDetectedActivitiesList();
                sensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);

            }
            @Override
            protected void onPause() {
                PreferenceManager.getDefaultSharedPreferences(this)
                        .unregisterOnSharedPreferenceChangeListener(this);
                super.onPause();
                sensorMan.unregisterListener(this);
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu2, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.Settings) {
                    Intent intent = new Intent(this,SettingsActivity.class);
                    startActivity(intent);

                    return true;
                }

                return super.onOptionsItemSelected(item);
            }







            public void getWifiInfo (View v){

                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String macAddress = wifiInfo.getMacAddress();
                String ssid = wifiInfo.getSSID();
                String bssid = wifiInfo.getBSSID();
                t17 = (TextView) findViewById(R.id.ssid);
                t18 = (TextView) findViewById(R.id.bssid);
                t19 = (TextView) findViewById(R.id.ip);
                t17.setText("Cellule:" + ssid);
                t18.setText("Numéro clé :" + bssid);
                t19.setText("ID:"+ macAddress);
                String number = "690639990";

                WifiManager wifiManager1 = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);



                if( bssid == null || ssid==null ){
                    Toast.makeText(this,"Not Connected",Toast.LENGTH_SHORT).show();

                }else{



                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, wifiInfo.getSSID()+ "\n"+ wifiInfo.getBSSID()+"\n"+wifiInfo.getMacAddress(),null,null);



                    Toast.makeText(this,"Message Sent",Toast.LENGTH_SHORT).show();
                }



            }



//wifimanager getapplicationcontext
            public void getWifi (){
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String macAddress = wifiInfo.getMacAddress();
                String ssid = wifiInfo.getSSID();
                String bssid = wifiInfo.getBSSID();
                t17 = (TextView) findViewById(R.id.ssid);
                t18 = (TextView) findViewById(R.id.bssid);
                t19 = (TextView) findViewById(R.id.ip);
                t17.setText("Service:" + ssid);
                t18.setText("Numéro clé :" + bssid);
                t19.setText("ID:"+ macAddress);
                String number = "690639990";



                if( bssid == null ){
                    Toast.makeText(this,"Not Connected",Toast.LENGTH_SHORT).show();

                }else{
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, wifiInfo.getSSID()+ "\n"+ wifiInfo.getBSSID()+"\n"+wifiInfo.getMacAddress(),null,null);

                    if(sw2.isChecked()){
                        SharedPreferences sharedPreferences = getSharedPreferences(FileName,Context.MODE_PRIVATE);
                        String defaultValue = "DefaultName";
                        String number1 = sharedPreferences.getString("number1", defaultValue);


                        smsManager.sendTextMessage(number1, null, wifiInfo.getSSID()+ "\n"+ wifiInfo.getBSSID()+"\n"+wifiInfo.getMacAddress(),null,null);



                    }

                    Toast.makeText(this,"Message Sent",Toast.LENGTH_SHORT).show();
                    saySomething("Salut Michel, vous êtes dans un vehicule certifié intélligent aucune inquiétude à vous faire", 0);
                }



            }


            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Intent intent = new Intent(Main2Activity.this, ActivityRecognizedService.class);
                PendingIntent pendingIntent = PendingIntent.getService(Main2Activity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 3000, pendingIntent);



            }

            @Override
            public void onConnectionSuspended(int i) {

            }

            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }

            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                if (key.equals(DETECTED_ACTIVITY)) {
                    updateDetectedActivitiesList();
                }


            }
            private void  saySomething(String text, int qmode) {
                if(qmode == 1)
                    mTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
                else
                    mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }


            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){

                    if (mTTS != null){
                        int result = mTTS.setLanguage(Locale.ENGLISH);
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                            Toast.makeText(this, "TTS language is not supported", Toast.LENGTH_LONG).show();
                        }else{
                            saySomething("Smart application", 0);
                        }
                    }
                }else {
                    Toast.makeText(this, "TTS initialization failed", Toast.LENGTH_LONG).show();
                }
            }


    @Override
    public void onSensorChanged(SensorEvent event) { if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
        mGravity = event.values.clone();

            // Shake detection


            float x = mGravity[0];

            float y = mGravity[1];

            float z = mGravity[2];

            mAccelLast = mAccelCurrent;

            mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);

            float
                    delta
                    =
                    mAccelCurrent - mAccelLast
                    ;

            mAccel = mAccel * 0.9f + delta
            ;

            // Make this higher or lower according to how much

            // motion you want to detect

            if (mAccel > 12 ) {
                SharedPreferences sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
                String defaultValue2 = "";
                String number2 = sharedPreferences.getString("number2", defaultValue2);
                saySomething(number2, 0);




            }

        }

    }

    private void  _turnBluetoothDiscoverabilityOn(){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);

        startActivity(discoverableIntent);

    }


    private void askForPermission(String permission,Integer requestCode) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, permission);


        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            _startDeviceDiscovery();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    private void _startDeviceDiscovery() {

        if (mBluetoothAdapter.startDiscovery()) {

            // Toast.makeText(getApplicationContext(), "Social Distancing AI", Toast.LENGTH_SHORT).show();

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

            // Register for broadcasts when a device is discovered.

            this.registerReceiver(mReceiver, filter);

        } else {

            //If discovery hasn’t started, then display this alternative toast//


        }

    }

    public void takePicture() {
        try
        {
            Toast.makeText(this, "Taking a picture",Toast.LENGTH_SHORT).show();
            Camera camera = Camera.open();
            camera.setErrorCallback(errorCallback);
            try
            {
                camera.startPreview();
                camera.takePicture(null, null, jpegCallback);
            }
            finally
            {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        }
        catch
                (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    Camera.ErrorCallback errorCallback = new Camera.ErrorCallback() {
        public void
        onError(int arg0, Camera arg1) {

        }
    };
    //Handles data for jpeg picture

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
                        //<8>

        public void onPictureTaken(byte[] data, Camera camera) {

            String filePath = String.format("/storage/emulated/0/%1$s.jpg", new SimpleDateFormat(" yyyy.MM.dd.HH.mm.ss" ).format(new Date()));

                            FileOutputStream outStream = null;
                            try
                            {
                                //Write to SD Card
                                outStream = new FileOutputStream(filePath);
                                outStream.write(data);
                                outStream.close();
                            }
                            catch
                                    (Exception e) {

                            }
                        }
                    };


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
//super on destroy
    @Override
            protected void  onDestroy() {
        super.onDestroy();
                if (mTTS != null){
                    mTTS.stop();
                    mTTS.shutdown();
                }
}



        }