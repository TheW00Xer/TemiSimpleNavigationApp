package com.example.simplenavigation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener;
import com.robotemi.sdk.navigation.model.Position;

public class MainActivity extends AppCompatActivity implements
        OnRobotReadyListener,
        OnCurrentPositionChangedListener {

    Robot mRobot;
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRobot = Robot.getInstance();   //Here we initialize Robot instance

        Button homePosition = findViewById(R.id.buttonHome);    //Next we defined "homePosition" button and found it by it's viewId

        homePosition.setOnClickListener(new View.OnClickListener() {    //setting up OnClickListener for this button
            @Override
            public void onClick(View view) {    //function onClick that starts action goTo "HomeBase" which is predefined position saved in Robot.
                mRobot.goTo("home base");
            }
        });

        Button firstPosition = findViewById(R.id.buttonFirst);
        firstPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //here function onClick starts action goTo but for a set of defined coordinates X,Y, and arrive there at defined Z axis rotation angle and display tilt angle
                mRobot.goToPosition(new Position((float) 0.5, (float) 0.5, (float) 0, 30));
                mRobot.tiltAngle(30,1);

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
        //Here we add Robot event listeners for events we will perform in onStart function
        mRobot.addOnRobotReadyListener(this);
        mRobot.addOnCurrentPositionChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Here we remove previously added event listeners in onStop function
        mRobot.removeOnRobotReadyListener(this);
        mRobot.removeOnCurrentPositionChangedListener(this);
    }

    /**
     * Here we add function "onRobotReady" that will hide our app's top bar when the parameter @param "isReady" is met
     */
    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            Log.i(TAG, "Robot is ready");
            mRobot.hideTopBar();
        }
    }

    /**
     * This function "onCurrentPositionChanged" uses parameter "position"
     * @param position to display robot's current X,Y position and rotation & display tilt angle in TextView
     */
    @Override
    public void onCurrentPositionChanged(@NonNull Position position) {
        TextView textViewPosition = findViewById(R.id.textViewPosition);
        String str = position.toString();
        Log.i(TAG, str);
        textViewPosition.setText(str);
    }
}