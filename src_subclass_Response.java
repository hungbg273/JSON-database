package subclass;

public class Response {
    private String response = null;
    private String reason = null;
    private String value = null;
    public Response(String response, String reason, String value) {
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

    public String getValue() {
        return value;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
