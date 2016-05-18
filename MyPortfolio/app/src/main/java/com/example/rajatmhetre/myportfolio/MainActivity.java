package com.example.rajatmhetre.myportfolio;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.datatype.Duration;


public class MainActivity extends ActionBarActivity {

    //Vars
    private Button SpotifyStreamerBtn, ScoresAppBtn, LibraryAppBtn, BuildItBiggerBtn, XYZReaderBtn, CapstoneBtn;
    private ViewGroup customToastLayout;


    //Custom Functions
    public static void showToast(Button button,final LayoutInflater inflater,final ViewGroup customToastLayout, final String msg, final Context appContext) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View layout = inflater.inflate(R.layout.toasts, customToastLayout);
                TextView TextViewToast = (TextView) layout.findViewById(R.id.customToastTextView);
                TextViewToast.setText(msg);

                Toast toast = new Toast(appContext);
                toast.setGravity(Gravity.BOTTOM,0,0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

            }
        });
    }

    //Default Functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context appContext = getApplicationContext();

        LayoutInflater inflater = getLayoutInflater();

        customToastLayout = (ViewGroup) findViewById(R.id.customToastLayout);

        SpotifyStreamerBtn = (Button) findViewById(R.id.button);
        ScoresAppBtn = (Button) findViewById(R.id.button2);
        LibraryAppBtn = (Button) findViewById(R.id.button3);
        BuildItBiggerBtn = (Button) findViewById(R.id.button4);
        XYZReaderBtn = (Button) findViewById(R.id.button5);
        CapstoneBtn = (Button) findViewById(R.id.button6);


        showToast(SpotifyStreamerBtn,inflater, customToastLayout, "This button will launch my Spotify Streamer App.", appContext);
        showToast(ScoresAppBtn,inflater, customToastLayout, "This button will launch my Scores App.", appContext);
        showToast(LibraryAppBtn,inflater, customToastLayout, "This button will launch my Library App.", appContext);
        showToast(BuildItBiggerBtn,inflater, customToastLayout, "This button will launch my Build It Bigger App.", appContext);
        showToast(XYZReaderBtn,inflater, customToastLayout, "This button will launch my XYZ Reader App.", appContext);
        showToast(CapstoneBtn,inflater, customToastLayout, "This button will launch my Capstone Project App.", appContext);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }
}
