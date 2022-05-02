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
public class UpdateBDSvipInfoForm {

    @NotNull(message = "id不能为空")
    private Long id;

    private String alias;

    private String ua;

    private String bduss;

    private String stoken;

}
