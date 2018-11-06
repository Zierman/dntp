@echo off
title launcher

echo We will compile the programs...
@echo on
javac -d bin -cp bin -sourcepath src src\project2\ChunkFrameReceiver.java
javac -d bin -cp bin -sourcepath src src\project2\ChunkFrameSender.java
@echo off
pause

echo Lets look at the help message for the sender quick
echo on
java -cp bin project2.ChunkFrameSender -help
@echo off 
echo Press any key to show in.txt...
pause > nul

echo The first thing we transmit is a small text file because it is easy to trace the output.


echo Press any key to start the receiver program...
pause > nul
start ReceiverTxt.bat

echo Press any key to start the sender program...
pause > nul
start SenderTxt.bat


echo Press any key to show in_copy.txt...
pause > nul
start in_copy.txt

pause



echo Press any key to start the receiver program...
pause > nul
start ReceiverJpeg.bat

echo Press any key to start the sender program...
pause > nul
start SenderJpeg.bat

echo Press any key to show in_copy.jpeg...
pause > nul
start in_copy.jpeg

