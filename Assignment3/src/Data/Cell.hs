--
-- Code by Steven X. Han
--

module Data.Cell (
    Cell (Empty, Blue, Red)
)where

data Cell = Empty | Blue | Red
    deriving (Eq, Read)
    
instance Show Cell where
    show cell = case cell of
        Empty -> " "
        Blue  -> "x"
        Red   -> "o"