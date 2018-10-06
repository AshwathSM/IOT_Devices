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
        
        if rateInSec>0:
            self.rateInSec=rateInSec


    def run(self):
        while True:
            if self.enableAdaptor:
                print('\n--------------------')
                print('New system performance readings:')
                print(' ' + str(psutil.cpu_stats()))
                print(' ' + str(psutil.virtual_memory()))
                print(' ' + str(psutil.sensors_temperatures(False)))
            sleep(self.rateInSec)
        