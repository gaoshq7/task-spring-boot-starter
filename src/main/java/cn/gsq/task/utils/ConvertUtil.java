package cn.gsq.task.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertUtil {
    public static String toJsonString(Object o)  {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json转换异常");
        }
    }
}
