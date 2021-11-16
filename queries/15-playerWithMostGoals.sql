SELECT p_name AS 'Athlete', max(ps_goals) AS 'Goals'
FROM Player, PlayerStats
WHERE p_id = ps_playerid
;
