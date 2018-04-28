package cn.isaxon.jsonfilter;

import cn.isaxon.jsonfilter.annotation.JsonFilterDesensitized;
import cn.isaxon.jsonfilter.modual.Result;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * <p></p>
 * <p>Copyright:@isaxon.cn</p>
 *
 * @author saxon/isaxon
 * Create 2018-04-27 下午5:34 By isaxon
 */
@ControllerAdvice // 注入spring ioc容器中
public class ControllerReurnHandler implements ResponseBodyAdvice<Object> // 这边实现Controller的返回接口以进行拦截
{
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass)
    {
        // 只拦截带有JsonFilterDesensitized注解的Controller
        return methodParameter.hasMethodAnnotation(JsonFilterDesensitized.class);
    }

    @Override
    public Object beforeBodyWrite(Object returnObject, MethodParameter paramter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse)
    {
        if (returnObject instanceof Result) // 返回值是包装类
        {
            // 我们取出Controller对应的过滤规则，也就是自定义注解的内容
            JsonFilterDesensitized.ClassFieldFilter[] classFieldFilters = paramter.getMethodAnnotation(JsonFilterDesensitized.class).value();
            if (classFieldFilters.length != 0)
            {
                // 这边进行字段过滤拦截了0
                JsonFilterSerializer serializer = new JsonFilterSerializer();
                ObjectMapper mapper = serializer.doFilter(classFieldFilters);
                return mapper.valueToTree(returnObject);
            }
        }
        return returnObject;
    }

    @JsonIgnoreProperties
    private static class JsonFilterSerializer
    {
        private static final String FILTER_INCLUDE = "FILTER_INCLUDE";
        private static final String FILTER_EXCLUDE = "FILTER_EXCLUDE";

        @JsonFilter(FILTER_EXCLUDE)
        interface FilterExclude { }

        @JsonFilter(FILTER_INCLUDE)
        interface FilterInclude { }

        public ObjectMapper doFilter(JsonFilterDesensitized.ClassFieldFilter[] classFieldFilters)
        {
            ObjectMapper mapper = new ObjectMapper();

            // 初始化过滤规则
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            for (JsonFilterDesensitized.ClassFieldFilter classFieldFilter : classFieldFilters)
            {
                // 注解数组循环后加入过滤规则
                Class clazz = classFieldFilter.type();
                String[] include = classFieldFilter.include();
                String[] exclude = classFieldFilter.exclude();

                if (include.length != 0)
                {
                    // SimpleBeanPropertyFilter#filterOutAllExcept(String[])这个方法是添加不需要过滤的字段
                    filterProvider.addFilter(FILTER_INCLUDE, SimpleBeanPropertyFilter.filterOutAllExcept(include));
                    mapper.addMixIn(clazz, FilterInclude.class);
                } else if (exclude.length != 0)
                {
                    // // SimpleBeanPropertyFilter#serializeAllExcept(String[])这个方法是对包含的进行添加过滤的字段
                    filterProvider.addFilter(FILTER_EXCLUDE, SimpleBeanPropertyFilter.serializeAllExcept(exclude));
                    mapper.addMixIn(clazz, FilterExclude.class);
                }
            }

            // 设置过滤规则
            mapper.setFilterProvider(filterProvider);
            return mapper;
        }
    }
}