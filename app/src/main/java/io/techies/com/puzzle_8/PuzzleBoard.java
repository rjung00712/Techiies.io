package io.techies.com.puzzle_8;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Richard on 4/3/17.
 */

public class PuzzleBoard {
    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };

    private ArrayList<PuzzleTile> tiles;

    private int steps;
    private PuzzleBoard previousBoard;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        tiles = new ArrayList<>();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, parentWidth, parentWidth, true);
        int chunkSizeWidth = parentWidth / NUM_TILES;
        int chunkSizeHeight = scaledBitmap.getHeight() / NUM_TILES;
        int count = 0;
        for(int x = 0; x < NUM_TILES; x++){
            for(int y = 0; y < NUM_TILES; y++){
                if((count) < NUM_TILES * NUM_TILES - 1) {
                    Bitmap tile = Bitmap.createBitmap(scaledBitmap, y * chunkSizeWidth, x * chunkSizeHeight, chunkSizeWidth, chunkSizeHeight);
                    PuzzleTile t = new PuzzleTile(tile, count);
                    tiles.add(t);
                } else {
                    tiles.add(null);
                }
                count++;
            }
        }
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();

        steps = otherBoard.steps + 1;
        previousBoard = otherBoard;
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
        this.steps = 0;
        this.previousBoard = null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }
        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbors() {
        ArrayList<PuzzleBoard> boards = new ArrayList<>();
        PuzzleBoard t;
        PuzzleTile nullTile = null;

        int nullTileIndex = 0;

        int nullR = nullTileIndex % 3;
        int nullC = nullTileIndex / 3;

        for (int[] delta : NEIGHBOUR_COORDS) {
            int nX = nullC + delta[0];
            int nY = nullR + delta[1];

            if (nX >= 0 && nX < NUM_TILES && nY >= 0 && nY < NUM_TILES) {
                t = new PuzzleBoard(this);
                t.tryMoving(nX, nY);
                boards.add(t);
            }
        }

        return boards;
    }

    public int priority() {
        int mPriority = 0;
        for(int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            if(tiles.get(i) != null) {
                int originalPlace = tiles.get(i).getNumber();
                int x = i / NUM_TILES;
                int y = i % NUM_TILES;
                int xOriginal = originalPlace / NUM_TILES;
                int yOriginal = originalPlace % NUM_TILES;

                mPriority += (Math.abs(x - xOriginal) + Math.abs(y - yOriginal));
            }
        }
        int manhattanDistance = mPriority + steps;
        return manhattanDistance;
    }

    public void shuffle(int moves)
    {
        Random rand = new Random();
        int tilePos = 0;
        boolean swappable = false;
        for(int i = 0; i < moves; i++)
        {
            tilePos = 0;
            PuzzleTile tile = tiles.get(tilePos);
            while(tile != null)
            {
                tilePos++;
                tile = tiles.get(tilePos);
            }
            int newPos = 0;
            while(!swappable)
            {
                int nextTo = rand.nextInt(4);
                switch(nextTo)
                {
                    case 0:
                        newPos = tilePos - 3;
                        if(newPos >= 0)
                            swappable = true;
                        break;
                    case 1:
                        newPos = tilePos + 1;
                        if(newPos % 3 != 0 && newPos <= 8)
                            swappable = true;
                        break;
                    case 2:
                        newPos = tilePos + 3;
                        if(newPos <= 8)
                            swappable = true;
                        break;
                    case 3:
                        newPos = tilePos - 1;
                        if(newPos % 3 != 2 && newPos >= 0)
                            swappable = true;
                        break;
                }
            }
            swapTiles(tilePos, newPos);
            swappable = false;
        }
    }
}