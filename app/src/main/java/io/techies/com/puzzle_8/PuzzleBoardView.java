package io.techies.com.puzzle_8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Richard on 4/3/17.
 */

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();

    private String userName;

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations You solved it!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        puzzleBoard.addUser(userName);
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

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


