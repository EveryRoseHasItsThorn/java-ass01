package comp1110.ass2.gui.assets;

import comp1110.ass2.WarringStatesGame;
import javafx.scene.Node;

import java.util.*;

import static comp1110.ass2.WarringStatesGame.*;
import static comp1110.ass2.gui.Card.*;
import static comp1110.ass2.gui.Viewer.*;

public class Rule {
    public static final String[] PLACEMENTS = {
            "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"
    };

    public int[] determineWin() {
        Map<Character, Integer> countOne = new HashMap<>();
        Map<Character, Integer> countTwo = new HashMap<>();
        Map<Character, Integer> countThree = new HashMap<>();
        Map<Character, Integer> countFour = new HashMap<>();
        for (Character i = 'a'; i < 'h'; i++) {
            countOne.put(i, 0);
            countTwo.put(i, 0);
            countThree.put(i, 0);
            countFour.put(i, 0);
        }
        for (Character i : WarringStatesGame.supporterOne.toCharArray())
            countOne.put(i, countOne.get(i) + 1);
        for (Character i : WarringStatesGame.supporterTwo.toCharArray())
            countTwo.put(i, countTwo.get(i) + 1);
        for (Character i : WarringStatesGame.supporterThree.toCharArray())
            countThree.put(i, countThree.get(i) + 1);
        for (Character i : WarringStatesGame.supporterFour.toCharArray())
            countFour.put(i, countFour.get(i) + 1);
        int[] winner = new int[7];
        winner[0] = getIndex(countOne.get('a'), countTwo.get('a'), countThree.get('a'), countFour.get('a'));
        winner[1] = getIndex(countOne.get('b'), countTwo.get('b'), countThree.get('b'), countFour.get('b'));
        winner[2] = getIndex(countOne.get('c'), countTwo.get('c'), countThree.get('c'), countFour.get('c'));
        winner[3] = getIndex(countOne.get('d'), countTwo.get('d'), countThree.get('d'), countFour.get('d'));
        winner[4] = getIndex(countOne.get('e'), countTwo.get('e'), countThree.get('e'), countFour.get('e'));
        winner[5] = getIndex(countOne.get('f'), countTwo.get('f'), countThree.get('f'), countFour.get('f'));
        winner[6] = getIndex(countOne.get('g'), countTwo.get('g'), countThree.get('g'), countFour.get('g'));
        return winner;
    }

    public int getIndex(int one, int two, int three, int four) {
        Map<Integer, Integer> out = new HashMap<>();
        out.put(1, one);
        out.put(2, two);
        out.put(3, three);
        out.put(4, four);
        int max = Collections.max(out.values());
        if (max == out.get(1))
            return 1;
        else if (max == out.get(2))
            return 2;
        else if (max == out.get(3))
            return 3;
        else
            return 4;
    }

    public static void updateSupporter(char location) {
        char kingdom = WarringStatesGame.objectiveKindom(placement, location);
        if (round % numPlayer == 1) {
            WarringStatesGame.supporterOne = WarringStatesGame.supporterOne + kingdom;
        } else if (round % numPlayer == 2) {
            WarringStatesGame.supporterTwo = WarringStatesGame.supporterTwo + kingdom;
        } else if (round % numPlayer == 3) {
            WarringStatesGame.supporterThree = WarringStatesGame.supporterThree + kingdom;
        } else {
            WarringStatesGame.supporterFour = WarringStatesGame.supporterFour + kingdom;
        }
    }

    public static void arrangeToPlayer(Node nd) {
        playerOne.toFront();
        playerTwo.toFront();
        playerThree.toFront();
        playerFour.toFront();
        if (round % numPlayer == 1) {
//            nd.setLayoutX(50);
//            nd.setLayoutY(50 + marginOne);
//            marginOne += 20;
            /////////////////////////
            nd.setLayoutX(50);
            nd.setLayoutY(50 + marginOne);
            marginOne += 13;
            ////////////////////////
            playerOne.getChildren().add(nd);
        } else if (round % numPlayer == 2) {
//            nd.setLayoutX(800);
//            nd.setLayoutY(50 + marginTwo);
//            marginTwo += 20;
            //////////////////////////
            nd.setLayoutX(800);
            nd.setLayoutY(50 + marginTwo);
            marginTwo += 13;
            /////////////////////////
            playerTwo.getChildren().add(nd);
        } else if (round % numPlayer == 3) {
//            nd.setLayoutX(50);
//            nd.setLayoutY(400 + marginThree);
//            marginThree += 20;
            //////////////////////////
            nd.setLayoutX(50);
            nd.setLayoutY(350 + marginThree);
            marginThree += 13;
            //////////////////////////
            playerThree.getChildren().add(nd);
        } else {
//            nd.setLayoutX(800);
//            nd.setLayoutY(400 + marginFour);
//            marginFour += 20;
            nd.setLayoutX(800);
            nd.setLayoutY(350 + marginFour);
            marginFour += 13;
            playerFour.getChildren().add(nd);
        }
    }

    public static char generatePossibleMove(String placement) {
        Random r = new Random();
        int zhangYiIndex = cardCharToIndex(ZhangYiLocation(placement));
        int outR = '/';
        int outC = '/';
        int zyR = zhangYiIndex / 6;
        int zyC = zhangYiIndex % 6;
        int RorC = r.nextInt(2) + 1;
        if (RorC == 1) {
            outR = zyR;
            do {
                outC = r.nextInt(6);
            } while (outC == zyC);
        } else if (RorC == 2) {
            outC = zyC;
            do {
                outR = r.nextInt(6);
            } while (outR == zyR);
        }
        char out = indexToChar(outR, outC);
        return out;
        // FIXME Task 10: generate a legal move
    }

    public static boolean isGameOver(String placement) {
        if (placement.length() == 3 || placement.isEmpty())
            return true;
        char zhangYi = ZhangYiLocation(placement);
        int zyLocation = cardCharToIndex(zhangYi);
        int zyRow = zyLocation / 6;
        int zyCol = zyLocation % 6;
        List<Character> sameCol = new ArrayList<>();
        List<Character> sameRow = new ArrayList<>();
        for (int i = 0; i < placement.length(); i += 3) {
            char location = placement.charAt(i + 2);
            int index = cardCharToIndex(location);
            int row = index / 6;
            int col = index % 6;
            if (row == zyRow && col != zyCol)
                sameRow.add(location);
            else if (col == zyCol && row != zyRow)
                sameCol.add(location);
        }
        if (sameRow.isEmpty() && sameCol.isEmpty()){
            return true;}
        return false;
    }

    public static List<Character> findBarrier(String placement, char move) {
        String updated = updateBoard(placement, move);
        List<Character> out = new ArrayList<>();
        List<String> origin = new ArrayList<>();
        List<String> after = new ArrayList<>();
        for (int i = 0; i < placement.length(); i += 3)
            origin.add(placement.substring(i, i + 3));
        for (int j = 0; j < updated.length(); j += 3)
            after.add(updated.substring(j, j + 3));
        origin.removeAll(after);
        List<Character> realOut = new ArrayList<>();
        for (String r : origin)
            if (r.charAt(2) != move && r.charAt(2) != ZhangYiLocation(placement))
                realOut.add(r.charAt(2));
        return realOut;
    }

    public static String makeOrder(List<String> unordered) {
        Collections.sort(unordered, (s1, s2) -> {
            String c1 = s1.substring(0, 1);
            String c2 = s2.substring(0, 1);
            return c1.compareTo(c2);
        });
        String out = "";
        List<String> allTemp = new ArrayList<>();
        List<List<String>> complex = new ArrayList<>();
        char h = unordered.get(0).charAt(0);
        for (String k : unordered) {
            if (k.charAt(0) == h) {
                allTemp.add(k);
            } else {
                h = k.charAt(0);
                List<String> copy = new ArrayList<>(allTemp);
                Collections.sort(copy, (s1, s2) -> {
                            String c1 = s1.substring(1);
                            String c2 = s2.substring(1);
                            return c1.compareTo(c2);
                        }
                );
                for (String r : copy) {
                    out += r;
                }
                complex.add(copy);
                allTemp.clear();
                allTemp.add(k);
            }
        }
        Collections.sort(allTemp, (s1, s2) -> {
                    String c1 = s1.substring(1);
                    String c2 = s2.substring(1);
                    return c1.compareTo(c2);
                }
        );
        for (String k : allTemp) {
            out += k;
        }
        return out;
    }
}