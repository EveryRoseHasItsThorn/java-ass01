package comp1110.ass1.gui;

import comp1110.ass1.IQPuzzlerPro;
import comp1110.ass1.Piece;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * This is a JavaFX application that gives a graphical user interface (GUI) to the
 * simple IQPuzzlerPro game.
 * <p>
 * The tasks set for assignment one do NOT require you to refer to this class, so...
 * <p>
 * YOU MAY IGNORE THE CODE HERE ENTIRELY
 * <p>
 * ...while you do assignment one.
 * <p>
 * However, the class serves as a working example of a number of JavaFX concepts
 * that you may need later in the semester, so you may find this code helpful
 * later in the semester.
 * <p>
 * Among other things, the class demonstrates:
 * - Using inner classes that subclass standard JavaFX classes such as ImageView
 * - Using JavaFX groups to control properties such as visibility and lifetime of
 * a collection of objects
 * - Using opacity/transparency
 * - Using mouse events to implement a draggable object
 * - Making dropped objects snap to legal destinations
 * - Using a clickable button with an associated event
 * - Using a slider for user-input
 * - Using keyboard events to implement toggles controlled by the player
 * - Using bitmap images (public domain, CC0)
 * - Using an mp3 audio track (public domain, CC0)
 * - Using IllegalArgumentExceptions to check for and flag errors
 */
public class Game extends Application {
    /* board layout */
    private static final int SQUARE_SIZE = 40;
    private static final int MARGIN_X = SQUARE_SIZE;
    private static final int BOARD_X = SQUARE_SIZE * 7;
    private static final int MARGIN_Y = SQUARE_SIZE;
    private static final int BOARD_Y = MARGIN_Y;
    private static final int BOARD_WIDTH = IQPuzzlerPro.COLS * SQUARE_SIZE;
    private static final int GAME_WIDTH = 2 * BOARD_X + BOARD_WIDTH;
    private static final int BOARD_HEIGHT = (IQPuzzlerPro.ROWS * SQUARE_SIZE);
    private static final int PIECE_AREA_HEIGHT = 2 * Piece.MAX_PIECE_WIDTH * SQUARE_SIZE;
    private static final int GAME_HEIGHT = 2 * MARGIN_Y + BOARD_HEIGHT + PIECE_AREA_HEIGHT;

    /* color the underlying board */
    private static final Paint SUBBOARD_FILL = Color.HONEYDEW;
    private static final Paint SUBBOARD_STROKE = Color.GREY;

    /* where to find media assets */
    private static final String URI_BASE = "assets/";

    /* Loop in public domain CC 0 http://www.freesound.org/people/oceanictrancer/sounds/211684/ */
    private static final String LOOP_URI = Game.class.getResource(URI_BASE + "211684__oceanictrancer__classic-house-loop-128-bpm.wav").toString();
    public static final int PIECES_PER_HOME_ROW = 6;
    private AudioClip loop;

    /* game variables */
    private boolean loopPlaying = false;

    /* node groups */
    private final Group root = new Group();
    private final Group solution = new Group();
    private final Group board = new Group();
    private final Group controls = new Group();
    private final Group pieces = new Group();

    /* the difficulty slider */
    private final Slider difficulty = new Slider();

    /* message on completion */
    private final Text completionText = new Text("Well done!");

    /* the underlying IQPuzzlerPro game */
    IQPuzzlerPro iqPuzzlerPro;

    String[] piecePlacements = new String[Piece.values().length];   //  all off screen to begin with

    class FXPiece extends ImageView {
        final Piece piece;
        int col;
        int row;
        int rotation;
        Rotate rotate;


        FXPiece(char id) {
            if (!(id >= 'A' && id <= 'L')) {
                throw new IllegalArgumentException("Bad piece id: '" + id + "'");
            }
            piece = Piece.valueOf(String.valueOf(id));
            String filename = String.valueOf(id);
            setImage(new Image(Game.class.getResource(URI_BASE + filename + ".png").toString()));
            setFitWidth(SQUARE_SIZE * piece.getColumnExtent());
            setFitHeight(SQUARE_SIZE * piece.getRowExtent());

            rotate = new Rotate(); // Pivot X Top-Left corner
            rotate.setPivotX(0);
            rotate.setPivotY(0);
            getTransforms().add(rotate);
        }

        /**
         * Construct a piece at a particular place on the
         * board at a given orientation.
         *
         * @param placementString a four-character piece placement string
         */
        FXPiece(String placementString) {
            this(placementString.charAt(0));
            char colChar = placementString.charAt(1);
            col = colChar - 'A';
            char rowChar = placementString.charAt(2);
            row = rowChar - 'A';
            char rotationChar = placementString.charAt(3);
            rotation = rotationChar - 'A';
            updateRotation();
            snapToGrid();
        }

        protected void updateRotation() {
            rotate.setAngle((rotation % 4) * 90);

            // flip patch
            if (rotation / 4 == 1) {
                setScaleX(-1);
                rotate.setAngle(-rotate.getAngle());
            } else {
                setScaleX(1);
            }

            switch (rotation) {
                case 0:
                case 4:
                    setTranslateX(0);
                    setTranslateY(0);
                    break;
                case 1:
                    setTranslateX(getFitHeight());
                    setTranslateY(0);
                    break;
                case 5:
                    setTranslateX(getFitHeight() - getFitWidth());
                    setTranslateY(getFitWidth());
                    break;
                case 2:
                    setTranslateX(getFitWidth());
                    setTranslateY(getFitHeight());
                    break;
                case 6:
                    setTranslateX(-getFitWidth());
                    setTranslateY(getFitHeight());
                    break;
                case 3:
                    setTranslateX(0);
                    setTranslateY(getFitWidth());
                    break;
                case 7:
                    setTranslateX(-getFitWidth());
                    setTranslateY(0);
                    break;
                default:
                    setTranslateX(0);
                    setTranslateY(0);
            }
        }

        /**
         * Snap the piece to the nearest grid position (if it is over the grid)
         */
        protected void snapToGrid() {
            setLayoutX(BOARD_X + SQUARE_SIZE * col);
            setLayoutY(BOARD_Y + SQUARE_SIZE * row);
        }
    }

    /**
     * This class extends FXPatch with the capacity for it to be dragged and dropped,
     * and snap-to-grid.
     */
    class DraggableFXPiece extends FXPiece {
        double homeX, homeY;         // the position in the window where the piece should be when not on the board
        double mouseX, mouseY;      // the last known mouse positions (used when dragging)

        /**
         * Construct a draggable piece
         *
         * @param id The piece identifier ('A' - 'L')
         */
        DraggableFXPiece(char id) {
            super(id);

            int index = id - 'A';
            int homeCol = (index % PIECES_PER_HOME_ROW);
            this.homeX = homeCol * SQUARE_SIZE * (Piece.MAX_PIECE_WIDTH + 0.1) + 0.1 * SQUARE_SIZE;
            int homeRow = index / PIECES_PER_HOME_ROW;
            this.homeY = BOARD_HEIGHT + MARGIN_Y + SQUARE_SIZE * 0.5 + Piece.MAX_PIECE_WIDTH * SQUARE_SIZE * homeRow;

            setLayoutX(homeX);
            setLayoutY(homeY);

            /* event handlers */
            setOnScroll(event -> {            // scroll to change orientation
                rotate();
                if (onBoard()) {
                    setPosition();
                    String placementString = getPlacementString();
                    if (iqPuzzlerPro.isValidPlacement(placementString)) {
                        // place piece
                        snapToGrid();
                    } else {
                        piecePlacements[piece.ordinal()] = IQPuzzlerPro.NOT_PLACED;
                        snapToHome();
                    }
                } else {
                    snapToHome();
                }
                event.consume();
            });

            setOnMousePressed(event -> {      // mouse press indicates begin of drag
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });

            setOnMouseDragged(event -> {      // mouse is being dragged
                setOpacity(0.5);
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });

            setOnMouseReleased(event -> {     // drag is complete
                if (onBoard()) {
                    setPosition();
                    String placementString = iqPuzzlerPro.fixOrientationsProperly(getPlacementString());
                    if (iqPuzzlerPro.isValidPlacement(placementString)) {
                        // place piece
                        snapToGrid();
                        if (placementString.equals(iqPuzzlerPro.getSolution())) {
                            showCompletion();
                        }
                    } else {
                        piecePlacements[piece.ordinal()] = IQPuzzlerPro.NOT_PLACED;
                        snapToHome();
                    }
                } else {
                    snapToHome();
                }
                setOpacity(1.0);
            });
        }

        /**
         * @return true if the piece is on the board
         */
        private boolean onBoard() {
            return getLayoutX() > (BOARD_X - SQUARE_SIZE) && (getLayoutX() < (BOARD_X + BOARD_WIDTH))
                    && getLayoutY() > (BOARD_Y - SQUARE_SIZE) && (getLayoutY() < (BOARD_Y + BOARD_HEIGHT));
        }

        /**
         * Snap the piece to its home position (if it is not on the grid)
         */
        private void snapToHome() {
            setLayoutX(homeX);
            setLayoutY(homeY);
            setRotate(0);
            piecePlacements[piece.ordinal()] = IQPuzzlerPro.NOT_PLACED;
            setOpacity(1.0);
        }

        /**
         * Rotate the piece by 90 degree. After a full circle, flip the piece
         * horizontally, unless there is a constraint on flipping (pieces A, B, C).
         */
        private void rotate() {
            rotation = (rotation + 1) % 8;
            updateRotation();
        }

        /**
         * Determine the grid-position of the origin of the piece (0 .. 12)
         * or -1 if it is off the grid, taking into account its rotation.
         */
        private void setPosition() {
            col = (int) (getLayoutX() + 0.5 * SQUARE_SIZE - BOARD_X) / SQUARE_SIZE;
            row = (int) (getLayoutY() + 0.5 * SQUARE_SIZE - BOARD_Y) / SQUARE_SIZE;
            piecePlacements[piece.ordinal()] = String.valueOf(new char[]{
                    piece.getId(),
                    (char) ('A' + col),
                    (char) ('A' + row),
                    (char) ('A' + rotation)});
        }


        /**
         * @return the piece placement represented as a string
         */
        public String toString() {
            return "" + piecePlacements[piece.ordinal()];
        }
    }

    private String getPlacementString() {
        StringBuilder sb = new StringBuilder();
        for (String piecePlacementString : piecePlacements) {
            sb.append(piecePlacementString);
        }
        return sb.toString();
    }


    /**
     * Set up event handlers for the main game
     *
     * @param scene The Scene used by the game.
     */
    private void setUpHandlers(Scene scene) {
        /* create handlers for key press and release events */
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.M) {
                toggleSoundLoop();
                event.consume();
            } else if (event.getCode() == KeyCode.Q) {
                Platform.exit();
                event.consume();
            } else if (event.getCode() == KeyCode.SLASH) {
                solution.setOpacity(1.0);
                event.consume();
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.SLASH) {
                solution.setOpacity(0);
                event.consume();
            }
        });
    }


    /**
     * Set up the sound loop (to play when the 'M' key is pressed)
     */
    private void setUpSoundLoop() {
        try {
            loop = new AudioClip(LOOP_URI);
            loop.setCycleCount(AudioClip.INDEFINITE);
        } catch (Exception e) {
            System.err.println(":-( something bad happened (" + LOOP_URI + "): " + e);
        }
    }


    /**
     * Turn the sound loop on or off
     */
    private void toggleSoundLoop() {
        if (loopPlaying)
            loop.stop();
        else
            loop.play();
        loopPlaying = !loopPlaying;
    }


    /**
     * Set up the group that represents the solution (and make it transparent)
     *
     * @param solution The solution string.
     */
    private void makeSolution(String solution) {
        this.solution.getChildren().clear();
        if (solution == null) return;

        if (solution.length() != Piece.values().length * 4) {
            throw new IllegalArgumentException("Solution incorrect length: " + solution);
        }
        for (int i = 0; i < solution.length(); i += 4) {
            this.solution.getChildren().add(new FXPiece(solution.substring(i, i + 4)));
        }
        this.solution.setOpacity(0);
    }


    /**
     * Set up the group that represents the spaces that make the board
     */
    private void makeBoard() {
        board.setLayoutX(BOARD_X);
        board.setLayoutY(BOARD_Y);
        board.getChildren().clear();
        for (int i = 0; i < IQPuzzlerPro.ROWS; i++) {
            for (int j = 0; j < IQPuzzlerPro.COLS; j++) {
                Rectangle r = new Rectangle((j * SQUARE_SIZE), (i * SQUARE_SIZE), SQUARE_SIZE, SQUARE_SIZE);
                r.setFill(SUBBOARD_FILL);
                r.setStroke(SUBBOARD_STROKE);
                board.getChildren().add(r);
            }
        }
        board.toBack();
    }


    /**
     * Set up each of the twelve pieces
     */
    private void makePieces(String objective) {
        pieces.getChildren().clear();
        for (Piece piece : Piece.values()) {
            if (iqPuzzlerPro.isPieceFixed(piece.getId())) {
                int offset = 0;
                while (objective.charAt(offset) != piece.getId()) offset += 4;
                String placement = objective.substring(offset, offset + 4);
                piecePlacements[piece.ordinal()] = placement;
                pieces.getChildren().add(new FXPiece(placement));
            } else {
                pieces.getChildren().add(new DraggableFXPiece(piece.getId()));
                piecePlacements[piece.ordinal()] = IQPuzzlerPro.NOT_PLACED;
            }
        }
    }

    /**
     * Put all of the pieces back in their home position
     */
    private void resetPieces() {
        pieces.toFront();
        for (Node n : pieces.getChildren()) {
            if (n instanceof DraggableFXPiece) {
                ((DraggableFXPiece) n).snapToHome();
            }
        }
    }


    /**
     * Create the controls that allow the game to be restarted and the difficulty
     * level set.
     */
    private void makeControls() {
        Button button = new Button("Restart");
        button.setLayoutX(GAME_WIDTH / 2 + 70);
        button.setLayoutY(GAME_HEIGHT - 45);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                newGame();
            }
        });
        controls.getChildren().add(button);

        difficulty.setMin(0);
        difficulty.setMax(10);
        difficulty.setValue(1);
        difficulty.setShowTickLabels(true);
        difficulty.setShowTickMarks(true);
        difficulty.setMajorTickUnit(5);
        difficulty.setMinorTickCount(1);
        difficulty.setSnapToTicks(true);

        difficulty.setLayoutX(GAME_WIDTH / 2 - 80);
        difficulty.setLayoutY(GAME_HEIGHT - 40);
        controls.getChildren().add(difficulty);

        final Label difficultyCaption = new Label("Difficulty:");
        difficultyCaption.setTextFill(Color.GREY);
        difficultyCaption.setLayoutX(GAME_WIDTH / 2 - 150);
        difficultyCaption.setLayoutY(GAME_HEIGHT - 40);
        controls.getChildren().add(difficultyCaption);
    }


    /**
     * Create the message to be displayed when the player completes the puzzle.
     */
    private void makeCompletion() {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(4.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        completionText.setFill(Color.WHITE);
        completionText.setEffect(ds);
        completionText.setCache(true);
        completionText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 80));
        completionText.setLayoutX(GAME_WIDTH / 2 - 200);
        completionText.setLayoutY(275);
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }


    /**
     * Show the completion message
     */
    private void showCompletion() {
        completionText.toFront();
        completionText.setOpacity(1);
    }


    /**
     * IQPuzzlerPro the completion message
     */
    private void hideCompletion() {
        completionText.toBack();
        completionText.setOpacity(0);
    }


    /**
     * Start a new game, resetting everything as necessary
     */
    private void newGame() {
        try {
            hideCompletion();
            iqPuzzlerPro = new IQPuzzlerPro(difficulty.getValue());
            makePieces(iqPuzzlerPro.getObjective());
            makeSolution(iqPuzzlerPro.getSolution());
        } catch (IllegalArgumentException e) {
            System.err.println("Uh oh. " + e);
            e.printStackTrace();
            Platform.exit();
        }
        resetPieces();
    }


    /**
     * The entry point for JavaFX.  This method gets called when JavaFX starts
     * The key setup is all done by this method.
     *
     * @param primaryStage The stage (window) in which the game occurs.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("IQPuzzlerPro");
        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);
        root.getChildren().add(pieces);
        root.getChildren().add(board);
        root.getChildren().add(solution);
        root.getChildren().add(controls);

        setUpHandlers(scene);
        setUpSoundLoop();
        makeBoard();
        makeControls();
        makeCompletion();

        newGame();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
