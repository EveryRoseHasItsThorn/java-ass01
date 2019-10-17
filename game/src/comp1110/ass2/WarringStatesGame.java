package comp1110.ass2;

import java.util.*;

import static comp1110.ass2.Rule.generatePossibleMove;
import static comp1110.ass2.Rule.makeOrder;


/**
 * This class provides the text interface for the Warring States game
 */
public class WarringStatesGame {
    /**
     * FIXME Task 2: determine whether a card placement is well-formed
     * @param cardPlacement A string describing a card placement
     * @return true if the card placement is well-formed
     */
    // author: Wenbo Du,Weiwei Liu
    static boolean isCardPlacementWellFormed(String cardPlacement) {
        int maximumCard = maximumCard(cardPlacement.charAt(0));
        //maximum number of cards.
        if (cardPlacement.charAt(0) == 'z' && cardPlacement.charAt(1) != '9') return false;
            // deal with Zhang Yi, a special case.
        else if (cardPlacement.length() != 3) return false;
            //the string is not valid because of the length does not fit the requirement.
        else if (!(('a' <= cardPlacement.charAt(0) && cardPlacement.charAt(0) <= 'g') || cardPlacement.charAt(0) == 'z'))
            return false;
            //the kingdom character is invalid.
        else if (cardPlacement.charAt(1) > maximumCard || cardPlacement.charAt(1) < '0') return false;
            // the card number of a kingdom is out of bound.
        else if ((cardPlacement.charAt(2) <= 'Z') && (cardPlacement.charAt(2) >= 'A')) return true;
            //the placement is invalid.
        else if ((cardPlacement.charAt(2) <= '9') && (cardPlacement.charAt(2) >= '0')) return true;
        //all conditions are satisfied, return true.
        return false;
        //the invalid input that is not filtered by control flow above, return false.
    }

    /**
     * Helper function for task2,represent the numbers of cards each kingdom can have.
     * @param character a-g: each character represent one kingdom
     * @return the numbers of cards each kingdom can have.
     */
    // author: Wenbo Du
    static char maximumCard(char character) {
        //for each kingdom, return the maximum numbers of cards the kingdom have as character.
        if (character == 'a')
            return '7';
        if (character == 'b')
            return '6';
        if (character == 'c')
            return '5';
        if (character == 'd')
            return '4';
        if (character == 'e')
            return '3';
        if (character == 'f')
            return '2';
        if (character == 'g')
            return '1';
        if (character == 'z')
            //for Zhang Yi, according to requirement, the card number can only be 9
            return '9';
        else//if the input character is invalid, return a trivial character.
            return '/';
    }

    /**
     * FIXME Task 3: determine whether a placement is well-formed
     * @param placement A string describing a placement of one or more cards
     * @return true if the placement is well-formed
     */
    //author: Wenbo Du,Weiwei Liu
    public static boolean isPlacementWellFormed(String placement) {
        if (placement == "" || placement == null) return false;
        //when input is empty or null type,return false
        Set<String> cards = new HashSet<>();
        //a collection of a distinct card kingdom and number
        Set<String> locations = new HashSet<>();//a collection of a distinct placement
        String card;//each card include kingdom and number
        String location;//each placement
        if (placement.length() > 3 * 36 || placement.length() % 3 != 0) return false;
        //Numbers of placement is beyond the maximum of placements is incomplete.
        for (int i = 0; i < placement.length(); i += 3) {
            card = placement.substring(i, i + 2);
            location = placement.substring(i + 2, i + 3);
            cards.add(card);
            if (locations.contains(location)) return false;
            locations.add(location);
            //check if duplicate placement.
            if (!isCardPlacementWellFormed(placement.substring(i, i + 3))) return false;
            //check if each card placement is well-formed.
        }
        if (cards.size() != placement.length() / 3) return false;
        //check if have duplicate cards.
        return true;
    }

    /**
     * FIXME Task 5: determine whether a given move is legal
     * @param placement  the current placement string
     * @param locationChar a location for Zhang Yi to move to
     * @return true if Zhang Yi can move to that location
     */
    // author: Wenbo Du
    public static boolean isMoveLegal(String placement, char locationChar) {
        if (!(('0' <= locationChar && locationChar <= '9') || ('A' <= locationChar && locationChar <= 'Z')))
            //check whether the intended location is valid.
            return false;
        int origin = cardCharToIndex(ZhangYiLocation(placement));
        //origin location of Zhang Yi.
        int destination = cardCharToIndex(locationChar);
        //the intended location to move in.
        int barrier;
        //the cards along the line from the same kingdom as the chosen card.
        int barrierRow;
        //the row of barrier
        int barrierCol;
        //the column of barrier
        int originRow = origin / 6;
        int originCol = origin % 6;
        int destinationRow = destination / 6;
        int destinationCol = destination % 6;
        if (originCol != destinationCol && originRow != destinationRow) return false;
        //destination and original location is not is the same row or column.
        for (int i = 0; i < placement.length(); i += 3)
            if (placement.charAt(i + 2) == locationChar)
                //reach the destination.
                break;
            else if (i == placement.length() - 3) return false;
        //the destination is can not be reached.
        for (int j = 0; j < placement.length(); j += 3)
            if (placement.charAt(j) == objectiveKindom(placement, locationChar)) {
                //when find the card from the same kingdom, check their location.
                //deal with the case when are are barrier. In 4 orientation:Up, down, left ,right
                barrier = cardCharToIndex(placement.charAt(j + 2));
                barrierCol = barrier % 6;
                barrierRow = barrier / 6;
                if (destinationRow < originRow && barrierRow < destinationRow && destinationCol == barrierCol && barrierCol == originCol)
                    return false;//up
                if (destinationRow > originRow && barrierRow > destinationRow && destinationCol == barrierCol && barrierCol == originCol)
                    return false;//down
                if (destinationCol < originCol && barrierCol < destinationCol && destinationRow == barrierRow && barrierRow == originRow)
                    return false;//left
                if (destinationCol > originCol && barrierCol > destinationCol && destinationRow == barrierRow && barrierRow == originRow)
                    return false;//right
            }
        return true;
    }

    /**
     * Helper function for Task5, return the location of ZhangYi.
     * @param placement Current placement of cards
     * @return zhang Yi's location by character
     */
    // author: Wenbo Du
    public static char ZhangYiLocation(String placement) {
        char out = '/';//the location where Zhang Yi is.
        for (int i = 0; i < placement.length(); i += 3)
            if (placement.charAt(i) == 'z')
                out = placement.charAt(i + 2);
        return out;
    }

    /**
     *Helper function for Task5, convert location represent by 0-9 or A-Z to index 0-35.
     * @param location
     * @return the index of a location(0...35)
     */
    //author: Wenbo Du
    public static int cardCharToIndex(char location) {
        int out;//output index
        int column;//the column of card
        int row;//the row of card
        //deal with the case when the location is between 0-9
        if ('4' <= location && location <= '9')
            out = (location - '4') * 6;
        else if ('0' <= location && location <= '3')
            out = 13 + (location - '0') * 6;
        else {//deal with the case when location is between A-Z
            column = 5 - ((location - 'A') / 6);
            row = (location - 'A') % 6;
            out = row * 6 + column;
        }
        return out;
    }

    /**
     * Helper function for Task5, return which kingdom's  card zhangYi intended to move.
     * @param placement Current placement of cards
     * @param locationChar
     * @return
     */
    //author: Wenbo Du
    public static char objectiveKindom(String placement, char locationChar) {
        //locationChar, the location Zhang Yi wants to move.
        char out = '/';// the kingdom the card, which Zhang Yi wants to take.
        for (int i = 0; i < placement.length(); i += 3)
            if (placement.charAt(i + 2) == locationChar)
                //check the placement of card, whether is the location Zhang Yi want to move in.
                out = placement.charAt(i);
        return out;
    }

    /**
     * FIXME Task 6: determine whether a placement sequence is valid
     * @param setup A placement string representing the board setup
     * @param moveSequence a string of location characters representing moves
     * @return every move is valid, the move sequence is valid.
     */
    // author: Wenbo Du
    static boolean isMoveSequenceValid(String setup, String moveSequence) {
        if (ifDuplicate(moveSequence))//check if duplicate movement.
            return false;
        for (char character : moveSequence.toCharArray()) {
            if (!isMoveLegal(setup, character)) return false;
            // check single move is legal or not.
            setup = updateBoard(setup, character);
            //update the placement sequence and remaining movement.
        }
        return true;
    }

    /**
     * Helper for task 6.UpdateBoard after the movement of zhangYi each time.
     * @param placement Current placement of cards
     * @param move a character represent a move
     * @return the placement string after move.
     * author: Wenbo Du
     */
    public static String updateBoard(String placement, char move) {
        //update the board after each movement.
        String out = "";//output placement sequence.
        String sameKingdom = "";//the card from the same kingdom.
        char zhangYiLocation = ZhangYiLocation(placement);//the location of Zhang Yi.
        char objCard = objectiveKindom(placement, move);//the destination location.
        for (int i = 0; i < placement.length(); i += 3)
            if (placement.charAt(i) == 'z')//delete origin location of Zhang Yi.
                placement = placement.substring(0, i) + placement.substring(i + 3);
        for (int i = 0; i < placement.length(); i += 3)
            if (placement.charAt(i) != objCard)
                out = out + placement.substring(i, i + 3);//add the card that is not moved.
            else if (placement.charAt(i + 2) == move)
                out = out + "z9" + move;//replace the destination card with zhang Yi card.
            else if (!whetherBarrier(zhangYiLocation, placement.charAt(i + 2), move))
                sameKingdom = sameKingdom + placement.substring(i, i + 3);//replace card barrier the movement.
        return out + sameKingdom;
    }

    /**
     * Check whether the move carry more than one cards.
     * @param start   the character represent origin location of a card.
     * @param barrier a card location
     * @param end  the move destination.
     * @return turn if the move is invalid: there are further card
     * from the same kingdom in the same orientation.
     * Vice Versa.
     * author: Wenbo Du
     */
    public static boolean whetherBarrier(char start, char barrier, char end) {
        int indexStart = cardCharToIndex(start);//the index of start location
        int startCol = indexStart % 6;// row and column for start location
        int startRow = indexStart / 6;
        int indexEnd = cardCharToIndex(end);// the index of end location
        int endCol = indexEnd % 6;// row and column for end location
        int endRow = indexEnd / 6;
        int indexBarrier = cardCharToIndex(barrier);// row and column for barrier location
        int barrierCol = indexBarrier % 6;// row and column for barrier location
        int barrierRow = indexBarrier / 6;
        if (startRow == endRow && startRow == barrierRow)
            if (startCol < barrierCol && barrierCol < endCol)
                return true;// the card can not be moved
            else if (startCol > barrierCol && barrierCol > endCol)
                return true;
            else
                return false;
        else if (startCol == endCol && startCol == barrierCol)
            if (startRow < barrierRow && barrierRow < endRow)
                return true;
            else if (startRow > barrierRow && barrierRow > endRow)
                return true;
            else
                return false;
        else
            return false;
    }

    /**
     * Helper for task 6. Check whether have duplicate move.
     * There are no other cards along the line from the same kingdom as the chosen card that are further away from Zhang Yi.
     * @param move a move sequence.
     * @return true if there is not duplicate move.
     * author: Wenbo Du
     */
    public static boolean ifDuplicate(String move) {
        char[] allMove = move.toCharArray();//check if contain duplicate movement.
        Set<Character> distinctMove = new HashSet<>();// a set of distinct move.
        for (char a : allMove)
            distinctMove.add(a);
        return distinctMove.size() != allMove.length;
    }

    /**
     * FIXME Task 7: get the list of supporters for a given player after a sequence of moves
     * @param setup A placement string representing the board setup
     * @param moveSequence a string of location characters representing moves
     * @param numPlayers the number of players in the game, must be in the range [2..4]
     * @param playerId the player number for which to get the list of supporters, [0..(numPlayers-1)]
     * @return the list of supporters for the given player
     * // author: Wenbo Du
     */
    public static String getSupporters(String setup, String moveSequence, int numPlayers, int playerId) {
        if (playerId > numPlayers - 1)
            return "";// invalid player.
        List<String> allSupporter = getAllSupporters(setup, moveSequence);
        // all suporter for all player.
        List<String> unordered = new ArrayList<>();
        // unordered supporter string.
        for (int i = 0; i < allSupporter.size(); i++)
            if (i % numPlayers == playerId)
                //select the supporter for the player.
                unordered.add(allSupporter.get(i));
        String out = "";
        // from list to a string.
        for (String l : unordered)
            out += l;
        List<String> seperate = new ArrayList<>();
        for (int i = 0; i < out.length(); i += 2)
            seperate.add(out.substring(i, i + 2));
        out = makeOrder(seperate);// make order for supporter string.
        return out;
    }

    /**
     * Helper for Task 7. all supporter for each player.
     * @param setup A placement string representing the board setup
     * @param moveSequence a string of location characters representing move sequence.
     * @return all supporter for all players.
     * author: Wenbo Du
     */
    public static List<String> getAllSupporters(String setup, String moveSequence) {
        List<String> out = new ArrayList<>();
        for (char l : moveSequence.toCharArray()) {
            String updated = updateBoard(setup, l);
            out.add(getCard(setup, updated));
            setup = updated;
        }
        return out;
    }

    /**
     * Helper for Task 7. Updated board and filter the card that already placed.
     * @param placement Current placement of cards
     * @param played Used cards
     * @return Get used cards
     */
    // author: Wenbo Du
    public static String getCard(String placement, String played) {
        List<String> origin = new ArrayList<>();
        List<String> after = new ArrayList<>();
        // all card on board.
        for (int i = 0; i < placement.length(); i += 3)
            origin.add(placement.substring(i, i + 2));
        // all card already played.
        for (int i = 0; i < played.length(); i += 3)
            after.add(played.substring(i, i + 2));
        origin.removeAll(after);
        String out = "";
        for (String r : origin) {
            out += r;
        }
        return out;
    }

    /**
     * FIXME Task 8: determine which player controls the flag of each kingdom after a given sequence of moves
     * @param setup A placement string representing the board setup
     * @param moveSequence a string of location characters representing a sequence of moves
     * @param numPlayers the number of players in the game, must be in the range [2..4]
     * @return an array containing the player ID who controls each kingdom, where
     * - element 0 contains the player ID of the player who controls the flag of Qin
     * - element 1 contains the player ID of the player who controls the flag of Qi
     * - element 2 contains the player ID of the player who controls the flag of Chu
     * - element 3 contains the player ID of the player who controls the flag of Zhao
     * - element 4 contains the player ID of the player who controls the flag of Han
     * - element 5 contains the player ID of the player who controls the flag of Wei
     * - element 6 contains the player ID of the player who controls the flag of Yan
     * If no player controls a particular house, the element for that house will have the value -1.
     */
    //author: Wenbo Du
    public static int[] getFlags(String setup, String moveSequence, int numPlayers) {
        Integer[] out = new Integer[]{-1, -1, -1, -1, -1, -1, -1};// init array.
        // get the supporter for each player.
        String playerOneSupporter = getSupporters(setup, moveSequence, numPlayers, 0);
        String playerTwoSupporter = getSupporters(setup, moveSequence, numPlayers, 1);
        String playerThreeSupporter = getSupporters(setup, moveSequence, numPlayers, 2);
        String playerFourSupporter = getSupporters(setup, moveSequence, numPlayers, 3);
        // the card and the quantity of card for each player each card.
        Map<Character, Integer> one = toMap(playerOneSupporter);
        Map<Character, Integer> two = toMap(playerTwoSupporter);
        Map<Character, Integer> three = toMap(playerThreeSupporter);
        Map<Character, Integer> four = toMap(playerFourSupporter);
        // find the player hold the most card of a kingdom.
        List<Integer> a = gotIndex('a', one, two, three, four);
        List<Integer> b = gotIndex('b', one, two, three, four);
        List<Integer> c = gotIndex('c', one, two, three, four);
        List<Integer> d = gotIndex('d', one, two, three, four);
        List<Integer> e = gotIndex('e', one, two, three, four);
        List<Integer> f = gotIndex('f', one, two, three, four);
        List<Integer> g = gotIndex('g', one, two, three, four);
        // replace in the flag array.
        if (a.size() == 1)
            out[0] = a.get(0);
        if (a.size() > 1)
            out[0] = lastCardPlayer('a', setup, moveSequence, numPlayers, a);
        if (b.size() == 1)
            out[1] = b.get(0);
        if (b.size() > 1)
            out[1] = lastCardPlayer('b', setup, moveSequence, numPlayers, b);
        if (c.size() == 1)
            out[2] = c.get(0);
        if (c.size() > 1)
            out[2] = lastCardPlayer('c', setup, moveSequence, numPlayers, c);
        if (d.size() == 1)
            out[3] = d.get(0);
        if (d.size() > 1) {
            out[3] = lastCardPlayer('d', setup, moveSequence, numPlayers, d);
        }
        if (e.size() == 1)
            out[4] = e.get(0);
        if (e.size() > 1)
            out[4] = lastCardPlayer('e', setup, moveSequence, numPlayers, e);
        if (f.size() == 1)
            out[5] = f.get(0);
        if (f.size() > 1)
            out[5] = lastCardPlayer('f', setup, moveSequence, numPlayers, f);
        if (g.size() == 1)
            out[6] = g.get(0);
        if (g.size() > 1)
            out[6] = lastCardPlayer('g', setup, moveSequence, numPlayers, g);
        int[] realOut = new int[7];
        for (int l = 0; l < 7; l++) {
            realOut[l] = out[l];
        }
        return realOut;
    }

    /**
     * Helper for task 8: get the card: quantity pair.
     * @param player player 1-4
     * @return get the card: quantity pair.
     */
    //author: Wenbo Du
    public static Map<Character, Integer> toMap(String player) {
        Map<Character, Integer> out = new HashMap<>();
        // eg {a:5, b:3} mean a player have 3 cards from qin, 3 card from chu.
        for (int i = 0; i < player.length(); i += 2) {
            if (out.containsKey(player.charAt(i)))
                out.put(player.charAt(i), out.get(player.charAt(i)) + 1);
            else
                out.put(player.charAt(i), 1);
        }
        return out;
    }

    /**
     * Helper for task 8: get the player have the most cards of a particular kingdom.
     * @param obj a kingdom represented by character
     * @param one player 1
     * @param two player 2
     * @param three player 3
     * @param four player 4
     * @return get a list of players who
     * hold the most supporter for this particular kingdom.
     * author: Wenbo Du
     */
    public static List<Integer> gotIndex(char obj, Map one, Map two, Map three, Map four) {
        List<Integer> values = new ArrayList<>();
        if (one.get(obj) != null)// player 1's supporter
            values.add(((Integer) one.get(obj)));
        else
            values.add(0);
        if (two.get(obj) != null)  // player 2's supporter
            values.add(((Integer) two.get(obj)));
        else
            values.add(0);
        if (three.get(obj) != null) // player 3's supporter
            values.add(((Integer) three.get(obj)));
        else
            values.add(0);
        if (four.get(obj) != null) // player 4's supporter
            values.add(((Integer) four.get(obj)));
        else
            values.add(0);
        Integer max = Collections.max(values);
        //the maximum quantity of supporters hold by one player.
        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (values.get(i) == max && max != 0)
                index.add(i);
        }
        return index;
    }

    /**
     * Helper for task 8: Deal with the case when player
     * hold the same numbers of card determine who hold the flag.
     * @param card
     * @param setup      \param setup A placement string representing the board setup
     * @param moveSequence a string of location characters representing a sequence of moves
     * @param numsPlayer the number of players in the game, must be in the range [2..4]
     * @param validPlayer a list of players who hold the same numbers of supporters
     *  for the same kingdom.
     * author: Wenbo Du
     * @return the players who get the flag.
     */
    public static int lastCardPlayer(char card, String setup, String moveSequence, int numsPlayer, List<Integer> validPlayer) {
        String validMoveSquence = "";
        int playerId = 100;//set a trival numeber for player than will not used.
        for (int i = 0; i < moveSequence.length(); i++) {
            char objKingdom = objectiveKindom(setup, moveSequence.charAt(i));
            // update move sequence.
            setup = updateBoard(setup, moveSequence.charAt(i));
            if (objKingdom == card) {
                validMoveSquence = moveSequence.substring(0, i);
                if (validPlayer.contains(validMoveSquence.length() % numsPlayer))
                    // find the player hold the last the card from latest move.
                    playerId = validMoveSquence.length() % numsPlayer;
            }
        }
        return playerId;
    }

    /**
     * FIXME Task 10: generate a legal move
     * @param placement the current placement string
     * @return a location character representing random move.
     */
    // author: Wenbo Du
    public static char generateMove(String placement) {
        char out;
        int timeOut = 0;
        do {//filter a valid move from possible move(not always valid).
            if (isMoveLegal(placement, generatePossibleMove(placement))) {
                out = generatePossibleMove(placement);// generate a move
            } else {
                out = '\0';
            }
            timeOut++;
        } while (!isMoveLegal(placement, out) && timeOut < 1000);
        // set a timeout a move generator.
        return out;
    }
}