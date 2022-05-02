package cn.yiidii.bdshare.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * cdk类型
 *
 * @author YiiDii Wang
 * @create 2021-11-27 00:53
 */
@Getter
@AllArgsConstructor
public enum CdkType {
    TIMES(0),
    PERIOD(10),
    ;

    @EnumValue
    Integer code;
}
