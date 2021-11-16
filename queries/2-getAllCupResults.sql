SELECT w.w_year , t.t_country 
FROM WorldCup w 
JOIN Team t ON t.t_id=w.w_winner
ORDER BY w_year DESC;

