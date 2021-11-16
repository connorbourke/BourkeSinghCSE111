SELECT p.p_name, ps.ps_goals 
FROM Player p 
JOIN PlayerStats ps ON p.p_id=ps.ps_playerid 
ORDER BY  ps.ps_goals DESC LIMIT 7;
