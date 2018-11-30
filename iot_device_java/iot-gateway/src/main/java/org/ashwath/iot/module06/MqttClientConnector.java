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

/*
 * MqttClientConnector class implements the interface MqttCallback which
 * enables an application to be notified when asynchronous
 * events related to the client occur. In this scenario, in case of arrival of any MqTT packets
 * messageArrived() method is called and when the client sends a message, 
 * the messageArrived() method is called which displays the success/failure of a message
 */

public class MqttClientConnector implements MqttCallback{
	
	private static final Logger _logger = Logger.getLogger(MqttClientConnector.class.getName());	
	
	/*
	 * Default protocol tcp, and mqtt host (server) iot.eclipse.org and the default port is 1883 for MqTT
	 */
	private String _protocol = "tcp";
	private String _host = "iot.eclipse.org";
	private int _port = 1883;	
	private String _clientID;
	private String _brokerAddr;	
	private MqttClient _mqttClient;	
	
	/*
	 * Create connector to connect to default MqTT broker with no secure connection
	 */
	public MqttClientConnector()
	{
		this(null, false);		
	}
	
	/*
	 * Create connector to connect to the broker passed as the 
	 * argument and with explicit specification of security options
	 * 
	 */
	public MqttClientConnector(String host, boolean isSecure)
	{
		super();
		
		/*
		 * Use default host if the host specified is null
		 * Remove any extra spaces in the host name
		 */
		if(host!=null && host.trim().length()>0)
		{
			this._host = host;
		}
		

		/*
		 * Generate a random client ID for the connection based 
		 * on user's login ID and system time
		 */
		_clientID = MqttClient.generateClientId();		
		
		/*
		 * Display the client ID and broker address
		 */
		_logger.info("client ID for broker connection: "+_clientID);
		
		_brokerAddr = _protocol+ "://"+_host+":"+_port;
		_logger.info("URL for broker connection: "+_brokerAddr);		
		
	}
	
	
	/*
	 * This function is used to connect to the MqTT broker
	 * with specified connection options
	 */
	public void connect()
	{
		/*Create a new client only if no client is created*/
		if(_mqttClient==null)
		{
			/*Use this type of persistence when reliability is not required/ memory restarts*/
			MemoryPersistence persistence = new MemoryPersistence();
			
			try {
				/*create a new client using the persistence, client ID and broker address*/
				_mqttClient = new MqttClient(_brokerAddr, _clientID, persistence);
				
				MqttConnectOptions connOpts = new MqttConnectOptions();
				
				/*Start a clean session*/
				connOpts.setCleanSession(true);
				
				_logger.info("connecting to broker: "+_brokerAddr);
				
				/*To enable notification in case of asynchronous events, like message arrival
				 * message delivery completion status etc*/
				_mqttClient.setCallback(this);
				
				/*Try to connect to the broker using the specified options*/
				_mqttClient.connect(connOpts);
				
				_logger.info("Connected to broker: "+_brokerAddr);
				
			}catch(MqttException e) {
				
				/*Raise exception in case of connection failure*/
				_logger.log(Level.SEVERE,"Failed to connect to broker: "+_brokerAddr, e);
			}
		}
	}
	
	/*Function to disconnect the client from the broker	*/
	public void disconnect()
	{
		try {
			/*disconnect and log the information*/
			_mqttClient.disconnect();
			_logger.info("disconnected from the broker: "+_brokerAddr);
			
		}catch(Exception e)
		{
			/*raise exception when disconnection is failed*/
			_logger.log(Level.SEVERE, "failed to disconnect from the broker: "+_brokerAddr, e);
		}
	}
	
	/*
	 * This method creates the MqTT message to send by taking the bytes (any other data)
	 *  publishes (sends) the data through a topic (analogy: like a virtual channel)
	 * to the broker. It takes the quality of service (qos) as a parameter,
	 * (qos = 0, maximum one confirmation about the delivery
	 * qos=2, at least one confirmation of the message delivery
	 * qos = 3, exactly one confirmation per message )  
	 */
	public boolean publishMessage(String topic, int qosLevel, byte[] payload)
	{
		/*publish status is failure unless no data is sent*/
		boolean success = false;
		
		try {
			_logger.info("publishing message to the topic: "+ topic);
			
			/*Create MqTT message, attach the payload to that message, 
			 * and adds the desired quality/assurance(qos) to the message */
			MqttMessage mqttMsg = new MqttMessage();			
			mqttMsg.setPayload(payload);
			mqttMsg.setQos(qosLevel);
			
			/*Publish to the specific topic*/
			_mqttClient.publish(topic, mqttMsg);
			
			_logger.info("published message ID: "+mqttMsg.getId());
			
			/*set success to true in case of a successful publish*/
			success=true;
			
		}catch(Exception e)
		{
			/*Log the failure event in case of a publish failure*/
			_logger.log(Level.SEVERE, "Failed to publish MQTT message: "+e.getMessage());
		}
		
		return success;
	}
	
	/*
	 * This function subscribes to all the topics of the broker
	 * */	
	public boolean subscribeToAll() 
	{
		try {
			/*Subscribe to topic with name $SYS/# (all the topics in the system)*/
			_mqttClient.subscribe("$SYS/#");
			
			/*Set success to true if subscribing to all the topics in successful*/
			return true;
			
		}catch(Exception e)
		{
			/*In case of failure, log the warning*/
			_logger.log(Level.WARNING, "failed to subscribe to all topics: ", e);
		}
		
		return false;
	}
	
	/*
	 * This function subscribes the client to a specific topic (passed as the argument
	 * to the method)
	 */	 
	public boolean subscribeToTopic(String topic)
	{
		try {
			/*Subscribe to the 'topic' topic*/
			_mqttClient.subscribe(topic);
			
			/*set success flag to true in case of a successful subscription*/
			return true;
			
		}catch(Exception e)
		{
			/*Log a warning in case of failure to subscribe*/
			_logger.log(Level.WARNING, "failed to subscribe to the topic: "+topic, e);
		}
		return false;
	}
	
	/*
	 * This function unsubscribes the client from a specific topic which is passed as
	 * the argument to the method
	 */
	public boolean unsubscribeToTopic(String topic)
	{
		try {
			/*Unsubscribe the client from the specified topic*/
			_mqttClient.unsubscribe(topic);
			
			/*return true if unsubscribing is successful*/
			return true;
			
		}catch(Exception e)
		{
			/*Log a failure in case of an unsuccessful unsubscribe attempt*/
			_logger.log(Level.SEVERE, "failed to unsubscribe to the topic: "+topic, e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
	 * 
	 * This function is called in case of a loss of client connection from the broker,
	 * displays the cause of connection loss
	 */
	public void connectionLost(Throwable cause) {		
		
		_logger.log(Level.WARNING, "connection to broker is lost", cause);
//		connect();
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 * This method is called when a message arrives from the broker
	 */
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		/**Display the* message arrived along with the topic*/
		_logger.info("Message arrived: "+ message+ " from topic: "+topic);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
	 * Called when the message delivery to the broker is complete (gets the token from the 
	 * broker)
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
		
		try {
			/*logs the message delivery success information */
			_logger.info("Delivery complete: "+token.getMessageId()+"-"+token.getResponse()+"-"+token.getMessage());
			
		}catch(Exception e)
		{
			/*Raise an exception in case of failure to deliver the message*/
			_logger.log(Level.SEVERE, "Failed to retrieve message from the token", e);
		}
		
	}	
	
}
