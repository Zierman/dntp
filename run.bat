@echo off
title launcher

echo We will compile the programs...
@echo on
javac -d bin -cp bin -sourcepath src src\project1\FileReceiver.java
javac -d bin -cp bin -sourcepath src src\project1\FileSender.java
@echo off
pause

echo The first thing we transmit is a small text file because it is easy to trace the output.

echo Press any key to show in.txt...
pause > nul
start in.txt

echo Press any key to start the receiver program...
pause > nul
start ReceiverTxt.bat

echo Press any key to start the sender program...
pause > nul
start SenderTxt.bat


echo Press any key to show out.txt...
pause > nul
start out.txt

pause

echo The second thing we will transmit is a jpeg.

echo Press any key to show test.jpeg...
pause > nul
start test.jpeg

echo Press any key to start the receiver program...
pause > nul
start ReceiverTxt.bat

echo Press any key to start the sender program...
pause > nul
start SenderTxt.bat

echo Press any key to show out.jpeg...
pause > nul
start out.jpeg

