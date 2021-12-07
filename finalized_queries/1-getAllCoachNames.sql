SELECT c.c_name , t.t_team
FROM Coach c 
JOIN Team t ON c.c_teamid=t.t_id
;