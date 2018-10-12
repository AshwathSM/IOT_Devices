'''
Created on Oct 6, 2018

@author: ashwath
'''

from time import sleep
import threading
from labs.common.SensorData import SensorData
from sense_hat import SenseHat
from labs.common.ActuatorData import ActuatorData
from labs.module02.SmtpClientConnector import SmtpClientConnector
from labs.common import ConfigConst
from labs.common import ConfigUtil
from labs.module03.TempActuatorEmulator import TempActuatorEmulator

class TempSensorAdaptor(threading.Thread):
    '''
    classdocs
    '''
    enableAdaptor = False
    prevTempSet = False
    isPrevTempSet = False
    curTemp = 0
    
    #random temperature limits
    lowVal = 0
    highVal = 30
    
    #probe rate
    rateInSec = 10
    
    #keep the alert threshold to +/- 10 degrees
    alertDiff = 3
    
    #Instantiate SensorData class
    sensorData = SensorData()
    
    #Instantiate ActuatorData class
    actuatorData = ActuatorData()
    
    #Instantiate snese_hat class
    sensehat = SenseHat()
    
    #Instantiate SmtpClientConnector class
    connector = SmtpClientConnector()
    
    
    tempActuatorEmulator = TempActuatorEmulator()
    
    config = ConfigUtil.ConfigUtil('../../../data/ConnectedDevicesConfig.props')
    config.loadConfig()
    
    nominalTemp = float(config.getProperty(ConfigConst.CONSTRAINED_DEVICE,ConfigConst.NOMINAL_TEMP))
    

    def __init__(self, rateInSec=10):
        '''
        Constructor
        '''
        super(TempSensorAdaptor,self).__init__()
        #make sure the rate is 5 seconds
        if rateInSec > 10:
                self.rateInSec = rateInSec
    
    def run(self):
        while True:
            #execute if the thread is enabled
            if self.enableAdaptor:
                
                #Get the temperature reading from sense_hat module                
                self.curTemp = self.sensehat.get_temperature_from_humidity()
                
                #save and process this measurement in sensorData instance
                self.sensorData.addValue(self.curTemp)
                
                print('\n-------------------------')
                print('New sensor Readings:')
                print(' '+str(self.sensorData))
                
                #Check if the this is the first reading, if so just move on till the next reading
                if self.isPrevTempSet == False:
                        
                    self.isPrevTempSet = True
                #If there have been already some readings, then go ahead and calculate average temperature
                else:
                    #If the current temperature is not in the range of avg_temp-10 < avg_temp < avg_temp+10
                    if ((abs(self.curTemp - self.sensorData.getAvgValue()))>=self.alertDiff):
                        
                        
                        temp_diff = abs(self.curTemp - self.sensorData.getAvgValue())
                        
                        
                        print('\n Current temp exceeds average by > '+ str(temp_diff)+ '. Triggering alert...')
                        #Send warning mail to the client
                        self.connector.publishMessage('Exceptional sensor Data  [test]', self.sensorData)
                        
                        
                        self.actuatorData.setValue(temp_diff)
                        if(self.curTemp> self.nominalTemp):
                            #If the temperature is out of threshold send the data to actuator                        
                            self.actuatorData.setCommand('DECREASE')
                        
                        elif (self.curTemp< self.nominalTemp):
                            self.actuatorData.setCommand('INCREASE')
                            
                        else:
                            
                            print('\nIdeal temperature')
                            self.actuatorData.setCommand('IDEAL')
                        
                        self.tempActuatorEmulator.processMessage(self.actuatorData)
                        
                        
                        
            #sleep for 5 seconds
            sleep(self.rateInSec)
                        