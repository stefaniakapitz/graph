package de.unistuttgart.ims.fictionnet.imp;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Singleton Class which processes the IMSInteractionObject by calling a webservice
 * 
 * @author Andre Blessing
 *
 */
public class IMSAnalyzer {

	public static final String URL_ANALYSER = "http://clarin05.ims.uni-stuttgart.de/fictionnetanalyzer";

	private static IMSAnalyzer instance;

	private WebResource webResource_analyzer;

	private IMSAnalyzer() {
		Client client = Client.create();
		webResource_analyzer = client.resource(URL_ANALYSER);
	}

	synchronized public static IMSAnalyzer getInstance() {
		if (instance == null)
			instance = new IMSAnalyzer();
		return instance;
	}

	/**
	 * sends an IMSInteractionObject to an webservice and returns a newly augmented IMSInteractionObject
	 * 
	 * @param imsInteractionObject
	 * @return imsInteractionObject
	 */
	public IMSInteractionObject analyze(IMSInteractionObject imsInteractionObject) {

		ClientResponse response;
		Gson gson = new Gson();
		response = webResource_analyzer.accept("application/json").post(ClientResponse.class,
				gson.toJson(imsInteractionObject));
		String result = response.getEntity(String.class);
		return gson.fromJson(result, IMSInteractionObject.class);

	}

}
