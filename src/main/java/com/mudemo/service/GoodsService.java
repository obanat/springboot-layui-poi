package com.mudemo.service;

import com.alibaba.fastjson.JSONObject;
import com.mudemo.model.seInfo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface GoodsService {
    JSONObject getTasksList(int pageNum, int pageSize);

    JSONObject addTask(JSONObject request);

    JSONObject updateTask(JSONObject request);

    JSONObject deleteTask(int id);

    JSONObject searchTasks(JSONObject request);

    JSONObject getSeList(int pageNum, int pageSize);

    JSONObject addSe(JSONObject request);

    List<seInfo> getSeList2();

    void putReportStr(int taskId, String sheetName, int nameCol, int valueCol, String reportStr);

    JSONObject getTaskReportInfo(int taskId);
}
