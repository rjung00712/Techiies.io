package io.techies.com.puzzle_8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Richard on 4/3/17.
 */

public class PuzzleBoardView extends View implements Serializable {
    public static final int NUM_SHUFFLE_STEPS = 120;

    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();
    private int moveCounter;
    private PuzzleActivity puzzleActivity = (PuzzleActivity) getContext();


    private String userName;

    // list of all players
    /*
    public static Queue<Player> listOfPlayers = new PriorityQueue<>(10, new Comparator<Player>() {
        @Override
        public int compare(Player player1, Player player2) {
            if(player1.getMoves() < player2.getMoves()) {
                return -1;
            } else if(player1.getMoves() > player2.getMoves()) {
                return 1;
            }
            return 0;
        }
    });
    */
    public static ArrayList<Player> listOfPlayers = new ArrayList<>(10);
    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
        moveCounter = 0;
    }

    public void initialize(Bitmap imageBitmap, String userName) {
        Log.i("this is width", String.valueOf(getWidth()));

        int width = getWidth();

        this.userName = userName;

        puzzleBoard = new PuzzleBoard(imageBitmap, width);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }
    /*
    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            // Do something. Then:
            for (int i = 0; i < NUM_SHUFFLE_STEPS; i++) {
                ArrayList<PuzzleBoard> boards = puzzleBoard.neighbors();
                puzzleBoard = boards.get(random.nextInt(boards.size()));

            }
            puzzleBoard.reset();
            invalidate();
        }
    }
    */

    public void shuffle()
    {
        puzzleBoard.shuffle(NUM_SHUFFLE_STEPS);
        puzzleBoard.reset();
        setMoveCounter(0);
        puzzleActivity.moveCounterText.setText("" + moveCounter);
        invalidate();
    }

    public static String fileName = "puzzleActivity";
    public void save( ) {
        Context context = getContext();
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


    public void load() {
        Context context = getContext();
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
          ///  PuzzleActivity simpleClass = (PuzzleActivity) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        moveCounter++;
                        puzzleActivity.moveCounterText.setText("" + moveCounter);
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations You solved it!", Toast.LENGTH_LONG);
                            toast.show();
                            if(listOfPlayers.size() < 10 || listOfPlayers.get(9).getMoves() > moveCounter)
                            {
                                if(listOfPlayers.size() == 10)
                                    listOfPlayers.remove(9);
                                puzzleActivity.createAlert();
                            }
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public int getMoveCounter(){
        return moveCounter;
    }

    public void setMoveCounter(int i) { moveCounter = i;}

    public void solve() {
        puzzleBoard.reset();
//        PriorityQueue<PuzzleBoard> solutionQueue = new PriorityQueue<PuzzleBoard>(1000, new PuzzleComparator());
//        puzzleBoard.reset();
//        solutionQueue.add(puzzleBoard);
//
//        while(!solutionQueue.isEmpty()){
//            PuzzleBoard currBoard = solutionQueue.poll();
//            if(currBoard.priority() == 0){
//                //create an ArrayList of all PuzzleBoards leading to this solution by getting
//                //PuzzleBoard.previousBoard then use Collections.reverse to turn it into
//                //an in-order sequence of all the steps to solving the puzzle. If you copy that
//                //ArrayList to PuzzleBoardView.animation the given implementation of onDraw will
//                //sequence of steps to solve the puzzle
//
//                ArrayList<PuzzleBoard> pathToVic = currBoard.getPreviousBoards();
//                Collections.reverse(pathToVic);
//                animation = pathToVic;
//            }
//
//            ArrayList<PuzzleBoard> solutions = currBoard.neighbors();
//            for (PuzzleBoard board:solutions) {
//                solutionQueue.add(board);
//            }
//        }
    }


}


