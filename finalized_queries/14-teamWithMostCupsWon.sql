SELECT count(w.w_winner) AS 'Most Wins' , t.t_team 
FROM WorldCup w 
JOIN Team t ON t.t_id=w.w_winner 
GROUP BY w.w_winner
ORDER BY count(w.w_winner)  DESC LIMIT 1
;