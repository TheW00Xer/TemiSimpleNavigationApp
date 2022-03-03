package com.example.simplenavigation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener;
import com.robotemi.sdk.navigation.model.Position;

public class MainActivity extends AppCompatActivity implements
        OnRobotReadyListener,
        OnCurrentPositionChangedListener,
        OnGoToLocationStatusChangedListener {

    private Robot mRobot;
    private final String TAG = MainActivity.class.getSimpleName();

    /**
     * Function getInt is used to get integer value from EditText views
     * @param viewId is used to find view we want to get value from
     * @return is value of view or if view is empty function returns "50" so robot ends up facing upwards towards user
     */
    public int getInt(int viewId) {
        EditText text = findViewById(viewId);
        String tiltVal = text.getText().toString();
        if (tiltVal.equals("")) {
            text.setText("50");
            return 50;
        }
        else {
            return Integer.parseInt(tiltVal);
        }
    }

    /**
     * Function getFloat is used to get float values from EditText views
     * @param viewId is used to find view we want to get value from
     * @return is value of view or if view is empty function returns "0" to prevent app from crashing and robot shouldn't change it's position
     */
    public float getFloat(int viewId) {
        EditText text = findViewById(viewId);
        String floatVal = text.getText().toString();
        if (floatVal.equals("")) {
            text.setText("0");
            return 0;
        }
        else {
            return Float.parseFloat(floatVal);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRobot = Robot.getInstance(); //Here we initialize Robot instance

        Button gotoCoordinates = findViewById(R.id.buttonCoordinates);
        gotoCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float posX = getFloat(R.id.editTextX);
                float posY = getFloat(R.id.editTextY);
                float yaw = getFloat(R.id.editTextYaw);
                int tilt = getInt(R.id.editTextTilt);
                mRobot.goToPosition(new Position(posX, posY, yaw, tilt));
            }
        });

        Button homePosition = findViewById(R.id.buttonHome); //Next we defined "homePosition" button and found it by it's viewId
        homePosition.setOnClickListener(new View.OnClickListener() { //setting up OnClickListener for this button
            @Override
            public void onClick(View view) { //function onClick that starts action goTo "HomeBase" which is predefined position saved in Robot.
                mRobot.goTo("home base");
            }
        });

        Button firstPosition = findViewById(R.id.buttonFirst);
        firstPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //OnClick starts action goTo for a set of previously defined coordinates X,Y, and Z axis rotation angle and display tilt angle
                mRobot.goTo("a");
            }
        });

        Button secondPosition = findViewById(R.id.buttonSecond);
        secondPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRobot.goTo("b");
            }
        });

        Button thirdPosition = findViewById(R.id.buttonThird);
        thirdPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRobot.goTo("c");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Adding event listeners
        mRobot.addOnRobotReadyListener(this);
        mRobot.addOnCurrentPositionChangedListener(this);
        mRobot.addOnGoToLocationStatusChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Removing event listeners
        mRobot.removeOnRobotReadyListener(this);
        mRobot.removeOnCurrentPositionChangedListener(this);
        mRobot.removeOnGoToLocationStatusChangedListener(this);
    }

    /**
     * Hide app's top bar when
     * @param "isReady" is true
     */
    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            Log.i(TAG, "Robot is ready");
            mRobot.hideTopBar();
        }
    }

    /**
     * @param position is used to display robot's current X,Y position and rotation & display tilt angle in TextView
     */
    @Override
    public void onCurrentPositionChanged(@NonNull Position position) {
        TextView textViewPosition = findViewById(R.id.textViewPosition);
        String str = position.toString();
        Log.i(TAG, str);
        textViewPosition.setText(str);
    }

    /**
     * Functions checks GoToLocation Status of robot with following parameters:
     * @param s name of location where Temi is going
     * @param s1 navigation status (eg. GOING, CALCULATING, COMPLETE) which is used here to determine that Robot arrived at set location
     * @param i Id Code that represents description of Navigation status
     * @param s2 informative description of navigation status (eg. obstacle info)
     */
    @Override
    public void onGoToLocationStatusChanged(@NonNull String s, @NonNull String s1, int i, @NonNull String s2) {
        if (s1.equals(COMPLETE)) {
            TtsRequest ttsRequest = TtsRequest.create("I'm here", true);
            mRobot.speak(ttsRequest);
            int tilt = getInt(R.id.editTextTilt);
            mRobot.tiltAngle(tilt, 1);
        }
    }
}