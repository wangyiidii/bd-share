package cn.yiidii.bdshare.model.vo;

import cn.yiidii.bdshare.model.enums.SvipSwitchMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 系统配置VO
 *
 * @author YiiDii Wang
 * @create 2021-11-26 22:37
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessVO {

    private String username;
    private SvipSwitchMode svipSwitchMode;
    private Integer freeTimes;
    private String token;

}
