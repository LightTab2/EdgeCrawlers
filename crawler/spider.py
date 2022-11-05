import scrapy
from scrapy.linkextractors import LinkExtractor
from urllib.parse import urlparse

#pip install scrapy
#Przykładowy kod, który przechodzi przez stronkę wydziałową, a nie zwraca linków związanych ze stroną polibudy i zwraca je do pliku textowego
#scrapy runspider spider.py -a allowed_domain="www.put.poznan.pl" -a url="https://www.put.poznan.pl/pl/wladze-i-struktura/wydzialy"
class spider(scrapy.Spider):
    name="spider"
    f = open('outcome.txt', 'w')
    def __init__(self, url='', allowed_domain='', **kwargs):
        self.start_urls = [url]
        self.allowed_domains = allowed_domain
        super().__init__(**kwargs)

    def parse(self, response):
        extractor = LinkExtractor(deny_domains = self.allowed_domains)
        links = extractor.extract_links(response)
        f = open('outcome.txt', 'a')
        for link in links:
            print(urlparse(link.url).netloc)
            f.write(urlparse(link.url).netloc + "\n")