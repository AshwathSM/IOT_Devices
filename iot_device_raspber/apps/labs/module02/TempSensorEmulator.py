'''
Created on Sep 21, 2018

@author: ashwath
'''
from random import uniform
from time import sleep
import threading
from labs.common.SensorData import SensorData
from labs.module02.SmtpClientConnector import SmtpClientConnector

class TempSensorEmulator(threading.Thread):
    '''
    classdocs
    '''
    enableEmulator = False
    prevTempSet = False
    isPrevTempSet = False
    curTemp = 0
    lowVal = 0
    highVal = 30
    rateInSec = 10
    alertDiff = 10
    
    #Instantiate SensorData class
    sensorData = SensorData()
    
    #Instantiate SmtpClient class
    connector = SmtpClientConnector()

    def __init__(self, rateInSec=10):
        '''
        Constructor
        '''
        super(TempSensorEmulator,self).__init__()
        #make sure the rate is 5 seconds
        if rateInSec > 10:
                self.rateInSec = rateInSec
    
    def run(self):
        while True:
            #execute if the thread is enabled
            if self.enableEmulator:
                
                #randomly generate a temperature between 0 and 30- model the temperature sensor
                self.curTemp = uniform(float(self.lowVal),float(self.highVal))
                
                #save this measurement in sensorData instance
                self.sensorData.addValue(self.curTemp)
                
                print('\n-------------------------')
                print('New sensor Readings:')
                print(' '+str(self.sensorData))
                
                #TEST_LINE
                print('testing 3rd')
                
                #Check if the this is the first reading, if so just move on till the next reading
                if self.isPrevTempSet == False:
                    print('testing 3rd once again')    
                    self.isPrevTempSet = True
                #If there have been already some readings, then go ahead and calculate average temperature
                else:
                    #TEST_LINE
                    print('testing 1st')
                    
                    #If the current temperature is not in the range of avg_temp-10 < avg_temp < avg_temp+10
                    if ((abs(self.curTemp - self.sensorData.getAvgValue()))>=self.alertDiff):
                        #TEST_LINE
                        print('testing 2ndt')
                        print('\n Current temp exceeds average by > '+ str(self.alertDiff)+ '. Triggering alert...')
                        #If the temperature is out of threshold call publishMessage() to send alert mail
                        #Send the sensorData elements as the body for the message
                        self.connector.publishMessage('Exceptional sensor Data  [test]', self.sensorData)
            #sleep for 5 seconds
            sleep(self.rateInSec)
                        