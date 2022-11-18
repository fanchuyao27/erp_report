package com.sfp.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private  Integer code;
    private  String message;
    private Integer count;
    private T data;

    /**
     * 结果包含分页
     * @param code 成功：0；失败：1
     */
    public Result(Integer code, String message){
        this(code, message, 0, null);
    }

    /**
     * 结果不包含分页
     * @param code 成功：0；失败：1
     */
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
