package com.sfp.mapper;

import com.sfp.entity.ErpMsg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author fcy
 * @version 1.0.0
 * @description
 * @createTime 2022/9/2 14:13
 **/

@Mapper
public interface UploadExlMapper {

   // 上月和上上月已审核数据插入数据库
    int batchSaveOrder(List<ErpMsg> erpMsgList);


    // 获取上上月所有单据编号(documentNo)
    List<String> getDocNo();
}
