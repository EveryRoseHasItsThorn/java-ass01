-- Code based on framework designed by Steven X. Han from
-- the Australian National University, who has granted
-- permission for the usage of their work by the cohort
-- enrolled in the course COMP1100 in 2017 Semester 2 for
-- education purposes only. No commercial usage is allowed
-- without the explicit permission from the original author. 

module Main where

import Prelude hiding (Left, Right)
import Battleship
import System.Random

-- main queries for playing mode.
main :: IO ()
main = do
    putStrLn "Type f for full mode, g to generate a board only, p to play only."
    args <- getLine
    processArgs args
        
-- processArgs switches between game modes.
processArgs :: String -> IO ()
processArgs s
    | s == "f" = genShips (GenShips (replicate 10 (replicate 10 False)) [] False) startGame
    | s == "g" = genShips (GenShips (replicate 10 (replicate 10 False)) [] False) printShipsGrid
    | s == "p" = do
        putStrLn "Input board configuration as a matrix of Bool:"
        bConfig <- getLine
        startGame (read bConfig)
    | otherwise = main
    
printShipsGrid :: Ships -> IO ()
printShipsGrid s = putStrLn $ show s
    
-- startGame initialises the game with all cells as Unchecked
startGame :: Ships -> IO ()
startGame s = game (State (replicate 10 (replicate 10 Unchecked)) s Playing 0)
    
-- genShips generates random information and recursively feed
-- into placeShip function, along with a few safety checks.
genShips :: GenShips -> (Ships -> IO ()) -> IO ()
genShips gs run
    | finished gs                   = run $ gsShips gs
    | length (existingShips gs) > 5 = error "Too many ships on the grid."
    | otherwise                     = do
        x         <- randomRIO (0 :: Integer, 9 :: Integer)
        y         <- randomRIO (0 :: Integer, 9 :: Integer)
        ship      <- randomRIO (1 :: Integer, 5 :: Integer)
        direction <- randomRIO (1 :: Integer, 4 :: Integer)
        let newShips = placeShip gs (x, y) (getDirection direction) (getShip ship)
        if validPlacement gs (x, y) (getDirection direction) (getShip ship)
            && (length (existingShips newShips) - length (existingShips gs)) /= 1
            then error "GenShips failure. ShipsOnBoard didn't increase."
            else genShips newShips run
        
    where
        getShip :: Integer -> ShipType
        getShip x
            | x == 1    = Carrier
            | x == 2    = Battleship
            | x == 3    = Submarine
            | x == 4    = Cruiser
            | x == 5    = Destroyer
            | otherwise = error "getShip Integer out of bound."
    
        getDirection :: Integer -> Direction
        getDirection x
            | x == 1    = Up
            | x == 2    = Down
            | x == 3    = Left
            | x == 4    = Right
            | otherwise = error "getDirection Integer out of bound."

-- game recursively iterates the State to play the game.
game :: State -> IO ()
game s 
    | numMoves s <= 20 = do
        putStrLn $ showBoard (board s)
        putStrLn "Enter the coordinate:"
        coord <- getLine
        game (transitionState s (read coord))
    | otherwise            = error "numMoves is beyond 20. Game should have been lost."