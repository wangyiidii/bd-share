package cn.yiidii.bdshare.model.form;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * SaveBDSvipInfoForm
 *
 * @author YiiDii Wang
 * @create 2021-11-25 21:55
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuppressWarnings("all")
public class SaveBDSvipInfoForm {

    @NotNull(message = "名称不能为空")
    private String alias;

    @NotNull(message = "bduss不能为空")
    private String bduss;

    @NotNull(message = "stoken不能为空")
    private String stoken;

    @NotNull(message = "ua不能为空")
    private String ua;
}
