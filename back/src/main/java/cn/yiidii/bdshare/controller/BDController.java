package cn.yiidii.bdshare.controller;

import cn.yiidii.bdshare.model.bo.SystemConfig;
import cn.yiidii.bdshare.model.dto.BDSharePageDTO;
import cn.yiidii.bdshare.model.form.GetFileForm;
import cn.yiidii.bdshare.service.ICdkService;
import cn.yiidii.bdshare.service.impl.AdminService;
import cn.yiidii.bdshare.service.impl.BDShareService;
import cn.yiidii.pigeon.common.core.base.R;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AnonymousController
 *
 * @author YiiDii Wang
 * @create 2021-11-25 23:02
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Api(tags = "BD网盘解析")
public class BDController {

    private final BDShareService bdShareService;
    private final AdminService adminService;

    @GetMapping("info")
    public R<?> info() {
        SystemConfig systemConfig = adminService.getSystemConfig();

        JSONObject data = new JSONObject();
        data.put("title", systemConfig.getTitle());
        data.put("footer", systemConfig.getFooter());
        data.put("cdkNotice", systemConfig.getCdkNotice());
        return R.ok(data);
    }

    @GetMapping("parse")
    @ApiOperation("网盘解析")
    public R<?> parse(@RequestParam @NotNull(message = "网盘链接不能为空(示例：https://pan.baidu.com/s/xxxxxxx)") String url,
                      @RequestParam String pwd) {
        BDSharePageDTO bdSharePageDTO = bdShareService.getSharePage(url, pwd);
        return R.ok(bdSharePageDTO);
    }

    @PostMapping("/openDir")
    @ApiOperation("打开文件夹")
    public R<?> openDir(@RequestBody GetFileForm form) {
        return R.ok(bdShareService.openDir(form));
    }

    @PostMapping("/downloadUrl")
    @ApiOperation("获取下载链接")
    public R<?> getDownloadUrl(@RequestBody GetFileForm form) {
        return R.ok(bdShareService.getFile(form));
    }

}
