package cn.gsq.task.context;

import cn.gsq.task.pojo.OpParams;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OpParamBaseContext<T extends OpParams> extends BaseContext{

    @Getter
    @Setter
    private T params;//参数的实体类

    public OpParamBaseContext( T  params) {
        super("", "");
        this.params=params;
    }
}
