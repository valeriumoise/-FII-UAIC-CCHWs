from concurrent.futures import ThreadPoolExecutor as PoolExecutor
import http.client
import socket
from datetime import datetime
from statistics import mean
import matplotlib.pyplot as plt



lats = []
timeouts = 0


def get(url):
    global timeouts
    try:
        conn = http.client.HTTPConnection(url, timeout=5, port=8080)
        start = datetime.now()
        conn.request("GET", "/")
        response = conn.getresponse()
        end = datetime.now()
        lats.append((end - start).seconds)
    except socket.timeout:
        timeouts += 1


urls = [
           "localhost",
       ] * 2500

with PoolExecutor(max_workers=50) as executor:
    for _ in executor.map(get, urls):
        pass

print(f"Mean: {mean(lats)}s")
plt.plot(lats)
plt.ylabel("Latency(s)")
plt.show()
