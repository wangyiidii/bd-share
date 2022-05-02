package cn.yiidii.bdshare.model.form;

import cn.yiidii.bdshare.model.enums.CdkType;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * 保存cdk form
 *
 * @author YiiDii Wang
 * @create 2021-11-27 01:00
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SavedCdkForm {

    @NotNull(message = "cdk类型不能为空")
    private CdkType type = CdkType.TIMES;

    @NotNull(message = "cdk不能为空")
    private String cdk;

    @NotNull(message = "备注不能为空")
    private String remark;

    private Integer times = 3;

    private Integer expireTime;

}
