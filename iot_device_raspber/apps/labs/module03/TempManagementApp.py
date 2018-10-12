'''
Created on Oct 6, 2018

@author: ashwath
'''

from time import sleep

from labs.module03 import TempSensorAdaptor

#Instantiate the thread class (create a thread)
sysTempSensAdaptor = TempSensorAdaptor.TempSensorAdaptor()
sysTempSensAdaptor.daemon = True

print("Starting system performance app daemon thread...")
sysTempSensAdaptor.enableAdaptor=True

#start the thread and give a sleep time of 5 seconds- the temperature is monitored every 5 seconds
sysTempSensAdaptor.start()
while (True):
    sleep(10)
    pass