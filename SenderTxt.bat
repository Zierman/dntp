@echo off 
title Sender
@echo on
java -cp bin project2.ChunkFrameSender -f in.txt -s 10 -t 500 -e -reqlog
@echo off
pause
