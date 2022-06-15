import socket

HOST = "192.168.1.113"  # The server's hostname or IP address
PORT = 8080  # The port used by the server

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

s.connect((HOST, PORT))
s.sendall("OK".encode())

while True:
    inp = raw_input("=> ")

    s.sendall(inp.encode())

s.close()