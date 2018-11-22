package org.ashwath.iot.module06;

import java.util.logging.Logger;

public class MqttSubClientApp {
	
	
	Logger _logger = Logger.getLogger(MqttSubClientApp.class.getName());
	
	private static MqttSubClientApp _App;
	private MqttClientConnector _clientConn;
	
	public MqttSubClientApp() {
		super();
	}
	
	public static void main(String[] args) {
		
		_App = new MqttSubClientApp();
		
		try {
			_App.start();
		}catch(Exception e)
		{
			e.printStackTrace();
		}		


	}
	
	public void start()
	{
		_clientConn = new MqttClientConnector();
		_clientConn.connect();
		
		String topicName = "test";
//		String payload = "this is a test retest......";
		
		_clientConn.subscribeToTopic(topicName);
//		_clientConn.publishMessage(topicName, 0, payload.getBytes());
		

		
		
		
//		_clientConn.messageArrived(topicName, message);
		
//		_clientConn.disconnect();
		
	}

}
