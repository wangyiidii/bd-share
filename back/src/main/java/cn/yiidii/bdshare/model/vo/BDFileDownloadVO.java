package cn.yiidii.bdshare.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 百度文件下载地址VO
 *
 * @author YiiDii Wang
 * @create 2021-11-26 13:12
 */
@Data
@Accessors(chain = true)
public class BDFileDownloadVO {

    private Long fsId;
    private String url;
    private String ua;
}
