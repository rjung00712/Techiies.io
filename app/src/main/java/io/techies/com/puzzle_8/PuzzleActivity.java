package io.techies.com.puzzle_8;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PuzzleActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap = null;
    private PuzzleBoardView boardView;
    public TextView moveCounterText;

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
        boardView.setMoveCounter(0);
        moveCounterText.setText(Integer.toString(boardView.getMoveCounter()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();

            imageBitmap = (Bitmap) extras.get("data");

            boardView.initialize(imageBitmap);
//            imageView.setImageBitmap(imageBitmap);
        }
    }

    public void shuffleImage(View view) {
        boardView.shuffle();
    }

    public void solve(View view) {
        boardView.solve();
    }

    public int getMoveCounter(View view) { return boardView.getMoveCounter();}
}