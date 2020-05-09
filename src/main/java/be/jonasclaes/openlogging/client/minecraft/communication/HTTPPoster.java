package be.jonasclaes.openlogging.client.minecraft.communication;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;

import java.io.IOException;

public class HTTPPoster implements Runnable {
    private String endpoint;
    private String id;
    private String category;
    private String message;

    public HTTPPoster(String endpoint, String id, String category, String message) {
        this.endpoint = endpoint;
        this.id = id;
        this.category = category;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            this.doPost();
        } catch (IOException | HTTPResponseException e) {
            e.printStackTrace();
        }
    }

    private void doPost() throws IOException, HTTPResponseException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(this.endpoint);

        JSONObject body = new JSONObject();
        body.put("source_id", this.id);
        body.put("category", category);
        body.put("message", message);

        StringEntity bodyEntity = new StringEntity(body.toString());
        httpPost.setEntity(bodyEntity);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() != 201) {
            throw new HTTPResponseException(String.format("HTTP logging backend responded with status code %d", response.getStatusLine().getStatusCode()));
        }

        httpClient.close();
    }
}
