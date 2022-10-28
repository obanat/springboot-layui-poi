package com.mudemo.model;

import lombok.Data;

/**
 * @author obana
 * @version 1.0
 * @date 2022/10/22 12:16
 * @description
 * @modify
 */
@Data
public class seTasks {
    private int id;
    private String name;
    private String start_time;
    private String end_time;
    private String owner;
    private String status;
    private int prio;
    private String url;
    private String description;
    private String sheetName;
    private int nameCol;
    private int valueCol;
    private String reportStr;
}
