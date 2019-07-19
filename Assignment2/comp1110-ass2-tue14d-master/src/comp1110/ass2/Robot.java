package comp1110.ass2;

import comp1110.ass2.gui.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static comp1110.ass2.Rule.indexToChar;
import static comp1110.ass2.gui.Card.placementCopy;
import static comp1110.ass2.gui.Viewer.*;

// the AI part of the game.
public class Robot {
    /**
     * @param pre A string describing the previous move.
     * @param placement A string describing placement
     * @return a list of all possible move based on previous move and current board state.
     * // author: Wenbo Du
     */
    public static List<String> allPossibleMove(String pre, String placement) {
        char origin = WarringStatesGame.ZhangYiLocation(placement);//zhang yi's location
        int oriIndex = WarringStatesGame.cardCharToIndex(origin);
        // the location zhang yi is moving to.
        List<String> allMove = new ArrayList<>();
        for (int i = oriIndex - 6; i > 0; i = i - 6) {
            if (WarringStatesGame.isMoveLegal(placement, indexToChar(i / 6, i % 6)))
                allMove.add(pre + indexToChar(i / 6, i % 6));
        }//search in the same column, down side
        for (int i = oriIndex + 6; i < 35; i = i + 6) {
            if (WarringStatesGame.isMoveLegal(placement, indexToChar(i / 6, i % 6)))
                allMove.add(pre + indexToChar(i / 6, i % 6));
        }//search in the same column, up side
        for (int i = oriIndex + 1; i < (oriIndex / 6 + 1) * 6; i = i + 1) {
            if (WarringStatesGame.isMoveLegal(placement, indexToChar(i / 6, i % 6)))
                allMove.add(pre + indexToChar(i / 6, i % 6));
        }//search in the same column, right side
        for (int i = oriIndex - 1; i >= (oriIndex / 6) * 6; i = i - 1) {
            if (WarringStatesGame.isMoveLegal(placement, indexToChar(i / 6, i % 6)))
                allMove.add(pre + indexToChar(i / 6, i % 6));
        }//search in the same column, left side
        return allMove;
    }

    /**
     * @param placement A string describing current placement
     * @param pre A List of String, each of them represent a possible move sequence.
     * @return a list of possible move sequence based on previous move sequence and current board state.
     *  author: Wenbo Du
     *  eg. looking forward 1 step have {0,A,B} possible move. looking forward 2 step
     *  has:  {01,AC,AD,AE,BF,B5} possible move sequence
     *  looking forward 2 step is all possible move base on
     *  each element in the list above(the length of each element increase 1).
     *  (notice here I flat a tree to a list,
     *  a branch down from the root to leaves form a element in the outcome list.
     *
     */
    public static List<String> nextLayer(String placement, List<String> pre) {
        List<String> out = new ArrayList<>();
        for (String s : pre) {
            String tmp = placement;
            for (int i = 0; i < s.length(); i++)
                // the get the move for each move steps for future,
                // we have to update placement.
                tmp = WarringStatesGame.updateBoard(tmp, s.charAt(i));
            out.addAll(allPossibleMove(s, tmp));
        }
        return out;
    }

    /**
 * @param setup A string describing current placement
 * @param moveSequence a string describe the move sequence
 * @param  numPlayers the numbers of players.
 * @param no the player number.
 * @return The gap between the number of flags this player hold
     * and the number of flags all other three player hold.
 *  author: Wenbo Du
 *  build a minimax model for the robot. Because the game is a multi-player zero-sum game,
 *  We need to modify the minimax model: assume one player as a side, all other players
 *  in another side. So the payoff is the number of total flags in each side.
 *  the maximum the possible of win the game, the player doing operating now always want the
 *  maximum own flag(x) and minimum the flag of other side(y). So the greater x-y value means
 *  better outcome.
    */
    public static int flagGap(String setup, String moveSequence, int numPlayers, int no) {
        // the flag array
        int[] getFlag = WarringStatesGame.getFlags(setup, moveSequence, numPlayers);
        int count = 0;// the numbers of flags for robot.
        int countAginst = 0;//the numbers of flags for others players.
        for (int i : getFlag) {
            if (i == no)
                count++;
            if (i != no && i != -1)
                countAginst--;
        }
        return count + countAginst;
    }


/**
 * @param setup A string describing current placement
 * @param moveSquences a string describe the move sequence
 * @param  numPlayer  numbers of players.
 * @param no the player number.
 * @return the best move based minimax algorithm.
 *  author: Wenbo Du
 * An AI player know the current state of the board and can predict its own flag and
 * other players' flags after several step. So it can generate the move that lead to
 * the best outcome it can predict.
 * */
    public static char evaluteBest(String setup, List<String> moveSquences, int numPlayer, int no) {
        List<Integer> out = new ArrayList<>();
        for (String st : moveSquences) {
            out.add(flagGap(setup, Card.moveSequence+st, numPlayer, no));
        }
        // the max score.
        try {// Can not deal with the case when out is empty(only one valid move).
            int max = Collections.max(out);
            // find the index the max score.
            int index = 0;
            for (int i : out) {
                if (i == max) {
                    break;// find the max score and its index.
                }
                index++;
            }
            //find the move satisfied the required score.
            return moveSquences.get(index).charAt(0);
        }catch (Exception r){
            //when the game is finish in one more move.There is only one possible move.
            return genMove(1);
        }
    }

/**
 * @param difficulty A string describing current placement
 * @return a move based on current difficulty
 *  author: Wenbo Du
 *  If the difficulty is 1 , just generate a random move.
 *  if the difficulty is 2, generate a move based on the score after 2 steps.
 *  if the difficulty is 3, generate a move based on the score after n steps.
 *  n is depends on the quantity of possible move.
 *  when there are lots of cards on the board,
 *  the possible moves for each future steps is a lot,
 *  so the execution is slow and the code stop to execute
 *  next n steps. When there is a few cards on the board
 *  vice versa, the possible move for each futyre step is
 *  not a lot, so it sometimes can predict to more than 8 steps.
 *  when the AI predict a point the game will finish,
 *  there is not need for further prediction.
 *  */
    public static char genMove(int difficulty) {
        char randomMove;
        List<String> temp=new ArrayList<>();
        if (difficulty == 1)//just random move.
            randomMove = WarringStatesGame.generateMove(placementCopy);
        else {
            List<String> next= Robot.allPossibleMove("", placementCopy);
            if (difficulty == 2)// looking for 1 step.
                randomMove = Robot.evaluteBest(placement, next, numPlayer, round);
            else {
                long startTime = System.currentTimeMillis();
                do {// looking forward as much as possible fix time
                    next = Robot.nextLayer(placementCopy, next);
                    if (next.isEmpty())
                        break;
                    temp=next;
                } while (System.currentTimeMillis() - startTime < 20);
                randomMove = Robot.evaluteBest(placement, temp, numPlayer, round);
                // the evaluate of game cost lots of time, if there are
                // too many possible move sequence, the program stuck,
                // so we need to cut the loop in a certain time.
            }
        }
        return randomMove;
    }
}