import os
from urllib.parse import urlparse
import sys

#Najlepiej uruchamiac to.

#Przyklad uruchomienia: python3 run_this.py https://www.put.poznan.pl/
#Wynik znajdzie się w outcome.txt

url = str(sys.argv).split()[1][1:-2]
os.system("scrapy runspider spider.py -a allowed_domain=\""+urlparse(url).netloc+"\" -a url=\""+url+"\" > test.txt")