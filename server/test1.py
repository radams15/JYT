from subprocess import Popen, PIPE
import shlex

import psutil

#test = "https://rr2---sn-aigl6n6s.googlevideo.com/videoplayback?expire=1654896055&ei=V2GjYo7_K4L11wL8hbegBQ&ip=2.86.71.176&id=o-AJb8cpanDQGmWnN27xmqi0Urr8HQUROXTHDlihRXiQnT&itag=22&source=youtube&requiressl=yes&vprv=1&mime=video%2Fmp4&cnr=14&ratebypass=yes&dur=272.904&lmt=1654534588849562&fexp=24001373,24007246&c=ANDROID&txp=5532434&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Ccnr%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRAIgC0ZZRk8SqYcJBXpX-XuBPveK98R7Mt4ZSIEUByYmWl4CIDnmp2JHptcZ4inrEi5b1cwP4GVsIEG5wjJyui2s76hT&host=rr4---sn-vuxbavcx-5uiz.googlevideo.com&rm=sn-vuxbavcx-5uiz7z,sn-4g5ek77s&req_id=59a77377802ba3ee&redirect_counter=2&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=so&mip=94.1.52.134&mm=29&mn=sn-aigl6n6s&ms=rdu&mt=1654874236&mv=m&mvi=2&pl=22&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AG3C_xAwRQIgD-BgbeWdnJiB_aYX1-s5t24WGfD1uLKbyujUmizM-cUCIQClnsnlCZp7IyO98L9G4U543E-BI4UaWndMSswqBqQA0A%3D%3D"
test = "https://rr1---sn-5hne6nsz.googlevideo.com/videoplayback?expire=1654899247&ei=z22jYv6REPXKx_APk8-xkAE&ip=176.9.47.66&id=o-APRNOdip65OzImV9R5jRbyLoJMfuIxcIeopV22KhH8tq&itag=22&source=youtube&requiressl=yes&vprv=1&mime=video%2Fmp4&cnr=14&ratebypass=yes&dur=45.720&lmt=1654505116119787&fexp=24001373,24007246,24208264&c=ANDROID&txp=5318224&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Ccnr%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRgIhAObgt0i7uo7FTJNf2lmJmofrlTScelugxUwfhN6P4oD3AiEA57cUSzQLPL5fTsODwWqoCeVERZv2QYPrlSYt9sXPcZI%3D&host=rr2---sn-4g5e6ns6.googlevideo.com&redirect_counter=1&cm2rm=sn-4g5ezz7l&req_id=f77a798d122aa3ee&cms_redirect=yes&cmsv=e&mh=Ap&mip=94.1.52.134&mm=34&mn=sn-5hne6nsz&ms=ltu&mt=1654877401&mv=u&mvi=1&pl=22&lsparams=mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AG3C_xAwRAIgWesFae20Z19HWyZk20RsZsAWYHMgjpjTsF35Z_TC6AkCIGZt3kXCY3e62ualyg22hDA_p5b7yzcJ7j_ts_4emwcD"


CHUNK_SIZ = 65536

def stream_get(url):
    print("\n\nURL!\n\n")

    proc = Popen(shlex.split(
        #f"ffmpeg -i \"{url}\" -strict experimental -preset ultrafast -acodec aac -vcodec h264 -pix_fmt yuv420p -f mp4 -movflags frag_keyframe+empty_moov pipe:1"),
        f"ffmpeg -re -stream_loop -1 -i \"{url}\" -f rtsp -rtsp_transport tcp rtsp://localhost:8554/live.stream"),
                 stdout=PIPE)

    return proc.pid

import socket

HOST = "0.0.0.0"  # Standard loopback interface address (localhost)
PORT = 8080  # Port to listen on (non-privileged ports are > 1023)

def run(conn):
    print(f"Connected!")

    while True:
        ret = conn.recv(1024)

        try:
            pid = stream_get(test)
            proc = psutil.Process(pid)

            while True:
                ret = conn.recv(1024)

                if ret:
                    ret = ret.decode()
                    if ret == "pause":
                        proc.suspend()
                    elif ret == "play":
                        proc.resume()
                    elif ret == "stop":
                        proc.kill()
                        return

        except Exception:
            print("Done!")

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    conn, addr = s.accept()
    with conn:
        run(conn)

    exit()