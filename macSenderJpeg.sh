#!/usr/bin/env bash

echo "starting macSenderJpeg.sh"
printf '\e[8;100;50t'
echo -n -e "\033]0;SenderJpeg\007"
cd $(dirname $0)


java -cp bin project2.ChunkFrameSender -f in.jpeg -s 1000 -t 500 -e -reqlog

echo "Press return to end bash session"
read -n1 -r button
