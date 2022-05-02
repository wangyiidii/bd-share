package cn.yiidii.bdshare.model.ex;

import cn.yiidii.pigeon.common.core.exception.BaseUncheckedException;
import cn.yiidii.pigeon.common.core.exception.code.BaseExceptionCode;

/**
 * BD通用异常
 *
 * @author YiiDii Wang
 * @create 2021-11-25 21:37
 */
public class BDException extends BaseUncheckedException {

    public BDException() {
        super(BDExceptionCode.GENERAL_EXCEPTION);
    }

    public BDException(int code, String message) {
        super(code, message);
    }

    public BDException(BaseExceptionCode baseExceptionCode) {
        super(baseExceptionCode);
    }
}
