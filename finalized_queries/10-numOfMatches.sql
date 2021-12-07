SELECT count(t.t_team) AS '# of matches played', t.t_team 
FROM Team t 
JOIN Match m ON m.m_hometeam=t.t_id 
GROUP BY t.t_team
ORDER BY count(t.t_team) DESC LIMIT 1
;