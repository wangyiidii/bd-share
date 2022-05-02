package cn.yiidii.bdshare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * bd 文件对象 DTO
 *
 * @author YiiDii Wang
 * @create 2021-11-25 23:25
 */
@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
public class BDFileDTO {

    // 这里都是百度响应的一些字段
    private String server_filename;
    private String parent_path;
    private String path;
    private String app_id = "250528";
    private Long fs_id;
    private Integer isdir;
    private Long size;
    private String server_mtime;

}
