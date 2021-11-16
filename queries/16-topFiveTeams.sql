SELECT (ROW_NUMBER() OVER (ORDER BY t_country)) AS 'Place' , t_country AS 'Country'
FROM
    (
    SELECT t_country
    FROM Team
    JOIN WorldCup w ON w.w_winner=t_id 
    GROUP BY w.w_winner
    ORDER BY count(w.w_winner)  DESC LIMIT 5
    );