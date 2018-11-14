package np.com.sudarshanregmi.multipleactivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static np.com.sudarshanregmi.multipleactivity.MinMax.Minimax;

public class SecondActivity extends AppCompatActivity {

    int player = 0;
    int ix, iy;
    boolean gameIsActive = true;
    int[] board = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    public static int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    public static int[][] validMoves = {{1, 3, 4}, {0, 2, 4}, {1, 4, 5}, {0, 4, 6}, {0, 1, 2, 3, 4, 5, 6, 7, 8}, {8, 2, 4}, {3, 7, 4}, {6, 8, 4}, {5, 7, 4}};
    public static int DEPTH;
    public static ArrayList<Integer> best_move = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> coords = new ArrayList<>();
    int pickedTappedCounter = -1;
    ImageView one, two, three, four, five, six, seven, eight, nine;
    int width;


    private boolean isBoardEmpty(int[] board) {
        for (int i : board) {
            if (i != 2) {
                return false;
            }
        }
        return true;
    }

    private boolean restrictRule(int tappedCounter){
        return tappedCounter == 4 && isBoardEmpty(board) && !canMove(board);
    }

    private int getNearestNode(float x, float y) {
        int index = -1;
        int temp = 1000000;
        System.out.println(coords.size());
        for (int i = 0; i < coords.size(); i++) {
            int distance = (int) Math.hypot(x - coords.get(i).get(0) - width, y - coords.get(i).get(1) - width);
            if (distance < temp) {
                temp = distance;
                index = i;
            }
        }
        return index;
    }

    private boolean isValidMove(int tappedCounter, int pickedTappedCounter){
        boolean valid = false;
        for (int i = 0; i < validMoves[tappedCounter].length; i++) {
            if (pickedTappedCounter == validMoves[tappedCounter][i]){
                valid = true;
                break;
            }
        }
        return valid;
    }

    private void fillBoard(int tappedCounter, ImageView counter){
        android.support.v7.widget.GridLayout parentView = findViewById(R.id.gridLayout);
        board[tappedCounter] = player;
        counter.setImageResource(R.drawable.blue_circle);
        ArrayList<Integer> ai_move = Minimax(board, DEPTH, -1000000, 1000000, true).moves;
        ImageView currentAiImage = parentView.findViewWithTag(ai_move.get(0) + "");
        board[ai_move.get(0)] = 1;
        currentAiImage.setImageResource(R.drawable.red_circle);
    }

    public static boolean isPieceMovable(int[] board, int counter) {
        boolean status = false;
        for (int index : validMoves[counter]) {
            if (board[index] == 2) {
                status = true;
                break;
            }
        }
        return status;
    }

    private boolean isOpponentPiece(int tappedCounter){
        return board[tappedCounter] != player && board[tappedCounter] != 2 && canMove(board);
    }

    private boolean canFillPieces(int tappedCounter){
        return board[tappedCounter] == 2 && gameIsActive && !canMove(board);
    }

    private boolean isImmovable(int tappedCounter){
        return !isPieceMovable(board, tappedCounter) && board[tappedCounter] == player;
    }

    public static boolean canMove(int[] board) {
        int countTwo = 0;
        for (int i : board) {
            if (i == 2) {
                countTwo++;
            }
        }
        if (countTwo == 3) {
            return true;
        } else {
            return false;
        }
    }

    private void aiMove(){
        android.support.v7.widget.GridLayout parentView = findViewById(R.id.gridLayout);
        ArrayList<Integer> ai_move = Minimax(board, DEPTH, -1000000, 1000000, true).moves;
        ImageView previousAiImage = parentView.findViewWithTag(ai_move.get(0) + "");
        board[ai_move.get(0)] = 2;
        board[ai_move.get(1)] = 1;
        previousAiImage.setImageResource(0);
        ImageView nextAiImage = parentView.findViewWithTag(ai_move.get(1) + "");
        nextAiImage.setImageResource(R.drawable.red_circle);
    }

    private void playerMove(int tappedCounter, int pickedTappedCounter){
        android.support.v7.widget.GridLayout parentView = findViewById(R.id.gridLayout);
        board[tappedCounter] = 2;
        board[pickedTappedCounter] = player;
        ImageView lastImage = parentView.findViewWithTag(tappedCounter + "");
        lastImage.setImageResource(0);
        ImageView setImage = parentView.findViewWithTag(pickedTappedCounter + "");
        setImage.setImageResource(R.drawable.blue_circle);
    }

    public static ArrayList isGameOver(int[] board) {
        int winner = 0;
        for (int[] winningPosition : winningPositions) {
            if (board[winningPosition[0]] == board[winningPosition[1]]
                    && board[winningPosition[1]] == board[winningPosition[2]]
                    && board[winningPosition[0]] != 2) {
                if (board[winningPosition[0]] == 1) {
                    winner = 1;
                }
                return new ArrayList<>(Arrays.asList(true, winner));
            }
        }
        return new ArrayList<>(Arrays.asList(false, null));
    }

    private void checkGameCondition(View view, int[] board){
        for (int[] winningPosition : winningPositions) {
            if (board[winningPosition[0]] == board[winningPosition[1]]
                    && board[winningPosition[1]] == board[winningPosition[2]]
                    && board[winningPosition[0]] != 2) {
                String winner = "AI won!";
                gameIsActive = false;
                if (board[winningPosition[0]] == 0) {
                    winner = "You won!";
                }
                TextView winnerMessage = findViewById(R.id.winnerMessage);
                winnerMessage.setText(winner);
                LinearLayout layout = findViewById(R.id.playAgainLayout);
                layout.setVisibility(view.VISIBLE);
            } else {
                boolean gameIsOver = true;

                for (int counterState : board) {
                    if (counterState == 2) gameIsOver = false;
                }

                if (gameIsOver) {
                    TextView winnerMessage = findViewById(R.id.winnerMessage);
                    winnerMessage.setText("It's a draw");
                    LinearLayout layout = findViewById(R.id.playAgainLayout);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void showGameOver(View view){
        TextView winnerMessage = findViewById(R.id.winnerMessage);
        winnerMessage.setText("Someone has won!");
        LinearLayout layout = findViewById(R.id.playAgainLayout);
        layout.setVisibility(view.VISIBLE);
    }

    public void playAgain(View view) {
        gameIsActive = true;
        LinearLayout layout = findViewById(R.id.playAgainLayout);
        layout.setVisibility(View.INVISIBLE);
        player = 0;

        for (int i = 0; i < board.length; i++) {
            board[i] = 2;
        }
        android.support.v7.widget.GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);
        }
    }

    public void backToMain(View view) {
        finish();
    }


    private View.OnTouchListener handleTouch = new View.OnTouchListener() {
        int dx, dy;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            ImageView counter = (ImageView) view;
            android.support.v7.widget.GridLayout parentView = findViewById(R.id.gridLayout);
            int tappedCounter = Integer.parseInt(counter.getTag().toString());

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    dx = (int) (view.getX() - event.getRawX());
                    dy = (int) (view.getY() - event.getRawY());
                    ix = (int) event.getRawX();
                    iy = (int) event.getRawY();

                    if (!isBoardEmpty(board)) {
                        break;
                    }

                    width = one.getWidth() / 2;

                    for (int i = 0; i < parentView.getChildCount(); i++) {
                        View currentView = parentView.findViewWithTag(i + "");
                        int[] location = new int[2];
                        ArrayList<Integer> temp = new ArrayList<>();
                        currentView.getLocationOnScreen(location);
                        temp.add(location[0]);
                        temp.add(location[1]);
                        coords.add(temp);
                    }
                    break;

                case MotionEvent.ACTION_MOVE:

                    if (!canMove(board)) {
                        break;
                    }
                    counter.animate().x(event.getRawX() + dx).y(event.getRawY() + dy).setDuration(0).start();
                    break;

                case MotionEvent.ACTION_UP:

                    if (restrictRule(tappedCounter)) {
                        Toast.makeText(SecondActivity.this, "First move can't be at the middle!", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (isImmovable(tappedCounter)) {
                        Toast.makeText(getApplicationContext(), "Immovable piece", Toast.LENGTH_SHORT).show();
                        counter.animate().x(ix + dx).y(iy + dy).setDuration(0).start();
                        return true;
                    }else if (canFillPieces(tappedCounter)) {
                        fillBoard(tappedCounter, counter);
                    }else if (isOpponentPiece(tappedCounter)) {
                        Toast.makeText(SecondActivity.this, "Not your piece!", Toast.LENGTH_SHORT).show();
                        counter.animate().x(ix + dx).y(iy + dy).setDuration(0).start();
                        return true;
                    }

                    pickedTappedCounter = getNearestNode(event.getRawX(), event.getRawY());

                    if (canMove(board)) {

                        if (pickedTappedCounter == -1) {
                            counter.animate().x(ix + dx).y(iy + dy).setDuration(0).start();
                            return true;
                        } else if (board[pickedTappedCounter] == 2) {
                            if (!isValidMove(tappedCounter, pickedTappedCounter)){
                                counter.animate().x(ix + dx).y(iy + dy).setDuration(0).start();
                                return true;
                            }

                            playerMove(tappedCounter, pickedTappedCounter);

                            if ((Boolean) isGameOver(board).get(0)) {
                                showGameOver(view);
                            } else {
                                aiMove();
                            }
                        }
                    }
                    counter.animate().x(ix + dx).y(iy + dy).setDuration(0).start();
                    break;
            }
            checkGameCondition(view, board);
            return true;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        one = findViewById(R.id.one);
        one.setOnTouchListener(handleTouch);
        two = findViewById(R.id.two);
        two.setOnTouchListener(handleTouch);
        three = findViewById(R.id.three);
        three.setOnTouchListener(handleTouch);
        four = findViewById(R.id.four);
        four.setOnTouchListener(handleTouch);
        five = findViewById(R.id.five);
        five.setOnTouchListener(handleTouch);
        six = findViewById(R.id.six);
        six.setOnTouchListener(handleTouch);
        seven = findViewById(R.id.seven);
        seven.setOnTouchListener(handleTouch);
        eight = findViewById(R.id.eight);
        eight.setOnTouchListener(handleTouch);
        nine = findViewById(R.id.nine);
        nine.setOnTouchListener(handleTouch);

        Intent intent = getIntent();
        switch (intent.getStringExtra("name")) {
            case "Easy":
                DEPTH = 3;
                break;
            case "Medium":
                DEPTH = 6;
                break;
            default:
                DEPTH = 9;
                break;
        }
    }
}