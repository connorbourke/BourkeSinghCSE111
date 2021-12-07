SELECT p_name, ps.ps_goals 
FROM Player 
JOIN PlayerStats ps ON p_id=ps.ps_playerid 
WHERE p_name IN
    (
    SELECT p_name 
    FROM Player p join LinkPlayerAndTeams L on l.p_id=p.p_id
    JOIN Team t ON l.t_id=t.t_id 
    WHERE t.t_team='Brazil'
    )
ORDER BY  ps.ps_goals DESC LIMIT 1
;
