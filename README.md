## 🔧 使用方法

- 在Spring Boot的Bean环境中添加'XmlPlanParser'接口的实现，提供任务规则。
- 在Spring Boot的Bean环境中添加任务算子，例如：

    ```shell
        @Operator(id = "a", name = "第一个算子")
        public class ACmp extends AbstractActuatorAction {
        	@Override
        	public void operate() {
        	    System.out.println("第一个算子运算完成!");
        	}
        }
    ```

- 在Spring Boot的Bean环境中添加任务实时通讯接口'TMTransport'的实现。

- 任务启动代码：

    ```shell
        TaskActuator actuator = SpringUtil.getBean(TaskActuator.class);
        List<PTStage> stages1 = CollUtil.newArrayList(
                PTStage.build("步骤01", 50),
                PTStage.build("步骤02", 100),
                PTStage.build("步骤03", 200)
        );
        actuator.execute("chain1", "测试任务01", "test01", () -> "系统", stages1);
    ```
  
## 🔔️ 特别提醒

- 多个模块共同引用task stater的时候注意需要保障所有算子和接口实现在Bean环境中。
- 规则配置中使用'begin'和'end'来标注任务步骤，步骤是绝对线性同步的。
- <font color="red">**启动任务函数中的步骤需要与规则配置中的'begin'、'end'相对应**</font>。
- 算子中的异常逻辑上需要throw到上层中。