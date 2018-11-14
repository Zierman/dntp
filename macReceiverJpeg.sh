#!/usr/bin/env bash

echo "starting macReceiverJpeg.sh"
printf '\e[8;100;50t'
echo -n -e "\033]0;ReceiverJpeg\007"
cd $(dirname $0)


java -cp bin project2.ChunkFrameReceiver

echo "Press return to end bash session"
read -n1 -r button
