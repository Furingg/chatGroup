package com.furing.commons.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.experimental.Accessors;

import static com.furing.commons.result.ResultConstant.*;

/**
 * @author furing
 */
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnmodifiableCommonResult<P> extends CommonResult<P> {
    protected static final UnmodifiableCommonResult SUCCESS = new UnmodifiableCommonResult<>(SUCCESS_CODE, OPERATE_SUCCESS_MESSAGE, true);
    protected static final UnmodifiableCommonResult FAIL = new UnmodifiableCommonResult<>(FAIL_CODE, OPERATE_FAIL_MESSAGE, false);
    protected static final UnmodifiableCommonResult NO_POWER = new UnmodifiableCommonResult<>(FORBIDDEN_CODE, OPERATE_NO_POWER, false);

    public UnmodifiableCommonResult(int code, String message, boolean isSuccess) {
        super.setCode(code);
        super.setMessage(message);
        super.setSuccess(isSuccess);
    }

    @Override
    public CommonResult<P> setCode(int code) {
        throw new UnsupportedOperationException("常量返回结果不允许被修改，如果需要修改结果请创建新的返回结果对象！");
    }

    @Override
    public CommonResult<P> setMessage(String message) {
        throw new UnsupportedOperationException("常量返回结果不允许被修改，如果需要修改结果请创建新的返回结果对象！");
    }

    @Override
    public CommonResult<P> setSuccess(boolean isSuccess) {
        throw new UnsupportedOperationException("常量返回结果不允许被修改，如果需要修改结果请创建新的返回结果对象！");
    }

    @Override
    public CommonResult<P> setData(P data) {
        throw new UnsupportedOperationException("常量返回结果不允许被修改，如果需要修改结果请创建新的返回结果对象！");
    }
}
