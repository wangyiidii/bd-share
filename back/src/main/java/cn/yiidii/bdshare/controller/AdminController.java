package cn.yiidii.bdshare.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.bdshare.common.annotation.Admin;
import cn.yiidii.bdshare.model.bo.SystemConfig;
import cn.yiidii.bdshare.model.entity.BDSvipInfo;
import cn.yiidii.bdshare.model.entity.Cdk;
import cn.yiidii.bdshare.model.enums.CdkType;
import cn.yiidii.bdshare.model.form.LoginForm;
import cn.yiidii.bdshare.model.form.SaveBDSvipInfoForm;
import cn.yiidii.bdshare.model.form.SaveOrUpdateSystemConfigForm;
import cn.yiidii.bdshare.model.form.SavedCdkForm;
import cn.yiidii.bdshare.model.form.UpdateBDSvipInfoForm;
import cn.yiidii.bdshare.model.form.UpdatedCdkForm;
import cn.yiidii.bdshare.model.vo.LoginSuccessVO;
import cn.yiidii.bdshare.service.IBDSvipInfoService;
import cn.yiidii.bdshare.service.ICdkService;
import cn.yiidii.bdshare.service.impl.AdminService;
import cn.yiidii.pigeon.common.core.base.BaseSearchParam;
import cn.yiidii.pigeon.common.core.base.R;
import cn.yiidii.pigeon.common.core.base.enumeration.Status;
import cn.yiidii.pigeon.common.core.exception.BizException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理
 *
 * @author YiiDii Wang
 * @create 2021-11-25 10:14
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "后台管理")
@RequiredArgsConstructor
public class AdminController {

    private final IBDSvipInfoService bdSvipInfoService;
    private final AdminService adminService;
    private final ICdkService cdkService;

    @PostMapping("login")
    public R<?> login(@RequestBody @Valid LoginForm form) {
        LoginSuccessVO loginSuccessVO = adminService.login(form);
        return R.ok(loginSuccessVO, "登陆成功");
    }

    @Admin
    @GetMapping("sysConfig")
    public R<?> getSystemConfig() {
        return R.ok(adminService.getSystemConfig());
    }

    @Admin
    @PostMapping("sysConfig")
    public R<?> updateSystemConfig(@RequestBody @Valid SaveOrUpdateSystemConfigForm form) {
        SystemConfig systemConfig = BeanUtil.toBean(form, SystemConfig.class);
        adminService.updateSystemConfig(systemConfig);
        return R.ok(null, "保存成功");
    }

    @Admin
    @GetMapping("svipInfo/list")
    @ApiOperation("svip列表")
    public R<?> listSvip(BaseSearchParam searchParam) {
        IPage<BDSvipInfo> cdkList = bdSvipInfoService.list(searchParam);
        return R.ok(cdkList);
    }

    /**
     * 添加一个svip
     *
     * @param form form
     * @return R
     */
    @Admin
    @PostMapping("svipInfo")
    @ApiOperation("添加svip信息")
    public R<?> add(@RequestBody @Validated SaveBDSvipInfoForm form) {
        Optional<BDSvipInfo> svipInfoOptional = bdSvipInfoService.lambdaQuery().eq(BDSvipInfo::getAlias, form.getAlias()).oneOpt();
        if (svipInfoOptional.isPresent()) {
            throw new BizException(StrUtil.format("名称【{}】的SVIP已存在", form.getAlias()));
        }
        BDSvipInfo bdSvipInfo = BeanUtil.toBean(form, BDSvipInfo.class);
        bdSvipInfo.setStatus(Status.ENABLED);
        Long st = System.currentTimeMillis() / 1000;
        bdSvipInfo.setCreateTime(st.intValue());
        bdSvipInfo.setUpdateTime(st.intValue());
        boolean b = bdSvipInfoService.save(bdSvipInfo);
        if (!b) {
            R.ok(bdSvipInfo, "保存失败，稍后重试一下吧~");
        }
        return R.ok(bdSvipInfo, "保存成功");
    }

    /**
     * 更新一个svip
     *
     * @param form form
     * @return R
     */
    @Admin
    @PutMapping("svipInfo")
    @ApiOperation("更新svip信息")
    public R<?> update(@RequestBody @Validated UpdateBDSvipInfoForm form) {
        BDSvipInfo bdSvipInfoExist = bdSvipInfoService.getById(form.getId());
        if (Objects.isNull(bdSvipInfoExist)) {
            throw new BizException("svip信息不存在, 保存失败");
        }
        if (!StrUtil.equals(bdSvipInfoExist.getAlias(), form.getAlias())) {
            Integer count = bdSvipInfoService.lambdaQuery().eq(BDSvipInfo::getAlias, form.getAlias()).count();
            if (count > 0) {
                throw new BizException(StrUtil.format("名称【{}】的SVIP已存在", form.getAlias()));
            }
        }
        BDSvipInfo bdSvipInfo = BeanUtil.toBean(form, BDSvipInfo.class);
        boolean b = bdSvipInfoService.updateById(bdSvipInfo);
        Long st = System.currentTimeMillis() / 1000;
        bdSvipInfo.setUpdateTime(st.intValue());
        if (!b) {
            return R.failed("更新失败，稍后重试一下吧~");
        }
        return R.ok(bdSvipInfo, "保存成功");
    }

    /**
     * 删除svip信息
     *
     * @param ids form
     * @return R
     */
    @Admin
    @DeleteMapping("svipInfo/del")
    @ApiOperation("删除svip信息")
    public R<?> del(@RequestBody @Validated @NotEmpty(message = "id不能为空") List<Long> ids) {
        boolean b = bdSvipInfoService.removeByIds(ids);
        if (!b) {
            R.ok("删除失败，稍后重试一下吧~");
        }
        return R.ok("删除成功");
    }

    /**
     * 添加cdk
     *
     * @param form form
     * @return R
     */
    @Admin
    @PostMapping("cdk")
    @ApiOperation("添加cdk")
    public R<?> addCdk(@RequestBody @Validated SavedCdkForm form) {
        CdkType cdkType = form.getType();
        switch (cdkType) {
            case TIMES: {
                Integer times = form.getTimes();
                if (Objects.isNull(times) || times <= 0) {
                    return R.failed("次数不能为空");
                }
                break;
            }
            case PERIOD: {
                Integer expireTime = form.getExpireTime();
                if (Objects.isNull(expireTime) && expireTime <= System.currentTimeMillis() / 1000) {
                    return R.failed("失效时间不正确");
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("cdk类型不正确");
            }
        }

        Optional<Cdk> cdkOptional = cdkService.lambdaQuery().eq(Cdk::getCdk, form.getCdk()).oneOpt();
        if (cdkOptional.isPresent()) {
            throw new BizException(StrUtil.format("CDK【{}】已存在", form.getCdk()));
        }
        Cdk cdk = BeanUtil.toBean(form, Cdk.class);
        Long timestamp = System.currentTimeMillis() / 1000;
        cdk.setCreateTime(timestamp.intValue());
        cdk.setUpdateTime(timestamp.intValue());
        boolean b = cdkService.saveOrUpdate(cdk);
        if (!b) {
            R.failed("保存失败");
        }
        return R.ok(cdk, "保存成功");
    }

    /**
     * 添加cdk
     *
     * @param form form
     * @return R
     */
    @Admin
    @PutMapping("cdk")
    @ApiOperation("更新cdk")
    public R<?> updateCdk(@RequestBody @Validated UpdatedCdkForm form) {

        Cdk cdkDb = cdkService.getById(form.getId());
        if (Objects.isNull(cdkDb)) {
            throw new BizException("svip信息不存在, 保存失败");
        }
        if (!StrUtil.equals(cdkDb.getCdk(), form.getCdk())) {
            Integer count = bdSvipInfoService.lambdaQuery().eq(BDSvipInfo::getAlias, form.getCdk()).count();
            if (count > 0) {
                throw new BizException(StrUtil.format("CDK【{}】已存在", form.getCdk()));
            }
        }

        CdkType cdkType = form.getType();
        switch (cdkType) {
            case TIMES: {
                Integer maxTimes = form.getMaxTimes();
                if (!(Objects.nonNull(maxTimes) && maxTimes > 0)) {
                    return R.failed("最大使用次数不能为空");
                }
                break;
            }
            case PERIOD: {
                Integer expireTime = form.getExpireTime();
                if (Objects.isNull(expireTime) && expireTime <= System.currentTimeMillis() / 1000) {
                    return R.failed("失效时间不正确");
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("cdk类型不正确");
            }
        }

        Cdk cdk = BeanUtil.toBean(form, Cdk.class);
        boolean b = cdkService.saveOrUpdate(cdk);
        if (!b) {
            R.failed("保存失败");
        }
        return R.ok(cdk, "保存成功");
    }


    @Admin
    @GetMapping("cdk/list")
    @ApiOperation("cdk列表")
    public R<?> listCdk(BaseSearchParam searchParam) {
        IPage<Cdk> cdkList = cdkService.list(searchParam);
        return R.ok(cdkList);
    }

    /**
     * 删除cdk
     *
     * @param ids form
     * @return R
     */
    @Admin
    @DeleteMapping("cdk/del")
    @ApiOperation("删除cdk")
    public R<?> delCdk(@RequestBody @Validated @NotEmpty(message = "id不能为空") List<Long> ids) {
        boolean b = cdkService.removeByIds(ids);
        if (!b) {
            R.ok("删除失败，稍后重试一下吧~");
        }
        return R.ok("删除成功");
    }

}
