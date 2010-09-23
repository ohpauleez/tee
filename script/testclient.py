import socket
from threading import Thread
import time

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print "connecting..."
s.connect(("localhost", 1234))

print "Setting up the recv..."
def print_resp(s):
    resp = s.recv(4096)
    print "RESP:", str(resp)
    s.close()

t = Thread(target=print_resp, args=[s])
t.setDaemon(True)
t.start()

print "Sending..."
s.send('(+ 1 2 3)')
#s.send('"a"')
time.sleep(2)
s.close()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print "connecting..."
s.connect(("localhost", 1234))

t = Thread(target=print_resp, args=[s])
t.setDaemon(True)
t.start()

print "A is..."
s.send('"a"')
time.sleep(2)
print "Stopping..."
s.close()

