package com.mudemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.mudemo.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author obana
 * @version 1.0
 * @date 2022/10/26 12:41
 * @description
 * @modify
 */
@RestController
@RequestMapping(value = "/tasks")
public class TasksController {
    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/tasksList", method = RequestMethod.GET)
    public JSONObject getTasksList(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return goodsService.getTasksList(pageNum, pageSize);
    }

    @RequestMapping(value = "/updateTask", method = RequestMethod.POST)
    public JSONObject updateTask(@RequestBody JSONObject request) {
        return goodsService.updateTask(request);
    }

    @RequestMapping(value = "/addTask", method = RequestMethod.POST)
    public JSONObject addTask(@RequestBody JSONObject request) {
        return goodsService.addTask(request);
    }

    @RequestMapping(value = "/deleteTask", method = RequestMethod.GET)
    public JSONObject deleteTask(@RequestParam("id") int id) {
        return goodsService.deleteTask(id);
    }

    @RequestMapping(value = "searchTasks",method = RequestMethod.POST)
    public JSONObject searchTask(@RequestBody JSONObject request){
        return goodsService.searchTasks(request);
    }

    @RequestMapping(value = "/taskReportInfo", method = RequestMethod.GET)
    public JSONObject getTaskReportInfo(@RequestParam("taskId") int taskId) {
        return goodsService.getTaskReportInfo(taskId);
    }
}
