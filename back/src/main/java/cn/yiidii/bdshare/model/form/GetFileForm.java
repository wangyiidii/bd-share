package cn.yiidii.bdshare.model.form;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;


/**
 * @author YiiDii Wang
 * @create 2021-11-26 09:41
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuppressWarnings("all")
public class GetFileForm {

    @NotNull(message = "参数不完整")
    private Long share_uk;
    @NotNull(message = "参数不完整")
    private Long shareid;
    @NotNull(message = "参数不完整")
    private String bdclnd;

    // file
    private String server_filename;
    private String parent_path;
    @NotNull(message = "参数不完整")
    private String app_id;
    @NotNull(message = "参数不完整")
    private Long fs_id;
    private Integer isdir;
    private Long size;
    private String path;

}
