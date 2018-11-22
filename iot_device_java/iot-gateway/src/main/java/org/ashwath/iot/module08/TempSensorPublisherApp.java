package org.ashwath.iot.module08;


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
		obj.put("value", 10);
//		
//		String payload = "{\"tempSensor\":\"30\"}";
		
		payload = obj.toJSONString();
		
//		_clientConn.subscribeToTopic(topicName);
		_clientConn.publishMessage(topicName, 0, payload.getBytes());
		

		
		
		
//		_clientConn.messageArrived(topicName, message);
		
//		_clientConn.disconnect();
		
	}

}
