package cn.yiidii.bdshare.model.dto;

import cn.yiidii.bdshare.model.entity.BDSvipInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 百度svip信息DTO
 *
 * @author YiiDii Wang
 * @create 2021-11-25 15:09
 */
@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("all")
public class BDSvipInfoDTO extends BDSvipInfo {

    private String cookie;

}
