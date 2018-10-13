'''
Created on Oct 12, 2018

@author: ashwath
'''
import smbus
import threading
from time import sleep
from labs.common import ConfigUtil
from labs.common import ConfigConst

i2cBus= smbus.SMBus(1) # Use I2C bus No.1 on Raspberry Pi3 +
enableControl = 0x2D
enableMeasure = 0x08

accelAddr = 0x1C # address for IMU (accelerometer)
magAddr = 0x6A #address for IMU (magnetometer)
pressAddr = 0x5C #address for pressure sensor
humidAddr = 0x5F #address for humidity sensor
begAddr = 0x28
totBytes = 6

DEFAULT_RATE_IN_SEC = 5

class I2CSenseHatAdaptor(threading.Thread):

    rateInSec = DEFAULT_RATE_IN_SEC
    
    enableAdaptor = False

    def __init__(self):
        
        super(I2CSenseHatAdaptor, self).__init__()
        self.config = ConfigUtil.ConfigUtil(ConfigConst.DEFAULT_CONFIG_FILE_NAME)
        self.config.loadConfig()
        
        print('Configuration data...\n' + str(self.config))
        
        self.initI2CBus()
        
    def initI2CBus(self):
        
        print("Initializing I2C bus and enabling I2C addresses...")
        i2cBus.write_byte_data(accelAddr, enableControl, enableMeasure)
        # TODO: do the same for the magAddr, pressAddr, and humidAddr
        # NOTE: Reading data from the sensor will look like the following:
        
#         data = i2cBus.read_i2c_block_data({sensor address}, {starting read address}, {number of bytes})
        
    def displayAccelerometerData(self):
        data = i2cBus.read_i2c_block_data(accelAddr, begAddr, totBytes)
        print('accelerometer reading: ')
        print(data)
    
    def displayMagnetometerData(self):
        data = i2cBus.read_i2c_block_data(magAddr, begAddr, totBytes)
        print('magnetometer reading: ')
        print(data)
        
    def displayPressureData(self):
        data = i2cBus.read_i2c_block_data(pressAddr, begAddr, totBytes)
        print('pressure sensor data: ')
        print(data)
        
        
    def displayHumidityData(self):
        data = i2cBus.read_i2c_block_data(humidAddr, begAddr, totBytes)
        print('humidity sensor data: ')
        print(data)
    
    
    def run(self):
        while True:
            if self.enableAdaptor:
                # NOTE: you must implement these methods
                self.displayAccelerometerData()
                self.displayMagnetometerData()
                self.displayPressureData()
                self.displayHumidityData()
            sleep(self.rateInSec)
        
        
        
        
        