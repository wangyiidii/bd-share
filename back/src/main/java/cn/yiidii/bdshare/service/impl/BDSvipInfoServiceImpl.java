package cn.yiidii.bdshare.service.impl;

import cn.yiidii.bdshare.mapper.BDSvipInfoMapper;
import cn.yiidii.bdshare.model.entity.BDSvipInfo;
import cn.yiidii.bdshare.model.entity.Cdk;
import cn.yiidii.bdshare.service.IBDSvipInfoService;
import cn.yiidii.pigeon.common.core.base.BaseSearchParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 百度svip信息操作实现
 *
 * @author YiiDii Wang
 * @create 2021-11-25 21:49
 */
@Service
public class BDSvipInfoServiceImpl extends ServiceImpl<BDSvipInfoMapper, BDSvipInfo> implements IBDSvipInfoService {

    @Override
    public IPage<BDSvipInfo> list(BaseSearchParam searchParam) {
        LambdaQueryWrapper<BDSvipInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.between(StringUtils.isNotBlank(searchParam.getStartTime()), BDSvipInfo::getCreateTime, searchParam.getStartTime(), searchParam.getEndTime());
        boolean isKeyword = StringUtils.isNotBlank(searchParam.getKeyword());
        queryWrapper.like(isKeyword, BDSvipInfo::getAlias, searchParam.getKeyword()).or(isKeyword)
                .like(isKeyword, BDSvipInfo::getId, searchParam.getKeyword());
        queryWrapper.eq(BDSvipInfo::getStatus, 0);
        // 分页查询
        Page<BDSvipInfo> page = new Page<>(searchParam.getCurrent(), searchParam.getSize());
        return this.baseMapper.selectPage(page, queryWrapper);
    }
}
