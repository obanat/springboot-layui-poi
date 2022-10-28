功能介绍：

 

1）主界面：比较简洁

 



 

2）添加任务管理页面

也支持删查改，不多介绍了



 

3）上传报表页面

说明：首先选择一个xlsx或者xlsm文件上传，这个表格至少需要包含两列，1）需要统计的名称列 2）待统计的数值列，上传后，后台会分析excel表格内容，根据上述两页计算任务的完成率，并产生报表。操作顺序是先上传excel文件，成功后再输入excel文件中的sheet页名称，待统计的列号，再点提交按钮。



 

具体说明，假如这是一个原始的任务表格：



后台计算是统计“责任人”列和“完成状态”列，只要“完成状态”列不为空，即认为这项任务已经反馈过了，完成的任务数+1

 

最终计算结果：



 

这个是最简单的规则，也可以输入正则表达式，让系统根据表达式判定任务的状态，计算实际的完成任务数。

 

 

E列的人员名单系统提供了3种选择：



1. 使用系统人员名单--按系统中提前录入的人员名单统计，忽略不在名单中的数据

2. 使用上传表格中的名单 --统计上传表格中的全部人员名单

3. 自定义名单   --手动输入名单，按名单中的人员进行统计

 

说明：当前仅支持换行符分隔的人员名单，不支持其它分隔符，例如下面的人员名单是合法的



 

 

 

 

 

表格的sheet页名称也是必填的，根据上传的excel文件自行填写



 

责任人列（D列），完成状态（E列），分别对应上述页面中的如下字段，需要手动填写，填写后即可点击“提交”按钮，进行计算。这两项是必填的，不管是否自定义人员名单，正则表达式。



 

4）报表显示页面

报表显示相对也比较简单，支持的报表格式就一种，后续可以加上折线图等，前端用的是H5，控件都是支持的，作者比较懒而已



 

本系统已经接入welink前台H5接口，上述报表左侧的welink列支持点击 “>>>” 区域后，跳转相应人员的welink聊天界面，并且把当前任务状态以文本的方式复制到剪贴板中，ctrl+v即可粘贴。

 

 

5）人员管理页面



 
数据库初始化：
任务表：
CREATE TABLE `oc80`.`setasks` (
  `id` INT NULL AUTO_INCREMENT,
  `name` VARCHAR(128) NULL,
  `start_time` VARCHAR(45) NULL,
  `end_time` VARCHAR(45) NULL,
  `owner` VARCHAR(128) NULL,
  `status` VARCHAR(45) NULL,
  `prio` INT NULL,
  `description` VARCHAR(512) NULL,
  `url` VARCHAR(256) NULL,
  `sheetName` VARCHAR(256) NULL,
  `nameCol` INT NULL,
  `valueCox` INT NULL,
  `reportStr` VARCHAR(1056) NULL,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC));

人员表：
CREATE TABLE `oc80`.`seinfo` (
  `id` INT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `plName` VARCHAR(45) NULL,
  `dept` VARCHAR(45) NULL,
  `description` VARCHAR(128) NULL,
  `welinkId` VARCHAR(45) NULL,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC));
