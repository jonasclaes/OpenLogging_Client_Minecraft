package be.jonasclaes.openlogging.client.minecraft.communication;

public class HTTPResponseException extends Exception {
    public HTTPResponseException() {
        super();
    }

    public HTTPResponseException(String message) {
        super(message);
    }
}
