--
-- Code written by Steven X. Han based on the
-- original Kalaha framework by Uwe Zimmer.
--

module Commandline.Options (
    Options (Options, timeLimit, isHuman, boardDim, winStreak),
    argsToOptions    -- :: [String] -> Options
) where

import Data.Player (Player (BlueBot, RedBot))
import Data.Board (Dimension)

import System.Console.GetOpt (OptDescr (Option), ArgOrder (Permute), ArgDescr (OptArg), getOpt, usageInfo)

data Options = Options {
    timeLimit :: Float, -- in seconds
    isHuman   :: Maybe Player,
    boardDim  :: Dimension,
    winStreak :: Int
    }
    
defaultOptions :: Options
defaultOptions = Options {
    timeLimit = 5.0,
    isHuman   = Nothing,
    boardDim  = (11, 10),
    winStreak = 5
    }
    
data Flags = TimeLimit Float
           | HumanPlayer (Maybe Player)
           | Dimension Dimension
           | Streak Int
           
header :: String
header = "Usage: ConnectX [OPTION...]"

availableOptions :: [OptDescr Flags]
availableOptions = [
    Option "t" ["time"] (OptArg fromMaybeTime "<time in seconds>") "limits the available time per move",
    Option "h" ["human"] (OptArg fromMaybePlayer "B|R") "selects a human player",
    Option "d" ["dimension"] (OptArg fromMaybeDimension "<dimension in (x, y)>") "sets the dimension of the board",
    Option "s" ["streak"] (OptArg fromMaybeStreak "<winning streak in pieces>") "sets the winning streak of the game"
    ] 
    where
        fromMaybeTime :: Maybe String -> Flags
        fromMaybeTime maybeTime = TimeLimit $ case maybeTime of
            Nothing -> timeLimit defaultOptions
            Just x  -> read x
            
        fromMaybePlayer :: Maybe String -> Flags
        fromMaybePlayer maybePlayer = HumanPlayer $ case maybePlayer of
            Nothing -> Nothing
            Just x
                | x == "B"  -> Just BlueBot
                | x == "R"  -> Just RedBot
                | otherwise -> error "Check human player argument"
                
        fromMaybeDimension :: Maybe String -> Flags
        fromMaybeDimension maybeDim = Dimension $ case maybeDim of
            Nothing -> boardDim defaultOptions
            Just x  -> read x
            
        fromMaybeStreak :: Maybe String -> Flags
        fromMaybeStreak maybeStr = Streak $ case maybeStr of
            Nothing -> winStreak defaultOptions
            Just x  -> read x
            
flagsToOptions :: [Flags] -> Options
flagsToOptions flags = foldl changeOpts defaultOptions flags
    where
        changeOpts :: Options -> Flags -> Options
        changeOpts opt f = case f of
            TimeLimit x   -> opt {timeLimit = x}
            HumanPlayer x -> opt {isHuman   = x}
            Dimension x   -> opt {boardDim  = x}
            Streak x      -> opt {winStreak = x}
            
argsToOptions :: [String] -> Options
argsToOptions args = case getOpt Permute availableOptions args of
   (flags, [], []) -> flagsToOptions flags
   (_, nonOpts, []  ) -> error $ "unrecognised arguments: " ++ unwords nonOpts
   (_, _      , msgs) -> error $ concat msgs ++ usageInfo header availableOptions