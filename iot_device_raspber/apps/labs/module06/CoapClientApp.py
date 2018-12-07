'''
Created on Dec 4, 2018

@author: ashwath
'''

# import psutil
from time import sleep
from labs.module06 import CoapClientConnector

#Instantiate the thread class (create the thread)
coapClient = CoapClientConnector.CoapClientConnector()

resource = "temp"
payload = "ashwath says hi luck is life ardha"
payload1 = "another line from client"
payloadDelete = ""

# coapClient.handleDeleteTest(resource, payloadDelete)

coapClient.handleGetTest(resource)

# coapClient.handlePostTest(resource, payload)
# 
# coapClient.handleGetTest(resource)
# 
# coapClient.handlePutTest(resource, payload1) 
# 
# coapClient.handleGetTest(resource)

 