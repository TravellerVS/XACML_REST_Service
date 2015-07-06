package smartie_dissertation.helperlib.rest;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.net.ssl.SSLContext;
////import javax.ws.rs.client.Client;
////import javax.ws.rs.client.ClientBuilder;
////import javax.ws.rs.client.Entity;
////import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.MultivaluedMap;
//
////import org.glassfish.jersey.SslConfigurator;
//import org.json.JSONObject;
//
//import smartie_dissertation.helperlib.log.MyLog;
//
//import com.sun.jersey.core.util.MultivaluedMapImpl;


public class SecureRestClient {

//	protected Client client;
//	protected WebTarget target;
//	
//	private SslConfigurator getSSLConfig(){
//		return SslConfigurator.newInstance();
//	}	
//	private SslConfigurator getSSLConfig(String trustStoreFile){
//		return getSSLConfig().trustStoreFile(trustStoreFile);
//	}
//	private SslConfigurator getSSLConfig(String trustStoreFile, String trustStorePassword){
//		return getSSLConfig(trustStoreFile).trustStorePassword( trustStorePassword);
//	}
//	private SslConfigurator getSSLConfig(String trustStoreFile, String trustStorePassword, String keyStoreFile){
//		return getSSLConfig(trustStorePassword, trustStorePassword).keyStoreFile(keyStoreFile);
//	}
//	private SslConfigurator getSSLConfig(String trustStoreFile, String trustStorePassword, String keyStoreFile, String keyPassword){
//		return getSSLConfig(trustStorePassword, trustStorePassword, keyStoreFile).keyPassword(keyPassword);
//	}
//	
//	private void initialize(SslConfigurator sslConfig, String restBaseURI){
//		SSLContext sslContext = sslConfig.createSSLContext();
//		this.client = ClientBuilder.newBuilder().sslContext(sslContext).build();
////		this.target = client.target("http://localhost:9998").path("resource");
//		this.target = client.target(restBaseURI);
//	}
//	
//	public SecureRestClient(String restBaseURI) {
//		initialize(getSSLConfig(), restBaseURI);		
////		SslConfigurator sslConfig = SslConfigurator.newInstance()
////		        .trustStoreFile("./truststore_client")
////		        .trustStorePassword("secret-password-for-truststore")
////		        .keyStoreFile("./keystore_client")
////		        .keyPassword("secret-password-for-keystore");
//	}
//	
//	public SecureRestClient(String restBaseURI, String trustStoreFile) {
//		initialize(getSSLConfig(trustStoreFile), restBaseURI);		
//	}	
//	public SecureRestClient(String restBaseURI, String trustStoreFile, String trustStorePassword) {
//		initialize(getSSLConfig(trustStoreFile, trustStorePassword), restBaseURI);		
//	}	
//	public SecureRestClient(String restBaseURI, String trustStoreFile, String trustStorePassword, String keyStoreFile) {
//		initialize(getSSLConfig(trustStoreFile, trustStorePassword, keyStoreFile), restBaseURI);		
//	}
//	public SecureRestClient(String restBaseURI, String trustStoreFile, String trustStorePassword, String keyStoreFile, String keyPassword) {
//		initialize(getSSLConfig(trustStoreFile, trustStorePassword, keyStoreFile, keyPassword), restBaseURI);		
//	}
//	
//	
//	public JSONObject postRequest(String functionName, HashMap<String, String> formData){
//		MultivaluedMap<String, String> restFormData = new MultivaluedMapImpl();
////			JSONObject inputJsonObj = new JSONObject();
//		for(Map.Entry<String, String> entry : formData.entrySet()){
//			restFormData.add(entry.getKey(), entry.getValue());	
////				inputJsonObj.put(entry.getKey(), entry.getValue());	
//		}
//		JSONObject responseJsonObject = new JSONObject();
//		try {			
//			responseJsonObject =
//					target.request(MediaType.APPLICATION_JSON_TYPE)
//					    .post(Entity.entity(formData,MediaType.APPLICATION_FORM_URLENCODED_TYPE),
//					    		JSONObject.class);
//		}catch (Exception e) {
//			MyLog.log("Exception happened with a REST call", MyLog.logMessageType.ERROR, this.getClass().getName(), e);
//		}
//		return responseJsonObject;	
//	}

}
