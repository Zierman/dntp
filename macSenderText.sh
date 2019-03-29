#!/usr/bin/env bash

echo "starting macSenderText.sh"
printf '\e[8;100;50t'
echo -n -e "\033]0;SenderText\007"
cd $(dirname $0)


java -cp bin project2.ChunkFrameSender -f in.txt -s 10 -t 500 -e -reqlog

echo "Press return to end bash session"
read -n1 -r button
