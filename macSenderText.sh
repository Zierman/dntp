#!/usr/bin/env bash

echo -n -e "\033]0;Sender\007"
cd /Users/dw/Dropbox/GitHub/dntp

java -cp bin project2.ChunkFrameSender -f in.txt -s 10 -t 500 -e -reqlog
#java src/project2.ChunkFrameSender

bash