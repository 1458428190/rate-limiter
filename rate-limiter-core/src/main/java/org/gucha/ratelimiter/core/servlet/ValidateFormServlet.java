package org.gucha.ratelimiter.core.servlet;

import org.gucha.ratelimiter.core.service.ValidateFormService;
import org.gucha.ratelimiter.core.util.CrosUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午11:49
 */
public class ValidateFormServlet extends HttpServlet {

    @Lazy
    @Autowired
    private ValidateFormService validateFormService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean result = validateFormService.validate(request);
        CrosUtils.setCrosHeader(response);
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(200);
        response.getWriter().write(result + "");
        response.getWriter().close();
        return;
    }
}
