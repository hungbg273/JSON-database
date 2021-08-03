package subclass;

import com.google.gson.JsonElement;

public class Response {
    private String response = null;
    private String reason = null;
    private JsonElement value = null;
    public Response(String response, String reason, JsonElement value) {
        this.response = response;
        if (this.response == "OK") {
            this.value = value;
            return;
        }
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public String getResponse() {
        return response;
    }

    public JsonElement getValue() {
        return value;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }
}