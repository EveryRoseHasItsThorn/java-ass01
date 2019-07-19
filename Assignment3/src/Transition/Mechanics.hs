--
-- Code written by Steven X. Han based on the
-- original Kalaha framework by Uwe Zimmer.
--

module Transition.Mechanics (
    playGame    -- :: Board -> Options -> IO (Int, Score)
) where

import Data.Player (Player (BlueBot, RedBot, Finished))
import Data.Board (Board (blueScore, redScore, turn), Index, LookAhead, Score, dimension, updateBoard)
import Commandline.Options (Options (timeLimit, isHuman))

import Data.Char (isDigit)
import Data.Time.Clock (NominalDiffTime, getCurrentTime, diffUTCTime)
import System.Mem (performGC)
import System.Timeout (timeout)
import Text.Printf (printf)

import Bot.Blue (makeMove)
import Bot.Red (makeMove)

playGame :: Board -> Options -> IO (Int, Score)
playGame b opts = do
    nextBoard <- case isHuman opts of
        Nothing -> nextInTime b
        Just x  -> 
            if turn b == x
            then nextFromHuman b
            else nextInTime    b
                
    putStrLn ">"
    putStrLn $ show nextBoard
    case turn nextBoard of
        Finished -> do
            putStrLn " End of game"
            return (case (blueScore nextBoard) `compare` (redScore nextBoard) of
                EQ -> (0, drawScoreDiff)
                GT -> (1, blueWinScoreDiff)
                LT -> ((-1), blueLostScoreDiff))
                where
                    drawScoreDiff     = blueScore nextBoard - redScore nextBoard
                    blueWinScoreDiff  = drawScoreDiff
                    blueLostScoreDiff = drawScoreDiff
        _ -> 
            if nextBoard == b
            then error $ show (turn b) ++ " failed to make a decision in time"
            else playGame nextBoard opts
                
    where
        worldTimeout      = floor (2.0 * maxCPUTimePerMove * 10 ^ (6 :: Int))
        maxCPUTimePerMove = timeLimit opts
        
        nextFromHuman :: Board -> IO Board
        nextFromHuman cBoard = do
            putStrLn $ 
                "Enter move for " 
                ++ show (turn cBoard) 
                ++ " (1-" ++ (show $ fst $ dimension cBoard) ++ "):"
            move <- readHumanMove
            return $ updateBoard cBoard move
            
        readHumanMove :: IO Index
        readHumanMove = do
            move <- getLine
            if (all isDigit move)
            then
                if (read move) `elem` [1 .. (fst $ dimension b)]
                then return $ read move
                else error "No such index"
            else error "You didn't enter a number"
                
        nextInTime :: Board -> IO Board
        nextInTime cBoard = do
            performGC
            performGC
            performGC
            startT <- getCurrentTime
            nextBoard <- timeout worldTimeout $ return $! (performMove cBoard 1)
            stopT  <- getCurrentTime
            let timeUsed = diffUTCTime stopT startT
            lookDeeper cBoard cBoard timeUsed nextBoard 2 0 0
            
            where
                lookDeeper :: Board 
                           -> Board 
                           -> NominalDiffTime
                           -> Maybe Board 
                           -> LookAhead 
                           -> Int 
                           -> Int 
                           -> IO Board
                lookDeeper startB lastB elapsedT maybeB look stalls timeouts
                     = case maybeB of
                         Nothing -> return lastB
                         Just validB -> do
                             putStrLn "-"
                             performGC
                             performGC
                             performGC
                             startT <- getCurrentTime
                             nextBoard <- timeout worldTimeout 
                                    $ return $! (performMove startB (look + 1))
                             stopT <- getCurrentTime
                             let timeUsed = diffUTCTime stopT startT
                             putStr $ show_1_3_digit_float (realToFrac timeUsed :: Float)
                             case ((realToFrac timeUsed :: Float) <= maxCPUTimePerMove,
                                   (realToFrac timeUsed :: Double) < stallTimeDiff * (realToFrac elapsedT :: Double)) of
                                       (True, True)
                                           | stalls == noOfStallTolerated -> return validB
                                           | otherwise -> lookDeeper startB validB elapsedT nextBoard (look + 1) (stalls + 1) timeouts
                                       (True, False) ->   lookDeeper startB validB timeUsed nextBoard (look + 1) 0 timeouts
                                       (False, _   ) -> return lastB
                             
                             where
                                 -- Less than 10% time increase per iteration is considered stalling.
                                 stallTimeDiff      = 1.1 
                                 -- Third stall in runtime will lead to break out.
                                 noOfStallTolerated = 2   
                                 
                                 show_1_3_digit_float :: Float -> String
                                 show_1_3_digit_float f = printf "%1.3f" f
                    

performMove :: Board -> LookAhead -> Board
performMove b i = case turn b of
    BlueBot -> updateBoard b blueMove
    RedBot  -> updateBoard b redMove
    _       -> b
    where
        blueMove    = Bot.Blue.makeMove b i
        redMove     = Bot.Red.makeMove b i
                

                
