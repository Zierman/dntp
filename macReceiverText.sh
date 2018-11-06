#!/usr/bin/env bash

echo "starting macreceiver.sh"

echo -n -e "\033]0;Receiver\007"
cd /Users/dw/Dropbox/GitHub/dntp

java -cp bin project2.ChunkFrameReceiver

bash
