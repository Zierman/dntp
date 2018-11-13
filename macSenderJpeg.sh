#!/usr/bin/env bash

#!/usr/bin/env bash

echo -n -e "\033]0;SenderJpeg\007"
printf '\e[8;20;100t'
cd /Users/dw/Dropbox/GitHub/dntp
echo "macSenderJpeg.sh script started"

java -cp bin project2.ChunkFrameSender -f in.jpeg -s 10000 -t 500 -e -reqlog
#java src/project2.ChunkFrameSender

bash
