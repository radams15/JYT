#!/bin/bash

URL="https://rr5---sn-p5qlsny6.googlevideo.com/videoplayback?expire=1654723647&ei=37-gYteeB8eJ8wSY6reQDQ&ip=135.148.149.204&id=o-AHXnlroGv3mSfjV3wY2I9MHIAU5c3pYgjovS9vhZVAFK&itag=22&source=youtube&requiressl=yes&mh=8U&mm=31%2C26&mn=sn-p5qlsny6%2Csn-4g5lznl6&ms=au%2Conr&mv=m&mvi=5&pl=24&initcwndbps=397500&vprv=1&mime=video%2Fmp4&cnr=14&ratebypass=yes&dur=1775.931&lmt=1653001584357169&mt=1654701663&fvip=4&fexp=24001373%2C24007246%2C24208265&c=ANDROID&txp=5432434&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Ccnr%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRQIgIJiq42C0ZSfQkS4jJ2cboruBpC5-d7xY3aaztIQ_94wCIQCTnystyIqLh-kXk2Y1ZCxsuXo4Y24ppmvZg3Wr9LMB0Q%3D%3D&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AG3C_xAwRQIhAPivKrG354NrQkGWd7kTKvM91EyyCvIDmkUzivyKLfelAiAl8-qSvtQmMeI8E19JR2AII5hwvGESCwSFvxnvmbXGoQ%3D%3D&host=rr5---sn-p5qlsny6.googlevideo.com"

#URL=/home/rhys/vid.mp4

ACODEC=aac
#VCODEC=msmpeg4
VCODEC=h264

ffmpeg -i "$URL"  -b:v 2M -acodec $ACODEC -vcodec $VCODEC -f mp4 -f segment -segment_list out.list -segment_time 3600 -segment_wrap 24 out%03d.mp4
#ffmpeg -re -stream_loop -1 -i "$URL" -f rtsp -rtsp_transport tcp rtsp://localhost:8554/live.stream
