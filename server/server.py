import requests
from flask import Flask, Response, stream_with_context, request
import urllib.parse
from subprocess import Popen, PIPE
import shlex

app = Flask(__name__)

#test = "https://rr2---sn-aigl6n6s.googlevideo.com/videoplayback?expire=1654896055&ei=V2GjYo7_K4L11wL8hbegBQ&ip=2.86.71.176&id=o-AJb8cpanDQGmWnN27xmqi0Urr8HQUROXTHDlihRXiQnT&itag=22&source=youtube&requiressl=yes&vprv=1&mime=video%2Fmp4&cnr=14&ratebypass=yes&dur=272.904&lmt=1654534588849562&fexp=24001373,24007246&c=ANDROID&txp=5532434&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Ccnr%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRAIgC0ZZRk8SqYcJBXpX-XuBPveK98R7Mt4ZSIEUByYmWl4CIDnmp2JHptcZ4inrEi5b1cwP4GVsIEG5wjJyui2s76hT&host=rr4---sn-vuxbavcx-5uiz.googlevideo.com&rm=sn-vuxbavcx-5uiz7z,sn-4g5ek77s&req_id=59a77377802ba3ee&redirect_counter=2&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=so&mip=94.1.52.134&mm=29&mn=sn-aigl6n6s&ms=rdu&mt=1654874236&mv=m&mvi=2&pl=22&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AG3C_xAwRQIgD-BgbeWdnJiB_aYX1-s5t24WGfD1uLKbyujUmizM-cUCIQClnsnlCZp7IyO98L9G4U543E-BI4UaWndMSswqBqQA0A%3D%3D"

CHUNK_SIZ = 65536

def stream_get(url):
    print("\n\nURL!\n\n")

    proc = Popen(shlex.split(f"ffmpeg -i \"{url}\" -strict experimental -preset ultrafast -acodec aac -vcodec h264 -vf format=yuv420p -f mp4 -movflags frag_keyframe+empty_moov+faststart pipe:1"), stdout=PIPE)
    
    try:
        f = proc.stdout
        byte = f.read(CHUNK_SIZ)
        while byte:
            yield byte
            byte = f.read(CHUNK_SIZ)
    finally:
       proc.kill()


codec_sessions = {}
@app.route('/stream.mp4')
def tstream():
    #url = request.args.get('url')
    url = test
    url = urllib.parse.unquote(url)#.replace(".mp4", "")
    
    mime = "video/mp4"

    print(url)

    return Response(response=stream_get(url), mimetype=mime, content_type=mime)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, debug=True)
