'''
Created on Oct 6, 2018

@author: ashwath
'''

from time import sleep
from labs.module02 import TempSensorEmulator

#Instantiate the thread class (create a thread)
sysTempSensAdaptor = TempSensorEmulator.TempSensorEmulator()
sysTempSensAdaptor.daemon = True

print("Starting system performance app daemon thread...")
sysTempSensAdaptor.enableEmulator=True

#start the thread and give a sleep time of 5 seconds- the temperature is monitored every 5 seconds
sysTempSensAdaptor.start()
while (True):
    sleep(10)
    pass