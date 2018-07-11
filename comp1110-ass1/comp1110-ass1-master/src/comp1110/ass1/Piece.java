package comp1110.ass1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.min;

/**
 * An enumeration representing the twelve pieces in the IQPuzzlerPro game.
 * <p>
 * You may want to look at the 'Planet' example in the Oracle enum tutorial for
 * an example of an enumeration.
 * <p>
 * http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
 */
public enum Piece {
    A(new Square[]{new Square(0, 0), new Square(1, 0), new Square(0, 1)}),
    B(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(1, 1)}),
    C(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(0, 1), new Square(0, 2)}),
    D(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(0, 1), new Square(2, 1)}),
    E(new Square[]{new Square(1, 0), new Square(2, 0), new Square(0, 1), new Square(1, 1), new Square(0, 2)}),
    F(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(0, 1)}),
    G(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(0, 1), new Square(1, 1)}),
    H(new Square[]{new Square(0, 0), new Square(1, 0), new Square(1, 1), new Square(2, 1)}),
    I(new Square[]{new Square(0, 0), new Square(0, 1), new Square(1, 1), new Square(2, 1), new Square(1, 2)}),
    J(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(3, 0), new Square(0, 1)}),
    K(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(3, 0), new Square(1, 1)}),
    L(new Square[]{new Square(1, 0), new Square(2, 0), new Square(3, 0), new Square(0, 1), new Square(1, 1)});

    public static final int MAX_PIECE_WIDTH = 4;

    public static class Square {
        public static final int NUM_COLUMNS = 11;
        public static final int NUM_ROWS = 5;
        final int col;
        final int row;

        Square(int col, int row) {
            this.col = col;
            this.row = row;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Square) {
                Square s = (Square) obj;
                return s.col == this.col && s.row == this.row;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return col * 37 + row;
        }

        public String toString() {
            return col + "," + row;
        }
    }

    /**
     * A list of spaces covered by this piece in its default rotation.
     * Each space in the list is given as an offset from the origin (0,0).
     */
    public final Square[] shape;

    Piece(Square[] shape) {
        this.shape = shape;
    }

    public char getId() {
        return this.name().charAt(0);
    }

    /**
     * Return indices corresponding to which board spaces would be covered
     * by this piece, given a correct provided placement.
     * <p>
     * Examples:
     * Given the piece placement string 'GACA' would return the indices: {22,23,24,33,34}.
//     * Given the piece placement string 'HGB.l.l//.....F' would return the indices: {17,28,29,40}.
//     * <p>
//     * Hint: You can associate values with each entry in the enum using a constructor,
//     * so you could use that to somehow encode the properties of each of the twelve pieces.
//     * Then in this method you could use the value to calculate the required indices.
//     * <p>
//     * See the 'Grade' enum given in the O2 lecture as part of the lecture code (live coding),
//     * for an example of an enum with associated state and constructors.
//     * <p>
//     * The tutorial here: http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
//     * has an example of a Planet enum, which includes two doubles in each planet's
//     * constructor representing the mass and radius.   Those values are used in the
//     * surfaceGravity() method, for example.
//     *
//     * @param column      the column in which the origin of the piece is placed ('A' to 'K')
//     * @param row         the row in which the origin of the tile is placed ('A' to 'E')
//     * @param orientation which orientation the tile is in ('A' to 'H')
//     * @return A set of indices corresponding to the board positions that would be covered by this piece
//     */
    int[] getCovered(char column, char row, char orientation) {
        Square [] finalShape;//final after rotate, flip.
        if (orientation<='D'){
           finalShape=rotatedTime(orientation-'A',shape);}
        else {
             finalShape=(rotatedTime(orientation-'E',flip(shape)));
        }
        Square [] outShape=relocated(finalShape);//piece after relocates.
        int lengthOfShape=outShape.length;
        int [] out=new int [lengthOfShape];// the index that the piece covered.
        int count=0;
        for (Square o:outShape){
            out[count]=(column-'A')+((row-'A')*11)+o.row*11+o.col;
            if (out[count]>54||out[count]<0)
                out[count]=-1;// the placement is out of bound
            count++;
        }
        return out;
        // FIXME Task 5: implement code that correctly creates an array of integers specifying the indices of the covered spaces
    }
    public Square [] rotate(Square [] shape){
        //rotate the piece by operating on every piece
        Square [] rotated=new Square[shape.length];//piece that is rotated
        int index=0;
        for (Square sq:shape){//loop over every piece to rotate.
            int newRow=sq.col;
            int newColumn=sq.row;
            Square r=new Square(-newColumn,newRow);
            rotated[index]=r;
            index++;
        }
        return rotated;
    }
    public Square [] rotatedTime(int times,Square [] shape){
        if (times==0)//rotate several times by instruction.
            return shape;
        else if(times==1)
            return rotate(shape);
        else if(times==2)
            return rotate(rotate(shape));
        else
            return rotate(rotate(rotate(shape)));
    }
    public Square [] flip(Square [] shape){
        // flip piece by flip each square.
        Square [] filped=new Square[shape.length];
        int count=0;
        for (Square  l:shape){
            filped[count]=new Square(-l.col,l.row);
            count++;
        }
        return filped;

    }
    public  Square [] relocated(Square [] shape) {
        //move the piece(rotated and flipped) to achieve the instruction:
        // the top-left location stays the same.
        int columnMin;
        int rowMin;
        List rowList=new ArrayList();
        List columnList=new ArrayList();
        for (Square j:shape){
            rowList.add(j.row);
            columnList.add(j.col);
        }
        columnMin= (int )Collections.min(columnList);
        rowMin= (int)Collections.min(rowList);
        Square [] out=new Square[shape.length];
        int index=0;
        for (Square p:shape){
            out[index]=new Square(p.col-columnMin,p.row-rowMin);
            index++;
        }
        return out;
    }
    public int getColumnExtent() {
        int xMax = 0;
        for (Square square : shape) {
            xMax = Math.max(xMax, square.col);
        }
        return xMax + 1;
    }

    public int getRowExtent() {
        int yMax = 0;
        for (Square square : shape) {
            yMax = Math.max(yMax, square.row);
        }
        return yMax + 1;
    }
}
