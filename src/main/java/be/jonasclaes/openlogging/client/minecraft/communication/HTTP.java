package be.jonasclaes.openlogging.client.minecraft.communication;

public class HTTP {
    private static String endpoint;
    private static String id;

    public static void sendLog(String category, String message) {
        Thread thread = new Thread(new HTTPPoster(HTTP.endpoint, HTTP.id, category, message));
        thread.start();
    }

    public static String getEndpoint() {
        return endpoint;
    }

    public static void setEndpoint(String endpoint) {
        HTTP.endpoint = endpoint;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        HTTP.id = id;
    }
}
