package cn.yiidii.bdshare.common.util;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.bdshare.model.entity.BDSvipInfo;

/**
 * BD 工具类
 *
 * @author YiiDii Wang
 * @create 2021-11-25 22:40
 */
public class BDUtil {

    /**
     * 通过svipinfo构建cookie
     *
     * @param info   svip信息
     * @param bdclnd bdclnd
     * @return cookie
     */
    public static String getCookieWithSvipInfo(BDSvipInfo info, String bdclnd) {
        return StrUtil.format("BDUSS={};STOKEN={};BDCLND={}", info.getBduss(), info.getStoken(), bdclnd);
    }
}
