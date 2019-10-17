package comp1110.ass2.gui;

import comp1110.ass2.Robot;
import comp1110.ass2.WarringStatesGame;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

import java.util.List;

import static comp1110.ass2.Rule.*;
import static comp1110.ass2.WarringStatesGame.*;
import static comp1110.ass2.gui.Game.*;
import static comp1110.ass2.gui.Viewer.*;

// the card class show cards placement on scene.
// Also make the zhangYi can be move other cannot be move.
//user interact the game.
public class Card extends ImageView {
    // author of all variables code below: Wenbo Du
    // all the image used here is modified and create from Weiwei Liu.
    private int row, column;
    // previous location of mouse
    private double homeX, homeY, mouseX, mouseY;
    // helper for robot use.
    private static boolean whetherDragged = false;
    // interact with user: whether use a robot.
    public static boolean ifRobot;
    public static int marginOne, marginTwo, marginThree, marginFour = 0;
    // a copy of placement used for displayed card.
    public static String placementCopy;
    // the group for the supporter of each player.
    public static final Group playerOne = new Group();
    public static final Group playerTwo = new Group();
    public static final Group playerThree = new Group();
    public static final Group playerFour = new Group();
    public static String moveSequence = "";
    //game difficulty
    public static int difficulty;
    public static int numberOfRobot;
    public static int numberOfHuman;
    //how many times a robot has move.
    private int robotMoves = 0;

    /**
     * Initialise the cards
     *
     * @param placement Current placement of cards
     */
    //author: Wenbo Du, some idea cames from ANU comp1110 assignment 1.
    Card(String placement) {
        // load image for card.
        Image image = new Image(Viewer.class.getResource(URI_BASE) + placement.substring(0, 2) + ".png");
        setImage(image);
        int index = cardCharToIndex(placement.charAt(2));
        row = index / 6;
        column = index % 6;
        // set the size of card
        setFitHeight(FITTED / 6 - 10);
        setFitWidth(FITTED / 6 - 10);
        updateCard();
        // only zhangYi can be moved.
        if (placement.substring(0, 2).equals("z9")) {
            //press left button.
            setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    // only can be moved when the robot finished their turn.
                    if (placementCopy.length() == 3 * 36 || ifRobot == true && whetherDragged == false || ifRobot == false) {
                        mouseX = event.getSceneX();
                        mouseY = event.getSceneY();
                    }
                }
            });

            // start to move mouse(when pressed left button , move card location with mouse.
            setOnMouseDragged(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    // only can be moved when the robot finished their turn.
                    if (placementCopy.length() == 3 * 36 || ifRobot == true && whetherDragged == false || ifRobot == false) {
                        toFront();
                        setLayoutX(getLayoutX() + event.getSceneX() - mouseX);
                        setLayoutY(getLayoutY() + event.getSceneY() - mouseY);
                        mouseX = event.getSceneX();
                        mouseY = event.getSceneY();
                    }
                }
            });
            //release mouse( left button ). Set the card to the new location of board if valid.
            setOnMouseReleased(event -> {
                // only can be moved when the robot finished their turn.
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (placementCopy.length() == 3 * 36 || ifRobot == true && whetherDragged == false || ifRobot == false) {
                        // if move out of board.
                        if (!(FITTED / 12 + (BOARD_WIDTH - BOARD_HEIGHT) / 2 < mouseX
                                && mouseX < FITTED / 12 + (BOARD_WIDTH - BOARD_HEIGHT) / 2 + (FITTED * 6 / 6) &&
                                0 < mouseY && mouseY < FITTED)) {
                            setLayoutX(homeX);
                            setLayoutY(homeY);
                        } else {
                            //on board, if a move is valid, move it.Otherwise, stay in the original location
                            moveIfValid();
                        }
                    }
                }
            });

            //to use robot just right click mouse button
            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    if (ifRobot == true && whetherDragged && !isGameOver(placementCopy)) {
                        char randomMove = Robot.genMove(difficulty);
                        useRobot(randomMove);
                        robotMoves++;
                        if (robotMoves % numberOfRobot == 0) {
                            //when the last robot finished his turn, enable player to move the card can be moved.
                            whetherDragged = false;
                            robotMoves = 0;
                        }
                    }
                }
            });
        }


    }

    /**
     * Update the card location to a new grid on board.
     */
    //author: Wenbo Du
    public void updateCard() {
        setLayoutX((FITTED * column / 6) + FITTED / 12 + (BOARD_WIDTH - BOARD_HEIGHT) / 2);
        setLayoutY(FITTED * row / 6 + 25);
        homeX = getLayoutX();
        homeY = getLayoutY();
    }

    /**
     * If a card if valid collected by zhangYi, than it should leave the board.
     */
    //author: Wenbo Du
    public void moveOutBoard() {
        Node nd;
        // find the location of the card.
        int id = cardCharToIndex(ZhangYiLocation(placement));
        int zy = (5 - id % 6) * 6 + id / 6;
        // get it from the card group
        if (((5 - column) * 6 + row) > zy)
            nd = cards.getChildren().get((5 - column) * 6 + row - 1);
        else
            nd = cards.getChildren().get((5 - column) * 6 + row);
        // set it to not visible.
        nd.setVisible(false);
        ImageView a = new ImageView();
        // create a new card with same image, use for arrange them to player out of board.
        a.setImage(((ImageView) nd).getImage());
        arrangeToPlayer(a);
    }

    /**
     * Use robot to generate a move.
     *
     * @param randomMove
     */
    //author: Wenbo Du
    public void useRobot(char randomMove) {
        // update move sequence
        moveSequence = moveSequence + randomMove;
        // carry cards from the same kingdom on the way Zhang Yi to destination.
        List<Character> barrier = findBarrier(placementCopy, randomMove);
        placementCopy = updateBoard(placementCopy, randomMove);
        barrier.add(randomMove);
        // add all empty grid in the board.
        // add all location zhangYi passes.(He can not come to this place again)
        placed.addAll(barrier);
        addBarrier(barrier);
        column = cardCharToIndex(randomMove) % 6;
        row = cardCharToIndex(randomMove) / 6;
        //update card placement on the board.
        updateCard();
        // for a user or robot to collect card.
        collectCards();
        //update Flag each player hold.
        showFlag();
        round++;
        // determine if game has been finished.
        if (isGameOver(placementCopy)) {
            Viewer.placeWinMassage(determineWin());
        }
    }

    /**
     * Update the state of background and on stage.
     *
     * @param location location character
     */
    //author: Wenbo Du
    public void nextStep(char location) {
        // for the fist move: add all the flag in the scene.
        if (placementCopy == Viewer.placement) {
            Game.addFlag();
        }
        List<Character> carrier = findBarrier(placementCopy, location);
        // update the board placement for next step.
        placementCopy = updateBoard(placementCopy, indexToChar(row, column));
        // add the locations zhangYi has already been( he can not go to this location again).
        placed.add(location);
        addBarrier(carrier);
        placed.addAll(carrier);
        // update the move sequence.
        moveSequence = moveSequence + location;
        // move the card out of board to the player.
        moveOutBoard();
        // each player collect the card.
        collectCards();
        // update the flag state for each player.
        Game.showFlag();
        round++;
        // determine if human's turn finished.
        if (numberOfHuman == round % numPlayer)
            // now right click trigger robot's turn.
            whetherDragged = true;
        if (isGameOver(placementCopy)) {
            // determine if game over.
            Viewer.placeWinMassage(determineWin());
        }
    }

    /**
     * fit the card into grids if valid. Whether, move it back to origin location.
     */
    // author: Wenbo Du
    public void moveIfValid() {
        // find the closet card grid for card.
        double marginY = getLayoutY() % (FITTED / 6);
        double marginX = (getLayoutX() - (BOARD_WIDTH - FITTED) / 2) % (FITTED / 6);
        int col = (int) (getLayoutX() - (BOARD_WIDTH - FITTED) / 2) / (FITTED / 6);
        int row = (int) getLayoutY() / (FITTED / 6);
        if (marginX > FITTED / 12)
            col = col + 1;
        if (marginY > FITTED / 12)
            row = row + 1;
        char location = indexToChar(row, col);
        // if the move obey game rule, update the board.
        if (((col == this.column) || (row == this.row))
                && col + row != column + this.row && !placed.contains(location)
                && WarringStatesGame.isMoveLegal(placementCopy, location)) {
            this.row = row;
            this.column = col;
            updateCard();
            nextStep(indexToChar(this.row, column));
        } else {
            // else move the card back to origin location.
            setLayoutX(homeX);
            setLayoutY(homeY);
        }
    }

    /**
     * If one movement can move more than one card. Remove the extra cards too.
     *
     * @param barrier
     */
    //author: Wenbo Du
    public static void addBarrier(List<Character> barrier) {
        int col, row;
        // zhang yi's index
        int id = cardCharToIndex(ZhangYiLocation(placement));
        int zy = (5 - id % 6) * 6 + id / 6;
        for (Character l : barrier) {
            // the cards that will be carried on the way.
            col = cardCharToIndex(l) % 6;
            row = cardCharToIndex(l) / 6;
            Node nd;
            if (((5 - col) * 6 + row) > zy)
                nd = cards.getChildren().get((5 - col) * 6 + row - 1);
            else
                nd = cards.getChildren().get((5 - col) * 6 + row);
            // remove it from board.
            nd.setVisible(false);
            ImageView a = new ImageView();
            a.setImage(((ImageView) nd).getImage());
            // move it to player card holder place.
            arrangeToPlayer(a);
        }
    }
}