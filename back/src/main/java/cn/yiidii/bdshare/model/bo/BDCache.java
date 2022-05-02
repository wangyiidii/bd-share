package cn.yiidii.bdshare.model.bo;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.lang.Singleton;
import cn.yiidii.bdshare.model.vo.BDFileDownloadVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 缓存
 * @author ed w
 * @since 1.0
 */

public class BDCache {

    /**
     * 单例
     */
    public static final BDCache INSTANCE = Singleton.get(BDCache.class);

    /**
     * 登录token缓存
     */
    @Getter
    @Setter
    private String token = "";

    /**
     * 文件缓存
     */
    @Getter
    private  TimedCache<String, BDFileDownloadVO> fileCache = CacheUtil.newTimedCache(60 * 60 * 1000 / 2);
}
