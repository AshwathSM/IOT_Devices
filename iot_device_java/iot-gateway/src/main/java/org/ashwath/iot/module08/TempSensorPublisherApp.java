package org.ashwath.iot.module08;


import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

public class TempSensorPublisherApp {
	
	
	Logger _logger = Logger.getLogger(TempSensorPublisherApp.class.getName());
	
	String token = "A1E-ntVJzB9Nm7a9ai9EN6AZJsjqTKXRHp";
	String pemFileName = "/home/ashwath/Downloads/connectedDocs/ubidots.pem";
	
	private static TempSensorPublisherApp _App;
	private MqttClientConnector _clientConn;
	
	public TempSensorPublisherApp() {
		super();
	}
	
	public static void main(String[] args) {
		
		_App = new TempSensorPublisherApp();
		
		
		try {
			_App.start();
		}catch(Exception e)
		{
			e.printStackTrace();
		}		


	}
	
	public void start()
	{
		_clientConn = new MqttClientConnector("things.ubidots.com", token, pemFileName);
		_clientConn.connect();
		
		String topicName = "/v1.6/devices/homeIoTGateway/tempSensor";
		
		String payload;
		JSONObject obj = new JSONObject();
		
		while(true) {
			
			try {
				Thread.sleep(1000*60);
			}catch(InterruptedException e)
			{
				Thread.currentThread().interrupt();
				_logger.log(Level.WARNING, "interrupted", e);
			}
			
			Random rand = new Random();
			int val = rand.nextInt(40) +1;
				
			obj.put("value", val);
			
		//	String payload = "{\"tempSensor\":\"30\"}";
				
			payload = obj.toJSONString();
				

			_clientConn.publishMessage(topicName, 0, payload.getBytes());
					
		
//			_clientConn.disconnect();
		}
		
	}

}
