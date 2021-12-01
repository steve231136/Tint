package com.example.user.numericalsecurity;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by USER on 23/04/2019.
 */




public class ActivityRecognizedService extends IntentService{
    private static final String TAG = "ActivityRecognizedServi";
   public List<DetectedActivity> value;

    Context c;
    public  ActivityRecognizedService(){
        super("ActivityRecognitionService");
    }

    public ActivityRecognizedService(String name){
        super(name);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)){
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivity(result.getProbableActivities());



            DetectedActivity mostProbableActivity = result.getMostProbableActivity();

            int confidence = mostProbableActivity.getConfidence();


            String activityType = getActivityName(mostProbableActivity.getType());

            Intent i = new Intent("ImActive");
            i.putExtra("activity", activityType);
            i.putExtra("confidence",confidence);


            this.sendBroadcast(i);

        }
    }

    public  String getActivityName(int type){

        switch (type) {
            case DetectedActivity.ON_BICYCLE:
                return "On Bicycle";
            case DetectedActivity.ON_FOOT:
                return "On Foot";
            case DetectedActivity.RUNNING:
                return "Running";
            case DetectedActivity.STILL:
                return "Still";
            case DetectedActivity.TILTING:
                return "Tilting";
            case DetectedActivity.WALKING:
                return "Walking";
            case DetectedActivity.IN_VEHICLE:
                return "In Vehicle";
            default:
                return "Unknown Activity";

        }
    }

    public void handleDetectedActivity(List<DetectedActivity> probableActivities){



        for (DetectedActivity activity: probableActivities){



            switch (activity.getType()){
                case DetectedActivity.IN_VEHICLE:{
                    Log.d(TAG, "handleDetectedActivity: IN_VEHICLE:" + activity.getConfidence());
                    break;
                }
                case DetectedActivity.ON_BICYCLE:{
                    Log.d(TAG, "handleDetectedActivity: ON_BICYCLE:" + activity.getConfidence());
                    break;
                }

                case DetectedActivity.RUNNING:{
                    Log.d(TAG, "handleDetectedActivity: RUNNING:" + activity.getConfidence());
                    break;
                }
                case DetectedActivity.STILL:{
                    Log.d(TAG, "handleDetectedActivity: STILL:" + activity.getConfidence());

                    break;
                }
                case DetectedActivity.WALKING:{
                    Log.d(TAG, "handleDetectedActivity: WALKING:" + activity.getConfidence());
                    break;
                }
                case DetectedActivity.TILTING:{
                    Log.d(TAG, "handleDetectedActivity: TILTING:" + activity.getConfidence());
                    break;
                }
                case DetectedActivity.UNKNOWN:{
                    Log.d(TAG, "handleDetectedActivity: UNKNOWN:" + activity.getConfidence());
                    break;
                }

            }


        }
    }







}
