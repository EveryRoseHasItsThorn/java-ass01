-- Code based on framework designed by Steven X. Han from
-- the Australian National University, who has granted
-- permission for the usage of their work by the cohort
-- enrolled in the course COMP1100 in 2017 Semester 2 for
-- education purposes only. No commercial usage is allowed
-- without the explicit permission from the original author.
--
-- Assignment completed by:
-- Name    :Wenbo Du
-- UID     :u6361796
-- Tutor   :Debashish Chakraborty
-- Lab Time: Monday 12:00-14:00pm

module Battleship where

import Data.List
import Prelude hiding (Left, Right)

type Board = Matrix Cell
type Ships = Matrix Bool

-- showBoard prepares a nice string for printing onto Terminal.
showBoard :: Board -> String
showBoard cells = "+" ++ replicate 21 '-' ++ "+\n"
                    ++ intercalate "\n" (map rowsToGrid cells)
                    ++ "\n+" ++ replicate 21 '-' ++ "+"
    where
        rowsToGrid :: Row Cell -> String
        rowsToGrid row = "| " ++ intercalate " " (map cStateToCell row) ++ " |"

        cStateToCell :: Cell -> String
        cStateToCell x = case x of
           Unchecked ->" "
           Hit->"o"
           Miss->"x"



data Cell = Unchecked | Hit | Miss
    deriving (Show, Read,Eq)
type Matrix a  = [Row a]
type Row a     = [a]

data Condition = Won | Lost | Playing
    deriving (Show)

data Direction  = Up | Down | Left | Right
    deriving (Show, Read)
type Coordinate = (XCoord, YCoord)
type XCoord     = Coord
type YCoord     = Coord
type Coord      = Integer

data ShipType = Carrier | Battleship | Submarine | Cruiser | Destroyer
    deriving (Show, Read, Eq)

data State = State {board        :: Board,
                    ships        :: Ships,
                    condition    :: Condition,
                    numMoves     :: Integer}
                    deriving (Show)

type ShipsOnGrid = [ShipType]
data GenShips = GenShips {gsShips       :: Ships,
                          existingShips :: ShipsOnGrid,
                          finished      :: Bool}
                          deriving (Show)


-- updateList replaces an element by index in a given list.
updateList :: [a] -> Int -> a -> [a]
updateList list n x = take (n) list ++ [x] ++ drop (n + 1) list

--validPlacement checks if given inputs satisfy with the rules of ship placement.


validPlacement :: GenShips -> Coordinate -> Direction -> ShipType -> Bool
validPlacement gs c d s =
                    not (s `elem` (existingShips gs))
                && (all coordInBound onCoords)
                && (not $ any (\x -> isShipAtCoord x (gsShips gs)) $
                         filter coordInBound $ nub $ concatMap getNeighbours onCoords)
    where
        onCoords = getCoords c d (shipLength s)

--shipLength informs the length of each ship.

-- | shipLength
--
-- >>> shipLength Carrier
-- 5
--
-- >>> shipLength Cruiser
-- 3


shipLength :: ShipType -> Integer
shipLength x = case x of
    Carrier->5
    Battleship->4
    Submarine->3
    Cruiser->3
    Destroyer->2



-- getCoords returns the list of Coordinate which would be
-- occupied by the ship.

--  | getCoords
--
-- >>> getCoords  (5,6) Left 2
--[(5,6),(4,6)]
-- >>> getCoords  (3,3) Up 3
--[(3,3),(3,2),(3,1)]


getCoords :: Coordinate -> Direction -> Integer -> [Coordinate]
getCoords (i, j)  dir l = case dir of
    Down  -> map (\x -> (i, x)) [j .. (j + l - 1)]
    Right -> map (\x -> (x, j)) [i .. (i + l - 1)]
    Up    -> map (\x -> (i, x)) [j, (j - 1) .. (j - l + 1)]
    Left  -> map (\x -> (x, j)) [i, (i - 1) .. (i - l + 1)]





-- getNeighbours returns a 9-element list containing
-- coordinates around the given coordinate and itself.

-- | getNeighbours
--
-- >>> getNeighbours (5,5)
--[(4,4),(4,5),(4,6),(5,4),(5,5),(5,6),(6,4),(6,5),(6,6)]
--
-- >>> getNeighbours (3,8)
-- [(2,7),(2,8),(2,9),(3,7),(3,8),(3,9),(4,7),(4,8),(4,9)]


getNeighbours :: Coordinate -> [Coordinate]
getNeighbours (x, y) = [(i, j) | i <- [(x - 1) .. (x + 1)], j <- [(y - 1) .. (y + 1)]]

--coordInBound checks if a coordinate is in the given Metrix

-- |coordInBound
--
-- >>> coordInBound (1,1)
-- True
--
-- >>> coordInBound (10,11)
-- False
coordInBound :: Coordinate -> Bool
coordInBound (x,y)
  |x<=9 && x>=0 ,y<=9 && y>=0 =True
  |otherwise  =False

-- isShipAtCoord determines if there is a ship already placed
-- at the coord.

-- |isShipAtCoord
--
-- >>> isShipAtCoord (1,2) [[False, False],[False, True], [False, True]]
-- True
--
-- >>> isShipAtCoord (2,1) [[False, False,False],[False, True,False], [False, True,False],[False, False,False]]
-- False

isShipAtCoord :: Coordinate -> Ships -> Bool
isShipAtCoord (x, y) grid = grid !! (fromIntegral y) !! (fromIntegral x)

--placeShip will select the valid placement of a ship from the random generation of ship type, coordinate, direction
-- and output matrix that contains 5 ships and each of them satisfy all the rules of game.

-- |placeShip
--
-- >>> placeShip GenShips{gsShips = [[False,False,False,False,False],[False,False,False,False,False],[False,False,False,False,False],[False,False,False,False,False]],existingShips=[],finished=False} (1,2) Up Destroyer
-- GenShips {gsShips = [[False,False,False,False,False],[False,True,False,False,False],[False,True,False,False,False],[False,False,False,False,False]], existingShips = [Destroyer], finished = False}
--
-- >>> placeShip GenShips{gsShips = [[False,False,False,False,False],[False,False,False,False,False],[False,False,False,False,False],[False,False,False,False,False],[False,False,False,False,False],[False,False,False,False,False]],existingShips=[],finished=False} (0,3) Right Submarine
-- GenShips {gsShips = [[False,False,False,False,False],[False,False,False,False,False],[False,False,False,False,False],[True,True,True,False,False],[False,False,False,False,False],[False,False,False,False,False]], existingShips = [Submarine], finished = False}


placeShip::GenShips -> Coordinate  -> Direction ->ShipType -> GenShips
placeShip s (y, x) dir l
      |validPlacement s (y,x) dir l ==True =GenShips{gsShips=updateMatrix (gsShips s) ( y,x ) dir l,existingShips=updateShipTypes  (existingShips s) l,finished=checkFinished (updateShipTypes (existingShips s) l)}
      |otherwise = s
      
      
--updateMatrix uses the pattern "take m a ++ [take (n) (a!! m) ++ [x] ++ drop (n + 1) (a !! m)] ++ drop (m+ 1) a" to place a ship with its type,
--coordinate, direction onto the matrix ships.


-- |updateMatrix
--
-- >>>updateMatrix [[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False]] (7,8) Left Submarine
-- [[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,True,True,True,False,False],[False,False,False,False,False,False,False,False,False,False]]
--
-- >>>updateMatrix [[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False]] (5,3)  Right Destroyer
--[[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,True,True,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False],[False,False,False,False,False,False,False,False,False,False]]


updateMatrix::Ships->Coordinate ->Direction -> ShipType ->Ships
updateMatrix a   (n,m) dir l = case dir  of
  Left
   |l==Destroyer-> take (fromIntegral m) a ++  [take (fromIntegral(n)-1) (a !!(fromIntegral m)) ++ [True]++ [True] ++ drop (fromIntegral(n) + 1) (a !! fromIntegral(m))] ++ drop (fromIntegral(m) + 1) a
   |l==Submarine|| l==Cruiser-> take (fromIntegral m) a ++  [take (fromIntegral(n)-2) (a !! (fromIntegral m)) ++ [True]++ [True]++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral m))] ++ drop (fromIntegral(m) + 1) a
   |l==Battleship-> take (fromIntegral m) a ++  [take (fromIntegral(n)-3) (a !! (fromIntegral m)) ++ [True]++ [True]++ [True]++ [True] ++ drop (fromIntegral(n) + 1) (a !! fromIntegral(m))] ++ drop (fromIntegral(m)+ 1) a
   |l==Carrier-> take (fromIntegral m) a ++  [take (fromIntegral(n)-4) (a !! (fromIntegral m)) ++ [True]++ [True]++ [True]++ [True]++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral m))] ++ drop (fromIntegral(m) + 1) a

  Right
   |l==Destroyer-> take (fromIntegral m) a ++  [take (fromIntegral n) (a !! (fromIntegral m)) ++ [True]++ [True] ++ drop ((fromIntegral (n)) + 2) (a !! (fromIntegral(m)))] ++ drop (fromIntegral(m) + 1) a
   |l==Submarine ||l==Cruiser-> take (fromIntegral m) a ++  [take (fromIntegral n) (a !! (fromIntegral m)) ++ [True]++ [True] ++ [True]++ drop ((fromIntegral n) + 3) (a !! (fromIntegral m))] ++ drop (fromIntegral(m) + 1) a
   |l==Battleship-> take (fromIntegral m) a ++  [take (fromIntegral n) (a !! (fromIntegral m)) ++ [True]++ [True] ++ [True]++ [True]++ drop (fromIntegral(n) + 4) (a !! (fromIntegral(m)))] ++ drop (fromIntegral(m) + 1) a
   |l==Carrier-> take (fromIntegral m) a ++  [take (fromIntegral n) (a !! (fromIntegral m)) ++ [True]++ [True] ++ [True]++ [True]++ [True]++ drop (fromIntegral(n) + 5) (a !! (fromIntegral m))] ++ drop (fromIntegral(m) + 1) a

  Up
   |l==Destroyer->take ((fromIntegral m)-1) a ++[take (fromIntegral n) (a !! (fromIntegral (m)-1)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)-1))]++  [take (fromIntegral(n)) (a !! fromIntegral(m)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! fromIntegral(m))] ++ drop (fromIntegral(m) + 1) a
   |l==Submarine||l==Cruiser->take (fromIntegral(m)-2) a ++[take (fromIntegral(n)) (a !! (fromIntegral(m)-2)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)-2))]++[take (fromIntegral(n)) (a !! (fromIntegral(m)-1)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)-1))]++  [take (fromIntegral(n)) (a !! fromIntegral(m)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral m))] ++ drop (fromIntegral(m) + 1) a
   |l==Battleship->take (fromIntegral(m)-3) a++[take (fromIntegral(n)) (a !! (fromIntegral(m)-3)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)-3))]++[take (fromIntegral(n)) (a !! (fromIntegral(m)-2)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)-2))]++[take (fromIntegral n) (a !! (fromIntegral(m)-1)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! ((fromIntegral m)-1))]++[take (fromIntegral n) (a !! (fromIntegral m)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)))]++ drop (fromIntegral(m) + 1) a
   |l==Carrier->take(fromIntegral(m)-4) a ++[take (fromIntegral(n)) (a !! (fromIntegral(m)-4)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)-4))] ++[take (fromIntegral(n)) (a !! (fromIntegral(m)-3)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)-3))]++[take (fromIntegral(n)) (a !! (fromIntegral(m)-2)) ++ [True]++drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)-2))]++[take (fromIntegral(n)) (a !! (fromIntegral(m)-1)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)-1))]++[take (fromIntegral(n)) (a !! (fromIntegral(m))) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)))]++ drop (fromIntegral(m) + 1) a
  Down
   |l==Destroyer->take (fromIntegral (m)) a ++[take (fromIntegral n) (a !! (fromIntegral m)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! fromIntegral(m))]++[take (fromIntegral n) (a !! (fromIntegral(m)+1)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)+1))]++drop(fromIntegral(m)+2) a
   |l==Submarine||l==Cruiser->take (fromIntegral m) a++[take (fromIntegral n) (a !! fromIntegral(m)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! fromIntegral(m))]++[take (fromIntegral n) (a !! (fromIntegral(m)+1)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)+1))]++[take (fromIntegral n) (a !! (fromIntegral(m)+2)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)+2))]++drop(fromIntegral(m)+3) a
   |l==Battleship->take (fromIntegral m) a++[take (fromIntegral n) (a !! (fromIntegral m)) ++ [True] ++ drop (fromIntegral (n) + 1) (a !!fromIntegral( m))]++[take (fromIntegral(n)) (a !! (fromIntegral(m)+1)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)+1))]++[take (fromIntegral n) (a !! (fromIntegral(m)+2)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)+2))]++[take (fromIntegral n) (a !! (fromIntegral(m)+3)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)+3))]++drop(fromIntegral(m)+4) a
   |l==Carrier->take (fromIntegral m) a++[take (fromIntegral n) (a !! (fromIntegral m)) ++ [True] ++ drop ((fromIntegral n) + 1) (a !! (fromIntegral m))]++[take (fromIntegral n) (a !! (fromIntegral(m)+1)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)+1))]++[take (fromIntegral n) (a !! (fromIntegral(m)+2)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)+2))]++[take (fromIntegral n) (a !! (fromIntegral (m)+3)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)+3))]++[take (fromIntegral n) (a !! (fromIntegral(m)+4)) ++ [True] ++ drop (fromIntegral(n) + 1) (a !! (fromIntegral(m)+4))]++drop(fromIntegral(m)+5) a
  _ ->error"this ship can not be placed on the matrix"      


-- updateShipType will filter the random input ships and finally throw out a list that contain all 5 different ships.

-- |updateShipType
--
-- >>> updateShipTypes [Battleship] Battleship
-- [Battleship]
--
-- >>> updateShipTypes [Battleship,Destroyer,Submarine,Carrier] Cruiser
-- [Cruiser,Battleship,Destroyer,Submarine,Carrier]

updateShipTypes::ShipsOnGrid->ShipType->ShipsOnGrid
updateShipTypes x ship= case ship of
   Battleship
       |Battleship `elem` x->x
       |otherwise  ->[Battleship]++x
   Destroyer
       |Destroyer `elem` x->x
       |otherwise ->[Destroyer]++x
   Submarine
       |Submarine `elem` x->x
       |otherwise->[Submarine]++x
   Carrier
       |Carrier `elem` x ->x
       |otherwise ->[Carrier]++x
   Cruiser
       |Cruiser `elem` x ->x
       |otherwise->[Cruiser]++x

--checkFinshed is used to check the length of ShipsOnGrids , if the length of the list is 5, output True.

-- |checkFinished
--
-- >>> checkFinished [Cruiser,Battleship,Destroyer,Submarine,Carrier]
-- True
--
-- >>> checkFinished [Battleship,Destroyer,Submarine]
-- False


checkFinished::ShipsOnGrid->Bool
checkFinished x
   |length x==5  =True
   | otherwise    = False









--transitionState changes the state of matrix board, game condition(Won, Lost, and Playing) and number count with an exact coordinate to progress the game.
--Finally, the game player will lose or win.


-- |transitionState
--
-- >>>transitionState (State [[Unchecked,Unchecked,Unchecked,Unchecked],[Unchecked,Unchecked,Unchecked,Unchecked],[Unchecked,Unchecked,Unchecked,Unchecked]] [[False, True,False, False],[False, True,False, False],[False, False,False, False],[False,False,False, False]] Playing 0) (1,1)
-- State {board = [[Unchecked,Unchecked,Unchecked,Unchecked],[Unchecked,Hit,Unchecked,Unchecked],[Unchecked,Unchecked,Unchecked,Unchecked]], ships = [[False,True,False,False],[False,True,False,False],[False,False,False,False],[False,False,False,False]], condition = Playing, numMoves = 0}
--
-- >>>transitionState (State [[Unchecked,Unchecked,Unchecked,Unchecked],[Unchecked,Miss,Unchecked,Unchecked],[Unchecked,Unchecked,Hit,Unchecked],[Unchecked,Unchecked,Unchecked,Unchecked]] [[False, True,False, False],[False, True,False, False],[False, False,False, False],[False,False,False, False]] Playing 19) (3,2)
-- State {board = [[Unchecked,Unchecked,Unchecked,Unchecked],[Unchecked,Miss,Unchecked,Unchecked],[Unchecked,Unchecked,Hit,Miss],[Unchecked,Unchecked,Unchecked,Unchecked]], ships = [[False,True,False,False],[False,True,False,False],[False,False,False,False],[False,False,False,False]], condition = Playing, numMoves = 20}

transitionState :: State -> Coordinate -> State
transitionState (State grid1  grid2  c num) (y,x) = case (State grid1 grid2 c num) of

    (State a b Won d1 )->(State a b c d1)
    (State a b Lost d1)->(State a b c d1)
    (State a b Playing  20)->(State a b Lost 20)
    (State a b Playing d1)
        |x>9 && x<0 ,y>9 && y<0  ->(State a b c d1)
        |(b!!(fromIntegral x))!!(fromIntegral y)==True && (a!!(fromIntegral x))!!(fromIntegral y)==Unchecked->
                  (State (take (fromIntegral x) a ++[take (fromIntegral y) (a !!(fromIntegral x)) ++ [Hit] ++ (drop (fromIntegral (y + 1)) (a !! (fromIntegral x)))] ++drop (fromIntegral (x + 1)) a) b c d1 )
        |(b!!(fromIntegral x))!!(fromIntegral y)==False&&sum (map length (map (filter (==Hit)) a))<17->(State (take (fromIntegral x) a ++[take (fromIntegral y) (a !! (fromIntegral x)) ++ [Miss] ++ drop (fromIntegral (y + 1)) (a !! (fromIntegral x))] ++drop (fromIntegral (x + 1)) a) b c ((d1)+1) )
        |(a!!(fromIntegral x))!!(fromIntegral y)==Miss&&sum (map length (map (filter (==Hit)) a))<17->(State a b c ((d1)+1) )
        |sum (map length (map (filter (==Hit)) a))==17->(State a b Won (d1))
        |(a!!(fromIntegral x))!!(fromIntegral y)==Hit ->(State a b c ((d1)+1) )
    _ -> error "The game can not be played with this State and Coordinate"

























