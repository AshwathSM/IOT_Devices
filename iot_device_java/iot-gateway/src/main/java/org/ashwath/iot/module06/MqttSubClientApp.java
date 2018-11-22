package org.ashwath.iot.module06;

import java.util.logging.Level;
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
		
		_clientConn.subscribeToTopic(topicName);
	
		try {
			Thread.sleep(1000*60);
		}catch(InterruptedException e)
		{
			Thread.currentThread().interrupt();
			_logger.log(Level.WARNING, "interrupted", e);
		}
		
		_clientConn.unsubscribeToTopic(topicName);

//		_clientConn.disconnect();
		
	}

}
