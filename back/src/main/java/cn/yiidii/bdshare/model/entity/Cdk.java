package cn.yiidii.bdshare.model.entity;

import cn.yiidii.bdshare.model.enums.CdkType;
import cn.yiidii.pigeon.common.core.base.entity.Entity;
import cn.yiidii.pigeon.common.core.base.enumeration.Status;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * cdk
 *
 * @author YiiDii Wang
 * @create 2021-11-27 00:52
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("cdk")
public class Cdk {

    private Long id;

    private CdkType type;

    private String cdk;

    private String remark;

    private Integer times;

    private Integer expireTime;

    private Status status;

    private Integer createTime;

    private Integer updateTime;

}
