'''
Created on Oct 6, 2018

@author: ashwath
'''
from random import uniform
class SenseHat():
    rotateDeg = 270
    clearFlag = False
    
    #Generate random temperature between lowVal and highVal
    lowVal = 15
    highVal = 25
    curTemp = 0

    def __init__(self):
        self.set_rotation(self.rotateDeg)

    def clear(self):
        self.clearFlag = True

    def get_humidity(self):
        # NOTE: This is just a sample
        return 48.5

    def get_temperature(self):
        return self.get_temperature_from_humidity()

    def get_temperature_from_humidity(self):
    # NOTE: This is just a sample
        #Generate a random temperature value between lowVal and highVal
        self.curTemp = uniform(float(self.lowVal),float(self.highVal))
    
        return self.curTemp;

    def get_temperature_from_pressure(self):
        return self.get_temperature_from_humidity()

    def get_pressure(self):
        # NOTE: This is just a sample     
        
        return 31.5

    def set_rotation(self, rotateDeg):
        self.rotateDeg = rotateDeg
        print('rotating by {rotateDeg}')

    def show_letter(self, val):
        print(val)

    def show_message(self, msg):
        print (msg)