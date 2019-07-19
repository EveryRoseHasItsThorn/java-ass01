package comp1110.ass2.gui;

import comp1110.ass2.WarringStatesGame;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import static comp1110.ass2.gui.Card.*;
import static comp1110.ass2.gui.Viewer.*;

//main class to start game, also includes some function help to game run correctly.
public class Game extends Application {
    // all the image used here is modified and create from Weiwei Liu.
    // author of all variables code below: Wenbo Du
    public static final int MARGIN_HEIGHT = 100;
    public static final int BOARD_WIDTH = 933;
    public static final int BOARD_HEIGHT = 700;
    private static Group flagOne = new Group();
    private static Group flagTwo = new Group();
    private static Group flagThree = new Group();
    private static Group flagFour = new Group();

    /**
     * Create the game starting page.
     */
    // author: Weiwei Liu
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Warring States Viewer");
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);
        Game.introGame();
        playBGM();
        Image image = new Image(Viewer.class.getResource(URI_BASE) + "cursor.png");
        scene.setCursor(new ImageCursor(image));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Create the the card collection sound.
     */
    //author: Weiwei Liu
    public static void collectCards() {
        Media sound = new Media(Viewer.class.getResource(URI_BASE) + "collectCards.wav");
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    /**
     * Create the background music.
     */
    // author: Weiwei Liu
    public static void playBGM() {
        Media bgm_1 = new Media(Viewer.class.getResource(URI_BASE) + "game_of_thrones.mp3");
        MediaPlayer bgmMedia = new MediaPlayer(bgm_1);
        bgmMedia.getOnRepeat();
        bgmMedia.setCycleCount(100);
        bgmMedia.play();
    }

    /**
     * Create the the mouse click sound.
     */
    // author: Weiwei Liu
    public static void click() {
        Media click = new Media(Viewer.class.getResource(URI_BASE) + "click.wav");
        MediaPlayer clickSound = new MediaPlayer(click);
        clickSound.play();
    }

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    static void makePlacement(String placement) {
        cards.getChildren().clear();
        for (int i = 0; i < placement.length(); i += 3) {
            String sub = placement.substring(i, i + 3);//each card
            Card single = new Card(sub);//place each card to the stage
            cards.getChildren().add(single);
        }
        Viewer.root.getChildren().add(cards);//add all node in cards to root.
    }

    /**
     * Return the groups of invisible flag.
     *
     * @param player player number
     * @return groups of invisible flag.
     */
    // author: Wenbo Du
    public static Group makeFlag(int player) {
        Group out = new Group();
        int xIndex, yIndex;
        if (player % 2 == 0)//set placement location for player 1,2,3,4
            xIndex = 830;
        else
            xIndex = -20;
        if (player <= 2)
            yIndex = 80;
        else
            yIndex = 400;
        //load and fit the flag image from background.
        // and move them to the correct location.
        for (int i = 0; i < 7; i++) {
            ImageView iv = new ImageView();
            Image image = new Image(Viewer.class.getResource(URI_BASE) + ((Character) ((char) ('a' + i))).toString() + ".png");
            iv.setImage(image);
            iv.setFitHeight(FITTED / 17);
            iv.setFitWidth(FITTED / 17);
            iv.setLayoutX(FITTED / 17 + xIndex);
            iv.setLayoutY(yIndex + (FITTED / 17) + i * 35);
            iv.setVisible(false);
            out.getChildren().add(iv);
        }
        return out;
    }

    /**
     * Add the invisible flags to corresponding groups.
     */
    // author: Wenbo Du
    public static void addFlag() {
        flagOne = makeFlag(1);
        flagTwo = makeFlag(2);
        flagThree = makeFlag(3);
        flagFour = makeFlag(4);
        root.getChildren().addAll(flagOne, flagTwo, flagThree, flagFour);
    }

    /**
     * set the flags visible/not visiable based on the flag the player get.
     */
    // author: Wenbo Du
    public static void showFlag() {
        // the flag array.
        int[] flagArray = WarringStatesGame.getFlags(Viewer.placement, Card.moveSequence, numPlayer);
        int index = 0;
        // if a player hold the flag, set the flag image to be visible.
        // otherwise, set it not to be visible.
        for (int i : flagArray) {
            if (i == 0)
                (flagOne.getChildren().get(index)).setVisible(true);
            else
                (flagOne.getChildren().get(index)).setVisible(false);
            if (i == 1)
                (flagTwo.getChildren().get(index)).setVisible(true);
            else
                (flagTwo.getChildren().get(index)).setVisible(false);
            if (i == 2)
                (flagThree.getChildren().get(index)).setVisible(true);
            else
                (flagThree.getChildren().get(index)).setVisible(false);
            if (i == 3)
                (flagFour.getChildren().get(index)).setVisible(true);
            else
                (flagFour.getChildren().get(index)).setVisible(false);
            index++;
        }
    }

    /**
     * Create the play game setup.
     */
    // author: Weiwei Liu
    public static void playGame() {
        reset();
        makeControls();
        Viewer.makeRobotLevel();
        addPlayer();
        Viewer.makePlayerNum();
    }

    /**
     * Add part that card is hold by player on scene.
     */
    // author: Wenbo Du
    public static void addPlayer() {
        playerOne.toFront();// the them to front so not been hide.
        playerTwo.toFront();
        playerThree.toFront();
        playerFour.toFront();
        root.getChildren().add(playerOne);
        root.getChildren().add(playerTwo);
        root.getChildren().add(playerThree);
        root.getChildren().add(playerFour);
    }

    /**
     * Initialise the game: clear all records last game.
     */
    // author: Wenbo Du ,Weiwei Liu
    public static void reset() {
        round = 0;
        cards.getChildren().clear();
        placed.clear();
        playerOne.getChildren().clear();
        playerTwo.getChildren().clear();
        playerThree.getChildren().clear();
        playerFour.getChildren().clear();
        controls.getChildren().clear();
        root.getChildren().clear();
        placementCopy = null;
        round = 0;
        flagFour.getChildren().clear();
        flagOne.getChildren().clear();
        flagTwo.getChildren().clear();
        flagThree.getChildren().clear();
        marginOne = 0;
        marginTwo = 0;
        marginThree = 0;
        marginFour = 0;
        addFlag();
        moveSequence = "";
    }

    /**
     * Load introduction pages
     */
    // author: Weiwei Liu
    public static void introGame() {
        GameIntro.ruleIntro1();
        GameIntro.ruleIntro2();
        GameIntro.ruleIntro3();
        setOpenScene();
    }
}