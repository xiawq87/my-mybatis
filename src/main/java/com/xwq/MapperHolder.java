package com.xwq;

import com.alibaba.fastjson.JSONArray;
import com.xwq.config.MapperItem;
import com.xwq.util.JdbcUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MapperHolder {
    private static Map<String, List<MapperItem>> mapperMap = new ConcurrentHashMap<>();

    static {
        /**
         * 读取mapper配置
         */
        try {
            String path = MapperHolder.class.getClassLoader().getResource("").getPath();
            File resourcePath = new File(path);

            File[] files = resourcePath.listFiles();

            for(File file : files) {
                if(file.getName().endsWith("Mapper.json")) {
                    String filename = file.getName();
                    String key = filename.substring(0, filename.lastIndexOf("."));

                    String mapperStr = FileUtils.readFileToString(file, "UTF-8");
                    List<MapperItem> mapperItemList = JSONArray.parseArray(mapperStr, MapperItem.class);
                    mapperMap.put(key, mapperItemList);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Object execute(Class<?> mapperClass, Method mapperMethod, Object...args) {
        String mapperClassName = mapperClass.getName();
        mapperClassName = mapperClassName.substring(mapperClassName.lastIndexOf(".")+1);
        List<MapperItem> mapperItemList = mapperMap.get(mapperClassName);

        String methodName = mapperMethod.getName();

        if(CollectionUtils.isNotEmpty(mapperItemList)) {
            Optional<MapperItem> itemOptional = mapperItemList.stream().filter(item -> methodName.equals(item.getId())).findAny();

            if(itemOptional.isPresent()) {
                MapperItem mapperItem = itemOptional.get();

                String sqlType = mapperItem.getType();
                String sql = mapperItem.getSql();
                String[] split = sql.split("#");

                StringBuffer sb = new StringBuffer();

                int index = 0;
                for(String str : split) {
                    if(str.startsWith(":")) {
                        Object arg = args[index++];
                        if(arg instanceof String) {
                            arg = "'" + arg + "'";
                        }
                        sb.append(arg + " ");
                    } else {
                        sb.append(str + " ");
                    }
                }
                String executeSql = sb.toString();
                System.out.println("sql>>>" + executeSql);

                switch (sqlType) {
                    case MapperItem.TYPE_INSERT:
                        JdbcUtil.execute(executeSql);
                        return null;
                    case MapperItem.TYPE_DELETE:
                        JdbcUtil.execute(executeSql);
                        return null;
                    case MapperItem.TYPE_UPDATE:
                        JdbcUtil.execute(executeSql);
                        return null;
                    case MapperItem.TYPE_SELECT:
                        Class<?> returnType = mapperMethod.getReturnType();
                        return JdbcUtil.select(executeSql, returnType);
                    default:
                        break;
                }
            }
        }
        return null;
    }

}
