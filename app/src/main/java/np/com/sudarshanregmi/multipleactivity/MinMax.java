package np.com.sudarshanregmi.multipleactivity;

import java.util.ArrayList;

import static np.com.sudarshanregmi.multipleactivity.SecondActivity.DEPTH;
import static np.com.sudarshanregmi.multipleactivity.SecondActivity.best_move;
import static np.com.sudarshanregmi.multipleactivity.SecondActivity.canMove;
import static np.com.sudarshanregmi.multipleactivity.SecondActivity.isGameOver;
import static np.com.sudarshanregmi.multipleactivity.SecondActivity.validMoves;

public final class MinMax {
    public int value;
    public ArrayList<Integer> moves;

    public MinMax(int value, ArrayList<Integer> moves){
        this.value = value;
        this.moves = moves;
    }

    public static ArrayList<ArrayList<Integer>> getPossibleDrags(int[] board, int player) {
        ArrayList<ArrayList<Integer>> possibleDrags = new ArrayList<>(8);
        for (int i = 0; i < board.length; i++) {
            if (board[i] == player) {
                for (int validMove : validMoves[i]) {
                    if (board[validMove] == 2) {
                        ArrayList<Integer> temp = new ArrayList<>();
                        temp.add(i);
                        temp.add(validMove);
                        possibleDrags.add(temp);
                    }
                }
            }
        }
        return possibleDrags;
    }


    public static MinMax Minimax(int[] board, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || (Boolean) isGameOver(board).get(0)) {
            if ((Boolean) isGameOver(board).get(0)) {
                if ((Integer) isGameOver(board).get(1) == 1) {
                    return new MinMax(1000 + depth, null);
                } else {
                    return new MinMax(-1000 - depth, null);
                }
            } else {
                return new MinMax(0, null);
            }
        }
        if (maximizingPlayer) {
            int value = -1000000;

            if (!canMove(board)) {
                ArrayList<Integer> possibleMoves = new ArrayList<>();
                for (int i = 0; i < board.length; i++) {
                    if (board[i] == 2) {
                        possibleMoves.add(i);
                    }
                }
                for (Integer possibleMove : possibleMoves) {
                    board[possibleMove] = 1;
                    int value_t = Minimax(board, depth - 1, alpha, beta, false).value;
                    if (value_t > value) {
                        value = value_t;
                        if (alpha < value) {
                            alpha = value;
                        }
                        if (depth == DEPTH) {
                            ArrayList<Integer> temp = new ArrayList<>();
                            temp.add(possibleMove);
                            best_move = temp;
                        }
                    }
                    board[possibleMove] = 2;
                    if (alpha >= beta) {
                        break;
                    }
                }
                return new MinMax(value, best_move);
            } else {
                ArrayList<ArrayList<Integer>> possibleDrags = getPossibleDrags(board, 1);
                for (ArrayList<Integer> possibleDrag : possibleDrags) {
                    board[possibleDrag.get(0)] = 2;
                    board[possibleDrag.get(1)] = 1;

                    int value_t = Minimax(board, depth - 1, alpha, beta, false).value;
                    if (value_t > value) {
                        value = value_t;
                        if (alpha < value) {
                            alpha = value;
                        }
                        if (depth == DEPTH) {
                            best_move = possibleDrag;
                        }
                    }
                    board[possibleDrag.get(0)] = 1;
                    board[possibleDrag.get(1)] = 2;
                    if (alpha >= beta) {
                        break;
                    }
                }
                return new MinMax(value, best_move);
            }
        } else {
            int value = 1000000;
            if (!canMove(board)) {
                ArrayList<Integer> possibleMoves = new ArrayList<>();
                for (int i = 0; i < board.length; i++) {
                    if (board[i] == 2) {
                        possibleMoves.add(i);
                    }
                }
                for (Integer possibleMove : possibleMoves) {
                    board[possibleMove] = 0;
                    int value_t = Minimax(board, depth - 1, alpha, beta, true).value;
                    if (value_t < value) {
                        value = value_t;
                        if (beta > value) {
                            beta = value;
                        }
                        if (depth == DEPTH) {
                            best_move.add(possibleMove);
                        }
                    }
                    board[possibleMove] = 2;
                    if (alpha >= beta) {
                        break;
                    }
                }
                return new MinMax(value, best_move);
            } else {
                ArrayList<ArrayList<Integer>> possibleDrags = getPossibleDrags(board, 0);
                for (ArrayList<Integer> possibleDrag : possibleDrags) {
                    board[possibleDrag.get(0)] = 2;
                    board[possibleDrag.get(1)] = 0;

                    int value_t = Minimax(board, depth - 1, alpha, beta, true).value;
                    if (value_t < value) {
                        value = value_t;
                        if (beta > value) {
                            beta = value;
                        }
                        if (depth == DEPTH) {
                            best_move = possibleDrag;
                        }
                    }
                    board[possibleDrag.get(0)] = 0;
                    board[possibleDrag.get(1)] = 2;
                    if (alpha >= beta) {
                        break;
                    }
                }
                return new MinMax(value, best_move);
            }
        }
    }

}
