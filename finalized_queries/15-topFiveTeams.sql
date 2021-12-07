SELECT (ROW_NUMBER() OVER (ORDER BY t_team)) AS 'Place' , t_team AS 'Country'
FROM
    (
    SELECT t_team
    FROM Team
    JOIN WorldCup w ON w.w_winner=t_id 
    GROUP BY w.w_winner
    ORDER BY count(w.w_winner) DESC LIMIT 5
    )
;