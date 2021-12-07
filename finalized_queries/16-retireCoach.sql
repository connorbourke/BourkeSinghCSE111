DELETE 
FROM Coach
WHERE c_id IN
    (
    SELECT min(c_id)
    FROM Coach
    )
;