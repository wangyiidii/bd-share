package cn.yiidii.bdshare.service;

import cn.yiidii.bdshare.model.entity.BDSvipInfo;
import cn.yiidii.bdshare.model.entity.Cdk;
import cn.yiidii.pigeon.common.core.base.BaseSearchParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 百度svip信息操作接口
 *
 * @author YiiDii Wang
 * @create 2021-11-25 21:49
 */
@SuppressWarnings("all")
public interface IBDSvipInfoService extends IService<BDSvipInfo> {

    IPage<BDSvipInfo> list(BaseSearchParam searchParam);

}
