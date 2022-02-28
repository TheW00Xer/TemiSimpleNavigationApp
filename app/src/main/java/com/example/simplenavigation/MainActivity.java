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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRobot = Robot.getInstance(); //Here we initialize Robot instance

        final EditText editTextX = findViewById(R.id.editTextX);
        final EditText editTextY = findViewById(R.id.editTextY);
        final EditText editTextYaw = findViewById(R.id.editTextYaw);
        final EditText editTextTilt = findViewById(R.id.editTextTilt);

        Button gotoCoordinates = findViewById(R.id.buttonCoordinates);
        gotoCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float posX = Float.parseFloat(editTextX.getText().toString());
                float posY = Float.parseFloat(editTextY.getText().toString());
                float yaw = Float.parseFloat(editTextYaw.getText().toString());
                int tilt = Integer.parseInt(editTextTilt.getText().toString());
                mRobot.goToPosition(new Position(posX, posY, yaw, tilt));
                mRobot.tiltBy(tilt);
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
     * Hide app's top bar when the parameter @param "isReady" is met
     */
    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            Log.i(TAG, "Robot is ready");
            mRobot.hideTopBar();
        }
    }

    /**
     * Value of @param position is used to display robot's current X,Y position and rotation & display tilt angle in TextView
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
     * Location name where Temi is going is represented by @param s
     * Navigation status (eg. GOING, CALCULATING, COMPLETE) is represented by @param s1 which is used here to determine that Robot arrived at set location
     * Id Code that represents description of Navigation status is represented by @param i
     * Informative description of navigation status (eg. obstacle info) is represented by @param s2
     */
    @Override
    public void onGoToLocationStatusChanged(@NonNull String s, @NonNull String s1, int i, @NonNull String s2) {
        if (s1.equals(COMPLETE)) {
            TtsRequest ttsRequest = TtsRequest.create("I'm here", true);
            mRobot.speak(ttsRequest);
        }
    }
}