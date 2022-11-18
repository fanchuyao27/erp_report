package com.sfp.mapper;

import com.sfp.entity.ErpMsg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UploadExlMapper {

   // 上月和上上月已审核数据插入数据库
    int batchSaveOrder(List<ErpMsg> erpMsgList);


    // 获取上上月所有单据编号(documentNo)
    List<String> getDocNo();
}
