--
-- Code written by Steven X. Han based on the
-- original Kalaha framework by Uwe Zimmer.
--

import Commandline.Options (argsToOptions, winStreak, boardDim)
import Data.Board (initialiseBoard)
import Data.Player (Player (BlueBot, RedBot))
import Transition.Mechanics (playGame)

import System.Environment (getArgs)

main :: IO ()
main = do
    args <- getArgs
    let opts = argsToOptions args
    
    putStrLn ""
    putStrLn "Blue goes first:"
    let blueFirstBoard = initialiseBoard (boardDim opts) BlueBot (winStreak opts)
    putStrLn $ show blueFirstBoard
    (firstP, firstS) <- playGame blueFirstBoard opts
    putStrLn "-----------------------------------------------------------------------"
    putStrLn ("# First result for Blue player: " ++ show (firstP) ++ " point with a score difference of " ++ show (firstS))
    putStrLn "-----------------------------------------------------------------------"
    putStrLn ""
    putStrLn "Blue goes second:"
    let redFirstBoard = initialiseBoard (boardDim opts) RedBot (winStreak opts)
    putStrLn $ show redFirstBoard
    (secondP, secondS) <- playGame redFirstBoard opts
    putStrLn "-----------------------------------------------------------------------"
    putStrLn ("# Second result for Blue player: " ++ show (secondP) ++ " point with a score difference of " ++ show (secondS))
    putStrLn "-----------------------------------------------------------------------"
    putStrLn ""
    putStrLn "-----------------------------------------------------------------------"
    putStrLn ("# Total result for Blue player: " ++ show (firstP + secondP) ++ " points with a score difference of " ++ show (firstS + secondS))
    putStrLn "-----------------------------------------------------------------------"
    putStrLn ""