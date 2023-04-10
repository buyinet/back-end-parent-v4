package com.kantboot.system.security.filter;

import com.alibaba.fastjson2.JSON;
import com.kantboot.system.module.entity.SysPermission;
import com.kantboot.system.module.entity.SysRole;
import com.kantboot.system.module.entity.SysUser;
import com.kantboot.system.service.ISysExceptionService;
import com.kantboot.system.service.ISysPermissionService;
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
            // 检查是否可放行
            try {
                checkPath(path);
            }catch (BaseException e){
                exceptionHandler(response,e);
                return;
            }
        }

        // 放行
        filterChain.doFilter(request, response);
    }

    /**
     * 根据路径检查是否可放行
     *
     * @param path 路径
     *             例：/system/user/list
     */
    private void checkPath(String path) {

        // 初始化用户
        SysUser user = null;

        // 根据uri获取权限
        SysPermission byUri = permissionService.getByUri(path);

        if (byUri == null) {
            // 如果没有权限，直接放行
            return;
        }

        // 判断是否需要登录
        if (byUri.getNeedLogin()) {
            user = userService.getSelf();
        }

        if (!byUri.getNeedRole()) {
            // 如果不需要登录，也不需要角色，直接放行
            return;
        }

        // 再次判断是否需要登录
        if (user == null) {
            // 如果需要角色限制，但是当前用户未登录，会告知客户端没有登录
            // 这一步是必要的，因为如果配置了需要角色限制，但没有配置需要登录，那么可能无法获取用户角色信息
            // 因此，配置了需要角色限制，无论是否配置需要登录，都必须在登录状态下才能访问
            user = userService.getSelf();
        }

        // 用户的角色
        Set<SysRole> rolesOfUser = user.getRoles();
        // 权限的角色
        Set<SysRole> rolesOfPermission = byUri.getRoles();
        // 判断是否有交集，true表示有交集，false表示没有交集
        boolean isIntersect = rolesOfUser.stream().anyMatch(rolesOfPermission::contains);

        // 如果没有交集，抛出异常，告知客户端没有权限
        if (!isIntersect) {
            throw exceptionService.getException("insufficientPermissions");
        }

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
        Arrays.stream(arr).filter(s -> !s.isEmpty()).forEach(s -> {
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
     * @param response 响应
     * @param e        异常
     * @throws IOException IO异常
     */
    private void exceptionHandler(HttpServletResponse response, BaseException e) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(baseException.exceptionHandler(e)));
        writer.flush();
        writer.close();
    }
}