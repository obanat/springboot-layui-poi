package com.mudemo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.mudemo.dao.SeInfoDao;
import com.mudemo.model.seInfo;
import com.mudemo.service.GoodsService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author obana
 * @version 1.0
 * @date 2022/10/25 10:41
 * @description
 * @modify
 */
@RestController
@RequestMapping(value = "/vars")
public class UploadFileController {
    private SeInfoDao infoDao;
    List<seInfo> infos;// = infoDao.getSesList();
    boolean useTableSeList = false;//使用表格中的人员名单
    List<seDetailInfo> seArray;
    String rules = null;
    XSSFWorkbook workbook;
    @Autowired
    private GoodsService goodsService;


    @RequestMapping(value = "/calc", method = RequestMethod.POST)
    public JSONObject calc(@RequestBody JSONObject request) {

        //返回结果
        JSONObject result = new JSONObject();

        result.put("code", "0");
        result.put("msg", "Excel解析成功，请查看报表！");
        try {
            String sheetName = request.getString("sheetName");
            String nameColIndex = request.getString("nameCol");
            String valueColIndex = request.getString("valueCol");
            String custSeList = request.getString("seList");
            rules = request.getString("rulesValue");
            int taskId = 0;
            try {
                taskId = request.getInteger("id");//调试时出现过id为空的情况
            } catch (Exception e){

            }

            if (infos == null) {
                infos = goodsService.getSeList2();//获取数据库中的人员信息，必须的，报表数据的来源
            }

            if (workbook == null) {
                try {
                    FileInputStream file = new FileInputStream(new File("D:\\(标记版本)MagicOS特性梳理 V1.1.xlsm"));
                    workbook = new XSSFWorkbook(file);//test
                } catch (Exception e) {

                }
                /*result.put("code", "500");
                result.put("msg", "excel表格为空，无法继续处理！");
                return result;*/
            }
            // 打开Excel中的第一个Sheet
            XSSFSheet  sheet = workbook.getSheet(sheetName);


            int i = 0;
            Pattern p = null;
            if (rules!= null && rules.length() > 0) {
                p = Pattern.compile(rules);
            }
            if ("1".equals(custSeList)) {
                //数值定义
                /*
                	<option value="1">使用系统SE名单</option>
					<option value="2">使用上传表格中的名单</option>
					<option value="3">自定义名单</option>
                 */
                useTableSeList = false;
                //从数据库中获取se名字列表,转成对象存储在列表中
                seArray = new ArrayList<seDetailInfo>();
                for(seInfo info: infos) {
                    seDetailInfo se = new seDetailInfo();
                    se.name = info.getName();
                    se.plName = info.getPlName();
                    se.welinkId = info.getWelinkId();
                    seArray.add(se);
                }
            } else if ("2".equals(custSeList)) {
                useTableSeList = true;
                seArray = new ArrayList<seDetailInfo>();
            } else {
                String[] strList = custSeList.split("\n", 100);//html 默认是\n,100是上限

                if (strList.length > 1) {
                    useTableSeList = false;
                    seArray = new ArrayList<seDetailInfo>();
                    for (String str : strList) {
                        seDetailInfo se = createOneSe(seArray,str);
                        if (se != null) {//se为null，表示数组已经存在，不需要添加
                            seArray.add(se);
                        }
                    }
                } else {
                    useTableSeList = true;//定制人员名单输入不合法，使用表格的名单
                }
            }

            int seCol = Integer.parseInt(nameColIndex);
            if (seCol <= 0 && seCol > 100) {
                result.put("code", "500");
                result.put("msg", "SE列号填写错误，无法继续处理！");
                return result;
            }

            int valueCol = Integer.parseInt(valueColIndex);
            if (valueCol <= 0 && valueCol > 100) {
                result.put("code", "500");
                result.put("msg", "内容列号填写错误，无法继续处理！");
                return result;
            }
            int rowCount = sheet.getLastRowNum();
            if (rowCount < 1) {
                result.put("code", "500");
                result.put("msg", "文件内容为空，无法继续处理！");
                return result;
            }
            // HashMap<Integer, Object> map=new HashMap<Integer, Object>();
            // 遍历行row
            int match = 0;
            String content;
            String str;
            for (int rowNum = 1; rowNum < rowCount+1; rowNum++) {
                XSSFRow xssfRow = sheet.getRow(rowNum);
                if (xssfRow == null) {
                    continue;
                }
                XSSFCell xssCell = xssfRow.getCell(seCol);
                if (xssCell == null) {
                    continue;
                }
                str = String.valueOf(getXSSFValue(xssCell));
                xssCell = xssfRow.getCell(valueCol);

                content = xssCell == null? "" : String.valueOf(getXSSFValue(xssCell));

                //important! 处理函数
                if (processOneRow(str, content, p)) match++;
            }
            if (match <= 0) {
                result.put("code", "500");
                result.put("msg", "自定义名单列表有误，没有匹配数据！");
                return result;
            }

            //转json输出
            String jsonStr = JSON.toJSONString(seArray);
            result.put("data", jsonStr);
            result.put("code", "0");
            result.put("msg", "Excel解析成功，请查看报表！id：" + taskId);
            goodsService.putReportStr(taskId, sheetName, seCol,valueCol,jsonStr);//记录数据库
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "sheet页填写错误，无法打开！");
        } finally {
            return result;
        }

    }

    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JSONObject importData(HttpServletRequest request){
        JSONObject result = new JSONObject();
        int ret = 0;
        try {
            result.put("code", "0");
            result.put("msg", "Excel读取正常！");

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = (MultipartFile) multipartRequest.getFile("file");
            String fileName = file.getOriginalFilename();

            //XSSFWorkbook workbook = new XSSFWorkbook(file);test
            workbook = new XSSFWorkbook(file.getInputStream());
        }catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "文件选择错误，上传失败！");
        } finally {
            return result;
        }
    }

    //单个的对象，最终要输出为json
    class seDetailInfo {
        public seDetailInfo(){}
        @JSONField
        private String name;
        @JSONField
        private int count;
        @JSONField
        private int total;
        @JSONField
        private String plName;
        @JSONField
        private String welinkId;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }
        public void setCount(int count) {
            this.count = count;
        }

        public int getTotal() {
            return total;
        }
        public void setTotal(int total) {
            this.total = total;
        }

        public String getPlName() {
            return plName;
        }
        public void setPlName(String plName) {
            this.plName = plName;
        }

        public String getWelinkId() {
            return welinkId;
        }
        public void setWelinkId(String welinkId) {
            this.welinkId = welinkId;
        }
    };

    boolean processOneRow(String name, String value, Pattern p) {
        if (name == null || name.length() == 0) return false;

        if (useTableSeList) {
            //对应选择2的场景，使用excel表格中的人员名单

            seDetailInfo se = getOrCreateFromArray(seArray, name);//遍历获取或者创建
            if (se != null) {
                se.total ++;
                if (matchValue(value,p)) se.count++;
            }
        } else {
            for (seDetailInfo se : seArray) {//仅遍历
                if (se != null && se.name.equals(name)) {
                    //名称匹配
                    se.total++;
                    if (matchValue(value, p)) se.count++;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * excel值处理
     *
     * @param hssfCell
     * @return
     */
    public static Object getXSSFValue(XSSFCell hssfCell) {
        Object result = null;
        CellType cellType = hssfCell.getCellType();
        switch (hssfCell.getCellType()) {
            case NUMERIC: //数字
                result = hssfCell.getNumericCellValue();
                break;
            case BOOLEAN: //Boolean
                result = hssfCell.getBooleanCellValue();
                break;
            case ERROR: //故障
                result = hssfCell.getErrorCellValue();
                break;
            case FORMULA: //公式
                result = hssfCell.getCellFormula();
                break;
            case BLANK: //空值
                result = "";
                break;
            default: //字符串
                result = hssfCell.getStringCellValue();
        }
        return result;
    }
    seDetailInfo createOneSe(List<seDetailInfo> array, String name) {
        if (name == null || name.length() < 2 || name.length() > 20) {
            return null;
        }

        for (seDetailInfo se:array) {//去重
            if (name.equals(se.name)) {
                return null;//已经存在，不需要创建，返回null
            }
        }
        seDetailInfo se = new seDetailInfo();
        se.name = name;

        for (seInfo info:infos) {
            String tmp = name.trim();
            if(tmp.equals(info.getName())){
                se.plName = info.getPlName();
                se.welinkId = info.getWelinkId();
                break;
            }
        }
        return se;
    }

    seDetailInfo  getOrCreateFromArray(List<seDetailInfo> array, String name){
        if (array == null) return null;

        if (name == null || name.length() == 0) return null;

        for (seDetailInfo se : array) {
            if (name.equals(se.name)) {
                //名字匹配
                return se;//已经存在的元素，基本信息已经填写，返回后再更新数值
            }
        }
        //不匹配
        seDetailInfo se = new seDetailInfo();
        se.name = name;
        for (seInfo info:infos) {//完成plname和weilink的填充
            String tmp = name.trim();
            if(tmp.equals(info.getName())){
                se.plName = info.getPlName();
                se.welinkId = info.getWelinkId();
                break;
            }
        }
        array.add(se);
        return se;
    }

    boolean matchValue(String value, Pattern p) {
        if (p != null) {
            return p.matcher(value).matches();
        } else {
            return value != null && value.length() > 0;
        }
    }
}
