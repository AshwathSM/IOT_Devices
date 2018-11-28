'''
Created on Sep 15, 2018

@author: ashwath

Description: This app gets the CPU environment data every 5 seconds and displays it on the console
'''
# import psutil
from time import sleep
from labs.module01 import SystemPerformanceAdaptor

#Instantiate the thread class (create the thread)
sysPerfAdaptor = SystemPerformanceAdaptor.SystemPerformanceAdaptor()
sysPerfAdaptor.daemon = True

print("Starting system performance app daemon thread...")

#Enable the adaptor
sysPerfAdaptor.enableAdaptor=True

#start the thread, and run it every 5 seconds (give a sleep time of 5 seconds)
sysPerfAdaptor.start()
while (True):
    sleep(5)
    pass
