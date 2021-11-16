UPDATE PlayerStats
SET ps_matchesplayed = (ps_matchesplayed + 1)
WHERE ps_playerid IN
    (
    SELECT ps_playerid
    FROM Player, PlayerStats
    WHERE p_id = ps_playerid
    AND p_teamid = 4
    )
;