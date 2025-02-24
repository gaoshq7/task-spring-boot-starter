package cn.gsq.task.context;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yomahub.liteflow.exception.LiteFlowException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Project : galaxy
 * Class : cn.gsq.task.context.ConclusionContext
 *
 * @author : gsq
 * @date : 2023-12-11 19:42
 * @note : It's not technology, it's art !
 **/
@Slf4j
@Getter
public class ConclusionContext extends BaseContext {

    private InputStream stream;

    /**
     * @Description : 构造函数
     * @Param : [id, name]
     * @Return :
     * @Author : gsq
     * @Date : 14:59
     * @note : An art cell !
    **/
    protected ConclusionContext(String id, String name) {
        super(id, name);
    }

    /**
     * @Description : 添加结果流
     * @Param : [stream]
     * @Return : void
     * @Author : gsq
     * @Date : 15:14
     * @note : An art cell !
    **/
    protected void setStream(InputStream stream) {
        this.stream = stream;
    }

    /**
     * @Description : 添加结果输入流
     * @Param : [details]
     * @Return : void
     * @Author : gsq
     * @Date : 14:59
     * @note : ⚠️ details可以是文件路径；也可以是文件内容 !
    **/
    protected void writeResult(String details) {
        if (ObjectUtil.isNotNull(this.stream)) {
            throw new LiteFlowException("结果流不可多次写入");
        }
        try {
            if (FileUtil.isFile(details)) {
                this.stream = new FileInputStream(details);
            } else {
                this.stream = new ByteArrayInputStream(details.getBytes());
            }
        } catch (Exception e) {
            throw new LiteFlowException("结果流写入失败：" + e.getMessage(), e);
        }
    }

}
