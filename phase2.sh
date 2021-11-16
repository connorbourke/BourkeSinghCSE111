#!/bin/bash

qnum=20

for (( i=1; i<=$qnum; i++ ))
do
	echo "---- $i ----"
	sqlite3 WorldCups.sqlite < queries/$i-*.sql
done
