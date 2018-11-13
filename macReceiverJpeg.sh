#!/usr/bin/env bash

echo "starting macReceiverJpeg.sh"
printf '\e[8;20;100t'

echo -n -e "\033]0;ReceiverJpeg\007"
cd /Users/dw/Dropbox/GitHub/dntp

java -cp bin project2.ChunkFrameReceiver

echo "Press return to end bash session"
read -n1 -r button
