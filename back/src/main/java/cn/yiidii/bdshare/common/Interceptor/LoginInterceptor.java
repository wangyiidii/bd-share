package cn.yiidii.bdshare.common.Interceptor;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.bdshare.common.annotation.Admin;
import cn.yiidii.bdshare.common.constant.BDShareConstant;
import cn.yiidii.bdshare.model.bo.BDCache;
import cn.yiidii.bdshare.model.ex.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 登录拦截器
 *
 * @author YiiDii Wang
 * @create 2021-11-27 00:29
 */
@Component
@RequiredArgsConstructor
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod h = (HandlerMethod) handler;
            Admin adminAnno = h.getMethodAnnotation(Admin.class);
            if (Objects.nonNull(adminAnno)) {
                String token = request.getHeader(BDShareConstant.HEADER_TOKEN);
                String cacheToken = BDCache.INSTANCE.getToken();
                boolean loginSuccess = StrUtil.isNotBlank(token) && (Objects.nonNull(cacheToken) && StrUtil.equals(cacheToken, token));
                if (!loginSuccess) {
                    throw new TokenExpiredException();
                }
            }
        }
        return super.preHandle(request, response, handler);
    }
}
