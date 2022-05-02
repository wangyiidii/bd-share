package cn.yiidii.bdshare.model.bo;

import cn.yiidii.bdshare.model.enums.SvipSwitchMode;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @author YiiDii Wang
 * @create 2021-11-26 22:37
 */
@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfig {

    private String username;
    private String password;
    private SvipSwitchMode svipSwitchMode;
    private String freeTimes;
    private String title;
    private String footer;
    private String cdkNotice;

}
