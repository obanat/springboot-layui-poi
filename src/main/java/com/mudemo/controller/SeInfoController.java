package com.mudemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.mudemo.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/vars")
public class SeInfoController {
    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/seList",method = RequestMethod.GET)
    public JSONObject getSeList(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        return goodsService.getSeList(pageNum, pageSize);
    }

    @RequestMapping(value = "/addSe", method = RequestMethod.POST)
    public JSONObject addSe(@RequestBody JSONObject request) {
        return goodsService.addSe(request);
    }
}
