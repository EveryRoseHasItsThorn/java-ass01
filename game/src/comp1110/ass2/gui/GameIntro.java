package comp1110.ass2.gui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static comp1110.ass2.gui.Game.*;
import static comp1110.ass2.gui.Game.BOARD_HEIGHT;
import static comp1110.ass2.gui.Viewer.*;
// the class for show introduction pages.
public class GameIntro {
    /**
     * Create the first rule introduction page.
     */
    //author: Weiwei Liu
    static void ruleIntro1() {
        ImageView Intro1 = new ImageView();
        Image ruleIntro1Image = new Image(Viewer.class.getResource(URI_BASE) + "Background2_1.png");
        Intro1.setImage(ruleIntro1Image);
        Intro1.setFitWidth(FITTED + 300);
        Intro1.setFitHeight(FITTED + 100);//set the size of ruleIntro1 Scene
        Intro1.setX(15);
        Intro1.setY(20);
        root.getChildren().add(Intro1);
        Button back1 = new Button("BACK");//create BACK button direct to the open scene page
        back1.setPrefSize(215, 70);
        back1.setLayoutX(BOARD_WIDTH / 2 - 385);
        back1.setLayoutY(BOARD_HEIGHT - 110);
        back1.setOpacity(0.001);

        Button next1 = new Button("NEXT");//create NEXT button direct to the next introduction page
        next1.setPrefSize(215, 70);
        next1.setLayoutX(BOARD_WIDTH / 2 + 160);
        next1.setLayoutY(BOARD_HEIGHT - 110);
        next1.setOpacity(0.001);

        root.getChildren().addAll(back1, next1);
        back1.setOnAction(event -> {
            click();
            setOpenScene();

        });
        next1.setOnAction(event -> {
            click();
            ruleIntro2();
        });
    }

    /**
     * Create the second rule introduction page.
     */
    //author: Weiwei Liu
    static void ruleIntro2() {
        ImageView Intro2 = new ImageView();
        Image ruleIntro2Image = new Image(Viewer.class.getResource(URI_BASE) + "Background2_2.png");
        Intro2.setImage(ruleIntro2Image);
        Intro2.setFitWidth(FITTED + 300);
        Intro2.setFitHeight(FITTED + 100);//set the size of ruleIntro2 Scene
        Intro2.setX(15);
        Intro2.setY(20);//set the location of openScene
        root.getChildren().add(Intro2);

        Button back2 = new Button("BACK");//create BACK button direct to the previous introduction page
        back2.setPrefSize(215, 70);
        back2.setLayoutX(BOARD_WIDTH / 2 - 385);
        back2.setLayoutY(BOARD_HEIGHT - 110);
        back2.setOpacity(0.001);

        Button next2 = new Button("NEXT");//create NEXT button direct to the next introduction page
        next2.setPrefSize(215, 70);
        next2.setLayoutX(BOARD_WIDTH / 2 + 160);
        next2.setLayoutY(BOARD_HEIGHT - 110);
        next2.setOpacity(0.001);

        root.getChildren().addAll(back2, next2);
        back2.setOnAction(event -> {
            click();
            ruleIntro1();
        });
        next2.setOnAction(event -> {
            click();
            ruleIntro3();
        });
    }

    /**
     * Create the third rule introduction page.
     */
    //author: Weiwei Liu
    static void ruleIntro3() {
        ImageView Intro3 = new ImageView();
        Image ruleIntro3Image = new Image(Viewer.class.getResource(URI_BASE) + "Background2_3.png");
        Intro3.setImage(ruleIntro3Image);
        Intro3.setFitWidth(FITTED + 300);
        Intro3.setFitHeight(FITTED + 100);//set the size of ruleIntro3 Scene
        Intro3.setX(15);
        Intro3.setY(20);
        root.getChildren().add(Intro3);

        Button back3 = new Button("BACK");//create BACK button direct to the previous introduction page
        back3.setPrefSize(215, 70);
        back3.setLayoutX(BOARD_WIDTH / 2 - 385);
        back3.setLayoutY(BOARD_HEIGHT - 110);
        back3.setOpacity(0.001);
        Button start3 = new Button("START");//create Start button direct to the game setting page
        start3.setPrefSize(215, 70);
        start3.setLayoutX(BOARD_WIDTH / 2 + 160);
        start3.setLayoutY(BOARD_HEIGHT - 110);
        start3.setOpacity(0.001);
        root.getChildren().addAll(back3, start3);
        back3.setOnAction(event -> {
            click();
            ruleIntro2();
        });
        start3.setOnAction(event -> {
            click();
            playGame();
        });
    }
}
