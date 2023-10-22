package my.spring.myargumentresolver.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class HeadOrPathVariableResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(HeadOrPathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String userIdFromHead = webRequest.getHeader("userId");

        HeadOrPathVariable ann = parameter.getParameterAnnotation(HeadOrPathVariable.class);
        String pathVariableName = List.of(ann.name(), ann.value(), parameter.getParameterName()).stream()
                .filter(x -> x != null && x.length() > 0)
                .findFirst().get();
        System.out.println("HeadOrPathVariable name: " + pathVariableName);

        Map<String, String> uriTemplateVars = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        String userIdFromPath = (uriTemplateVars != null ? uriTemplateVars.get(pathVariableName) : null);

//        throw new RuntimeException("user error ... ");

        return userIdFromHead == null ? userIdFromPath : userIdFromHead;
    }


}
