package org.ashwath.iot.module08;

import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.logging.Level;


/*
 * *
 * MqttClientConnector class implements the interface MqttCallback which
 * enables an application to be notified when asynchronous
 * events related to the client occur. In this scenario, in case of arrival of any MqTT packets
 * messageArrived() method is called and when the client sends a message, 
 * the messageArrived() method is called which displays the success/failure of a message
 *
 *In this program, the server is the Ubidots cloud services
 * 
 */

public class MqttClientConnector implements MqttCallback{
	
	private static final Logger _logger = Logger.getLogger(MqttClientConnector.class.getName());
	
	
	/*set all variables to default values
	 * use secure protocol ssl
	 * Ubidots cloud service as the host
	 * Ubidots uses 8883 as the port for ssl connection
	 * */
	private String _protocol = "ssl";
	private String _host = "things.ubidots.com";
	private int _port = 8883;	
	private String _clientID;
	private String _brokerAddr;	
	private MqttClient _mqttClient;	
	private String _userName;
	private String _authToken;
	private String _pemFileName;
	private boolean _isSecureConn = false;
	
	public MqttClientConnector()
	{
		/*create a default insecure client connection*/
		this(null, false);			
	}
	
	
	public MqttClientConnector(String host, boolean isSecure)
	{
		super();
		
		/*get a valid hostname*/
		if(host!=null && host.trim().length()>0)
		{
			this._host = host;
		}
		
		/*generate a random client ID for MqTT connection to the broker*/
		_clientID = MqttClient.generateClientId();	
		
		/*Display the client ID, broker URL*/
		_logger.info("client ID for broker connection: "+_clientID);		
		_brokerAddr = _protocol+ "://"+_host+":"+_port;
		_logger.info("URL for broker connection: "+_brokerAddr);		
		
	}
	
	   // constructor
	   /**
	    * Constructor.
	    *
	    * @param host        The name of the broker to connect.
	    * @param userName    The username for authorizing access to the broker.
	    * @param pemFileName The name of the certificate file to use. If null / invalid, ignored.
	    * 
	    * certificate is needed to avoid using the access keys in the code, which gives
	    * security against any service theft/identity theft
	    */
	 public MqttClientConnector(String host, String userName, String pemFileName)
	 {
		 super();
		 
		 /*use only valid host name*/
		 if (host != null && host.trim().length() > 0) 
		 {
			 _host = host;
		 }
		 
		 /*valid username, without any white spaces*/
		 if (userName != null && userName.trim().length() > 0) {
			 _userName = userName;
		 }
//		 if (authToken != null && authToken.trim().length() > 0) {
//			 _authToken = authToken;
//		 }
		 
		 /*use all default values for the connection and check if the certificate file exists*/
		 if (pemFileName != null) {
			 File file = new File(pemFileName); 
			 if (file.exists()) {
				 _protocol     = "ssl";
				 _port         = 8883;
				 _pemFileName  = pemFileName;
				 _isSecureConn = true;
				 _logger.info("PEM file valid. Using secure connection: " + _pemFileName);
			 } else {
				 _logger.warning("PEM file invalid. Using insecure connection: " + pemFileName);
			 }
		 }
		 _clientID   = MqttClient.generateClientId();
		 _brokerAddr = _protocol + "://" + _host + ":" + _port;
		 _logger.info("Using URL for broker conn: " + _brokerAddr);
	   }

	/*
	 * This method creates a MqTT client using specified connection options 
	 * (username is the public ID of Ubidots, persistence connection type, and no clean session)
	 */
	public void connect()
	{
		if(_mqttClient==null)
		{
			/*Use this type of persistence when reliability is not required/ memory restarts*/
			MemoryPersistence persistence = new MemoryPersistence();
			
			try {
				/*Create a MqTT client using the specified server URL, persistence and client ID
				 * add connection options to the client*/
				_mqttClient = new MqttClient(_brokerAddr, _clientID, persistence);
				MqttConnectOptions connOpts = new MqttConnectOptions();				
				connOpts.setUserName("A1E-adR67vFqGFdJK0GSztehlkrBF9PKgz");				
				connOpts.setCleanSession(false);
				/*valid username*/
				if(_userName!=null)
				{
					connOpts.setUserName(_userName);
				}				
				if(_isSecureConn)
					initSecureConnection(connOpts);				
				
				/*To enable notification in case of asynchronous events, like message arrival
				 * message delivery completion status etc*/
				_mqttClient.setCallback(this);
				/*add connection options to the client*/
				_mqttClient.connect(connOpts);
				
				_logger.info("Connected to broker: "+_brokerAddr);
				
			}catch(MqttException e) {
				/*log in case of failure to connect to the broker*/
				_logger.log(Level.SEVERE,"Failed to connect to broker: "+_brokerAddr, e);
			}
		}
	}
	
	
	/*disconnects from the broker
	 * logs the result
	 * logs failure in case of disconnct error*/	
	public void disconnect()
	{
		try {
			_mqttClient.disconnect();
			_logger.info("disconnected from the broker: "+_brokerAddr);			
		}catch(Exception e)
		{
			_logger.log(Level.SEVERE, "failed to disconnect from the broker: "+_brokerAddr, e);
		}
	}
	
	/*
	 * This function creates the topic, creates a message using 
	 * the string input, and publishes the message on the topic
	 * */
	public boolean publishMessage(String topic, int qosLevel, byte[] payload)
	{
		/*unsuccessful till the message is published*/
		boolean success = false;
		
		try {
			_logger.info("publishing message to the topic: "+ topic);
			
			/*create the MqTT message, 
			 * add the payload to it
			 * set the quality of service level*/
			MqttMessage mqttMsg = new MqttMessage();
			mqttMsg.setPayload(payload);
			mqttMsg.setQos(qosLevel);
			
			/*publish the message on the topic specified*/
			_mqttClient.publish(topic, mqttMsg);
			
			_logger.info("published message ID: "+mqttMsg.getId());
			
			/*set the success flag to true in case of a successful publish*/
			success=true;
			
		}catch(Exception e)
		{
			/*log the failure in case of error in publishing*/
			_logger.log(Level.SEVERE, "Failed to publish MQTT message: "+e.getMessage());
		}
		
		return success;
	}
	
	/*subscribe to all the topics of the server*/
	public boolean subscribeToAll() 
	{
		try {
			/*subscribe to topic $SYS/# -  all system topics*/
			_mqttClient.subscribe("$SYS/#");
			return true;
		}catch(Exception e)
		{
			_logger.log(Level.WARNING, "failed to subscribe to all topics: ", e);
		}
		
		return false;
	}
	
	/*subscribe to a specific topic log the success or failure*/
	public boolean subscribeToTopic(String topic)
	{
		try {
			
			_mqttClient.subscribe(topic);
			
			_logger.info("Subscribed to the topic: "+topic);
			return true;
		}catch(Exception e)
		{
			_logger.log(Level.WARNING, "failed to subscribe to the topic: "+topic, e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.Throwable)
	 * Asynchronous call, called when the connection to the broker/client is lost
	 * This function is implemented here to reconnect and display a warning
	 */
	public void connectionLost(Throwable cause) {		
		
		_logger.log(Level.WARNING, "connection to broker is lost", cause);
		connect();
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 * Asynchronous call, called when a message arrives from the server on a subscribed topic
	 * THis function is implemented here to display the message from the server on a specified topic
	 * log the success or the failure
	 */
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
//		message.getPayload().
		_logger.info("ARRIVED!!");
		
		_logger.info("Message arrived: "+ message.toString()+ "from topic: "+topic);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken)
	 * This is called when the publishing (delivery) is successful by a client
	 * THis is implemented here to display the success message with the token and log the success or dailure
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
		
		try {
			_logger.info("Delivery complete: "+token.getMessageId()+"-"+token.getResponse()+"-"+token.getMessage());
		}catch(Exception e)
		{
			_logger.log(Level.SEVERE, "Failed to retrieve message from the token", e);
		}
		
	}
	
	/*
	 * this function establishes a secure connection settings on the MqTT client
	 * 
	 */
	private void initSecureConnection(MqttConnectOptions connOpts)
	{
		try {
			_logger.info("Configuring TLS...");
			
			/*sets the SSL context and creates a keystore adds this to a new trust manage factory
			 * ssl Context is initialized with the trust manage factory and 
			 * this ssl context is added to the MqTT connection options
			 */
			SSLContext sslContext = SSLContext.getInstance("SSL");
			KeyStore keystore = readCertificate();			
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keystore);
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			connOpts.setSocketFactory(sslContext.getSocketFactory());
			
		}catch(Exception e)
		{
			/*log the failure in case of unsuccessful MqTT initialization*/
			_logger.log(Level.SEVERE, "failed to initialize MqTT connection", e);
		}
	}
	
	/*This function reads pem file from the local file  */
	private KeyStore readCertificate() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
	{
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream fis = new FileInputStream(_pemFileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		ks.load(null);
		
		while(bis.available()>0)
		{
			Certificate cert = cf.generateCertificate(bis);
			ks.setCertificateEntry("asm_store"+bis.available(), cert);
		}
		
		return ks;
	}

	
	
}
