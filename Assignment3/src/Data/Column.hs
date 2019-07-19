--
-- Code by Steven X. Han
--

module Data.Column (
    Column,
    unfillColumn,
    pushToColumn,
    fillColumn
) where
    
type Column a  = [a]

pushToColumn :: a -> Column a -> Column a
pushToColumn x list = list ++ [x]

fillColumn :: a -> Int -> Column a -> Column a
fillColumn x i list = list ++ replicate (i - (length list)) x

unfillColumn :: (Eq a) => a -> Column a -> Column a
unfillColumn e list = case list of
    [] -> []
    x : xs
        | x == e -> []
        | otherwise -> x : unfillColumn e xs