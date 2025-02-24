package cn.gsq.task.external;

import cn.hutool.core.date.DateUtil;
import lombok.Getter;

/**
 * Project : galaxy
 * Class : cn.gsq.task.external.TSBase
 *
 * @author : gsq
 * @date : 2023-11-29 17:41
 * @note : It's not technology, it's art !
 **/
@Getter
public class TSBase {

    protected final String id;   // 任务唯一标示

    protected final String timestamp;   // 时间戳

    /**
     * @Description : task stage构造器
     * @Param : [id]
     * @Return :
     * @Author : gsq
     * @Date : 17:55
     * @note : An art cell !
    **/
    protected TSBase(String id) {
        this.id = id;
        this.timestamp = DateUtil.now();
    }

}
