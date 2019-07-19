package comp1110.ass1;
import java.util.*;

/**
 * This class represents a game of 'IQPuzzlerPro', which is based directly on the puzzle
 * from 'SmartGames' called "IQPuzzlerPro"
 * <p>
 * http://www.smartgames.eu/en/smartgames/iq-puzzler-pro
 * <p>
 * The class and the those that it refer to provide the core logic of
 * the puzzle, which is used by the GUI, which runs the game in a window.
 * <p>
 * The board is composed of spaces arranged in a 11x5 grid.
 * Each space is assigned an index using row-major order, starting at the
 * top left of the board.
 * Thus the top-left space has an index of 0;
 * the top-right space has an index of 10;
 * the bottom-left space has an index of 44; and
 * the bottom-right space has an index of 54.
 * <p>
 * There are twelve puzzle pieces which are composed of various numbers of linked
 * balls, arranged in a planar manner so that they may be laid flat on the grid
 * Each piece's position is described in terms of its origin, which is the
 * top-left-most ball when in its unrotated state (as illustrated above).
 * <p>
 * The puzzle uses a 'placement string' to represent the state of the board.
 * A placement string is composed of zero or more 'piece placement strings',
 * indicating the location of each piece that has already been placed on the board.
 * <p>
 * A piece placement string consists of four characters:
 * - The first character identifies which of the 12 pieces is being placed ('A' to 'L').
 * - The second character identifies the column in which the origin of the tile is placed ('A' to 'K').
 * - The third character identifies the row in which the origin of the tile is placed ('A' to 'E').
 * - The fourth character identifies which orientation the tile is in ('A' to 'H').
 * <p>
 * The default (unrotated) orientation is 'A'.
 * Orientation 'B' means the piece is rotated 90 degrees clockwise;
 * 'C' means the piece is rotated 180 degrees; and
 * 'D' means the piece is rotated 270 degrees clockwise.
 * Rotations 'E' through 'H' mean the piece is flipped horizontally
 * (i.e. reflected about the y-axis) before rotating clockwise.
 * <p>
 * Assume that in its default orientation, a piece is M columns wide and
 * N rows tall.
 * After a 90 degree rotation, it will be N columns wide and M rows tall.
 * To make rotation regular and ensure that rotated pieces correctly align
 * with the quilt board squares, rotation is performed so that the top-left
 * hand corner of the MxN bounding box is always in the same place.
 */
public class IQPuzzlerPro {
    /* constants describing the shape of the board */
    public static final int ROWS = 5;
    public static final int COLS = 11;
    public static final int SPACES = ROWS * COLS; // number of spaces on board

    /* a trivial objective that can be used to drive the game */
    public static final String TRIVIAL_OBJECTIVE = "ADDDBCBDCACDDHBAEICDFGAAGGDGHECFIEAFJJABKBAE";
    public static final String TRIVIAL_SOLUTION = "ADDDBCBDCACDDHBAEICDFGAAGGDGHECFIEAFJJABKBAELAAB";

    /* empty placement string for unplaced pieces */
    public static final String NOT_PLACED = "";

    /* a set of progressively harder objectives that can be used to drive the game */
    public static final String[][] SAMPLE_OBJECTIVES = {
            /* EASY */
            {"ADDDBCBDCACDDGAAGGDGHECFIEAFKBAELAAB", "AGDDDIAAECCDFAAHGADGHBABJEBBKEAELDAD", "AAADBGAADACDEBBCFGCFIDBDJCDCKBAALFAB"},
            /* HARDER */
            {"AEDCBEAADAADGHAEHBAEIDBHJADCLACC", "AADDBCADCFAADDABEBCDGAAHHDDAIFCF", "AFAABEDCCABDDCABIAAHJADCKEADLGBD"},
            /* HARD */
            {"ABAAEDABGIACKFAALAAD", "AHABEDAAGFAFHABAKAAE"},
            /* HARDEST */
            {"CGABDEABECBAKAAE", "AGAAGAAGHEBAKCAA"},
    };


    private String objective;         // the objective of this instance of the game
    private String solution;          // the solution to the current game

    /**
     * Constructor for a game, given a level of difficulty for the new game
     * <p>
     * This should create a new game with a single valid solution and a level of
     * difficulty that corresponds to the argument difficulty.
     *
     * @param difficulty A value between 0.0 (easiest) and 10.0 (hardest) specifying the desired level of difficulty.
     */
    public IQPuzzlerPro(double difficulty) {
        //objective = establishInterestingObjective(difficulty);
        if (objective == null)
            objective = establishSimpleObjective(difficulty);
    }


    /**
     * Constructor for a game, given a particular objective for that game
     * <p>
     * This should create a new game with the given objective.
     *
     * @param objective The objective for the new game
     */
    public IQPuzzlerPro(String objective) {
        this.objective = objective;
    }

    /**
     * @param piece the name of the piece, 'A'-'L'
     * @return true if the placement of the given piece is fixed in current puzzle objective;
     * otherwise return false
     */
        public boolean isPieceFixed(char piece) {
            int length = objective.length();//length represent the length of fixed objective;
            for (int i = 0; i < length; i += 4) {//pick one letter every four letter,represent the piece.
                char eachPiece = objective.charAt(i);
                if (eachPiece == piece) {//check whether the char input is the same as char looped.
                    return true;
                }
            }//the loop finished means the input is not equal the any pieces, so it did not appear in objective.
            return false;
            // FIXME Task 2:  Replace the code below with code that correctly checks the objective

        }



    /**
     * Set the game's objective using the given difficulty level and the sample
     * objectives provided in SAMPLE_OBJECTIVES.
     * <p>
     * The code should index into the samples according to the difficulty, using the
     * first arrays for difficulty values less than 2.5/10, the next for values
     * less than 5.0/10, the next for values less than 7.5/10, and the last for
     * the remaining values.
     * <p>
     * Note that difficulty is a double in the range 0.0 and 10.0.  It may take on any
     * value in the range 0.0 to 10.0.   Your task is to map those values to the
     * SAMPLE_OBJECTIVES provided.
     * <p>
     * The code should choose within the arrays randomly, so for a given difficulty
     * level, any one of the sample values might be used.
     * <p>
     * For example, if the difficulty level was 1/10, then the first array ('EASY')
     * of values should be used.   A random number generator should then choose
     * an index between 0 and 2 and set the objective accordingly, so if the randomly
     * generated value was 1, then it would choose the objective
     * "AGDDDIAAECCDFAAHGADGHBABJEBBKEAELDAD" and so on.
     *
     * @param difficulty A value between 0.0 (easiest) and 10.0 (hardest) specifying the desired level of difficulty.
     */
    public static String establishSimpleObjective(double difficulty) {
        Random r=new Random();
        int randomIndex;//the number represent random selected index.
        String outObj;//output the objective according to difficulty.
        if (0.0<=difficulty&&difficulty<2.5){//level 1
            randomIndex=r.nextInt(SAMPLE_OBJECTIVES[0].length);
            outObj=SAMPLE_OBJECTIVES[0][randomIndex];
        }
        else if (2.5<=difficulty&&difficulty<5){//level 2
            randomIndex=r.nextInt(SAMPLE_OBJECTIVES[1].length);
            outObj=SAMPLE_OBJECTIVES[1][randomIndex];
        }
        else if (5<=difficulty&&difficulty<7.5){//level 3
            randomIndex=r.nextInt(SAMPLE_OBJECTIVES[2].length);
            outObj=SAMPLE_OBJECTIVES[2][randomIndex];
        }
        else {//level 4
            randomIndex=r.nextInt(SAMPLE_OBJECTIVES[3].length);
            outObj=SAMPLE_OBJECTIVES[3][randomIndex];
        }
        return outObj;
        // FIXME Task 4:  Replace the code below with code that draws from SAMPLE_OBJECTIVES
    }


    /**
     * Determine whether a given piece placement string is valid.
     * A pieces placement string is valid if and only if:
     * the first character is a piece ID 'A'-'L';
     * the second character is a column 'A'-K';
     * the third character is a row 'A'-'E'; and
     * the fourth character is an orientation 'A'-'H'
     *
     * @return true if the given piece placement string is valid
     */
    public static boolean isValidPiecePlacement(String piecePlacementString) {
        if (piecePlacementString.length() != 4) return false;
        if (piecePlacementString.charAt(0) < 'A' || piecePlacementString.charAt(0) > 'L') return false;
        if (piecePlacementString.charAt(1) < 'A' || piecePlacementString.charAt(1) > 'K') return false;
        if (piecePlacementString.charAt(2) < 'A' || piecePlacementString.charAt(2) > 'E') return false;
        if (piecePlacementString.charAt(3) < 'A' || piecePlacementString.charAt(3) > 'H') return false;
        return true;
    }

    /**
     * Determine whether a given placement string is valid.
     * A placement string is valid if and only if:
     * it is composed of zero or more four-character piece placement strings,
     * each piece appears only once; and
     * no two pieces overlap.
     *
     * @return true if the given placement string is valid
     */
    public static boolean isValidPlacement(String placementString) {
        HashSet<Piece> pieces = new HashSet<>();
        HashSet<Integer> covered = new HashSet<>();
        for (int offset = 0; offset < placementString.length(); offset += 4) {
            String piecePlacementString = placementString.substring(offset, offset + 4);
            if (!isValidPiecePlacement(piecePlacementString)) {
                return false;
            }
            Piece piece = Piece.valueOf(String.valueOf(piecePlacementString.charAt(0)));
            if (!pieces.add(piece)) {
                return false;
            }
            int[] pieceCover = piece.getCovered(piecePlacementString.charAt(1), piecePlacementString.charAt(2), piecePlacementString.charAt(3));
            for (int space : pieceCover) {
                if (space < 0 || space >= SPACES) {
                    return false;
                }
                if (!covered.add(space)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Set the game's objective using the given difficulty level.
     * <p>
     * This method should generate different objectives according to the following:
     * <p>
     * - It should respect the given difficulty, using some *principled* and *documented*
     * approach determining the difficulty of a particular objective.
     * <p>
     * - It should not use TRIVIAL_OBJECTIVE or objectives from SAMPLE_OBJECTIVES.
     * <p>
     * - It should provide a rich number of objectives (much more than SAMPLE_OBJECTIVES),
     * so that the player is not likely to be given the same objective repeatedly.
     * <p>
     * - It should offer a more graduated notion of difficulty levels, more than just the
     * four levels provided by SAMPLE_OBJECTIVES.   The tests expect to see difficulty
     * resolved to at least eight levels.
     * <p>
     * <p>
     * Note that difficulty is given as a double that is greater or equal to 0.0 and less than 10.0.
     * It may take on any value in the range [0,10).
     * <p>
     * This requires a deeper understanding of the problem, and some way of determining
     * what makes a particular objective difficult or easy.
     *
     * @param difficulty A value between 0.0 (easiest) and 10.0 (hardest) specifying the desired level of difficulty.
     */
    public static String establishInterestingObjective(double difficulty) {
        //the test takes 5-20 second.
         if (difficulty<1.25)
            return selectObj(8);//when the difficulty is less than 1.25, random place 8 pieces on board
        else if(difficulty<2.5)
            return selectObj(7);//the following code place different numbers of pieces on board.
         // Generally, as the level of difficulty increases, the numbers of placed pieces decrease,
         // so the player have to place more pieces on board and it is more challenging.
        else if (difficulty<3.75)
            return selectObj(6);
        else if(difficulty<5)
            return selectObj(5);
        else if(difficulty<6.25)
            return selectObj(4);
        else if(difficulty<7.5)
            return selectObj(3);
        else if(difficulty<8.75)
            return selectObj(2);
        else
            return selectObj(1);
        // FIXME Task 7: Replace this code with a good objective generator that does not draw from a simple set of samples
    }
    static String board="0000000000000000000000000000000000000000000000000000000";
    // a string formed by 55 "0" represent an empty board.
    static List<Character> allPiece=Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L');
    //all playable piece in the game.
    static List<Character> usedPiece=new ArrayList<>();
    // all used pieces.
    private static String randomPlacement(){
        Random r=new Random();
        Character piece;
        while (true) {
            piece = allPiece.get(r.nextInt(allPiece.size()));
            // random select a piece.
            if (!usedPiece.contains(piece))
                //check whether it has been used, if no, use the piece.
                break;
        }
        int placement=r.nextInt(55);//random place the piece.
        Character col=(char) ('A'+(placement%11));//find the column of placement.
        Character row= (char) ('A'+(placement/11));//find the row of placement.
        Character orientation;
        if ('A'<=piece&&piece<='E'){
            orientation=(char) ('A'+r.nextInt(4));
        }
        else {
            orientation=(char) ('A'+r.nextInt(8));
        }//random select the orientation of piece(note for piece 'A'-'E' flip is not allowed.
        String formed=piece.toString()+col.toString()+row.toString()+orientation.toString();
        return formed;//output a string represent the placement, type and orientation of piece.
    }
    private static boolean ifValidPlacement(String placement ){
        Piece piece=Piece.valueOf(((Character)placement.charAt(0)).toString());
        //convert piece represent in character to type piece.
        int[] covered=piece.getCovered(placement.charAt(1),placement.charAt(2),placement.charAt(3));
        //all the index covered by the piece.
        for (int r:covered){// check whether each covered index valid.
            if (r>54||r<0){
                return false;//outbound
            }
            else if (board.charAt(r)=='1'){
                return false;//overlap
            }
            else {
                board=board.substring(0,r)+"1"+board.substring(r+1);
                //the index is valid, so change the "0" to "1" on the index.
            }
        }
        return true;//all index covered is valid,so the placement is valid.
    }
    private static String genObjective(int num){//random generate an objective.
        String out="";//initialise the output objective.
        int count=0;
        while (true){
            String placePiece=randomPlacement();//a random generated piece placement.
            Character placed=placePiece.charAt(0);
            if (ifValidPlacement(placePiece)&&(!usedPiece.contains(placed))){
                out+=placePiece;
                // if placement is valid and the piece is not used, add it to objective
                usedPiece.add(placed);
                //add the piece the used list.
            }
            count++;
            if (count>1000){
                //set a "timeout",eg. when we need to place 3 piece, we have already place
                // two valid ones, but the third one can not fit in any place with any locations,
                //the program will get into an infinite loop, this part avoid that and output an
                //invalid objective(no enough piece)
                break;
            }
            if (out.length()==4*num)//case when we find valid placement for all pieces.
                break;
        }
        return out;
    }
    private static String selectObj(int num){
        String obj;
        while (true){
            obj=genObjective(num);//generate a objective according to the number of pieces.
            // but not necessarily valid.
            usedPiece=new ArrayList<>();
            //set back the list of used pieces to empty for the generation of another objective.
            board="0000000000000000000000000000000000000000000000000000000";
            //set back the origin empty board.
            if (obj.length()==num*4){
                break;//case when we find valid objective.
            }
        }
        return fixOrientationsProperly(obj);//fix the orientation of objective and return it.
    }


    /**
     * @return the objective of the current game.
     */
    public String getObjective() {
        return objective;
    }

    /**
     * Take a non-empty string composed of four-character piece placement strings,
     * and if the piece name is 'A' through 'E' and the orientation is any char 'E'-'H',
     * replace the orientation as follows:
     * 'E' -> 'A'
     * 'F' -> 'B'
     * 'G' -> 'C'
     * 'H' -> 'D'
     * The orientation char for pieces 'F' through 'L' should not be changed.
     * <p>
     * Examples:
     * <p>
     * in:  "ADDHBGAACECF"    out: "ADDDBGAACECB"
     * in:  "BDAGFGEHGABA"    out: "BDACFGEHGABA"
     * <p>
     * Hint: You may want to convert from String to array of char using toCharArray(), and then
     * do your work using the char array before converting back by creating a new String with
     * the char array as the argument to the constructor.
     *
     * @param in A string composed of four-character piece placement strings
     * @return the input string with corrected orientations for pieces 'A' through 'E'
     */
    public static String fixOrientations(String in) {
        int length=in.length();//the length of input string(in)
        String fixObjective="";//fixed objective to output.
        for (int i=0;i<length;i+=4){//loop over every 4 char which represent for
            String piece=in.substring(i,i+4);//a single piece
            fixObjective+=fixSingleOrientation(piece);//fix the orientation and add to fixObjective.
        }
        return fixObjective;
        // FIXME Task 2: implement code that correctly returns a canonical string according to the comment above.
    }
    private static String fixSingleOrientation(String piece) {//fix the orientation of a single piece.
        String fixedPiece;//the orientation-fixed piece.
        if (('A'<=piece.charAt(0)&&piece.charAt(0)<='E')&&('E' <= piece.charAt(3) && piece.charAt(3) <= 'H')) {
            //the case when the piece is selected from 'A'-'E', and its orientation is between 'E' and 'H'.
            fixedPiece = piece.substring(0, 3) + ((Character) (char) (piece.charAt(3) - 4)).toString();
            // change the char represent orientation so that the piece is fixed.
        }
        else {
            fixedPiece=piece;
            //else just return the original piece.
        }
        return fixedPiece;
    }

    // FIXME Task 3: implement code that correctly returns a canonical string according to the comment above.


    /**
     * Take a non-empty string composed of four-character piece placement strings,
     * and if the piece name is 'B' or 'D' and the orientation is any char 'E'-'H',
     * replace the orientation as follows:
     * 'E' -> 'A'
     * 'F' -> 'B'
     * 'G' -> 'C'
     * 'H' -> 'D'
     * If the piece name is 'A', 'C' or 'E' and the orientation is any char 'E'-'H',
     * replace the orientation as follows:
     * 'E' -> 'B'
     * 'F' -> 'C'
     * 'G' -> 'D'
     * 'H' -> 'A'
     * If the piece name is 'H' and the orientation is 'C', 'D', 'G' or 'H',
     * replace the orientation as follows:
     * 'C' -> 'A'
     * 'D' -> 'B'
     * 'G' -> 'E'
     * 'H' -> 'F'
     * The orientation char for pieces 'F', 'G', and 'I' through 'L' should not be changed.
     * <p>
     * Examples:
     * <p>
     * in:  "ADDHBGAACECF"    out: "ADDABGAACECC"
     * in:  "BDAGFGEHGABA"    out: "BDACFGEHGABA"
     * in:  "HADCDABAICCD"    out: "HADADABAICCD"
     * <p>
     * Hint: You may want to convert from String to array of char using toCharArray(), and then
     * do your work using the char array before converting back by creating a new String with
     * the char array as the argument to the constructor.
     *
     * @param in A string composed of four-character piece placement strings
     * @return the input string with corrected orientations for pieces 'A' through 'E'
     */
    public static String fixOrientationsProperly(String in) {
        char[] placementChars = in.toCharArray();
        for (int i = 0; i < in.length(); i += 4) {
            int rotation = placementChars[i + 3] - 'A';
            if ((placementChars[i] == 'A' || placementChars[i] == 'C' || placementChars[i] == 'E')
                    && (rotation > 4)) {
                rotation = rotation + 1 % 4;
            } else if ((placementChars[i] == 'B' || placementChars[i] == 'D')
                    && (rotation > 4)) {
                rotation -= 4;
            } else if ((placementChars[i] == 'H')
                    && (rotation % 4 == 2 || (rotation % 4 == 3))) {
                rotation -= 2;
            }
            placementChars[i + 3] = (char) (rotation + 'A');
        }
        return String.valueOf(placementChars);
    }

    /**
     * Find all solutions to this game, and return them as an array of strings, each string
     * describing a placement of the pieces as a sequence of four-character piece placement
     * strings.
     * <p>
     * Invalid piece orientations should be fixed using the fixOrientationsProperly method.
     * <p>
     * Invalid solutions should not be returned by this method.
     *
     * @return An array of strings representing the set of all solutions to this puzzle.
     * If there are no solutions, the array should be empty (not null).
     */
    public String[] getSolutions() {
        /*getSolution may take 50s to 90s */
        String[] out;
        if (objective.length()==12*4)
            //case when all the pieces are placed so there is no need for place pieces.
        {out=new String[0];
            return out;}
        Set<String> ans=findSolution();
        if (ans.isEmpty())//case there is no solution.
        { out=new String[0];
            return out;}
            int count=0;
        out=new String[ans.size()];// case when there are solutions.
        for (String string:ans){
            out[count]=objective+string;//string represents each solution(only pieces to play).
            count++;//to convert a set of solutions to array.
        }
        return out;
        // FIXME Task 6: replace this code with code that determines all solutions for this puzzle's objective
    }
    private Long objCovered(String objective){
        //present the board after the placement of objective
        //In order to use bitBoard and bitwise operator, convert the board to type Long.
        Piece piece;
        char col;
        char row;
        char orientation;
        for (int i=0;i<objective.length();i+=4){
            piece=Piece.valueOf(objective.substring(i,i+1));
            col=objective.charAt(i+1);
            row=objective.charAt(i+2);
            orientation=objective.charAt(i+3);
            for (int t:piece.getCovered(col,row,orientation))
            {
                fixedBoard=fixedBoard.substring(0,t)+"1"+fixedBoard.substring(t+1);
            }//update the board.
        }
        Long binaryBoard=Long.parseLong(fixedBoard,2);
        // convert binary string to type long for bit shift.
        return binaryBoard;
    }
    private static Long intiPlacement(Character piece,char orientation){
        Piece r=Piece.valueOf(piece.toString());
        // For each piece intended to place, first place it at the top-left of the board.
        int[] initCovered=r.getCovered('A','A',orientation);
        // find the covered index.
        String initBoard="0000000000000000000000000000000000000000000000000000000";
        for (int a:initCovered){
            initBoard=initBoard.substring(0,a)+"1"+initBoard.substring(a+1);
            //update to the board when the piece is placed.
        }
        Long out=Long.parseLong( initBoard,2);
        //convert the board to Long to use bit shift.
        return out;
    }
    private String shift(Character piece,Character orientation){
        //move the piece to right or down side until find a location the piece can fit in.
        Long initBoard=intiPlacement(piece,orientation);
        int count=0;
        Piece p=Piece.valueOf(piece.toString());
        int limit;
        //limit represent the index when the last index of location where piece can place.
        // it is related the orientation of piece.
        if ((orientation-'A')%2==0)
        {
            limit=55-(p.getRowExtent()-1)*11-p.getColumnExtent();}
        else{
            limit=55-(p.getColumnExtent()-1)*11-p.getRowExtent();}
        String flag="";
        //used later to indicate that the function finally did not find a piece where the piece can fit in.
        while ((Long.parseLong(fixedBoard,2)&initBoard)!=0){
            //the piece is overlapped with objective
            initBoard=initBoard>>1;//move the index of piece(add 1).
            if (count==limit)
                //finally the piece is going to be outbounded, set the flag to 1 means the piece can not be place.
            {flag="1";
                break;}
            count++;
        }
        Long b=Long.parseLong(fixedBoard,2);
        if (flag.equals(""))
            fixedBoard=Long.toBinaryString(b+initBoard);
        //flag is not set, so the piece is located, update the fixed board
        // change some 0 to 1 as these points are not playable.
        Character col=(char)(count%11+'A');
        Character row=(char)(count/11+'A');
        out=(piece.toString()+col.toString()+row.toString()+orientation)+flag;
        //return the string represent the placement of piece.
        // note the when the flag is set, the length of out is not 4,so out is invalid.
        return out;
    }
    private List<Character> remainPiece(){
        String placement=getObjective();
        List<Character> all= new ArrayList<>();
        all.add('A');
        all.add('B');
        all.add('C');
        all.add('D');
        all.add('E');
        all.add('F');
        all.add('G');
        all.add('H');
        all.add('I');
        all.add('J');
        all.add('K');
        all.add('L');
        List<Character> used=new ArrayList<>();
        for (int j=0;j<placement.length();j+=4) {
            Character piece = placement.charAt(j);
            used.add(piece);
        }
        List<Character> out=new ArrayList<>();
        for (Character l:all){
            if (!used.contains(l))
            {out.add(l);}
        }
        return out;
    }
    String fixedBoard="0000000000000000000000000000000000000000000000000000000";
    //origin board without any piece on board.
    String out="";
    //all placement of remain pieces.
    private Set<String> findSolution(){
        Set<String> ans=new HashSet<>();//all solution for the objective.
        String out;//ont solution
        List<String> allorientation=allOrientation();
        //all possible combination of the orientation of remaining pieces.
        List<List<Character>> allorder=allOrder();
        //all possible order of placement of piece.
        for (List<Character>list:allorder) {
            //generate solution based on every orientation and order.eg. (A__D)(F__H)(B__C)
            for (String allori:allorientation){
                objCovered(getObjective());
                out="";
                int index=0;
                while (index<allori.length()){
                    out+=(shift(list.get(index),allori.charAt(index)));//generate an solution
                    index++;
                }
                if ((fixedBoard.equals("1111111111111111111111111111111111111111111111111111111"))&&ifValidOri(out))
                {//case when all index are covered, a valid solution formed.
                    ans.add(fixedOrder(fixOrientationsProperly(fixOrientations(out))));
                }
                fixedBoard="0000000000000000000000000000000000000000000000000000000";
                //initialise the board again for next loop.
        }
        }
        return ans;
    }
    private boolean ifValidOriSingle(String string){
        //determine whether a placement is valid:
            //for piece 'A'-'E' ,no flipping.
        char piece=string.charAt(0);
        char orientation=string.charAt(3);
        if (((piece=='A')||(piece=='B')||(piece=='C')||(piece=='D')||(piece=='E'))&&
                ((orientation=='E')||(orientation=='F')||(orientation=='G')||(orientation=='H'))){
            return false;
            }
            return true;
        }
    private boolean ifValidOri(String string){
        //check all placement of orientation is valid.
            // (cannot used fixOrientation due to my function design issue).
        for (int i=0;i<string.length();i+=4){
            if (!ifValidOriSingle(string.substring(i,i+4))){
                return false;
            }
        }
        return true;
        }
    private List<String> allOrientation(){
        Random r=new Random();//generate all permutation of orientation.
        Set<String> out=new HashSet<>();
        int length=remainPiece().size();
        int lengthCopy=length;
        int sum=1;
        while (lengthCopy>0){
            sum=sum*8;
            lengthCopy--;
        }
        while (out.size()<sum)
        {String temp="";
        for (int i=0;i<length;i++){
            Character or=(char) (r.nextInt(8)+'A');
            temp+=or.toString();
        }
        out.add(temp);
        }
        List<String> realOut=new ArrayList<>(out);
        return realOut;
        }
    private List<List<Character>>allOrder(){
        //generate all orders of placement.
        Set<List<Character>> out=new HashSet<>();
        int sum=1;
        List<Character> allPiece=remainPiece();
        int length=allPiece.size();
        while(length>0){
            sum=sum*length;
            length--;
        }
        while (out.size()<sum){
            List<Character> copy=remainPiece();
            Collections.shuffle(copy);
            out.add(copy);
        }
        List<List<Character>> realout=new ArrayList<>(out);
        return realout;
        }
    private String fixedOrder(String in){
        //turn all solution to a fixed order.eg: ABCEFACDBACA->ABCEBACAFACD
        String out="";
        int length=in.length();
        List<Character> tempCh=new ArrayList<>();
        List<String> tempStr=new ArrayList<>();
        for (int i=0;i<length;i+=4){
            tempCh.add(in.charAt(i));
            tempStr.add(in.substring(i,i+4));
        }
        Collections.sort(tempCh);
        for (Character ch:tempCh){
            for (String str:tempStr){
                if (str.charAt(0)==ch)
                    out+=str;
            }
        }
        return out;
        }







    /**
     * Return the solution to the puzzle.  The solution is calculated lazily, so first
     * check whether it's already been calculated.
     *
     * @return A string representing the solution to this habitat.
     */
    public String getSolution() {
        if (solution == null) setSolution();
        return solution;
    }

    /**
     * Establish the solution to this puzzle.
     */
    private void setSolution() {
        String[] solutions = getSolutions();
        if (solutions.length != 1) {
            throw new IllegalArgumentException("IQPuzzlerPro " + objective + " " + (solutions.length == 0 ? " has no " : " has more than one ") + "solution");
        } else
            solution = solutions[0];
    }
}
