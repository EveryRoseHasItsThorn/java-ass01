--
-- Code by Steven X. Han
--

module Data.Player (
    Player (BlueBot, RedBot, Finished),
    corresCell,  -- :: Player -> Cell
    otherPlayer  -- :: Player -> Player
) where
    
import Data.Cell (Cell (Blue, Red, Empty))
    
data Player = BlueBot | RedBot | Finished
    deriving (Show, Eq)

corresCell :: Player -> Cell
corresCell p = case p of
    BlueBot -> Blue
    RedBot  -> Red
    _       -> Empty
    
otherPlayer :: Player -> Player
otherPlayer p = case p of
    BlueBot -> RedBot
    RedBot  -> BlueBot
    _       -> p