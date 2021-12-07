SELECT p.p_name , t.t_team 
FROM Player p JOIN LinkPlayerAndTeams L on L.p_id=p.p_id
JOIN Team t ON L.t_id=t.t_id 
WHERE t.t_team LIKE "%Germany%"
;