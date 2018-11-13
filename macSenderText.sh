#!/usr/bin/env bash

echo "starting macSenderText.sh"
printf '\e[8;20;100t'

echo -n -e "\033]0;SenderText\007"
cd /Users/dw/Dropbox/GitHub/dntp

java -cp bin project2.ChunkFrameSender -f in.txt -s 10 -t 500 -e -reqlog

bash