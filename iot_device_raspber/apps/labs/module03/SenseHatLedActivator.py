'''
Created on Oct 6, 2018

@author: ashwath

Description: This module controls how the sensehat LED lights behave (actuator)
depending on the temperature sensor adaptor command 
'''

from time import sleep
from sense_hat import SenseHat
import threading
class SenseHatLedActivator(threading.Thread):
    
    '''
    Set all the default values to the class variables
    '''
    enableLed  = False
    rateInSec  = 1
    rotateDeg  = 270
    sh  = None
    displayMsg = None
   
    def __init__(self, rotateDeg = 270, rateInSec = 1):
        
        super(SenseHatLedActivator, self).__init__()
        
        #Make sure the rate is 5 seconds
        if rateInSec > 0:
            self.rateInSec = rateInSec
            
        #Sensehat rotate degree
        if rotateDeg >= 0:
            self.rotateDeg = rotateDeg
        
        '''
        Instantiate Sensehat class
        This class is a substitution for the actual Sensehat hardware        
        This module just emulates the behaviour of an actual Sensehat device
        '''
        self.sh = SenseHat()
        
        #Attach the angle of rotation to sensehat instance
        self.sh.set_rotation(self.rotateDeg)
    
    '''
    This run method refreshed the Sensehat display every 5 seconds (rateInSec) 
    but when there is no message from the Sensor adaptor it displays the letter R
    '''    
    def run(self):
        while True:
            if self.enableLed:
                if self.displayMsg != None:
                    self.sh.show_message(str(self.displayMsg))
                else:
                    self.sh.show_letter(str('R'))
                
                sleep(self.rateInSec)
                self.sh.clear()
            
            sleep(self.rateInSec)
    
    '''
    getters and setters
    '''
    def getRateInSeconds(self):
        return self.rateInSec
    
    def setEnableLedFlag(self, enable):
        self.sh.clear()
        self.enableLed = enable
    
    def setDisplayMessage(self, msg):
        self.displayMsg = msg

