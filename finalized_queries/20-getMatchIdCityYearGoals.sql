Select m.m_id, m.m_city, w.w_year, w.w_goals 
from match m 
join LinkWorldCupAndMatch L on l.m_id=m.m_id 
join WorldCup w on w.w_year=l.w_year;