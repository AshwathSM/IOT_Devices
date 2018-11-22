package org.ashwath.iot.module06;

import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.logging.Level;

public class MqttClientConnector implements MqttCallback{
	
	private static final Logger _logger = Logger.getLogger(MqttClientConnector.class.getName());
	
	private String _protocol = "tcp";
	private String _host = "iot.eclipse.org";
	private int _port = 1883;
	
	private String _clientID;
	private String _brokerAddr;
	
	private MqttClient _mqttClient;
	
	
	public MqttClientConnector()
	{
		this(null, false);			
		
	}
	
	
	public MqttClientConnector(String host, boolean isSecure)
	{
		super();
		
		if(host!=null && host.trim().length()>0)
		{
			this._host = host;
		}
		

		_clientID = MqttClient.generateClientId();		
		_logger.info("client ID for broker connection: "+_clientID);
		
		_brokerAddr = _protocol+ "://"+_host+":"+_port;
		_logger.info("URL for broker connection: "+_brokerAddr);		
		
	}
	
	public void connect()
	{
		if(_mqttClient==null)
		{
			
			MemoryPersistence persistence = new MemoryPersistence();
			try {
				_mqttClient = new MqttClient(_brokerAddr, _clientID, persistence);
				MqttConnectOptions connOpts = new MqttConnectOptions();
				connOpts.setCleanSession(true);
				
				_logger.info("connecting to broker: "+_brokerAddr);
				
				_mqttClient.setCallback(this);
				_mqttClient.connect(connOpts);
				
				_logger.info("Connected to broker: "+_brokerAddr);
				
			}catch(MqttException e) {
				_logger.log(Level.SEVERE,"Failed to connect to broker: "+_brokerAddr, e);
			}
		}
	}
		
	public void disconnect()
	{
		try {
			_mqttClient.disconnect();
			_logger.info("connected to the broker: "+_brokerAddr);			
		}catch(Exception e)
		{
			_logger.log(Level.SEVERE, "failed to disconnect from the broker: "+_brokerAddr, e);
		}
	}
	
	
	public boolean publishMessage(String topic, int qosLevel, byte[] payload)
	{
		boolean success = false;
		try {
			_logger.info("publishing message to the topic: "+ topic);
			
			MqttMessage mqttMsg = new MqttMessage();
			
			mqttMsg.setPayload(payload);
			mqttMsg.setQos(qosLevel);
			
			_mqttClient.publish(topic, mqttMsg);
			
			_logger.info("published message ID: "+mqttMsg.getId());
			
			
			success=true;
			
		}catch(Exception e)
		{
			_logger.log(Level.SEVERE, "Failed to publish MQTT message: "+e.getMessage());
		}
		
		return success;
	}
	
	public boolean subscribeToAll() 
	{
		try {
			_mqttClient.subscribe("$SYS/#");
			return true;
		}catch(Exception e)
		{
			_logger.log(Level.WARNING, "failed to subscribe to all topics: ", e);
		}
		
		return false;
	}
	
	public boolean subscribeToTopic(String topic)
	{
		try {
			_mqttClient.subscribe(topic);
			return true;
		}catch(Exception e)
		{
			_logger.log(Level.WARNING, "failed to subscribe to the topic: "+topic, e);
		}
		return false;
	}

	public void connectionLost(Throwable cause) {
		
		
		_logger.log(Level.WARNING, "connection to broker is lost", cause);
//		connect();
		
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		_logger.info("Message arrived: "+ message+ " from topic: "+topic);
		
	}
	public void deliveryComplete(IMqttDeliveryToken token) {
		
		try {
			_logger.info("Delivery complete: "+token.getMessageId()+"-"+token.getResponse()+"-"+token.getMessage());
		}catch(Exception e)
		{
			_logger.log(Level.SEVERE, "Failed to retrieve message from the token", e);
		}
		
	}
	

	
	
}
