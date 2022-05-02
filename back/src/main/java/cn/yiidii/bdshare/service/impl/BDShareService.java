package cn.yiidii.bdshare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.bdshare.common.constant.BDShareConstant;
import cn.yiidii.bdshare.common.util.BDUtil;
import cn.yiidii.bdshare.model.bo.BDCache;
import cn.yiidii.bdshare.model.dto.BDFileDTO;
import cn.yiidii.bdshare.model.dto.BDSharePageDTO;
import cn.yiidii.bdshare.model.dto.BDSvipInfoDTO;
import cn.yiidii.bdshare.model.entity.BDSvipInfo;
import cn.yiidii.bdshare.model.enums.SvipSwitchMode;
import cn.yiidii.bdshare.model.ex.BDException;
import cn.yiidii.bdshare.model.ex.BDExceptionCode;
import cn.yiidii.bdshare.model.form.GetFileForm;
import cn.yiidii.bdshare.model.vo.BDFileDownloadVO;
import cn.yiidii.bdshare.service.IBDSvipInfoService;
import cn.yiidii.bdshare.service.ICdkService;
import cn.yiidii.pigeon.common.core.base.enumeration.Status;
import cn.yiidii.pigeon.common.core.util.HttpClientUtil;
import cn.yiidii.pigeon.common.core.util.dto.HttpClientResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * BDShareService
 *
 * @author YiiDii Wang
 * @create 2021-11-25 11:53
 */
@Component("bdShareService")
@RequiredArgsConstructor
public class BDShareService {

    private final IBDSvipInfoService bdSvipInfoService;
    private final AdminService adminService;
    private final ICdkService cdkService;

    /**
     * 获取分享页信息
     *
     * @param url url，示例：https://pan.baidu.com/s/xxxxxxxxxxxxx
     * @param pwd 提取码，无则留空
     * @return BDSharePageDTO
     */
    public BDSharePageDTO getSharePage(String url, String pwd) {
        // 预处理，提取url中的唯一id，这里和以后我都会以sid为名来处理
        String sid = this.preHandleUrl(url);
        // 获取bdclnd
        String bdclnd = this.getBDCLND(sid, pwd);
        // 获取随机一个svip信息
        BDSvipInfoDTO svipInfoBO = this.getABDSvipInfo(bdclnd);
        // 获取分享页信息
        return getShareInfo(sid, bdclnd, svipInfoBO);
    }

    /**
     * 获取文件的真是下载地址
     *
     * @param form form
     * @return 文件
     */
    public BDFileDownloadVO getFile(GetFileForm form) {
        BDSvipInfoDTO svipInfoDTO = this.getABDSvipInfo(form.getBdclnd());
        return this.getRealDownloadUrl(form, svipInfoDTO);
    }

    /**
     * 文件夹下的文件
     *
     * @param form form
     * @return 文件
     */
    public List<BDFileDTO> openDir(GetFileForm form) {
        BDSvipInfoDTO svipInfoDTO = this.getABDSvipInfo(form.getBdclnd());
        return this.openDir(form, svipInfoDTO);
    }

    /**
     * 预处理，预处理，提取url中的唯一id，这里和以后我都会以sid为名来处理
     *
     * @param url 示例：https://pan.baidu.com/s/xxxxxxxxxxxxx
     * @return sid，就是xxxxxxxxxxxxx
     */
    private String preHandleUrl(String url) {
        String sid = null;
        if (StrUtil.startWithAny(url, BDShareConstant.HTTP, BDShareConstant.HTTPS)) {
            sid = url.replace(BDShareConstant.BD_SHARE_URL_PREFIX, "");
        }
        if (StrUtil.isEmpty(sid)) {
            throw new IllegalArgumentException("无效的链接");
        }
        return sid;
    }

    /**
     * 获取BDCLND，在响应头的setCookie上
     *
     * @param sid sid
     * @param pwd 提取码
     * @return bdclnd
     */
    private String getBDCLND(String sid, String pwd) {
        HttpResponse response = HttpRequest.get(StrUtil.format("https://pan.baidu.com/share/wxlist?clienttype=25&shorturl={}&pwd={}", sid, pwd)).header(Header.USER_AGENT, "netdisk").execute();
        List<String> setCookies = response.headerList(Header.SET_COOKIE.getValue());
        if (Objects.isNull(setCookies)) {
            throw new BDException(BDExceptionCode.PWD_NOT_CORRECT);
        }
        String bdclnd = setCookies.stream().filter(e -> e.contains("BDCLND")).map(e -> Arrays.stream(e.split("; ")).filter(e1 -> e1.contains("BDCLND")).findFirst().get().split("=")[1]).findFirst().orElseGet(null);
        if (StrUtil.isEmpty(bdclnd)) {
            throw new BDException();
        }
        return bdclnd;
    }

    /**
     * 获取一个svip信息
     *
     * @param bdclnd bdclnd
     * @return svip信息
     */
    private BDSvipInfoDTO getABDSvipInfo(String bdclnd) {
        // 获取所有svip账户
        List<BDSvipInfo> svipInfos = bdSvipInfoService.lambdaQuery().eq(BDSvipInfo::getStatus, Status.ENABLED).orderByDesc(BDSvipInfo::getCreateTime).list();
        if (CollUtil.isEmpty(svipInfos)) {
            throw new BDException(BDExceptionCode.NON_SVIP_ACCOUNT);
        }
        BDSvipInfo svipInfo;
        SvipSwitchMode svipSwitchMode = adminService.getSvipSwitchMode();
        switch (svipSwitchMode) {
            case SINGLETON: {
                svipInfo = svipInfos.get(0);
                break;
            }
            default: {
                int count = svipInfos.size();
                svipInfo = svipInfos.get(RandomUtil.randomInt(count));
            }
        }

        BDSvipInfoDTO infoBO = BeanUtil.toBean(svipInfo, BDSvipInfoDTO.class);
        // 设置cookie
        String cookie = BDUtil.getCookieWithSvipInfo(infoBO, bdclnd);
        infoBO.setCookie(cookie);
        return infoBO;
    }

    /**
     * 获取分享页面信息
     *
     * @param sid        sid
     * @param bdclnd     bdclnd
     * @param svipInfoBO 会员信息
     * @return 分享页
     */
    private BDSharePageDTO getShareInfo(String sid, String bdclnd, BDSvipInfoDTO svipInfoBO) {
        // 请求获取分享页信息
        HttpResponse response = HttpRequest.get(StrUtil.format("https://pan.baidu.com/s/{}", sid)).header(Header.USER_AGENT, BDShareConstant.CHROME_UA).header(Header.COOKIE, svipInfoBO.getCookie()).execute();
        // 正则从html获取json格式的数据(即：分享页数据)
        String dataJsonStr = ReUtil.get("locals.mset\\((\\{.*?\\})\\);", response.body(), 1);
        if (StrUtil.isBlank(dataJsonStr)) {
            throw new BDException(BDExceptionCode.ERR_PARSE_BD_SHARE_INDEX);
        }
        dataJsonStr = dataJsonStr.substring(dataJsonStr.indexOf("{"), dataJsonStr.lastIndexOf("}") + 1);
        JSONObject dataJo = JSONObject.parseObject(dataJsonStr);
        BDSharePageDTO bdSharePageDTO = JSONObject.toJavaObject(dataJo, BDSharePageDTO.class);
        // 每个分享页的bdclnd好像是固定的
        bdSharePageDTO.setBdclnd(bdclnd);
        bdSharePageDTO.setRawData(dataJo);
        return bdSharePageDTO;
    }

    /**
     * 获取文件真正的下载地址
     *
     * @param form       form
     * @param svipInfoBO svip
     * @return 文件信息
     * @throws Exception Exception
     */
    private BDFileDownloadVO getRealDownloadUrl(GetFileForm form, BDSvipInfoDTO svipInfoBO) {
        String fsIdStr = String.valueOf(form.getFs_id());
        BDFileDownloadVO cache = BDCache.INSTANCE.getFileCache().get(fsIdStr);
        if (Objects.nonNull(cache)) {
            return BeanUtil.toBean(cache, BDFileDownloadVO.class);
        }
        cdkService.checkCdkFromContext();
        // 请求获取sign和timestamp
        // 这里用hutool的请求工具获取的的响应json不完整，原因未知！！！ 这里用apache的
        Map<String, String> header = new HashMap<>();
        header.put(Header.COOKIE.getValue(), svipInfoBO.getCookie());
        header.put(Header.USER_AGENT.getValue(), BDShareConstant.CHROME_UA);
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = HttpClientUtil.doGet(StrUtil.format("https://pan.baidu.com/share/tplconfig?shareid={}&uk={}&fields=sign,timestamp&channel=chunlei&web=1&app_id=250528&clienttype=0", form.getShareid(), form.getShare_uk()), header, null);
        } catch (Exception e) {
            throw new BDException(BDExceptionCode.GET_SIGN_ERR);
        }

        JSONObject signJo = JSONObject.parseObject(httpClientResult.getContent());
        signJo = signJo.getJSONObject("data");
        Long timestamp = signJo.getLong("timestamp");
        String sign = signJo.getString("sign");

        // 请求download url, 最终的不限速链接在响应Location里面，需要设置UA下载
        String dlUrl = StrUtil.format("https://pan.baidu.com/api/sharedownload?app_id={}&channel=chunlei&clienttype=12&sign={}&timestamp={}&web=1", form.getApp_id(), sign, timestamp);
        JSONObject extraJo = new JSONObject();
        extraJo.put("sekey", URLUtil.decode(form.getBdclnd(), StandardCharsets.UTF_8));
        JSONArray fileJa = new JSONArray();
        fileJa.add(form.getFs_id());

        String formStr = StrUtil.format("encrypt=0&extra={}&fid_list=[{}]&primaryid={}&uk={}&product=share&type=nolimit", URLUtil.encodeQuery(extraJo.toJSONString(), StandardCharsets.UTF_8), form.getFs_id(), form.getShareid(), form.getShare_uk());
        HttpResponse response = HttpRequest.post(dlUrl).body(formStr, ContentType.FORM_URLENCODED.getValue()).header(Header.USER_AGENT, BDShareConstant.CHROME_UA).header(Header.COOKIE, svipInfoBO.getCookie()).header(Header.REFERER, "https://pan.baidu.com/disk/home").execute();
        JSONObject respJo = JSONObject.parseObject(response.body());
        Integer errno = respJo.getInteger("errno");
        if (!errno.equals(0)) {
            throw new BDException(errno, "获取下载链接发生错误");
        }
        String dlink = respJo.getJSONArray("list").getJSONObject(0).getString("dlink");

        HttpResponse resp = HttpRequest.get(dlink).header(Header.COOKIE, svipInfoBO.getCookie()).header(Header.USER_AGENT, svipInfoBO.getUa()).execute();
        List<String> locations = resp.headerList(Header.LOCATION.getValue());

        BDFileDownloadVO vo = new BDFileDownloadVO().setUrl(locations.get(0)).setFsId(form.getFs_id()).setUa(svipInfoBO.getUa());
        BDCache.INSTANCE.getFileCache().put(fsIdStr, vo);
        // 减少cdk使用次数
        cdkService.addUsedTimeFromContext();
        return vo;

    }

    private List<BDFileDTO> openDir(GetFileForm form, BDSvipInfoDTO svipInfoDTO) {
        HttpResponse response = HttpRequest.get(StrUtil.format("https://pan.baidu.com/share/list?shareid={}&uk={}&dir={}", form.getShareid(), form.getShare_uk(), URLUtil.encodeQuery(form.getPath())))
                .header(Header.USER_AGENT, BDShareConstant.CHROME_UA)
                .header(Header.COOKIE, svipInfoDTO.getCookie())
                .header(Header.REFERER, "https://pan.baidu.com/disk/home")
                .execute();
        JSONObject bodyJo = JSONObject.parseObject(response.body());
        return BeanUtil.copyToList(bodyJo.getJSONArray("list"), BDFileDTO.class);
    }

}
