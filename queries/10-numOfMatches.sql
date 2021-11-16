SELECT count(t.t_country) AS 'No of matches played', t.t_country 
from Team t 
JOIN Match m ON m.m_hometeam=t.t_id 
GROUP BY t.t_country
ORDER BY count(t.t_country) DESC LIMIT 1;