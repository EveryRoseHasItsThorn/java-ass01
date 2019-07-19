-- Assignment completed by
-- Name    :Wenbo Du
-- UID     :u6361796
-- Tutor   :Debashish Chakraborty
-- Lab Time: Monday 12-2PM

module Bot.Blue where
import Data.Player
import Data.Board
import Data.List
-- different case here indicate how many move the player look ahead. redTurn=Ture and otherwise are used for red player and blue player
makeMove :: Board -> LookAhead -> Int
makeMove x y=case y of
  1
    |redTurn x ->finalMove (decision score1 x) (controlCenter x)
    |otherwise -> finalMove (decision' score1' x) (controlCenter x)
  2
    |redTurn x ->finalMove (decision lay2 x) (controlCenter x)
    |otherwise ->finalMove (decision' lay2' x) (controlCenter x)
  3
    |redTurn x ->finalMove (decision lay3 x) (controlCenter x)
    |otherwise  ->finalMove (decision' lay3' x) (controlCenter x)
  4
    |redTurn x->finalMove (decision lay4 x) (controlCenter x)
    |otherwise -> finalMove (decision' lay4' x) (controlCenter x)
  5
    |redTurn x ->finalMove (decision lay5 x) (controlCenter x)
    |otherwise -> finalMove (decision' lay5' x) (controlCenter x)
  6
    |redTurn x ->finalMove (decision lay6 x) (controlCenter x)
    |otherwise -> finalMove (decision' lay6' x) (controlCenter x)
  _
    |redTurn x ->finalMove (decision lay7 x)(controlCenter x)
    |otherwise -> finalMove (decision' lay7' x) (controlCenter x)

-- >>>makeMove (initialiseBoard (5,6) RedBot 5) 4
-- 3
-- >>>makeMove (updateBoard (initialiseBoard (7,8) RedBot 5) 5) 1
-- 4

--decide which columns the player should go to maximum their possibility to won.
finalMove::[(Int,Int)] ->Int->Int
finalMove x y
 |length x==1 = fst $ x !! 0
 |fst(head x)==y =y
 |fst(head x)<y&&fst((tail x)!!0)<y=finalMove (tail x) y
 |fst(head x)>=y&&fst((tail x)!!0)>y=fst $head x
 |otherwise  =nearCenter  (fst(head x)) (fst(head(tail x))) y
                where nearCenter::Int->Int->Int->Int
                      nearCenter a b z
                         |abs(z-b)>abs(z-a) =a
                         |otherwise = b
-- >>> finalMove [(1,2),(5,6)] 4
-- 5

-- >>> finalMove [(1,2),(2,6),(7,8)] 6
-- 7

--indicate where the player can player their discs.( include invalid move within the board)
boardRange::Board->[Int]
boardRange x=[1..(getColumn x)]


-- >>>boardRange (initialiseBoard (5,6) RedBot 5)
-- [1,2,3,4,5]
-- >>>boardRange (initialiseBoard (7,8) BlueBot 5)
-- [1,2,3,4,5,6,7]

-- get how many column the board have.
getColumn::Board->Int
getColumn Board{dimension=n}=fst n
-- >>>getColumn (initialiseBoard (3,4) RedBot 5)
-- 3
-- >>>getColumn (initialiseBoard (9,10) BlueBot 5)
-- 9

--find the column that on the center of board.
controlCenter::Board->Int
controlCenter x=(getColumn x) `div`2 +1
-- >>>controlCenter (initialiseBoard (11,12) RedBot 5)
-- 6
-- >>>controlCenter (initialiseBoard (7,8) RedBot 5)
-- 4

--find the valid move in the case when some column has full of discs.
allValidMove::Board->[Int]
allValidMove x =map (+1) $findIndices (==True) (isValid x)
    where isValid::Board->[Bool]
          isValid i =map (validMove i)  $boardRange i
-- >>>allValidMove (updateBoard(initialiseBoard (11,1) RedBot 5)6)
-- [1,2,3,4,5,7,8,9,10,11]

-- >>>decision score1 (initialiseBoard (11,12) RedBot 5)
-- [(1,0),(2,0),(3,0),(4,0),(5,0),(6,0),(7,0),(8,0),(9,0),(10,0),(11,0)]

--to maximum the possibility to win, get which columns the player can place their discs(the same as decision' just for different player).

decision::(Board->[Int])->Board->[(Int,Int)]
decision f x = filter  ((==minimum (f x)).snd) $zip (allValidMove x)$f x

-- >>>decision score1 (initialiseBoard (11,12) BlueBot 5)
-- [(1,0),(2,0),(3,0),(4,0),(5,0),(6,0),(7,0),(8,0),(9,0),(10,0),(11,0)]

decision'::(Board->[Int])->Board->[(Int,Int)]
decision' f x = filter  ((==maximum (f x)).snd) $zip (allValidMove x)$f x

-- >>>redTurn (initialiseBoard (11,12) BlueBot 5)
-- False
-- >>>redTurn (initialiseBoard (11,12) RedBot 5)
-- True

-- determine whether the turn is for redBot
redTurn:: Board->Bool
redTurn   Board{turn=n}=n==RedBot

--generate possible board in next one moves.
newBoard1:: Board->[Board]
newBoard1  x
   |all (==x) [updateBoard x t|t<-(boardRange x),updateBoard x t/=x ]=[x]
   |otherwise=[updateBoard x t|t<-(boardRange x),updateBoard x t/=x ]

--generate possible board in next two moves.
newBoard2::Board->[[Board ]]
newBoard2 x = map newBoard1 $newBoard1 x
-- nearly the same as above just one more move.
newBoard3::Board->[[[Board ]]]
newBoard3 x=map (map newBoard1) $newBoard2 x

newBoard4::Board->[[[[Board]]]]
newBoard4 x =map (map (map newBoard1)) $newBoard3 x

newBoard5::Board->[[[[[Board]]]]]
newBoard5 x= map (map (map (map newBoard1))) $newBoard4 x

newBoard6::Board->[[[[[[Board]]]]]]
newBoard6 x= map (map (map (map (map newBoard1)))) $newBoard5 x

newBoard7::Board->[[[[[[[Board]]]]]]]
newBoard7 x= map (map (map (map (map (map newBoard1))))) $newBoard6 x

-- >>>minusScore (initialiseBoard (11,12) BlueBot 1)
-- 0
-- >>>minusScore (updateBoard(initialiseBoard (11,12) BlueBot 1) 6)
-- 16

-- get the gap between blue bot and red bot.
minusScore::Board->Int
minusScore  Board{blueScore=x,redScore=y}=x-y

-- >>>score1 (initialiseBoard (11,12) BlueBot 1)
--[16,16,16,16,16,16,16,16,16,16,16]

--obtain the possible score for look ahead one move.
score1::Board ->[Int]
score1 x =map minusScore $newBoard1 x

--obtain the possible score for look ahead two move.
-- >>>score2 (initialiseBoard (3,2) BlueBot 2)
--[[0,0,0],[0,0,0],[0,0,0]]

score2::Board->[[Int]]
score2 x =map (map minusScore) $newBoard2 x

-- the same as above just one more move.

score3::Board->[[[Int]]]
score3 x=map (map (map minusScore)) $newBoard3 x

score4::Board->[[[[Int]]]]
score4 x=map (map (map (map minusScore))) $newBoard4 x

score5::Board->[[[[[Int]]]]]
score5 x=map (map (map (map (map minusScore)))) $newBoard5 x

score6::Board->[[[[[[Int]]]]]]
score6 x=map (map (map (map (map (map minusScore))))) $newBoard6 x

score7::Board->[[[[[[[Int]]]]]]]
score7 x=map (map (map (map (map (map (map minusScore)))))) $newBoard7 x


-- >>>lay2 (initialiseBoard (3,2) BlueBot 2)
-- [0,0,0]

--when look ahead two move, transfer the layer2(when the player have to make decision.
lay2::Board->[Int]
lay2 x =  map maximum $score2 x
-- >>>lay3 (initialiseBoard (3,2) BlueBot 2)
-- [0,8,0]

--when look ahead three move, transfer the layer2(when the player have to make decision.
lay3::Board->[Int]
lay3 x=map minimum $map maximum $score3 x


-- >>>lay4 (initialiseBoard (3,2) BlueBot 2)
-- [0,20,0]

--the same as above, just one more move.
lay4::Board->[Int]
lay4 x=map maximum $map minimum $map maximum $score4 x

-- >>>lay5 (initialiseBoard (3,2) BlueBot 2)
-- [16,20,12]

lay5::Board->[Int]
lay5 x=map minimum $map maximum $map minimum $map maximum $score5 x

-- >>>lay6 (initialiseBoard (3,2) BlueBot 2)
-- [20,20,20]

lay6::Board->[Int]
lay6 x=map maximum $map minimum $map maximum $map minimum $map maximum $score6 x

-- >>>lay7 (initialiseBoard (3,2) BlueBot 2)
-- [20,20,20]

lay7::Board->[Int]
lay7 x=map minimum $map maximum $map minimum $map maximum $map minimum $map maximum $score7 x
--the following are for blue bot.
score1':: Board->[Int]
score1' x=  [(-t) | t<-(score1 x)]

lay2'::Board->[Int]
lay2' x =  map minimum $score2 x

-- >>>lay2' (initialiseBoard (3,2) BlueBot 2)
-- [0,0,0]

lay3'::Board->[Int]
lay3' x=map maximum $map minimum $score3 x

-- >>>lay3' (initialiseBoard (3,2) BlueBot 2)
-- [8,8,8]

lay4'::Board->[Int]
lay4' x=map minimum $map maximum $map minimum $score4 x

-- >>>lay4' (initialiseBoard (3,2) BlueBot 2)
-- [20,20,20]

lay5'::Board->[Int]
lay5' x=map maximum $map minimum $map maximum $map minimum $score5 x

-- >>>lay5' (initialiseBoard (3,2) BlueBot 2)
-- [20,20,20]

lay6'::Board->[Int]
lay6' x=map minimum $map maximum $map minimum $map maximum $map minimum $score6 x

-- >>>lay6' (initialiseBoard (3,2) BlueBot 4)
-- [0,0,0]

lay7'::Board->[Int]
lay7' x=map maximum $map minimum $map maximum $map minimum $map maximum $map minimum $score7 x

-- >>>lay7' (initialiseBoard (3,2) BlueBot 4)
-- [0,0,0]