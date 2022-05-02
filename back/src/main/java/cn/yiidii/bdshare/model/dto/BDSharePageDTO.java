package cn.yiidii.bdshare.model.dto;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * bd 分享页 DTO
 * <p/>
 * 包含该分享页的一些信息和文件列表
 *
 * @author YiiDii Wang
 * @create 2021-11-25 22:54
 */
@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
public class BDSharePageDTO {

    // 分享页的一些信息

    private String bdstoken;
    private Long share_uk;
    private Long shareid;
    private String linkusername;
    private String username;
    private String description;
    private Long expiredType;
    private Long vip_level;
    private String photo;
    private String share_photo;
    /**
     * 文件列表F
     */
    private List<BDFileDTO> file_list;

    // 其他信息
    /**
     * 原始的数据
     */
    private JSONObject rawData;
    private String bdclnd;
}
