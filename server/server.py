import requests
from ffmpeg import _ffmpeg as ffmpeg
from flask import Flask, Response, stream_with_context, request
import urllib.parse

from subprocess import Popen, PIPE
import shlex

app = Flask(__name__)

CHUNK_SIZ = 65536

def stream_get(url):
    proc = Popen(shlex.split(f"ffmpeg -i \"{url}\" -strict experimental -preset ultrafast -acodec aac -vcodec h264 -pix_fmt yuv420p -f mp4 -movflags frag_keyframe+empty_moov+faststart pipe:1"), stdout=PIPE)
    
    try:
        f = proc.stdout
        byte = f.read(CHUNK_SIZ)
        while byte:
            yield byte
            byte = f.read(CHUNK_SIZ)
    finally:
       proc.kill()


codec_sessions = {}
@app.route('/stream')
def tstream():
    url = urllib.parse.unquote(request.args.get('url'))
    
    mime = "video/quicktime"

    return Response(response=stream_get(url), mimetype=mime, content_type=mime)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, debug=True)
