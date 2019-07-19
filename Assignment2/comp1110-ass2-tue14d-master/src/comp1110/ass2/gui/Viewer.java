package comp1110.ass2.gui;

import comp1110.ass2.Rule;
import comp1110.ass2.WarringStatesGame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.*;

import static comp1110.ass2.gui.Card.*;
import static comp1110.ass2.gui.Game.*;


/**
 * A very simple viewer for card layouts in the Warring States game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various card placements.
 */
public class Viewer {
    // author of all variables code below: Wenbo Du,  Weiwei Liu.
    // all the image used here is modified and create from Weiwei Liu.
    public static final int FITTED = BOARD_HEIGHT - MARGIN_HEIGHT;
    //display the cards and board in center
    public static final String URI_BASE = "assets/";
    public static final Group root = new Group();
    public static final Group cards = new Group();//add all the cards to a group card.
    public static Group controls = new Group();
    static TextField textField;
//    static Text reminder = new Text("reminder: to trigger robot turn, just simply move mouse af your turn.(Robot have different response time based on difficulty)");
    public static int round = 0;
    public static int numPlayer = 4;
    public static String placement;
    public static Set<Character> placed = new HashSet<>();

    /**
     * Create the first rule introduction page.
     */
    //author: Weiwei Liu
    static void setOpenScene() {
        ImageView openSceneView = new ImageView();
        Image openSceneImage = new Image(Viewer.class.getResource(URI_BASE) + "Background4_900x733.png");
        openSceneView.setImage(openSceneImage);
        openSceneView.setFitWidth(FITTED + 300);
        openSceneView.setFitHeight(FITTED + 100);//set the size of openScene
        openSceneView.setX(15);
        openSceneView.setY(20);//set the location of openScene
        root.getChildren().add(openSceneView);
        Button start = new Button("Start");//create Start button direct to the game setting page
        start.setPrefSize(300, 70);
        start.setLayoutX(BOARD_WIDTH / 2 - 155);
        start.setLayoutY(BOARD_HEIGHT - 310);
        start.setOpacity(0.001);

        Button howToPlay = new Button("Help");//create Help button direct to the gam introduction page
        howToPlay.setPrefSize(300, 70);
        howToPlay.setLayoutX(BOARD_WIDTH / 2 - 155);
        howToPlay.setLayoutY(BOARD_HEIGHT - 135);
        howToPlay.setOpacity(0.001);
        root.getChildren().addAll(start, howToPlay);
        start.setOnAction(event -> {
            click();
            playGame();
        });

        howToPlay.setOnAction(event -> {
            click();
            GameIntro.ruleIntro1();
        });
    }

    /**
     * Create the game playing page.
     */
    //author: Weiwei Liu
    public static void makeControls() {
        ImageView control = new ImageView();
        Image controlSceneImage = new Image(Viewer.class.getResource(URI_BASE) + "Background3_900x733_placement.png");
        control.setImage(controlSceneImage);
        control.setFitWidth(FITTED + 300);
        control.setFitHeight(FITTED + 100);
        control.setX(15);
        control.setY(20);

        Button controlStartBtn = new Button("Start");//create Start button direct to the game setting page
        controlStartBtn.setPrefSize(75, 30);
        controlStartBtn.setLayoutX(BOARD_WIDTH / 2 + 80);
        controlStartBtn.setLayoutY(BOARD_HEIGHT - 72);
        controlStartBtn.setOpacity(0.001);

        Button controlRandomBtn = new Button("Random");//automatically set a legal random cards placement
        controlRandomBtn.setPrefSize(95, 30);
        controlRandomBtn.setLayoutX(BOARD_WIDTH / 2 + 185);
        controlRandomBtn.setLayoutY(BOARD_HEIGHT - 72);
        controlRandomBtn.setOpacity(0.001);

        Button controlExitBtn = new Button("Exit");//create Exit button direct to the open scene page
        controlExitBtn.setPrefSize(75, 30);
        controlExitBtn.setLayoutX(BOARD_WIDTH / 2 + 300);
        controlExitBtn.setLayoutY(BOARD_HEIGHT - 72);
        controlExitBtn.setOpacity(0.001);

        textField = new TextField();
        textField.setPrefSize(300, 30);
        textField.setLayoutX(BOARD_WIDTH / 2 - 235);
        textField.setLayoutY(BOARD_HEIGHT - 72);

        Text error = showError("invalid placement! Please try again");//Error message when start invalid placement input
        controlExitBtn.setOnAction(event -> {
            click();
            setOpenScene();
        });

        //Random cards placement, direct to the game playing page(wellPlacement page)
        controlRandomBtn.setOnAction(event -> {
            click();
            error.setVisible(false);
            ImageView wellPlacement = new ImageView();

            if (numPlayer == 2 && !ifRobot) {
                Image wellPlacementImage = new Image(Viewer.class.getResource(URI_BASE) + "wellPlacement2_0.png");
                wellPlacement.setImage(wellPlacementImage);
            }  else if (numPlayer == 3 && !ifRobot) {
                Image wellPlacementImage = new Image(Viewer.class.getResource(URI_BASE) + "wellPlacement3_0.png");
                wellPlacement.setImage(wellPlacementImage);
            } else if (numPlayer == 4 && !ifRobot) {
                Image wellPlacementImage = new Image(Viewer.class.getResource(URI_BASE) + "wellPlacement4_0.png");
                wellPlacement.setImage(wellPlacementImage);
            }else{
                Image wellPlacementImage = new Image(Viewer.class.getResource(URI_BASE) + "wellPlacement"+numPlayer+"_"+Card.numberOfRobot+".png");
                wellPlacement.setImage(wellPlacementImage);
            }

            wellPlacement.setFitWidth(FITTED + 300);
            wellPlacement.setFitHeight(FITTED + 100);
            wellPlacement.setX(15);
            wellPlacement.setY(20);

            Button wellExitBtn = new Button("Exit");
            wellExitBtn.setPrefSize(75, 30);
            wellExitBtn.setLayoutX(BOARD_WIDTH / 2 + 325);
            wellExitBtn.setLayoutY(BOARD_HEIGHT - 76);
            wellExitBtn.setOpacity(0.001);

            Button wellRestartBtn = new Button("Restart");
            wellRestartBtn.setPrefSize(120, 30);
            wellRestartBtn.setLayoutX(BOARD_WIDTH / 2 + 170);
            wellRestartBtn.setLayoutY(BOARD_HEIGHT - 76);
            wellRestartBtn.setOpacity(0.001);

            root.getChildren().addAll(wellPlacement, wellExitBtn, wellRestartBtn);

            wellRestartBtn.setOnAction(event1 -> {
                click();
                playGame();
            });

            wellExitBtn.setOnAction(event1 -> {
                click();
                setOpenScene();
            });
            cards.getChildren().clear();//clear the previous placement.
            root.getChildren().remove(cards);//remove cards group since we need the add it again.

            Random randomPlacement = new Random();
            placement = Rule.PLACEMENTS[randomPlacement.nextInt(Rule.PLACEMENTS.length)];
            Game.makePlacement(placement);
            root.getChildren().removeAll(controls, controlStartBtn, controlExitBtn, textField);
            placementCopy = placement;
        });

        //When the input is legal, direct to the game playing page(wellPlacement page)
        //When the input is illegal, direct to the error page
        controlStartBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                click();
                if (!WarringStatesGame.isPlacementWellFormed(textField.getText()) || textField.getText().isEmpty()) {
                    if (!root.getChildren().contains(error)) {
                        ImageView error = new ImageView();
                        Image errorImage = new Image(Viewer.class.getResource(URI_BASE) + "Background3_900x733_error.png");
                        error.setImage(errorImage);
                        error.setFitWidth(FITTED + 300);
                        error.setFitHeight(FITTED + 100);
                        error.setX(15);
                        error.setY(20);

                        Button controlRestartBtn = new Button("Restart");
                        controlRestartBtn.setPrefSize(120, 40);
                        controlRestartBtn.setLayoutX(BOARD_WIDTH / 2 - 80);
                        controlRestartBtn.setLayoutY(BOARD_HEIGHT - 295);
                        controlRestartBtn.setOpacity(0.001);

                        root.getChildren().addAll(error, controlRestartBtn, controlExitBtn);

                        controlRestartBtn.setOnAction(event -> {
                            click();
                            playGame();
                        });
                        textField.clear();
                    }
                } else {
                    error.setVisible(false);
                    ImageView wellPlacement = new ImageView();
                    if (numPlayer == 2 && !ifRobot) {
                        Image wellPlacementImage = new Image(Viewer.class.getResource(URI_BASE) + "wellPlacement2_0.png");
                        wellPlacement.setImage(wellPlacementImage);
                    }  else if (numPlayer == 3 && !ifRobot) {
                        Image wellPlacementImage = new Image(Viewer.class.getResource(URI_BASE) + "wellPlacement3_0.png");
                        wellPlacement.setImage(wellPlacementImage);
                    } else if (numPlayer == 4 && !ifRobot) {
                        Image wellPlacementImage = new Image(Viewer.class.getResource(URI_BASE) + "wellPlacement4_0.png");
                        wellPlacement.setImage(wellPlacementImage);
                    }else{
                        Image wellPlacementImage = new Image(Viewer.class.getResource(URI_BASE) + "wellPlacement"+numPlayer+"_"+Card.numberOfRobot+".png");
                        wellPlacement.setImage(wellPlacementImage);
                    }
                    wellPlacement.setFitWidth(FITTED + 300);
                    wellPlacement.setFitHeight(FITTED + 100);
                    wellPlacement.setX(15);
                    wellPlacement.setY(20);

                    Button wellExitBtn = new Button("Exit");
                    wellExitBtn.setPrefSize(75, 30);
                    wellExitBtn.setLayoutX(BOARD_WIDTH / 2 + 325);
                    wellExitBtn.setLayoutY(BOARD_HEIGHT - 76);
                    wellExitBtn.setOpacity(0.001);

                    Button wellRestartBtn = new Button("Restart");
                    wellRestartBtn.setPrefSize(120, 30);
                    wellRestartBtn.setLayoutX(BOARD_WIDTH / 2 + 170);
                    wellRestartBtn.setLayoutY(BOARD_HEIGHT - 76);
                    wellRestartBtn.setOpacity(0.001);

                    root.getChildren().addAll(wellPlacement, wellExitBtn, wellRestartBtn);

                    wellRestartBtn.setOnAction(event1 -> {
                        click();
                        playGame();
                    });

                    wellExitBtn.setOnAction(event -> {
                        click();
                        setOpenScene();
                    });
                    cards.getChildren().clear();//clear the previous placement.
                    root.getChildren().remove(cards);//remove cards group since we need add it again.
                    placement = textField.getText();
                    root.getChildren().removeAll(controls, controlStartBtn, controlExitBtn, textField);
                    placementCopy = placement;
                    placed.add(WarringStatesGame.ZhangYiLocation(placement));
                    Game.makePlacement(placement);
                }
            }
        });
        controls.getChildren().addAll(error, control, controlStartBtn, controlRandomBtn, controlExitBtn, textField);
    }

    /**
     * Show error
     * @param string error message
     * @return text of error
     */
    private static Text showError(String string) {
        Text error = new Text(string);
        error.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        error.setLayoutX(600);
        error.setFill(Color.RED);
        error.setLayoutY(BOARD_HEIGHT - 30);
        return error;
    }

    /**
     * Create the winner page.
     */
    // author: Weiwei Liu, Wenbo Du
    public static void placeWinMassage(int numPlayer) {
        ImageView imageView = new ImageView();
        Image showWin = new Image(Viewer.class.getResource(URI_BASE) + "win" + numPlayer + ".png");
        imageView.setImage(showWin);
        imageView.setFitWidth(FITTED + 300);
        imageView.setFitHeight(FITTED + 100);
        imageView.setX(15);
        imageView.setY(20);
        Button winRestartBtn = new Button("Restart");
        winRestartBtn.setPrefSize(120, 40);
        winRestartBtn.setLayoutX(BOARD_WIDTH / 2 - 60);
        winRestartBtn.setLayoutY(BOARD_HEIGHT - 285);
        winRestartBtn.setOpacity(0.001);;
        root.getChildren().addAll(imageView, winRestartBtn);
        winRestartBtn.setOnAction(event -> {
            click();
            playGame();
        });
    }

    /**
     * Create the number of player option page.
     */
    // author: Weiwei Liu, Wenbo Du
    public static void makePlayerNum() {
        ImageView playerNum = new ImageView();
        Image openSceneImage = new Image(Viewer.class.getResource(URI_BASE) + "Background3_900x733_Num.png");
        playerNum.setImage(openSceneImage);
        playerNum.setFitWidth(FITTED + 300);
        playerNum.setFitHeight(FITTED + 100);
        playerNum.setX(15);
        playerNum.setY(20);
        root.getChildren().add(playerNum);
        Button twoBtn = new Button("2");
        twoBtn.setPrefSize(150, 70);
        twoBtn.setLayoutX(BOARD_WIDTH / 2 - 340);
        twoBtn.setLayoutY(BOARD_HEIGHT - 300);
        twoBtn.setOpacity(0.001);

        Button threeBtn = new Button("3");
        threeBtn.setPrefSize(150, 70);
        threeBtn.setLayoutX(BOARD_WIDTH / 2 - 78);
        threeBtn.setLayoutY(BOARD_HEIGHT - 300);
        threeBtn.setOpacity(0.001);

        Button fourBtn = new Button("4");
        fourBtn.setPrefSize(150, 70);
        fourBtn.setLayoutX(BOARD_WIDTH / 2 + 185);
        fourBtn.setLayoutY(BOARD_HEIGHT - 300);
        fourBtn.setOpacity(0.001);

        Button exit = new Button("Exit");
        exit.setPrefSize(200, 65);
        exit.setLayoutX(BOARD_WIDTH / 2 - 103);
        exit.setLayoutY(BOARD_HEIGHT - 140);
        exit.setOpacity(0.001);

        root.getChildren().addAll(twoBtn, threeBtn, fourBtn, exit);

        twoBtn.setOnAction(event -> {
            click();
            numPlayer = 2;
            root.getChildren().removeAll(twoBtn, threeBtn, fourBtn, exit);
            makeRobot();
        });
        threeBtn.setOnAction(event -> {
            click();
            numPlayer = 3;
            root.getChildren().removeAll(twoBtn, threeBtn, fourBtn, exit);
            makeRobot();
        });
        fourBtn.setOnAction(event -> {
            click();
            numPlayer = 4;
            root.getChildren().removeAll(twoBtn, threeBtn, fourBtn, exit);
            makeRobot();
        });
        exit.setOnAction(event -> {
            click();
            setOpenScene();
        });
    }

    /**
     * Create the robot option page.
     */
    // author: Weiwei Liu
    private static void makeRobot() {
        ImageView robot = new ImageView();
        Image openSceneImage = new Image(Viewer.class.getResource(URI_BASE) + "Background3_900x733_robot.png");
        robot.setImage(openSceneImage);
        robot.setFitWidth(FITTED + 300);
        robot.setFitHeight(FITTED + 100);
        robot.setX(15);
        robot.setY(20);

        Button yes = new Button("Yes");
        yes.setPrefSize(110, 48);
        yes.setLayoutX(BOARD_WIDTH / 2 - 190);
        yes.setLayoutY(BOARD_HEIGHT - 320);
        yes.setOpacity(0.001);

        Button no = new Button("No");
        no.setPrefSize(110, 48);
        no.setLayoutX(BOARD_WIDTH / 2 + 40);
        no.setLayoutY(BOARD_HEIGHT - 320);
        no.setOpacity(0.001);

        Button exit = new Button("Exit");
        exit.setPrefSize(30, 30);
        exit.setLayoutX(BOARD_WIDTH / 2 + 180);
        exit.setLayoutY(BOARD_HEIGHT - 423);
        exit.setOpacity(0.001);

        root.getChildren().addAll(robot, yes, no, exit);

        yes.setOnAction(event -> {
            click();
            if(numPlayer==2){
                ifRobot = true;
                numberOfRobot=1;
                numberOfHuman=1;
                makeRobotLevel();
            }else {
                ifRobot =true;
                makeRobotNum();
            }
        });
        no.setOnAction(event -> {
            click();
            ifRobot = false;
            root.getChildren().removeAll(yes, no, exit, robot);
            root.getChildren().add(controls);
        });
        exit.setOnAction(event -> {
            click();
            makePlayerNum();
        });
    }

    /**
     * Create the robot difficulty level option page.
     */
    // author: Weiwei Liu
    public static void makeRobotNum(){
        ImageView robotNum = new ImageView();
        if(numPlayer ==3) {
            Image robotNumImage = new Image(Viewer.class.getResource(URI_BASE) + "Background3_900x733_NumOfRobotsTwo.png");
            robotNum.setImage(robotNumImage);

            robotNum.setFitWidth(FITTED + 300);
            robotNum.setFitHeight(FITTED + 100);
            robotNum.setX(15);
            robotNum.setY(20);

            Button oneBtn = new Button("1");
            oneBtn.setPrefSize(150, 70);
            oneBtn.setLayoutX(BOARD_WIDTH / 2 - 220);
            oneBtn.setLayoutY(BOARD_HEIGHT - 300);
            oneBtn.setOpacity(0.001);

            Button twoBtn = new Button("2");
            twoBtn.setPrefSize(150, 70);
            twoBtn.setLayoutX(BOARD_WIDTH / 2 +42);
            twoBtn.setLayoutY(BOARD_HEIGHT - 300);
            twoBtn.setOpacity(0.001);

            Button back = new Button("Back");
            back.setPrefSize(200, 65);
            back.setLayoutX(BOARD_WIDTH / 2 - 103);
            back.setLayoutY(BOARD_HEIGHT - 140);
            back.setOpacity(0.001);

            root.getChildren().addAll(robotNum,twoBtn, oneBtn, back);

            oneBtn.setOnAction(event -> {
                click();
                numberOfRobot=1;
                numberOfHuman=2;
                root.getChildren().removeAll(twoBtn, oneBtn, back);
                makeRobotLevel();
            });
            twoBtn.setOnAction(event -> {
                click();
                numberOfRobot=2;
                numberOfHuman=1;
                root.getChildren().removeAll(twoBtn, oneBtn, back);
                makeRobotLevel();
            });
            back.setOnAction(event -> {
                click();
                setOpenScene();
            });
        }else if(numPlayer ==4){
            Image robotNumImage = new Image(Viewer.class.getResource(URI_BASE) + "Background3_900x733_NumOfRobotsThree.png");
            robotNum.setImage(robotNumImage);
            robotNum.setFitWidth(FITTED + 300);
            robotNum.setFitHeight(FITTED + 100);
            robotNum.setX(15);
            robotNum.setY(20);

            Button oneBtn = new Button("1");
            oneBtn.setPrefSize(150, 70);
            oneBtn.setLayoutX(BOARD_WIDTH / 2 - 340);
            oneBtn.setLayoutY(BOARD_HEIGHT - 300);
            oneBtn.setOpacity(0.001);

            Button twoBtn = new Button("2");
            twoBtn.setPrefSize(150, 70);
            twoBtn.setLayoutX(BOARD_WIDTH / 2 - 78);
            twoBtn.setLayoutY(BOARD_HEIGHT - 300);
            twoBtn.setOpacity(0.001);

            Button threeBtn = new Button("3");
            threeBtn.setPrefSize(150, 70);
            threeBtn.setLayoutX(BOARD_WIDTH / 2 + 185);
            threeBtn.setLayoutY(BOARD_HEIGHT - 300);
            threeBtn.setOpacity(0.001);

            Button back = new Button("Back");
            back.setPrefSize(200, 65);
            back.setLayoutX(BOARD_WIDTH / 2 - 103);
            back.setLayoutY(BOARD_HEIGHT - 140);
            back.setOpacity(0.001);

            root.getChildren().addAll(robotNum,twoBtn, threeBtn, oneBtn, back);

            oneBtn.setOnAction(event -> {
                click();
                numberOfRobot=1;
                numberOfHuman=3;
                root.getChildren().removeAll(twoBtn, threeBtn, oneBtn, back);
                makeRobotLevel();
            });
            twoBtn.setOnAction(event -> {
                click();
                numberOfRobot=2;
                numberOfHuman=2;
                root.getChildren().removeAll(twoBtn, threeBtn, oneBtn, back);
                makeRobotLevel();
            });
            threeBtn.setOnAction(event -> {
                click();
                numberOfRobot=3;
                numberOfHuman=1;
                root.getChildren().removeAll(twoBtn, threeBtn, oneBtn, back);
                makeRobotLevel();
            });
            back.setOnAction(event -> {
                click();
                setOpenScene();
            });
        }
    }
    /**
     * Create the robot difficulty level option page.
     */
    // author: Weiwei Liu, Wenbo Du
    public static void makeRobotLevel() {
        ImageView robotLevel = new ImageView();
        Image robotLevelImage = new Image(Viewer.class.getResource(URI_BASE) + "Background3_900x733_LevelOfRobot.png");
        robotLevel.setImage(robotLevelImage);
        robotLevel.setFitWidth(FITTED + 300);
        robotLevel.setFitHeight(FITTED + 100);
        robotLevel.setX(15);
        robotLevel.setY(20);

        Button back = new Button("BACK");//create BACK button direct to the previous introduction page
        back.setPrefSize(160, 50);
        back.setLayoutX(BOARD_WIDTH / 2 - 90);
        back.setLayoutY(BOARD_HEIGHT - 128);
        back.setOpacity(0.001);

        Button easy = new Button("EASY");
        easy.setPrefSize(270, 70);
        easy.setLayoutX(BOARD_WIDTH / 2 - 145);
        easy.setLayoutY(BOARD_HEIGHT - 450);
        easy.setOpacity(0.001);

        Button hard = new Button("HARD");
        hard.setPrefSize(270, 70);
        hard.setLayoutX(BOARD_WIDTH / 2 - 145);
        hard.setLayoutY(BOARD_HEIGHT - 370);
        hard.setOpacity(0.001);

        Button expert = new Button("EXPERT");
        expert.setPrefSize(270, 70);
        expert.setLayoutX(BOARD_WIDTH / 2 - 145);
        expert.setLayoutY(BOARD_HEIGHT - 285);
        expert.setOpacity(0.001);

        root.getChildren().addAll(robotLevel, easy, hard, expert, back);
        easy.setOnAction(event -> {
            click();
            ifRobot = true;
            root.getChildren().removeAll(easy, hard, expert, back);
            root.getChildren().add(controls);
            Card.difficulty=1;
        });
        hard.setOnAction(event -> {
            click();
            ifRobot = true;
            root.getChildren().removeAll(easy, hard, expert, back);
            root.getChildren().add(controls);
            Card.difficulty=2;
        });
        expert.setOnAction(event -> {
            click();
            ifRobot = true;
            root.getChildren().removeAll(easy, hard, expert, back);
            root.getChildren().add(controls);
            Card.difficulty=3;
        });
        back.setOnAction(event -> {
            click();
            makePlayerNum();
        });
    }
}