package com.sfp.controller;

import com.sfp.entity.ErpMsg;
import com.sfp.service.UploadExlService;
import com.sfp.utils.ProjectCache;
import com.sfp.utils.Result;
import jxl.*;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

/**
 * @author fcy
 * @version 1.0.0
 * @description
 * @createTime 2022/8/23 16:34
 **/
@RestController
public class UploadExcelController {

    @Autowired
    private UploadExlService uploadExlServiceImpl;

    private Result result;

    @ResponseBody
    @RequestMapping(value = "/uploadExcel")
    public String uploadExcel(MultipartFile file) {
        InputStream is;
        Workbook rwb;
        List<ErpMsg> erpMsgList;
        List<ErpMsg> toInsertList = null;

        try {
            // 取得upload文件的流
            is = file.getInputStream();
            rwb = Workbook.getWorkbook(is);

            try {
                erpMsgList = uploadExlServiceImpl.fileToErpList(rwb);
                if (erpMsgList.isEmpty()) {
                    result = new Result(1, "文件内容为空");
                } else {
                    try {
                        toInsertList = uploadExlServiceImpl.getInsertList(erpMsgList);
                    } catch (Exception e) {
                        result = new Result(1, e.getMessage());
                        return result.toString();
                    }
                    //插入数据库
                    try {
                        if (toInsertList.isEmpty()) {
                            result = new Result(1, "数据已存在");
                        } else {
                            int saveResult = uploadExlServiceImpl.batchSaveOrder(toInsertList);
                            if (saveResult > 0) {
                                result = new Result(0, "上传成功");
                                ProjectCache.getInstance().clearAll();
                            } else {
                                result = new Result(1, "上传失败");
                            }
                        }
                    } catch (Exception e) {
                        result = new Result(1, "数据库异常，上传失败");
                    }
                }
            } catch (Exception e) {
                result = new Result(1, e.getMessage());
            }
        } catch (BiffException e) {
            result = new Result(1, "文件异常，上传失败");
        } finally {
            return result.toString();
        }
    }
}




