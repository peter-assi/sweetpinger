package appchk;

import java.util.Map;

public class PingResponse {
    private String status;
    private Map headers;
    private String content;
    private boolean errors;
    
    public PingResponse(String status, Map headers, String content, boolean errors) {
        this.status = status;
        this.headers = headers;
        this.content = content;
        this.errors = errors;
    }
    
    public PingResponse(String status, Map headers, boolean errors) {
        this(status, headers, null, errors);
    }
    
    public PingResponse(String status, Map headers) {
        this(status, headers, null, false);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map getHeaders() {
        return headers;
    }

    public void setHeaders(Map headers) {
        this.headers = headers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public boolean hasErrors(){
        return this.errors;
    }

}
