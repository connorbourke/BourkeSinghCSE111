SELECT p.p_name , t.t_country 
FROM Player p 
JOIN Team t ON p.p_teamid=t.t_id 
WHERE t.t_country='Germany';