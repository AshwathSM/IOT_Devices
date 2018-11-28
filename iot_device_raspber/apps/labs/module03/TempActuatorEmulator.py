'''
Created on Oct 6, 2018

@author: ashwath

Description: This module takes the commands/messages from the Sensor adaptor, 
processes it and performs the tasks (actuation) according to that

'''
from labs.common.ActuatorData import ActuatorData
from RPi import GPIO
from labs.module03.SimpleLedActivator import SimpleLedActivator
from labs.module03.SenseHatLedActivator import SenseHatLedActivator

class TempActuatorEmulator():   

    def __init__(self):
        
        self.thisActuatorData = ActuatorData()
        self.thisActuatorData.setValue(20)
        self.thisActuatorData.setCommand('IDEAL')
    
    '''
    Processes the data from adaptor sets the GPIO lights to rotate 
    corresponding to the temperature value from the adaptor     
    ''' 
    def processMessage(self, actuatorData):
        
#         led = SenseHatLedActivator()
#         led.setEnableLedFlag(True)
#         led.start()
        
        
        if(self.thisActuatorData!=actuatorData):
            
            #If the command is to increase, display the same and command the GPIO to rotate 
            #according to the temperature value
            if (actuatorData.getCommand() == 'INCREASE'):           
                print('Increasing temperature')
                GPIO.set_rotation(actuatorData.getValue())
#                 led.run()
            
            #If the command is to decrease, display the same and command the GPIO to rotate 
            #according to the temperature value
            elif (actuatorData.getCommand() == 'DECREASE'):
                print('Decreasing temperature')
                GPIO.set_rotation(actuatorData.getValue()) 
                
            #If no temperature change display that the temperature is at the ideal state       
            else:
                print('maintaining the ideal temperature')
                GPIO.set_rotation(actuatorData.getValue())
                
            self.thisActuatorData = actuatorData
        