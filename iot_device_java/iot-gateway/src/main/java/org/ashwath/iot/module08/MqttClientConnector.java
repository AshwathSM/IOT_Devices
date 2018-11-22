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

public class MqttClientConnector implements MqttCallback{
	
	private static final Logger _logger = Logger.getLogger(MqttClientConnector.class.getName());
	
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
	
	   // constructor
	   /**
	    * Constructor.
	    *
	    * @param host        The name of the broker to connect.
	    * @param userName    The username for authorizing access to the broker.
	    * @param pemFileName The name of the certificate file to use. If null / invalid, ignored.
	    */
	 public MqttClientConnector(String host, String userName, String pemFileName)
	 {
		 super();
		 if (host != null && host.trim().length() > 0) 
		 {
			 _host = host;
		 }
		 
		 if (userName != null && userName.trim().length() > 0) {
			 _userName = userName;
		 }
//		 if (authToken != null && authToken.trim().length() > 0) {
//			 _authToken = authToken;
//		 }
		 
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

	
	public void connect()
	{
		if(_mqttClient==null)
		{
			
			MemoryPersistence persistence = new MemoryPersistence();
			try {
				
				_mqttClient = new MqttClient(_brokerAddr, _clientID, persistence);
				MqttConnectOptions connOpts = new MqttConnectOptions();
				
				connOpts.setUserName("A1E-adR67vFqGFdJK0GSztehlkrBF9PKgz");
				
				connOpts.setCleanSession(false);
				
				if(_userName!=null)
				{
					connOpts.setUserName(_userName);
				}
				
				if(_isSecureConn)
					initSecureConnection(connOpts);				
				
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
		connect();
		
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
//		message.getPayload().
		
		_logger.info("Message arrived: "+ message.toString()+ "from topic: "+topic);
		
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		
		try {
			_logger.info("Delivery complete: "+token.getMessageId()+"-"+token.getResponse()+"-"+token.getMessage());
		}catch(Exception e)
		{
			_logger.log(Level.SEVERE, "Failed to retrieve message from the token", e);
		}
		
	}
	
	private void initSecureConnection(MqttConnectOptions connOpts)
	{
		try {
			_logger.info("Configuring TLS...");
			SSLContext sslContext = SSLContext.getInstance("SSL");
			KeyStore keystore = readCertificate();
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keystore);
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			connOpts.setSocketFactory(sslContext.getSocketFactory());
			
		}catch(Exception e)
		{
			_logger.log(Level.SEVERE, "failed to initialize MqTT connection", e);
		}
	}
	
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
