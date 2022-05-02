package cn.yiidii.bdshare.model.form;

import cn.yiidii.bdshare.model.enums.SvipSwitchMode;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 系统配表单
 *
 * @author YiiDii Wang
 * @create 2021-11-26 23:37
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SaveOrUpdateSystemConfigForm {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "svip切换模式不能为空")
    private SvipSwitchMode svipSwitchMode;

    @NotNull(message = "免费次数不能为空")
    private Integer freeTimes;

    private String title;

    private String footer;

    private String cdkNotice;
}
