UPDATE PlayerStats
SET ps_cupsplayed = (ps_cupsplayed + 1)
WHERE ps_playerid IN
    (
    SELECT ps_playerid
    FROM Player, PlayerStats
    WHERE p_id = ps_playerid
    AND p_id = 1
    )
    ;