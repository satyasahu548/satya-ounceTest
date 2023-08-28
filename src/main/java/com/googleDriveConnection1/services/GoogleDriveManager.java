package com.googleDriveConnection1.services;

 
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

@Service
public class GoogleDriveManager {

	private static final String APPLICATION_NAME = "Technicalsand.com - Google Drive API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	private static String AUTHENTICATION_URL = "";
	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	private static final String CREDENTIALS_FILE_PATH = "C:\\New folder\\secret3.json";

	public Drive getInstance() throws GeneralSecurityException, IOException {
		// Build a new authorized API client service.
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME)
				.build();
 		System.out.println("****service.DEFAULT_SERVICE_PATH********* : "+service.getBaseUrl()+" ,service.DEFAULT_ROOT_URL : "+ service.getRootUrl()+" , service.DEFAULT_BATCH_PATH : "+service.getServicePath()+" service.DEFAULT_SERVICE_PATH"+service.toString() );
		return service;

	}

 
	private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		GoogleClientSecrets clientSecrets =null;
		
        try (FileInputStream fis = new FileInputStream(CREDENTIALS_FILE_PATH)) {
        	clientSecrets	= GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(fis));
        	System.out.println("clientSecrets.toPrettyString()************************** : "+clientSecrets.toPrettyString());
        Set<Entry<String, Object>> clientData	= clientSecrets.entrySet();
      int count=0;
        for(Entry<String, Object> clientSecrets2 : clientData) {
        	Class<? extends Entry> client = clientSecrets2.getClass();
        	Details details = (Details) clientSecrets2.getValue();
        	System.out.println("inside for loop : "+clientSecrets2.getKey());
         	 System.out.println(details.getAuthUri()+", "+details.getClientId()+" ,"+details.getClientSecret()+", "+details.getTokenUri());
         	AUTHENTICATION_URL+=details.getAuthUri()+"?access_type=offline&client_id="+details.getClientId()+"&redirect_uri="+details.getRedirectUris().get(0)+"&response_type=code&scope="+SCOPES.get(0);
        }
        System.out.println("AUTHENTICATION_URL : "+AUTHENTICATION_URL);
        } catch (IOException e) {
            e.printStackTrace();
        }
		

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
				.setAccessType("online")
				.build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("127.0.0.1").setPort(8089).build();
		
		System.out.println("***************************receiver.toString();***********************"+receiver.toString());

		Credential authorizationCodeInstalledApp = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	String url = 	authorizationCodeInstalledApp.getTokenServerEncodedUrl();
	
	System.out.println("***************************url***********************"+url);
		return authorizationCodeInstalledApp;
	}

}