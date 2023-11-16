package kiosk.pleaKiosk.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class commonResponse<T> {

    // API 응답 코드 Response
    private int resultCode;

    // API 응답 코드 Message
    private String resultMsg;

    // API 응답 결과 Response
    private T result;

    @Builder
    public commonResponse(final T result, final int resultCode, final String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Builder
    public commonResponse(final int resultCode, final String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
