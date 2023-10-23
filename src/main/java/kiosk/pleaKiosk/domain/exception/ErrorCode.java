package kiosk.pleaKiosk.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "해당하는 상품을 찾을 수 없습니다."),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "재고보다 요구수량이 많습니다"),
    CONFLICT(HttpStatus.CONFLICT,"해당 상품에 들어온 주문이 존재해서 상품정보 수정이 불가능합니다."),
    CONFLICT_DELETE(HttpStatus.CONFLICT,"해당 상품에 들어온 주문이 존재해서 상품정보 삭제가 불가능합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN,"주문 상태값 문제입니다"),
    FORBIDDEN_ORDER(HttpStatus.FORBIDDEN,"해당주문은 이미 승인되었습니다"),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "해당하는 주문을 찾을 수 없습니다."),
    NOT_FOUND_PAYMENT(HttpStatus.NOT_FOUND, "해당하는 결제를 찾을 수 없습니다"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"결제금액이 모자릅니다"),
    BAD_REQUEST_PAGE(HttpStatus.BAD_REQUEST,"페이지와 사이즈는 음수값일수 없습니다.");



    private final HttpStatus httpStatus;
    private final String description;

}