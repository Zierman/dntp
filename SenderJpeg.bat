@echo off 
title Sender
@echo on
java -cp bin project2.ChunkFrameSender -f in.jpeg -s 10000 -t 500 -e -reqlog
@echo off
pause
