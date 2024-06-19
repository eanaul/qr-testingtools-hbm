package dev.hbm.qris_testingtools.AppConfiguration.HttpTemplate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class HttpResponse<T> {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss", timezone = "Asia/Jakarta")
    String timeStamp = new Date().toString();
    int responseCode;
    String responseReason;
    String responseMessage;
    T responseData;

    public HttpResponse(int responseCode, String responseReason, String responseMessage) {
        this.responseCode = responseCode;
        this.responseReason = responseReason;
        this.responseMessage = responseMessage;
    }

    public HttpResponse(String responseMessage, int responseCode, String responseReason, T responseData) {
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
        this.responseReason = responseReason;
        this.responseData = responseData;
    }

    public HttpResponse(String responseMessage, int responseCode, String responseReason) {
        Date timeStamp = new Date();
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
        this.responseReason = responseReason;
        this.responseData = null;
    }

    @Override
    public String toString() {
        return "{" +
                "timeStamp='" + timeStamp + '\'' +
                ", responseCode=" + responseCode +
                ", responseReason='" + responseReason + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                ", responseData=" + responseData +
                '}';
    }
}
