#!/usr/bin/env bash

#@echo off
#title launcher
printf '\e[8;25;100t'

echo "\n\n\n"
echo "###########################################################################"
echo "  Welcome to the demonstration script for the dntp transfer protocol."
echo "  We will quickly send a text file, then an image file."
echo "  Some random errors will be inserted into the transfer."
echo "  These will be logged in the popup receiver & sender terminal windows."
echo "###########################################################################"
echo "\n\n\n"
echo "Press return to continue"
read -n1 -r button

echo We will compile the programs...

javac -d bin -cp bin -sourcepath src src/project2/ChunkFrameSender.java

javac -d bin -cp bin -sourcepath src src/project1/FileReceiver.java
javac -d bin -cp bin -sourcepath src src/project2/ChunkFrameReceiver.java
# javac -d bin -cp bin -sourcepath src src/project2/ChunkFrameReceiver.java



echo "Lets look at the help message for the sender quick"
echo "Press return to continue"
read -n1 -r button
java -cp bin project2.ChunkFrameSender -help

echo "The first thing we transmit is a small text file because it is easy to trace the output."
echo "Before proceeding, check the working directory that in_copy.txt does not exist";
echo "Press return to show in.txt and delete in_copy.txt if exists..."
read -n1 -r button
rm in_copy.txt
open -a "Sublime Text" in.txt


echo "Press any key to start the receiver program..."
read -n1 -r button
open -a Terminal.app  macReceiverText.sh

echo "Press any key to start the sender program..."
read -n1 -r button
open -a Terminal.app macSenderText.sh

echo "Press return to show the received text file in_copy.txt..."
read -n1 -r button
open -a "Sublime Text" in_copy.txt


echo "in.txt opened"
echo "Press return to open the image which will be sent: in.jpeg..."
read -n1 -r button
open in.jpeg

echo "in.jpeg opened"
echo "Press return to start the receiver program..."
read -n1 -r button
open -a Terminal.app macReceiverJpeg.sh

echo "Press return to start the sender program with input in.jpeg..."
read -n1 -r button
open -a Terminal.app macSenderJpeg.sh

echo "Press return to open the received image file in_copy.jpeg..."
read -n1 -r button
open in_copy.jpeg


