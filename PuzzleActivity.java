package io.techies.com.puzzle_8;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;

public class PuzzleActivity extends AppCompatActivity implements Serializable {

    static final int    REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap = null;
    private PuzzleBoardView boardView;
    public TextView moveCounterText;
    public String userName;

//    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        // This code programmatically adds the PuzzleBoardView to the UI.
        RelativeLayout container = (RelativeLayout) findViewById(R.id.puzzle_container);

//        imageView = (ImageView) findViewById(R.id.imageView);

        boardView = new PuzzleBoardView(this);
        moveCounterText = (TextView) findViewById(R.id.MoveCounter);
        // Some setup of the view.
        boardView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        container.addView(boardView);


        // put number of moves into the text box
        //moveCounterText.setText(Integer.toString(boardView.getMoveCounter()));
        moveCounterText.setText(Integer.toString(boardView.getMoveCounter()));

//        final SharedPreferences prefs = PreferenceManager
//                .getDefaultSharedPreferences(this);
//        String userName = prefs.getString("user_name", null);
//        if (userName == null) {
//            EditText input = new EditText(this);
//            input.setId(1000);
//            AlertDialog dialog = new AlertDialog.Builder(this)
//                    .setView(input).setTitle("Enter your username!")
//                    .setPositiveButton("Ok",
//                            new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    EditText theInput = (EditText) ((AlertDialog) dialog)
//                                            .findViewById(1000);
//                                    String enteredText = theInput.getText()
//                                            .toString();
//                                    if (!enteredText.equals("")) {
//                                        SharedPreferences.Editor editor = prefs
//                                                .edit();
//                                        editor.putString("user_name",
//                                                enteredText);
//                                        editor.commit();
//                                    }
//                                }
//                            }).create();
//            dialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_puzzle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement2
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void dispatchTakePictureIntent(View view) {  // handler for the "Take photo" button
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        // maybe add shuffle here when ever a new picture is taken (optional)
    }

    // calls puzzle board activity to handle the leader board display intent
    public void displayLeaderBoardIntent(View view) {
        Intent leaderBoardIntent = new Intent(this, LeaderBoard.class);
        startActivity(leaderBoardIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();

            imageBitmap = (Bitmap) extras.get("data");

            boardView.initialize(imageBitmap, userName);
//            imageView.setImageBitmap(imageBitmap);
            shuffleImage(boardView);
        }
    }

    //attempting to save
    protected void onSavingResult(){}

    public void shuffleImage(View view) {
        boardView.shuffle();
    }

    public void solve(View view) {
        boardView.solve();
    }

    //to save the game
    public  void saveGame(View view){
        boardView.save();
    }
    public void LoadGame(View view){
        boardView.load();
    }
    //creating a file
  /*  public static String fileName = "PuzzleActivity";
    public void saveGame( Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void LoadGame(Context context) {

        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            PuzzleActivity simpleClass = (PuzzleActivity) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/
    public int getMoveCounter(View view) { return boardView.getMoveCounter();}

    // creates custom alert dialog box for username input
    public void createAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        View mView = inflater.inflate(R.layout.user_name, null);
        final EditText editText = (EditText) mView.findViewById(R.id.username);

        // inflate and set the layout for the dialog
        // pass null as a parent view because its going in the dialog layout
        builder.setView(mView)
            .setPositiveButton("enter", new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    userName = editText.getText().toString();

                    if(userName.equals("")) {
                        Toast.makeText(PuzzleActivity.this, "must enter a username", Toast.LENGTH_SHORT).show();
                        createAlert();
                    } else {
                        boardView.listOfPlayers.add(new Player(userName, boardView.getMoveCounter()));
                        Collections.sort(boardView.listOfPlayers);
                        boardView.setMoveCounter(0);
                    }

                }
            });
        builder.setCancelable(false);
        builder.show();
    }
}