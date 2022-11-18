package com.sfp.service;
import com.sfp.entity.ErpMsg;
import jxl.Workbook;
import java.util.List;


public interface UploadExlService {

    // 批量插入数据库
    int batchSaveOrder(List<ErpMsg> erpMsgList);

    // 获取上上月所有单据编号(documentNo)
    List<String> getDocNo();

    // 将file文件转换成list
    List<ErpMsg> fileToErpList(Workbook rwb) throws Exception;

    //将list转换成insertlist
    List<ErpMsg> getInsertList(List<ErpMsg> erpMsgList) throws Exception;
}
