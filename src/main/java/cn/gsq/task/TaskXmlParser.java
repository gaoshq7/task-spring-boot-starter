package cn.gsq.task;

import cn.hutool.core.collection.CollUtil;
import com.yomahub.liteflow.parser.el.ClassXmlFlowELParser;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Project : galaxy
 * Class : cn.gsq.task.TaskXmlParser
 *
 * @author : gsq
 * @date : 2023-12-25 17:33
 * @note : It's not technology, it's art !
 **/
@Slf4j
public class TaskXmlParser extends ClassXmlFlowELParser {

    @Autowired
    private List<XmlPlanParser> parsers;

    @Override
    public String parseCustom() {
        try {
            List<String> contents = CollUtil.map(this.parsers, XmlPlanParser::getPlanXml, true);
            return merge(contents);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任务配置文件读取错误：" + e.getMessage());
        }
    }

    /**
     * @Description : 合并xml配置文件内容
     * @Param : [contents]
     * @Return : java.lang.String
     * @Author : gsq
     * @Date : 15:55
     * @note : An art cell !
    **/
    private String merge(List<String> contents) throws DocumentException {
        Document document = DocumentHelper.createDocument();
        Element bookStore = document.addElement("flow");
        SAXReader saxReader = new SAXReader();
        Element parent = bookStore.getDocument().getRootElement();
        for (String p : contents) {
            InputStream inputStream = new ByteArrayInputStream(p.getBytes());
            Document read = saxReader.read(inputStream);
            List<Element> elements = read.getDocument().getRootElement().elements();
            for (Element emt : elements) {
                parent.add(emt.detach());
            }
        }
        return parent.asXML();
    }

}
