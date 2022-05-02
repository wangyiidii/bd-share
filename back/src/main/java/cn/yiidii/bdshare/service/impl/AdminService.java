package cn.yiidii.bdshare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.yiidii.bdshare.common.constant.BDShareConstant;
import cn.yiidii.bdshare.model.bo.BDCache;
import cn.yiidii.bdshare.model.bo.SystemConfig;
import cn.yiidii.bdshare.model.entity.Dictionary;
import cn.yiidii.bdshare.model.enums.SvipSwitchMode;
import cn.yiidii.bdshare.model.ex.LoginFailedException;
import cn.yiidii.bdshare.model.form.LoginForm;
import cn.yiidii.bdshare.model.vo.LoginSuccessVO;
import cn.yiidii.bdshare.service.IDictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * AdminService
 *
 * @author YiiDii Wang
 * @create 2021-11-26 22:32
 */
@Component
@RequiredArgsConstructor
public class AdminService implements ApplicationRunner {

    private static SystemConfig globalSystemConfig = null;
    private final IDictionaryService dictionaryService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initSystemConfig();
    }

    public SystemConfig getSystemConfig() {
        return  globalSystemConfig;
    }

    /**
     * 更新系统配置
     *
     * @param systemConfig 系统配置
     */
    public void updateSystemConfig(SystemConfig systemConfig) {
        List<Dictionary> systemDicts = dictionaryService.lambdaQuery()
                .eq(Dictionary::getType, BDShareConstant.SYSTEM_CONFIG)
                .list();
        Field[] fields = ReflectUtil.getFields(SystemConfig.class);
        Map<String, String> systemConfigMap = Arrays.stream(fields).collect(Collectors.toMap(
                f -> ReflectUtil.getFieldName(f),
                f -> StrUtil.toString(ReflectUtil.getFieldValue(systemConfig, f)),
                (f1, f2) -> f1
        ));
        systemDicts.forEach(d -> {
            if (systemConfigMap.containsKey(d.getCode())) {
                d.setValue(systemConfigMap.get(d.getCode()));
            }
        });
        boolean b = dictionaryService.saveOrUpdateBatch(systemDicts, 10);
        if (b) {
            globalSystemConfig = systemConfig;
        }
    }

    /**
     * svip切换模式
     *
     * @return svip切换模式
     */
    public SvipSwitchMode getSvipSwitchMode() {
        return globalSystemConfig.getSvipSwitchMode();
    }

    /**
     * 登录
     *
     * @param form form
     */
    public LoginSuccessVO login(LoginForm form) {
        // 比较
        boolean success = StrUtil.equals(form.getUsername(), globalSystemConfig.getUsername())
                && StrUtil.equals(form.getPassword(), globalSystemConfig.getPassword());
        if (!success) {
            throw new LoginFailedException();
        }
        // token放入缓存
        String token = RandomUtil.randomStringUpper(32);
        BDCache.INSTANCE.setToken(token);
        return BeanUtil.toBean(globalSystemConfig, LoginSuccessVO.class).setToken(token);
    }

    /**
     * 初始化配置
     */
    private void initSystemConfig() {
        List<Dictionary> systemDicts = dictionaryService.lambdaQuery()
                .eq(Dictionary::getType, BDShareConstant.SYSTEM_CONFIG)
                .list();
        Map<String, String> systemConfigMap = systemDicts.stream().collect(Collectors.toMap(Dictionary::getCode, Dictionary::getValue, (e1, e2) -> e1));
        boolean nullPresent = systemConfigMap.values().stream().filter(StrUtil::isBlank).findAny().isPresent();
        if (nullPresent) {
            throw new IllegalArgumentException("系统配置出错");
        }
        if (Objects.isNull(globalSystemConfig)) {
            synchronized (AdminService.class) {
                if (globalSystemConfig == null) {
                    globalSystemConfig = BeanUtil.toBean(systemConfigMap, SystemConfig.class);
                }
            }
        }
    }

}
