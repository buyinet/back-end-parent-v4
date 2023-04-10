package com.kantboot.system.security.filter;

import com.alibaba.fastjson2.JSON;
import com.kantboot.system.module.entity.SysPermission;
import com.kantboot.system.module.entity.SysRole;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.service.ISysPermissionService;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 系统安全拦截器
 * 在单体应用中，可以使用这个拦截器来做一些安全拦截，比如登录拦截，权限拦截等等
 * 在微服务中，会使用别的方式进行拦截
 *
 * @author 方某方
 */
@Slf4j
@WebFilter(filterName = "kantbootSystemSecurityFilter", urlPatterns = "/*")
public class KantbootSystemSecurityFilter implements Filter {

    @Resource
    private ISysExceptionService exceptionService;

    @Resource
    private ISysPermissionService permissionService;

    @Resource
    private ISysUserService userService;

    private final BaseException baseException = new BaseException();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 向上转型
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUri = request.getRequestURI();

        // 输出拦截的请求
        log.info("拦截请求: {}", requestUri);
        List<String> pathList = requestUriSplit(requestUri);
        // 遍历匹配
        for (String path : pathList) {
            SysPermission byUri = permissionService.getByUri(path);
            if (byUri == null) {
                continue;
            }
            SysUser user = null;
            // 判断是否需要登录
            if (byUri.getNeedLogin()) {
                try {
                    user = userService.getSelf();
                } catch (BaseException e) {
                    // 异常处理
                    exceptionHandler(response, e);
                    return;
                }
            }
            // 判断是否限制角色
            if (byUri.getNeedRole()) {
                log.info("需要角色，路径：{}", path);
                if (user == null) {
                    try {
                        user = userService.getSelf();
                        log.info("用户：{}，路径：{}", user, path);
                    } catch (BaseException e) {
                        // 异常处理
                        exceptionHandler(response, e);
                        return;
                    }
                }
                // 判断角色
                Set<SysRole> roles = user.getRoles();
                // 遍历角色
                for (SysRole role : roles) {
                    // 遍历权限
                    Set<SysRole> roles1 = byUri.getRoles();
                    // 遍历角色
                    for (SysRole role1 : roles1){
                        // 判断角色是否匹配
                        if (role.getCode().equals(role1.getCode())) {
                            try {
                                // 放行
                                filterChain.doFilter(request, response);
                                return;
                            } catch (IOException | ServletException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

                exceptionHandler(response, exceptionService.getException("insufficientPermissions"));
                return;

            }


        }

        // 放行
        filterChain.doFilter(request, response);
    }

    /**
     * URI匹配分割
     * 例：requestURI是/system/user/list，这个数组便是/system/**，/system/user/**，/system/user/list
     *
     * @param requestUri 请求URI
     * @return 分割后的集合
     */
    private List<String> requestUriSplit(String requestUri) {
        String[] arr = requestUri.split("/");
        List<String> pathList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        Arrays.stream(arr)
                .filter(s -> !s.isEmpty())
                .forEach(s -> {
                    sb.append("/").append(s);
                    pathList.add(sb + "/**");
                });
        // 添加最后一个路径
        pathList.set(pathList.size() - 1, pathList.get(pathList.size() - 1).substring(0, pathList.get(pathList.size() - 1).length() - 3));
        return pathList;
    }


    /**
     * 异常处理
     *
     * @param servletResponse 响应
     * @param e               异常
     * @throws IOException IO异常
     */
    private void exceptionHandler(ServletResponse servletResponse, BaseException e) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(baseException.exceptionHandler(e)));
        writer.flush();
        writer.close();
    }
}