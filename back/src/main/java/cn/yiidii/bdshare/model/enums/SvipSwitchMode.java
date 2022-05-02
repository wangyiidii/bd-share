package cn.yiidii.bdshare.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author YiiDii Wang
 * @create 2021-11-26 23:17
 */
@Getter
@AllArgsConstructor
public enum SvipSwitchMode {

    /**
     * 固定一个, 默认最新添加的一个
     */
    SINGLETON("固定一个"),
    RANDOM("随机");

    @EnumValue
    String desc;
}
