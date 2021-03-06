import email.message
import http.server
from http.server import HTTPServer, BaseHTTPRequestHandler, SimpleHTTPRequestHandler, CGIHTTPRequestHandler
import os
from oknem import domain

'''
[使用 mitmproxy 抓包](http://einverne.github.io/post/2017/02/mitmproxy.html)
[Mitmproxy 是怎么工作的？](https://www.oschina.net/translate/how-mitmproxy-works)
'''


class RHandler(CGIHTTPRequestHandler):
    def parse_request(self):
        ret = CGIHTTPRequestHandler.parse_request(self)

        print(self.requestline)
        print('client id %s ' % (self.client_address))
        # print(self.headers.get('REMOTE_ADDR'), self.headers.get(
        #     'HTTP_VIA'), self.headers.get('HTTP_X_FORWARDED_FOR'), self.address_string(), self.client_address)
        return ret

    def do_CONNECT(self):
        print("do_CONNECT, path: %s %s" % (self.path, self.client_address[0]))
        pass

    def do_GET(self):
        CGIHTTPRequestHandler.do_GET(self)
        print("do_GET, path: %s %s" % (self.path, self.client_address[0]))
        pass

    def do_HEAD(self):
        CGIHTTPRequestHandler.do_HEAD(self)
        print("do_HEAD, path: %s %s" % (self.path, self.client_address[0]))
        pass

    def do_POST(self):
        CGIHTTPRequestHandler.do_POST(self)
        print("do_POST, path: %s %s" % (self.path, self.client_address[0]))
        pass


def run(server_class=HTTPServer, handler_class=BaseHTTPRequestHandler):
    # print('local ip', get_host_ip())
    server_address = ('', 8889)
    print('listening %s' % (server_address,))
    httpd = server_class(server_address, handler_class)
    httpd.serve_forever()


def main():
    run(handler_class=RHandler)


if __name__ == "__main__":
    main()
