#!/usr/bin/env bash

echo "starting macreceiver.sh"
printf '\e[8;20;100t'

echo -n -e "\033]0;ReceiverText\007"
cd /Users/dw/Dropbox/GitHub/dntp

java -cp bin project2.ChunkFrameReceiver

bash
