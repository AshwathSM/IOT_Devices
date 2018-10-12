'''
Created on Oct 6, 2018

@author: ashwath
'''
from labs.common.ActuatorData import ActuatorData
from RPi import GPIO

class TempActuatorEmulator():   

    def __init__(self):
        
        self.thisActuatorData = ActuatorData()
        self.thisActuatorData.setValue(20)
        self.thisActuatorData.setCommand('IDEAL')
     
    def processMessage(self, actuatorData):
        
        
        if(self.thisActuatorData!=actuatorData):
        
            if (actuatorData.getCommand() == 'INCREASE'):           
                print('Increasing temperature')
                GPIO.set_rotation(actuatorData.getValue())
            
            elif (actuatorData.getCommand() == 'DECREASE'):
                print('Decreasing temperature')
                GPIO.set_rotation(actuatorData.getValue())        
            else:
                print('maintaining the ideal temperature')
                GPIO.set_rotation(actuatorData.getValue())
                
            self.thisActuatorData = actuatorData
        