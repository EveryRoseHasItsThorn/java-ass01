--
-- Code by Steven X. Han
--

module Data.Matrix (
    Matrix,
    flattenMatrix,
    dimension
) where

import Data.Column (Column)

type Matrix a = [Column a]

flattenMatrix :: Matrix a -> [a]
flattenMatrix = concat

dimension :: (Integral b) => Matrix a -> (b, b)
dimension mat = ( fromIntegral $ length mat
                , fromIntegral $ maximum $ map length mat )