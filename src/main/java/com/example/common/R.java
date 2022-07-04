package com.example.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用结果返回类
 * @param <T>
 */
@Data
public class R<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败

    //private String success;

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 200;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 410;
        return r;
    }
    public static <T> R<T> nferror(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 404;
        //r.success = "false";
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
