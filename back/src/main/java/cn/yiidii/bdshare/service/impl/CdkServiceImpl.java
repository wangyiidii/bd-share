package cn.yiidii.bdshare.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.bdshare.common.constant.BDShareConstant;
import cn.yiidii.bdshare.mapper.CdkMapper;
import cn.yiidii.bdshare.model.entity.Cdk;
import cn.yiidii.bdshare.model.enums.CdkType;
import cn.yiidii.bdshare.service.ICdkService;
import cn.yiidii.pigeon.common.core.base.BaseSearchParam;
import cn.yiidii.pigeon.common.core.exception.BizException;
import cn.yiidii.pigeon.common.core.util.ContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * cdk 业务实现
 *
 * @author YiiDii Wang
 * @create 2021-07-31 23:33
 */
@Service
@Slf4j
public class CdkServiceImpl extends ServiceImpl<CdkMapper, Cdk> implements ICdkService {

    @Override
    public IPage<Cdk> list(BaseSearchParam searchParam) {
        LambdaQueryWrapper<Cdk> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.between(StringUtils.isNotBlank(searchParam.getStartTime()), Cdk::getCreateTime, searchParam.getStartTime(), searchParam.getEndTime());
        boolean isKeyword = StringUtils.isNotBlank(searchParam.getKeyword());
        queryWrapper.like(isKeyword, Cdk::getCdk, searchParam.getKeyword()).or(isKeyword)
                .like(isKeyword, Cdk::getId, searchParam.getKeyword());
        queryWrapper.eq(Cdk::getStatus, 0);
        // 分页查询
        Page<Cdk> page = new Page<>(searchParam.getCurrent(), searchParam.getSize());
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Cdk checkCdkFromContext() {
        String cdkStr = ContextUtil.get(BDShareConstant.CDK);
        Cdk cdk = this.lambdaQuery().eq(Cdk::getCdk, cdkStr).one();
        if (Objects.isNull(cdk)) {
            throw new BizException("cdk不存在");
        }
        CdkType type = cdk.getType();
        switch (type) {
            case TIMES: {
                if (cdk.getTimes() > 0) {
                    return cdk;
                }
                throw new BizException("cdk超过解析次数");
            }
            case PERIOD: {
                Integer expireTime = cdk.getExpireTime();
                if (expireTime > System.currentTimeMillis() / 1000) {
                    return cdk;
                }
                throw new BizException("cdk已过期");
            }
            default:
                throw new BizException("系统错误");
        }
    }

    @Override
    public void addUsedTimeFromContext() {
        String contextCdk = ContextUtil.get(BDShareConstant.CDK);
        if (StrUtil.isBlank(contextCdk)) {
            return;
        }
        Cdk cdk = this.lambdaQuery().eq(Cdk::getCdk, contextCdk).one();
        if (cdk.getType().equals(CdkType.PERIOD)) {
            return;
        }
        cdk.setTimes(cdk.getTimes() - 1);
        this.updateById(cdk);
    }
}
