DELETE 
FROM Player
WHERE p_id IN
    (
    SELECT p_id
    FROM Player, PlayerStats
    WHERE p_id = ps_playerid
    AND ps_goals < 2
    )
;