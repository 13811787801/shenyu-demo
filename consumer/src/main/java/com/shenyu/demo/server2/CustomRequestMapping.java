package com.shenyu.demo.server2;

import com.google.common.base.CaseFormat;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class CustomRequestMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo requestMappingInfo = super.getMappingForMethod(method, handlerType);
        if (requestMappingInfo != null) {
            if (handlerType.isAnnotationPresent(ModelAnnotation.class)) {
//                if (requestMappingInfo.getPatternsCondition().getPatterns().isEmpty() ||
//                        requestMappingInfo.getPatternsCondition().getPatterns().contains("")) {
                    String[] path = getMethodPath(method, handlerType);
                    requestMappingInfo = RequestMappingInfo.paths(path).build().combine(requestMappingInfo);
//                }
            }
        } else {
//            if (handlerType.isAnnotationPresent(ModelAnnotation.class)) {
//                String[] path = getMethodPath(method, handlerType);
//                requestMappingInfo = RequestMappingInfo.paths(path).methods(RequestMethod.POST).build();
//            }
        }
        return requestMappingInfo;
    }

    private String[] getMethodPath(Method method, Class<?> handlerType) {
        String path = "/hello";
        return new String[]{path};
    }

}
