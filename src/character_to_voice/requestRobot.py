# -*- coding: utf-8 -*-
import urllib
import urllib2
import json
import sys
test_data = {'key': '7486312636912ce0a97a729374f7e980', 'info': sys.argv[1], 'userid': '123456'}
test_data_urlencode = urllib.urlencode(test_data)
requrl = "http://www.tuling123.com/openapi/api"
req = urllib2.Request(url=requrl, data=test_data_urlencode)
res_data = urllib2.urlopen(req)
res = res_data.read()

res=json.loads(res)
print res["text"]