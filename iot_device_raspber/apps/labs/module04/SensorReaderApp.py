'''
Created on Oct 6, 2018

@author: ashwath
'''

from time import sleep

from labs.module04 import I2CSenseHatAdaptor

#Instantiate the thread class (create a thread)
sysI2CSenseHatAdaptor = I2CSenseHatAdaptor.I2CSenseHatAdaptor()
sysI2CSenseHatAdaptor.daemon = True

print("Starting system performance app daemon thread...")
sysI2CSenseHatAdaptor.enableAdaptor=True

#start the thread and give a sleep time of 5 seconds- the temperature is monitored every 5 seconds
sysI2CSenseHatAdaptor.start()
while (True):
    sleep(10)
    pass