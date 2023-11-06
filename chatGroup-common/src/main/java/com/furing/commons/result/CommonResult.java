package com.furing.commons.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author furing
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResult<T> {

    private int code;

    private String message;

    private boolean isSuccess;

    private T data;

    public CommonResult() {
    }

    public CommonResult(int code, String message, boolean isSuccess, T data) {
        this.code = code;
        this.message = message;
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public CommonResult(int code, String message, boolean isSuccess) {
        this.code = code;
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public static <P> CommonResult<P> autoResult(Boolean isSuccess) {
        if (isSuccess) {
            return UnmodifiableCommonResult.SUCCESS;
        } else {
            return UnmodifiableCommonResult.FAIL;
        }
    }

    public static <P> CommonResult<P> autoResult(Boolean isSuccess, P data) {
        if (isSuccess) {
            return CommonResult.operateSuccess(data);
        } else {
            return CommonResult.operateFail(data);
        }
    }

    private static <P> CommonResult<P> operateFail(P data) {
        return new CommonResult<>(ResultConstant.FAIL_CODE, ResultConstant.OPERATE_FAIL_MESSAGE, false, data);
    }

    public CommonResult<T> operateFail() {
        return UnmodifiableCommonResult.FAIL;
    }

    public static <P> CommonResult<P> operateFailWithMessage(String message) {
        return new CommonResult<P>(ResultConstant.FAIL_CODE, message, false);
    }

    public static <P> CommonResult<P> operateSuccess(P data) {
        return new CommonResult<P>(ResultConstant.SUCCESS_CODE, ResultConstant.OPERATE_SUCCESS_MESSAGE, true, data);
    }

    public static <P> CommonResult<P> operateSuccess() {
        return UnmodifiableCommonResult.SUCCESS;
    }

    public static <P> CommonResult<P> operateNoPower(P p) {
        return new CommonResult<P>(ResultConstant.FORBIDDEN_CODE, ResultConstant.OPERATE_NO_POWER, false, p);
    }
    public CommonResult<T> operateNoPower() {
        return UnmodifiableCommonResult.NO_POWER;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", isSuccess=" + isSuccess +
                ", data=" + data +
                '}';
    }
}
