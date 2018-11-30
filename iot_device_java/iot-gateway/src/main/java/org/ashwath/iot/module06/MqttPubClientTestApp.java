package org.ashwath.iot.module06;

import java.util.logging.Logger;

/*
 * This is an application class
 * This app is a MqTT client which creates a message publishes to a topic 
 * (Sends the information across a topic so that broker and all the clients subscribed to
 * the topic can see the message)
 * 
 */
public class MqttPubClientTestApp {

	Logger _logger = Logger.getLogger(MqttPubClientTestApp.class.getName());	
	private static MqttPubClientTestApp _App;
	private MqttClientConnector _clientConn;
	
	public MqttPubClientTestApp() {
		super();
	}
	/*Main method of the application, tries to starts the application, 
	 * logs the information, in case of a failure logs an exception*/
	public static void main(String[] args) {
		
		_App = new MqttPubClientTestApp();
		
		try {
			_App.start();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/*Start function of the application
	 * 
	 */
	public void start()
	{
		/*Create the instance of the ClientConnector and connects it to the broker*/
		_clientConn = new MqttClientConnector();
		_clientConn.connect();
		
		/*creation of test topic and payload (data to send)*/
		String topicName = "test";
		String payload = "this is a test ......";
		
		
//		_clientConn.subscribeToTopic(topicName);
//		_clientConn.publishMessage(topicName, 0, payload.getBytes());
//		_clientConn.publishMessage(topicName, 1, payload.getBytes());
		
		/*publish the message to the topic with desired qos level*/
		_clientConn.publishMessage(topicName, 2, payload.getBytes());

		/*Disconnect after publishing (not necessary)*/
		_clientConn.disconnect();
		
	}
}
