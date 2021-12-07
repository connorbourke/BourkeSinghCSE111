SELECT t.t_team, m.m_city, w.w_year, m.m_homegoals 
FROM Match m 
JOIN LinkWorldCupAndMatch L ON l.m_id=m.m_id 
JOIN WorldCup w ON w.w_year=l.w_year
JOIN Team t ON t.t_id=m.m_hometeam
;