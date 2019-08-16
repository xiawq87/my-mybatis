package com.xwq.config;

import lombok.Data;

@Data
public class MapperItem {
    public static final String TYPE_INSERT = "insert";
    public static final String TYPE_DELETE = "delete";
    public static final String TYPE_UPDATE = "update";
    public static final String TYPE_SELECT = "select";

    private String id;
    private String type;
    private String sql;
}
