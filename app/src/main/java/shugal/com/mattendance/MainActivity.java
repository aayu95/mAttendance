package shugal.com.mattendance;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    ListView lectureList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Attendance");

        // timeTable = (Button) findViewById(R.id.timetableButton);

        notifications();
        // Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewLecture();
            }
        });

        lectureList = (ListView) findViewById(R.id.list_of_lectures);
        printLectures();

    }

    private void notifications() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("mAttendance")
                        .setContentText("Lecture Alert");

        Intent resultIntent = new Intent(this, TodayAttendance.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        mBuilder.setAutoCancel(true);

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);

        Log.d("Time ", "time is " + hour);

        // if (hour == 21) {
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        Vibrator v = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(1000);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DatabaseHelper db = new DatabaseHelper(this);

        if (id == R.id.showTimetable) {

            if (db.isLectureListEmpty()) {
                Snackbar.make(getCurrentFocus(), "Please start by adding some lectures ", Snackbar.LENGTH_LONG)
                        .setAction("Add Lectures", null).show();
            } else {
                db.close();
                startActivity(new Intent(MainActivity.this, DaysActivity.class));
            }

        } else if (id == R.id.todayAttendance) {

            if (db.isLectureListEmpty()) {
                Snackbar.make(getCurrentFocus(), "Please start by adding some lectures ", Snackbar.LENGTH_LONG)
                        .setAction("Add Lectures", null).show();
            } else {
                db.close();
                startActivity(new Intent(MainActivity.this, TodayAttendance.class));
            }

        } else if (id == R.id.dangerZone) {

            if (db.isLectureListEmpty()) {
                Snackbar.make(getCurrentFocus(), "Please start by adding some lectures ", Snackbar.LENGTH_LONG)
                        .setAction("Add Lectures", null).show();
            } else {
                db.close();
                startActivity(new Intent(MainActivity.this, Dangerzone.class));
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void addNewLecture() {
        final Context context = this;
        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.new_lecture, null);

        final AlertDialog.Builder customEventDialog = new AlertDialog.Builder(context);

        final EditText lecturename = (EditText) dialogView.findViewById(R.id.lecture_name);
        //amountText.setText("0");

        customEventDialog.setView(dialogView);
        customEventDialog.setTitle("New Lecture");
        customEventDialog.setCancelable(true);

        customEventDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (lecturename.getText().toString().equals("")) {
                    String message = "You did not Enter any Lecture";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    String nameOfLecture = lecturename.getText().toString();

                    LectureData data = new LectureData(nameOfLecture, 0, 0);
                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    db.addLecture(data);
                    printLectures();
                    Toast.makeText(getApplicationContext(), "Lecture Added", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        customEventDialog.create().show();
    }

    private void printLectures() {


        DatabaseHelper db = new DatabaseHelper(this);

        //Gets date from this activity

        View emptyLinearLayout = findViewById(R.id.frame);


        if (db.isLectureListEmpty()) {
            lectureList.setVisibility(View.GONE);
            emptyLinearLayout.setVisibility(View.VISIBLE);
            TextView emptyText = (TextView) findViewById(R.id.emptyListText);
            Typeface tf= Typeface.createFromAsset(getAssets(), "fonts/empty_list.ttf");
            emptyText.setTypeface(tf);

        } else {
            emptyLinearLayout.setVisibility(View.GONE);
            lectureList.setVisibility(View.VISIBLE);
            List<LectureData> contacts = db.showAllLectures();

            LectureCustomList adapter = new LectureCustomList(this, contacts);
            lectureList.setAdapter(adapter);
        }

        db.close();
    }


}
