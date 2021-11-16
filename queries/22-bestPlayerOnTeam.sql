SELECT p_name, ps.ps_goals 
FROM Player 
JOIN PlayerStats ps ON p_id=ps.ps_playerid 
WHERE p_name IN
    (
    SELECT p_name 
    FROM Player 
    JOIN Team t ON p_teamid=t.t_id 
    WHERE t.t_country='Brazil'
    )
ORDER BY  ps.ps_goals DESC LIMIT 1;
