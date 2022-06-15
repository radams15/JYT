import requests
from ffmpeg import _ffmpeg as ffmpeg
from flask import Flask, Response, stream_with_context, request
import urllib.parse
from time import sleep

from subprocess import Popen, PIPE
import shlex

app = Flask(__name__)

test = """https%3A%2F%2Frr1---sn-p5qddn7d.googlevideo.com%2Fvideoplayback%3Fexpire%3D1654809963%26ei%3DCxGiYt_rH7aR2LYPlZCpkAk%26ip%3D135.148.149.204%26id%3Do-AFLE7lpVvzezbinGkCKkcg8AW3S-QKKssBs6qOgP4zyg%26itag%3D22%26source%3Dyoutube%26requiressl%3Dyes%26mh%3D1e%26mm%3D31%252C26%26mn%3Dsn-p5qddn7d%252Csn-4g5e6nsz%26ms%3Dau%252Conr%26mv%3Dm%26mvi%3D1%26pl%3D24%26pcm2%3Dyes%26initcwndbps%3D271250%26vprv%3D1%26mime%3Dvideo%252Fmp4%26cnr%3D14%26ratebypass%3Dyes%26dur%3D1388.437%26lmt%3D1654747272423890%26mt%3D1654788063%26fvip%3D1%26fexp%3D24001373%252C24007246%26beids%3D24221429%26c%3DANDROID%26rbqsm%3Dfr%26txp%3D6318224%26sparams%3Dexpire%252Cei%252Cip%252Cid%252Citag%252Csource%252Crequiressl%252Cpcm2%252Cvprv%252Cmime%252Ccnr%252Cratebypass%252Cdur%252Clmt%26sig%3DAOq0QJ8wRgIhALdkUnidnDbjH36c45-2EsZ2orh9n91iYv96Tijr7NMsAiEAqA542MlARvnshJB8wNYGJzlb7Iomfr3C0KmPHoXu1KI%253D%26lsparams%3Dmh%252Cmm%252Cmn%252Cms%252Cmv%252Cmvi%252Cpl%252Cinitcwndbps%26lsig%3DAG3C_xAwRQIhAMqDSppblwAOaM752UbOo5CvCbX2zpLdg_V0rC1VBnSSAiB-2QDSOAvGHjZaM9545qLylRX5Z8XTCfNCbaf2YEBjKA%253D%253D%26host%3Drr1---sn-p5qddn7d.googlevideo.com.mov"""

CHUNK_SIZ = 65536

def stream_get(url):
    print(url)
    proc = Popen(shlex.split(f"ffmpeg -re -i \"{url}\" -preset ultrafast -movflags frag_keyframe+empty_moov -acodec aac -vcodec h264 -filter:v fps=10 -pix_fmt yuv420p -f mp4 pipe:1"), stdout=PIPE)
    
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
    #url = request.args.get('url')
    url = test
    url = urllib.parse.unquote(url).replace(".mp4", "")
    
    mime = "video/mp4"

    return Response(response=stream_get(url), mimetype=mime, content_type=mime)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080, debug=True)
