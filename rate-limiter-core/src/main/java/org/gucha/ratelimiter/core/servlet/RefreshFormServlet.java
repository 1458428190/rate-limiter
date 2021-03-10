package org.gucha.ratelimiter.core.servlet;

import com.alibaba.fastjson.JSON;
import org.gucha.ratelimiter.core.module.VerifyImageVO;
import org.gucha.ratelimiter.core.service.ValidateFormService;
import org.gucha.ratelimiter.core.util.CrosUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 刷新表单验证码接口
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午10:09
 */
public class RefreshFormServlet extends HttpServlet {

    @Lazy
    @Autowired
    private ValidateFormService validateFormService;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        VerifyImageVO refresh = validateFormService.refresh(request);
        CrosUtils.setCrosHeader(response);
        response.setContentType("application/json;charset-utf-8");
        response.setStatus(200);
        response.getWriter().write(JSON.toJSONString(refresh));
        response.getWriter().close();
        return;
    }

}
