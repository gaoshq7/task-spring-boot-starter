package cn.gsq.task.external;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Project : galaxy
 * Class : cn.gsq.task.external.ETLevel
 *
 * @author : gsq
 * @date : 2023-11-30 15:25
 * @note : It's not technology, it's art !
 **/
@Getter
@AllArgsConstructor
public enum ETLevel {

    INFO("info", "信息"),

    WARN("warn","警告"),

    ERROR("error","错误");

    private final String key;

    private final String name;

}
