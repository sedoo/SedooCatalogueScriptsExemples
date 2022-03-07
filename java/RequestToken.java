package fr.sedoo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.keycloak.OAuth2Constants;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.constants.ServiceUrlConstants;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.util.JsonSerialization;

public class RequestToken {
	
	public static void main(String[] args) throws IOException {
		RequestToken main = new RequestToken();
		AccessTokenResponse token = main.getToken();
		System.out.println(token.getToken());
	}
	
	 public AccessTokenResponse getToken() throws IOException {
	        CloseableHttpClient client = HttpClientBuilder.create().build();

	        try {
	            HttpPost post = new HttpPost(KeycloakUriBuilder.fromUri("https://sso.aeris-data.fr/auth")
	                    .path(ServiceUrlConstants.TOKEN_PATH).build("aeris"));
	            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	            formparams.add(new BasicNameValuePair("username", "MY_USERNAME"));
	            formparams.add(new BasicNameValuePair("password", "MY_PASSWORD"));
	            formparams.add(new BasicNameValuePair(OAuth2Constants.GRANT_TYPE, "password"));
	            formparams.add(new BasicNameValuePair(OAuth2Constants.CLIENT_ID, "aeris-public"));
	            UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
	            post.setEntity(form);

	            HttpResponse response = client.execute(post);

	            int status = response.getStatusLine().getStatusCode();
	            HttpEntity entity = response.getEntity();
	            if (status != 200) {
	                String json = getContent(entity);
	                throw new IOException("Bad status: " + status + " response: " + json);
	            }
	            if (entity == null) {
	                throw new IOException("No Entity");
	            }
	            String json = getContent(entity);
	            return JsonSerialization.readValue(json, AccessTokenResponse.class);
	        } finally {
	            client.close();
	        }
	    }

	    private static String getContent(HttpEntity entity) throws IOException {
	        if (entity == null) { 
	        	return null;
	        }
	        else {
	        	return IOUtils.toString(entity.getContent(), Charset.defaultCharset());
	        }
  	    }

}
