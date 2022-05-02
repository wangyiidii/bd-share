package cn.yiidii.bdshare.common.Interceptor;

import cn.yiidii.bdshare.common.constant.BDShareConstant;
import cn.yiidii.pigeon.common.core.util.ContextUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * CDK拦截器
 *
 * @author YiiDii Wang
 * @create 2021-11-27 00:29
 */
@Component
@RequiredArgsConstructor
public class CdkInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String cdk = request.getHeader(BDShareConstant.CDK);
        if (StringUtils.isNotBlank(cdk)) {
            ContextUtil.set(BDShareConstant.CDK, cdk);
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ContextUtil.getLocalMap().remove(BDShareConstant.CDK);
    }
}
