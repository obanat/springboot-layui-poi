package com.mudemo.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.*;
import com.mudemo.model.seInfo;
import java.util.List;

@Mapper
public interface SeInfoDao {
    @Select("SELECT id,name,plName,dept,description,welinkId FROM oc80.seinfo")
    List<seInfo> getSesList();

    @Insert("INSERT INTO oc80.setasks(name,start_time,end_time,owner,url,description)VALUES(#{name},#{start_time},#{end_time},#{owner},#{url},#{description})")
    int addSe(JSONObject request);
}
