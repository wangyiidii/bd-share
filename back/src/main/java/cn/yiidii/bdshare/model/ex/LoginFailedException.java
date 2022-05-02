package cn.yiidii.bdshare.model.ex;

import cn.yiidii.pigeon.common.core.exception.BaseUncheckedException;
import cn.yiidii.pigeon.common.core.exception.code.BaseExceptionCode;

/**
 * 登录失败异常
 *
 * @author YiiDii Wang
 * @create 2021-11-25 21:37
 */
public class LoginFailedException extends BaseUncheckedException {

    public LoginFailedException() {
        super(BDExceptionCode.LOGIN_FAILED);
    }

}
