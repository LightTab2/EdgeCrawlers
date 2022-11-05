import os
from flask import Flask, request, jsonify
from urllib.parse import urlparse
import subprocess

app = Flask(__name__)

def links_withot_duplicates():
    f = open("outcome.txt", 'r')
    file=f.read().splitlines()
    return list(set(file))

def scrap(url):
    ex = "scrapy runspider spider.py -a allowed_domain=\"" + urlparse(url).netloc + "\" -a url=\"" + url + "\""
    result = subprocess.run(ex, stdout=subprocess.PIPE)
    urls = []
    for burl in result.stdout.splitlines():
        urls.append(burl.decode("utf-8"))
    return list(set(urls))

@app.post("/scrap")
def scrap_links():
    if request.is_json:
        json = request.get_json()
        return jsonify({"links":scrap(json["url"])})



