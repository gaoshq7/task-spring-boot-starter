package cn.gsq.task.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpParams {

    private String serveName;//服务名

    private String processName;//进程名

    private String hostName;//主机名

    private String action;//操作行为 启动|停止|重启

    private Map<String,Object> extend;//拓展参数

}
