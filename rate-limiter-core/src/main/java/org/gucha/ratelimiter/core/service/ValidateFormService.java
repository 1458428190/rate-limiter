package org.gucha.ratelimiter.core.service;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.gucha.ratelimiter.core.module.VerifyImageDTO;
import org.gucha.ratelimiter.core.module.VerifyImageVO;
import org.gucha.ratelimiter.core.rule.RateLimiterRuleActuator;
import org.gucha.ratelimiter.core.util.VerifyImageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午10:09
 */
public class ValidateFormService {

    @Autowired
    private RateLimiterRuleActuator rateLimiterRuleActuator;

    @Autowired
    private VerifyImageUtils verifyImageUtils;

    /**
     * 验证码验证
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    public boolean validate(HttpServletRequest request) throws UnsupportedEncodingException {
        // TODO 优化从request中获取表单的逻辑
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        List<FileItem> items = null;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        Map<String, String> params = new HashMap<>();
        for (FileItem fileItem : items) {
            if (fileItem.isFormField()) {
                params.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
            }
        }
        String verifyId = params.get("verifyId");
        String result = params.get("result");
        // 需要传递之前的请求url
        String realRequestUrl = params.get("realRequestUri");
        String actualResult = verifyImageUtils.getVerifyCodeFromExternalStorage(verifyId);
        if (StringUtils.equalsIgnoreCase(actualResult, result)) {
            // 通过验证码, 重置
            rateLimiterRuleActuator.reset(request, realRequestUrl);
            return true;
        }
        return false;
    }

    /**
     * 刷新验证码
     * @param request
     * @return
     */
    public VerifyImageVO refresh(HttpServletRequest request) {
        String verifyId = request.getParameter("verifyId");
        verifyImageUtils.deleteVerifyCodeFromExternalStorage(verifyId);
        // 生成验证码
        VerifyImageDTO verifyImageDTO = verifyImageUtils.generateVerifyImg();
        verifyImageUtils.saveVerifyCodeToExternalStorage(verifyImageDTO);
        VerifyImageVO verifyImageVO = new VerifyImageVO();
        BeanUtils.copyProperties(verifyImageDTO, verifyImageVO);
        return verifyImageVO;
    }
}
