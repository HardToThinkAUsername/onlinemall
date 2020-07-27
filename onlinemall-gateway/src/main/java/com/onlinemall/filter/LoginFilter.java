package com.onlinemall.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.onlinemall.auth.utils.JwtUtils;
import com.onlinemall.common.utils.CookieUtils;
import com.onlinemall.config.FilterProperties;
import com.onlinemall.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        //获取上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = currentContext.getRequest();
        //获取路径
        String requestURI = request.getRequestURI();
        //判断白名单
        //遍历允许访问的路径
        for(String path: this.filterProperties.getAllowPaths()){
            //然后判断是否是符合
            if(requestURI.startsWith(path)){
                return false;
            }
        }

        return true;
    }

    @Override
    public Object run() {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = context.getRequest();
        //获取token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        //校验
        try {
            //校验通过, 什么都不做, 即放行
            JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());

        }catch (Exception e){
            //校验出现异常, 返回403
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        }
        return null;
    }
}
