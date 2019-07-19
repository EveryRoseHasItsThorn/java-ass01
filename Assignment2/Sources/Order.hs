module Order where

import Types

-- Change this implementation to your own non-trivial trading strategy.
-- Do not modify the type signature of the function.
--q;[=.;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[;=[/
makeOrders :: Portfolio -> [StockHistory] -> [Order]
makeOrders (_ , _) [] =[]
makeOrders (cash, t) history
     =case history of
        []->[]
        (s,p):xs
                   |p==[]->[]
                   |length p>timeObserve&&allTraderOrder p=="All Trade Purchase" ->[Order s (investQuantity p)]++makeOrders (cash, t) xs
                   |length p>timeObserve&&allTraderOrder p=="All Trade Sell" ->[Order s (-(investQuantity p))]++makeOrders (cash, t) xs
                   |length p>timeObserve&&averageCross p=="Golden Cross" ->[Order s (investQuantity p)]++makeOrders (cash, t) xs
                   |length p>timeObserve&&averageCross p=="Death Cross"->[Order s (-(investQuantity p))]++makeOrders (cash, t) xs
                   |otherwise ->[]

-- |time to observe when stock history is not long
-- >>>timeObserve 
--20

--It is better to do not order any stock at first 20 days since we need history to analyse.
timeObserve::Int
timeObserve=20

-- |calculate Sharpe Ratio
-- >>>sharpeRatio [1.1,1.2,1.3]
--1.1103845096768414
-- >>>sharpeRatio []
-- 0.0
-- >>>sharpeRatio [1,(-1.55),2.5,(-0.99)]
-- -3.201761919339805

--Sharpe Ratio is widely used to calculate the risk of trade. 
sharpeRatio::[Double]->Double
sharpeRatio x
     |x==[] =0
     |otherwise =((returnExpected x)-dailyFreeRiskRate*convertedLength)/(standardDev x)
      where
      dailyReturn::[Double]->[Double]
      dailyReturn  t=case t of
         l:ls
            |length(ls)>1 ->[((head(ls)-l)/l)]++(dailyReturn ls)
            |otherwise->[]
         []->[]
      standardDev::[Double]->Double
      standardDev n = (sqrt $sum$ map (^2) $map (+(-mean)) n)/(sqrt convertedLength)
              where
               mean= sum x/ (fromIntegral (length x)/1)
      returnExpected:: [Double]->Double
      returnExpected u =sum (dailyReturn u)
      convertedLength = (fromIntegral (length x)/1)
      dailyFreeRiskRate=0.03/365
-- |decide how many stocks to order
-- >>>investQuantity [1,2,3,1.12,2.45]
-- 50000
-- >>>investQuantity [1,2,1.22,7]
-- 0
-- >>>investQuantity []
-- 0


--the higher Sharpe Ratio is, the lower risk is. So when Sharpe Ratio is high, we order more stocks. When is low, to minimise the risk, we order less stoscks.
investQuantity::[Double] ->Integer
investQuantity x = case (calculateRisk x) of
    "Super Safe"->5000000
    "Safe"->2500000
    "Relatively Safe"->500000
    "A Little Dangergous"->250000
    "Dangerous"->50000
    "Extrmely Dangerous"->25000
    _ ->0
-- |output the extent of risk
-- >>>calculateRisk []
--"no"
-- >>>calculateRisk [12,13,14,100]
-- "no"
-- >>>calculateRisk [1,2,3,1.12,2.45]
-- "Dangerous"


-- use the outcome of function sharpeRetio, determine several class of risk. 
calculateRisk::[Double]->String
calculateRisk x
              |sharpeRatio x>3  ="Super Safe"
              |sharpeRatio x>2.5 ="Safe"
              |sharpeRatio x>2 ="Relatively Safe"
              |sharpeRatio x>1.5="A Little Dangergous"
              |sharpeRatio x>1="Dangerous"
              |sharpeRatio x>0.5="Extrmely Dangerous"
calculateRisk _ ="no"

-- |calculate moving average
-- >>>countAverage [1,2] 2
-- [1.5]
-- >>>countAverage [] 5
-- []
-- >>>countAverage [1,2,3,1,2,3,1,23,1,1,1,2,1,5] 5
-- [1.8,5.8,2.25]

-- It used to calculate the simple moving average(count the mean of price on every several days' price), derived from technical anaylsis.
countAverage::(Num a, Fractional a)=>[Double]->Int->[a]
countAverage x y
   |length(x)>y=  [(/100)  $fromIntegral  $round $((sum (take y x ))*100)/( fromIntegral y)]++countAverage (drop y x) y
   |x==[]=[]
   |otherwise = [(/100)  $fromIntegral  $round $((sum x )*100)/(fromIntegral (length x)/1)]

-- |determine whether exist a cross(golden or death)
-- >>>averageCross []
-- "No Cross"
-- >>>averageCross [1,2,3,1,2,3,1,1,3,2,1,1,2,3,1,5,3,15,3,15,3,15,3]
--"No Cross"
-- >>>averageCross [1,2,3,4,5,6,40,0.1,0.1,0.1,0.1,0.1,0.5,0.2,0.4,0.5,1.5,0.5,0.3,0.1,0.3]
--"Death Cross"


--Determine whether there is a cross apperar(gold or death), if gold cross, buy stocks. If death cross, sell stocks.
averageCross::[Double]->String
averageCross x
   |x==[]="No Cross"
   |(head average5Days)>(head average20Days) &&(head (tail average5Days))<(head (tail average20Days))="Golden Cross"
   |(head average5Days)<(head average20Days) &&(head (tail average5Days))>(head (tail average20Days))="Death Cross"
   |otherwise ="No Cross"
          where
               average20Days =countAverage x 20
               average5Days =countAverage x 5

-- |determine whether the current price is near round number
-- >>>allTraderOrder [5,5,5,5,4,4.01]
-- "All Trade Sell"
-- >>>allTraderOrder []
-- "No Influence"
-- >>>allTraderOrder [4.5,4.2,4.3,4.5,1.4,15]
-- "No Influence"


--obverse the traders' psychology to predict the proformance of market
allTraderOrder::[Double]->String
allTraderOrder y
       |y==[]= "No Influence"
       |psychologicalRoundNumber y==True&&nearest10Days>head y    ="All Trade Purchase"
       |psychologicalRoundNumber y==True&&nearest10Days<head y   ="All Trade Sell"
       |otherwise ="No Influence"
               where
                      psychologicalRoundNumber::[Double]->Bool
                      psychologicalRoundNumber a
                            | abs((fromIntegral(round((head a)*1000)))/1000-(fromIntegral (round(head a))))<=0.1 =True
                            |otherwise =False
                      nearest10Days  = head (countAverage (take 10 y) 10)

