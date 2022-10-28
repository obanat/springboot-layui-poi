package com.mudemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mudemo.dao.TasksDao;
import com.mudemo.dao.SeInfoDao;
import com.mudemo.model.seTasks;
import com.mudemo.model.seInfo;
import com.mudemo.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author obana
 * @version 1.0
 * @date 2022/10/26 13:55
 * @description
 * @modify
 */
@Service(value = "GoodsService")
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private TasksDao tasksDao;
    @Autowired
    private SeInfoDao infoDao;
    @Override
    public JSONObject getTasksList(int pageNum, int pageSize) {
        JSONObject result = new JSONObject();
        try {
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<seTasks> pageInfo = new PageInfo(tasksDao.getTasksList());

            result.put("code", "0");
            result.put("msg", "操作成功！");
            result.put("data", pageInfo.getList());
            result.put("count", pageInfo.getTotal());
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "查询异常！");
        }
        return result;
    }

    @Override
    public JSONObject addTask(JSONObject request) {
        JSONObject result = new JSONObject();
        try {
            tasksDao.addTask(request);
            result.put("code", "0");
            result.put("msg", "操作成功！");
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "新增商品异常！");
        }
        return result;
    }

    @Override
    public JSONObject updateTask(JSONObject request) {
        JSONObject result = new JSONObject();
        try {
            tasksDao.updateTask(request);
            result.put("code", "0");
            result.put("msg", "操作成功！");
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "修改商品异常！");
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public JSONObject deleteTask(int id) {
        JSONObject result = new JSONObject();
        try {
            tasksDao.deleteTask(id);
            result.put("code", "0");
            result.put("msg", "操作成功！");
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "删除商品异常！");
        }
        return result;
    }

    @Override
    public JSONObject searchTasks(JSONObject request) {
        JSONObject result = new JSONObject();
        try {
            int pageNum = request.getInteger("pageNum");
            int pageSize = request.getInteger("pageSize");
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<seTasks> pageInfo = new PageInfo(tasksDao.searchTasks(request));

            result.put("code", "0");
            result.put("msg", "操作成功！");
            result.put("data", pageInfo.getList());
            result.put("count", pageInfo.getTotal());
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "查询商品异常！");
        }
        return result;
    }

    @Override
    public JSONObject getSeList(int pageNum, int pageSize) {
        JSONObject result = new JSONObject();
        try {
            PageHelper.startPage(pageNum, pageSize);
            PageInfo<seInfo> pageInfo = new PageInfo(infoDao.getSesList());

            result.put("code", "0");
            result.put("msg", "操作成功！");
            result.put("data", pageInfo.getList());
            result.put("count", pageInfo.getTotal());
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "查询异常！");
        }
        return result;
    }

    @Override
    public JSONObject addSe(JSONObject request){
        JSONObject result = new JSONObject();
        try {
            infoDao.addSe(request);
            result.put("code", "0");
            result.put("msg", "操作成功！");
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "新增商品异常！");
        }
        return result;
    }

    public List<seInfo> getSeList2() {

        return infoDao.getSesList();

    }

    public void putReportStr(int taskId, String sheetName, int nameCol, int valueCol, String reportStr){
        tasksDao.putReportStr(taskId, sheetName, nameCol, valueCol, reportStr);
    }

    public JSONObject getTaskReportInfo(int taskId) {
        JSONObject result = new JSONObject();
        try {
            seTasks task = tasksDao.getTaskReportInfo(taskId);

            result.put("code", "0");
            result.put("msg", "操作成功！");
            result.put("sheetName", task.getSheetName());
            result.put("nameCol", task.getNameCol());
            result.put("valueCol", task.getValueCol());
            result.put("reportStr", task.getReportStr());
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", "查询异常！");
        }
        return result;
    }
}
