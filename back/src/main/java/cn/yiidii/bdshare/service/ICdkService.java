package cn.yiidii.bdshare.service;

import cn.yiidii.bdshare.model.entity.Cdk;
import cn.yiidii.pigeon.common.core.base.BaseSearchParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * cdk 业务接口
 *
 * @author YiiDii Wang
 * @create 2021-11-03 22:20
 */
public interface ICdkService extends IService<Cdk> {

    IPage<Cdk> list(BaseSearchParam searchParam);

    Cdk checkCdkFromContext();

    void addUsedTimeFromContext();
}
