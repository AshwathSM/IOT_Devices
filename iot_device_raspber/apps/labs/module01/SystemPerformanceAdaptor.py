'''
Created on Sep 15, 2018

@author: ashwath
'''
import threading, psutil
from time import sleep
class SystemPerformanceAdaptor(threading.Thread):
    '''
    classdocs
    '''
    enableAdaptor =False
    rateInSec = 5
    
    def __init__(self, rateInSec=5):
        super(SystemPerformanceAdaptor,self).__init__()
        #Make sure the rate is 5 seconds
        if rateInSec>0:
            self.rateInSec=rateInSec
    '''
    Define a thread run method that runs executes every 5 seconds 
    (Check the temperature variation every 5 seconds)
    This function senses uses the psutil module to get information about sensors and cpu status etc
    and displays it on the console
    '''
    def run(self):
        while True:
            if self.enableAdaptor:
                print('\n--------------------')
                print('New system performance readings:')
                print(' ' + str(psutil.cpu_stats()))
                print(' ' + str(psutil.virtual_memory()))
                print(' ' + str(psutil.sensors_temperatures(False)))
            sleep(self.rateInSec)
        