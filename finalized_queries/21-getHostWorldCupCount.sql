Select Count (w.w_host) as 'host Count', t.t_team from match m 
join LinkWorldCupAndMatch L on l.m_id=m.m_id 
join WorldCup w on w.w_year=l.w_year 
join Team t on t.t_id=w.w_host 
group by w.w_host;