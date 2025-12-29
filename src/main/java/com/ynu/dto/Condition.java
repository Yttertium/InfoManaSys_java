package com.ynu.dto;

import lombok.Data;

@Data
public class Condition {
    private String field;    // 字段名
    private String operator; // 操作符
    private String value;    // 值
    private String logic;    // 逻辑关系
}