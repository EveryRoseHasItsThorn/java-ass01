--
-- Code by Steven X. Han
--

module Data.Board (
    Board (Board, board, blueScore, redScore, turn, connect, dimension),
    Index,              -- = Int
    Dimension,          -- = (Int, Int)
    LookAhead,          -- = Int
    Score,              -- = Int
    scorise,            -- :: Board -> Board
    validMove,          -- :: Board -> Index -> Bool,
    winBonus,           -- :: Board -> Score
    updateBoard,        -- :: Board -> Index -> Board
    updateBoardNoScore, -- :: Board -> Index -> Board
    initialiseBoard,    -- :: Dimension -> Player -> Int -> Board
    getScore,           -- :: Board -> Player -> Score
) where

import Data.Cell (Cell (Empty))
import Data.Matrix (Matrix)
import Data.Player (Player (BlueBot, RedBot, Finished), corresCell, otherPlayer)
import Data.Column (Column, fillColumn, pushToColumn)

import Data.List (intercalate, transpose)
import Data.List.Split (splitOn)
import Data.Universe.Helpers (diagonals)

data Board = Board { board     :: Matrix Cell
                   , blueScore :: Score
                   , redScore  :: Score
                   , turn      :: Player
                   , dimension :: Dimension
                   , connect   :: Int
                   }
    deriving (Eq)

type Dimension = (Int, Int)

type LookAhead = Int

type Index     = Int

type Score     = Int

instance Show Board where
    show b =
        intercalate " | " (map show [1 .. x]) ++ "\n" ++
        replicate (4 * x - 3) '-' ++ "\n" ++
        (intercalate "\n"
            $ map (intercalate " | ")
                $ map (map show)
                    $ reverse . transpose $ filledMat) ++
        "\n" ++ replicate (4 * x - 3) '-' ++
        "\n" ++ intercalate " | " (map show [1 .. x]) ++
        "\n" ++ "Turn: " ++ show (turn b) ++ " - " ++ show (corresCell (turn b)) ++
        "\n" ++ "BlueScore: " ++ show (blueScore b) ++ "; RedScore: " ++ show (redScore b)

        where
            (x, _)    = dimension b
            boardMat  = board b
            filledMat = map (fillColumn Empty (snd $ dimension b)) boardMat

initialiseBoard :: Dimension -> Player -> Int -> Board
initialiseBoard d@(x, _) p i = Board {
    board     = replicate x [],
    blueScore = 0,
    redScore  = 0,
    turn      = p,
    dimension = d,
    connect   = i
    }


validMove :: Board -> Index -> Bool
validMove b i = case turn b of
    Finished -> False
    _        -> i >= 1 && i <= width && length (mat !! i') < height
    where
        width = fst $ dimension b
        height = snd $ dimension b
        i' = i - 1
        mat = board b

scorise :: Board -> Board
scorise b = b { blueScore = getScore b BlueBot,
                redScore  = getScore b RedBot }

getScore :: Board -> Player -> Score
getScore b p = sum [columnScore, rowScore, diagonalScore, otherDiagScore]
    where
        streak    = connect b
        minStreak = 1 + streak `div` 2
        otherBot  = otherPlayer p

        columnScore    = calculateScore $ filledMatrix
        rowScore       = calculateScore $ transpose $ filledMatrix
        diagonalScore  = calculateScore $ diagonals $ filledMatrix
        otherDiagScore = calculateScore $ diagonals $ map reverse $ filledMatrix

        filledMatrix   = map (fillColumn Empty $ snd $ dimension b) $ board b

        calculateScore mat =
            sum $ (map (streakScore . length))
                $ filter ((>=minStreak).length)
                    $ concatMap
                        (concatMap (splitOn [corresCell otherBot]))
                            $ map (splitOn [Empty]) mat

        streakScore :: Int -> Score
        streakScore i
            | i < minStreak = 0
            | otherwise     = i * 4

winBonus :: Board -> Score
winBonus b = (2*) . (uncurry (*)) $ dimension b

-- Takes the current board, the index generated by the Bot,
-- and the Player of the NEXT turn, returns a new Board.
updateBoard :: Board -> Index -> Board
updateBoard b i
    | hasWon b            = case turn b of
                                BlueBot -> b { turn = Finished, redScore = redScore b + winBonus b}
                                RedBot  -> b { turn = Finished, blueScore = blueScore b + winBonus b}
                                _       -> b
    | isGameOver b        = b { turn = Finished }
    | not $ validMove b i = b
    | otherwise           = scorise $ b { board = board newBoard
                              , turn = otherPlayer $ turn b}
        where
            newBoard = Board { board = placePiece (board b) i (turn b) }
            
updateBoardNoScore :: Board -> Index -> Board
updateBoardNoScore b i
    | hasWon b            = case turn b of
                                BlueBot -> b { turn = Finished }
                                RedBot  -> b { turn = Finished }
                                _       -> b
    | isGameOver b        = b { turn = Finished }
    | not $ validMove b i = b
    | otherwise           = b { board = board newBoard
                              , turn = otherPlayer $ turn b}
        where
            newBoard = Board { board = placePiece (board b) i (turn b) }

isGameOver :: Board -> Bool
isGameOver b = hasWon b || (and $ map (\x -> length x == depth) mat)
    where
        mat         = board b
        depth       = snd $ dimension b

hasWon :: Board -> Bool
hasWon b = columnWin || rowWin || diagonalWin || otherDiagWin
    where
        streak      = connect b
        unfilledMat = board b
        height      = snd $ dimension b

        identicalElems :: (Eq a) => [a] -> Bool
        identicalElems list = case list of
            x : y : xs -> x == y && identicalElems (y : xs)
            _          -> True

        columnWin    = or $ map winInColumn unfilledMat
        rowWin       = or $ map winInColumn 
                            $ concatMap (splitOn [Empty]) 
                                $ transpose 
                                    $ map (fillColumn Empty height) unfilledMat
        diagonalWin  = or $ map winInColumn 
                            $ concatMap (splitOn [Empty]) 
                                $ diagonals 
                                    $ map (fillColumn Empty height) unfilledMat
        otherDiagWin = or $ map winInColumn 
                                $ concatMap (splitOn [Empty]) 
                                    $ diagonals 
                                        $ map (reverse . (fillColumn Empty height)) 
                                            unfilledMat

        winInColumn :: Column Cell -> Bool
        winInColumn list = case list of
            []     -> False
            _ : xs
                | length list >= streak ->
                    identicalElems (take streak list)
                    || winInColumn xs
                | otherwise             -> False

placePiece :: Matrix Cell -> Index -> Player -> Matrix Cell
placePiece mat i p = take i' mat ++ [pushToColumn (corresCell p) (mat !! i')] ++ drop i mat
    where
        i' = i - 1