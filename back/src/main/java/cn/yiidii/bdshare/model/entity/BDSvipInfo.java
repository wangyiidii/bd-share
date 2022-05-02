package cn.yiidii.bdshare.model.entity;

import cn.yiidii.pigeon.common.core.base.enumeration.Status;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 百度svip信息
 *
 * @author YiiDii Wang
 * @create 2021-11-25 15:09
 */
@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("bd_svip_info")
public class BDSvipInfo{

    private Long id;

    @TableField(value = "alias")
    private String alias;

    @TableField(value = "bduss")
    private String bduss;

    @TableField(value = "s_token")
    private String stoken;

    @TableField(value = "ua")
    private String ua;

    private Status status;

    private Integer createTime;

    private Integer updateTime;

}
