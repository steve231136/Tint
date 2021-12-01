package com.example.user.numericalsecurity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    int sto=4000;
    ImageView imageView1;
    TextView textView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);

        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate);

        imageView1 = (ImageView) findViewById(R.id.imageView);

        textView4 = (TextView) findViewById(R.id.textView);


        imageView1.startAnimation(animation1);

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);


        if(hour>=6 && hour<12){
            textView4.setText(String.valueOf("BONJOUR"));

        }else if (hour>= 12 && hour < 17){
            textView4.setText(String.valueOf("SALUT"));
        }else if (hour>=17 && hour < 24){
            textView4.setText(String.valueOf("BONSOIR"));
        }else if (hour>=0 && hour < 3){
            textView4.setText(String.valueOf("HELLO!"));
        }

        textView4.startAnimation(animation);







        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,FullscreenActivity.class);
                startActivity(intent);
                finish();
            }
        },sto);






    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
