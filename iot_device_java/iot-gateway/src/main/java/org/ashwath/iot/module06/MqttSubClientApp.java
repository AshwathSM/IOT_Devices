package org.ashwath.iot.module06;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This is an application class
 * This app is a MqTT client which subscribes to a topic and 
 * displays the message whenever the broker sends the message through that topic
 * i.e if any other client sends a message through the topic, all the subscribed clients
 * will receive the message through this topic
 */
public class MqttSubClientApp {
	
	
	Logger _logger = Logger.getLogger(MqttSubClientApp.class.getName());	
	private static MqttSubClientApp _App;
	private MqttClientConnector _clientConn;
	
	public MqttSubClientApp() {
		super();
	}
	
	/*Main method of the app which creates the app instance
	 * tries to start the app 
	 * */
	public static void main(String[] args) {
		
		_App = new MqttSubClientApp();
		
		try {
			_App.start();
		}catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/*
	 * Start method of the application
	 */
	public void start()
	{
		/*Create the MqttClientConnector instance and connect to the broker*/
		_clientConn = new MqttClientConnector();
		_clientConn.connect();
		
		/*topic name to subscribe to*/
		String topicName = "test";
		
		/*Subscribe to a topic*/
		_clientConn.subscribeToTopic(topicName);
	
		
		/*The commented parts below are just for test*/
		
		/*Wait for 60 seconds for a message to arrive on the topic
		 * */ 
//		try {
//			/*Sleep time of 60 seconds*/
//			Thread.sleep(1000*60);
		
//		}catch(InterruptedException e)
//		{
		/*Raise an exception in case of an interrupt*/
//			Thread.currentThread().interrupt();
//			_logger.log(Level.WARNING, "interrupted", e);
//		}
		
		
		/*Unsubscribe from the topic*/
//		_clientConn.unsubscribeToTopic(topicName);
		
		/*Disconnect from the topic*/
//		_clientConn.disconnect();
		
	}

}
