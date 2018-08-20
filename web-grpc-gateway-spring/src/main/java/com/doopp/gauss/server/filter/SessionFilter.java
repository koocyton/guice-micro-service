package com.doopp.gauss.server.filter;

import com.doopp.gauss.common.entity.User;
import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.common.utils.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * Created by henry on 2017/4/16.
 */
@Component
public class SessionFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // get bean
        LoginService accountService = (LoginService) ApplicationContextUtil.getBean("accountService");

        // 不过滤的uri
        String[] notFilters = new String[]{
            "/api/register",
            "/api/login",
            "/api/logout",
            "/index.html",
            "/favicon.ico",
            "/js/",
            "/css/",
            "/tpl",
        };

        // 请求的uri
        String uri = request.getRequestURI();

        // 是否过滤
        boolean doFilter = true;

        // 如果uri中包含不过滤的uri，则不进行过滤
        for (String notFilter : notFilters) {
            if (uri.contains(notFilter)) {
                doFilter = false;
                break;
            }
        }

        try {
            if (doFilter) {

                logger.info(" >>> request uri : " + uri);

                // 从 header 里拿到 access token
                String sessionToken = request.getHeader("session-token");
                if (sessionToken==null) {
                    writeErrorResponse(501, response, "token failed");
                    return;
                }

                // get user
                User user = accountService.tokenUser(sessionToken);
                if (user == null) {
                    writeErrorResponse(404, response, "not found user");
                    return;
                }

                // set request attrib
                request.setAttribute("sessionUser", user);
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
            writeErrorResponse(501, response, e.getMessage());
        }
    }

    private static void writeErrorResponse(int errorCode, HttpServletResponse response, String message) throws IOException {
        response.setStatus(errorCode);
        String data = "{\"errcode\":" + errorCode + ", \"errmsg\":\"" + message + "\"}";
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(data);
    }
}