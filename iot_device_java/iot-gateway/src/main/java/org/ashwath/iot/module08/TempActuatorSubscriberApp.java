package org.ashwath.iot.module08;

import java.util.logging.Logger;

public class TempActuatorSubscriberApp {

	Logger _logger = Logger.getLogger(TempActuatorSubscriberApp.class.getName());
	
	String token = "A1E-ntVJzB9Nm7a9ai9EN6AZJsjqTKXRHp";
	String pemFileName = "/home/ashwath/Downloads/connectedDocs/ubidots.pem";
	
	private static TempActuatorSubscriberApp _App;
	private MqttClientConnector _clientConn;
	
	
	public TempActuatorSubscriberApp() {
		super();
	}
	
	public static void main(String[] args) {
		
		_App = new TempActuatorSubscriberApp();
		
		try {
			_App.start();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
//		return 0;

	}
	
	public void start()
	{
		_clientConn = new MqttClientConnector("things.ubidots.com", token, pemFileName);
		_clientConn.connect();
		
		String topicName = "/v1.6/devices/homeiotgateway/tempactuator/lv";
//		String topicName = "$SYS/#";
		
		
		_clientConn.subscribeToTopic(topicName);
		

		
	}
}
