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
		
		String topicName = "/v1.6/devices/homeIoTGateway/tempActuator";
		String payload = "this is a test ......";
		
		_clientConn.subscribeToTopic(topicName);
		
//		_clientConn.publishMessage(topicName, 0, payload.getBytes());
//		_clientConn.publishMessage(topicName, 1, payload.getBytes());
//		_clientConn.publishMessage(topicName, 2, payload.getBytes());
//		
		
		
//		_clientConn.messageArrived(topicName, message);
		
//		_clientConn.disconnect();
		
	}
}
