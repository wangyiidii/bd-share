package cn.yiidii.bdshare.model.ex;

import cn.yiidii.pigeon.common.core.exception.BaseUncheckedException;

/**
 * token失效
 *
 * @author YiiDii Wang
 * @create 2021-11-25 21:37
 */
public class TokenExpiredException extends BaseUncheckedException {

    public TokenExpiredException() {
        super(BDExceptionCode.TOKEN_EXPIRED);
    }

}
