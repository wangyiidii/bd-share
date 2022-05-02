package cn.yiidii.bdshare.model.ex;

import cn.yiidii.pigeon.common.core.exception.code.BaseExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author YiiDii Wang
 * @create 2021-11-25 21:39
 */
@Getter
@AllArgsConstructor
public enum BDExceptionCode implements BaseExceptionCode {

    GENERAL_EXCEPTION(10000, "换个链接试试吧~"),
    NON_SVIP_ACCOUNT(10001, "未配置SVIP账户，请联系系统管理员!"),
    ERR_PARSE_BD_SHARE_INDEX(10002, "解析分享页貌似出了什么问题~"),
    BD_FILE_NOT_FOUND(10003, "指定文件不存在"),
    GET_SIGN_ERR(10004, "获取文件出错，换个链接试试吧~"),
    PWD_NOT_CORRECT(10005, "提取码不正确，换个链接试试吧~"),
    LOGIN_FAILED(10006, "登陆失败"),
    TOKEN_EXPIRED(10007, "token失效，请重新登录"),
    ;

    private int code;
    private String msg;
}
