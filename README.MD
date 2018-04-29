### Spring boot 中jackson/gson在返回结果时灵活过滤字段
- 测试代码 
![](code.png)
- 结果
![](effect.png)

## 注意
Gson集成进spring boot不在本示例讨论范围内，可以自行添加后再使用本示例内[相应的Gson拦截代码](src/main/java/cn/isaxon/jsonfilter/ControllerReurnHandler.java)