package org.ashwath.iot.module06;

import java.util.logging.Logger;

public class MqttPubClientTestApp {

	Logger _logger = Logger.getLogger(MqttPubClientTestApp.class.getName());
	
	private static MqttPubClientTestApp _App;
	private MqttClientConnector _clientConn;
	
	public MqttPubClientTestApp() {
		super();
	}
	
	public static void main(String[] args) {
		
		_App = new MqttPubClientTestApp();
		
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
		_clientConn = new MqttClientConnector();
		_clientConn.connect();
		
		String topicName = "test";
		String payload = "this is a test ......";
		
		_clientConn.subscribeToTopic(topicName);
//		_clientConn.publishMessage(topicName, 0, payload.getBytes());
//		_clientConn.publishMessage(topicName, 1, payload.getBytes());
		_clientConn.publishMessage(topicName, 2, payload.getBytes());
//		
		
		
//		_clientConn.messageArrived(topicName, message);
		
		_clientConn.disconnect();
		
	}
}
