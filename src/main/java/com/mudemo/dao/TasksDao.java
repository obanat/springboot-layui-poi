package com.mudemo.dao;

import com.alibaba.fastjson.JSONObject;
import com.mudemo.model.seTasks;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TasksDao {
    @Select("SELECT id,name,start_time,end_time,owner,status,prio,url,description,sheetName,nameCol,valueCol FROM oc80.setasks")
    List<seTasks> getTasksList();

    @Insert("INSERT INTO oc80.setasks(name,start_time,end_time,owner,url,description)VALUES(#{name},#{start_time},#{end_time},#{owner},#{url},#{description})")
    int addTask(JSONObject request);

    @Update("UPDATE oc80.setasks SET name=#{name},start_time=#{start_time},end_time=#{end_time},owner=#{owner},url=#{url},description=#{description} WHERE id=#{id}")
    void updateTask(JSONObject request);

    @Delete("DELETE FROM oc80.setasks WHERE id=#{id}")
    void deleteTask(@Param("id") int id);

    List<seTasks> searchTasks(JSONObject request);
    @Update("UPDATE oc80.setasks SET sheetName=#{sheetName},nameCol=#{nameCol},valueCol=#{valueCol},reportStr=#{reportStr} WHERE id=#{taskId}")
    void putReportStr(int taskId, String sheetName, int nameCol, int valueCol, String reportStr);

    @Select("SELECT id,name,start_time,end_time,owner,status,prio,url,description,sheetName,nameCol,valueCol,reportStr FROM oc80.setasks WHERE id=#{taskId}")
    seTasks getTaskReportInfo(int taskId);
}
