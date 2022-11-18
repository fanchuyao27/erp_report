package com.sfp.service.impl;

import com.sfp.entity.ErpMsg;
import com.sfp.mapper.UploadExlMapper;
import com.sfp.service.UploadExlService;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fcy
 * @version 1.0.0
 * @description
 * @createTime 2022/9/2 13:40
 **/

@Service
public class UploadExlServiceImpl implements UploadExlService {

    @Resource
    private UploadExlMapper UploadExlMapper;


    @Override
    public int batchSaveOrder(List<ErpMsg> erpMsgList) {
        return UploadExlMapper.batchSaveOrder(erpMsgList);
    }

    @Override
    public List<String> getDocNo() {
        return UploadExlMapper.getDocNo();
    }

    @Override
    public List<ErpMsg> fileToErpList(Workbook rwb) throws Exception {
        List<ErpMsg> erpMsgList = new ArrayList<>();
        int rowNumber = 0;
        StringBuffer errorMsg = new StringBuffer();
        try {
            Sheet[] sheets = rwb.getSheets();
            Sheet sheet = sheets[0];//获取excel中的sheet1
            //遍历sheet中的行，保存为数组
            //前两行为表格标题栏，i从2开始
            for (int i = 2; i < sheet.getRows(); i++) {
                rowNumber = i + 1;
                Cell[] cellArray = sheet.getRow(i);//单元格数组
                // 处理每个单元格的数据
                String supplier = cellArray[0].getContents().trim();
                String date = cellArray[1].getContents().trim();
                String documentNo = cellArray[2].getContents().trim();
                String auditFlag = cellArray[3].getContents().trim();
                String closeFlag = cellArray[4].getContents().trim();
                String materialCode = cellArray[5].getContents().trim();
                String materialLongCode = cellArray[6].getContents().trim();
                String materialName = cellArray[7].getContents().trim();
                String specificationAndModel = cellArray[8].getContents().trim();
                String salesman = cellArray[9].getContents().trim();
                String unit = cellArray[10].getContents().trim();
                String currency = cellArray[11].getContents().trim();
                BigDecimal quantity = strToBigDecimal(cellArray[12].getContents().trim());
                BigDecimal unitPrice = strToBigDecimal(cellArray[13].getContents().trim());
                BigDecimal totalPrice = strToBigDecimal(cellArray[14].getContents().trim());
                BigDecimal warehousingQuantity = strToBigDecimal(cellArray[15].getContents().trim());
                String deliveryDate = cellArray[16].getContents().trim();
                String auditStatus = cellArray[17].getContents().trim();
                String reviewer = cellArray[18].getContents().trim();

                //补全表格中 供应商、日期、单据编号、业务员、币别、审核流程状态 的空格：空行等于上一行
                if (StringUtils.isBlank(supplier)) {
                    supplier = erpMsgList.get(erpMsgList.size() - 1).getSupplier();
                }
                if (StringUtils.isBlank(date)) {
                    date = erpMsgList.get(erpMsgList.size() - 1).getDate();
                }
                if (StringUtils.isBlank(documentNo)) {
                    documentNo = erpMsgList.get(erpMsgList.size() - 1).getDocumentNo();
                }
                if (StringUtils.isBlank(salesman)) {
                    salesman = erpMsgList.get(erpMsgList.size() - 1).getSalesman();
                }
                if (StringUtils.isBlank(currency)) {
                    currency = erpMsgList.get(erpMsgList.size() - 1).getCurrency();
                }
                if (StringUtils.isBlank(auditStatus)) {
                    auditStatus = erpMsgList.get(erpMsgList.size() - 1).getAuditStatus();
                }

                // 设置数据
                ErpMsg erpMsg = new ErpMsg();
                erpMsg.setSupplier(supplier);
                erpMsg.setDate(date);
                erpMsg.setDocumentNo(documentNo);
                erpMsg.setAuditFlag(auditFlag);
                erpMsg.setCloseFlag(closeFlag);
                erpMsg.setMaterialCode(materialCode);
                erpMsg.setMaterialLongCode(materialLongCode);
                erpMsg.setMaterialName(materialName);
                erpMsg.setSpecificationAndModel(specificationAndModel);
                erpMsg.setSalesman(salesman);
                erpMsg.setUnit(unit);
                erpMsg.setCurrency(currency);
                erpMsg.setQuantity(quantity);
                erpMsg.setUnitPrice(unitPrice);
                erpMsg.setTotalPrice(totalPrice);
                erpMsg.setWarehousingQuantity(warehousingQuantity);
                erpMsg.setDeliveryDate(deliveryDate);
                erpMsg.setAuditStatus(auditStatus);
                erpMsg.setReviewer(reviewer);

                // 添加到列表
                erpMsgList.add(erpMsg);
            }
        } catch (ParseException e) {
            erpMsgList.clear();
            errorMsg.append("第").append(rowNumber).append("行数据格式错误");
            throw new Exception(errorMsg.toString());
        } catch (Exception e) {
            erpMsgList.clear();
            throw new Exception("文件内容未知错误");
        }
        return erpMsgList;
    }

    /**
     * 将BigDecimal类型转换成String类型
     */
    public BigDecimal strToBigDecimal(String str) throws ParseException {
        double strToDouble = new DecimalFormat().parse(str).doubleValue();
        BigDecimal doubleToBigDecimal = BigDecimal.valueOf(strToDouble);
        return doubleToBigDecimal;
    }

    @Override
    public List<ErpMsg> getInsertList(List<ErpMsg> erpMsgList) throws Exception {
        List<ErpMsg> toInsertList = new ArrayList();
        List<String> documentNo = getDocNo();//获取上上月,上月所有的单据编号
        int rowNumber = 0;
        StringBuffer errorMsg = new StringBuffer();
        try {
            for (int i = 0; i < erpMsgList.size(); i++) {
                rowNumber = i + 3;
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(erpMsgList.get(i).getDate());
                int month = date.getMonth();//获取list中日期的月份
                int thisMouth = new Date().getMonth() - 1;// 上月 月份
                int lastMouth = new Date().getMonth() - 2;// 上上月 月份
                String auditStatus = erpMsgList.get(i).getAuditStatus();//获取list中的审核状态
                String innerDocNo = erpMsgList.get(i).getDocumentNo();//获取list中的单据编号

                if (auditStatus.equals("已审核") && !documentNo.contains(innerDocNo) && ((month == lastMouth) || month == thisMouth)) {
                    toInsertList.add(erpMsgList.get(i));
                }
            }
        } catch (ParseException e) {
            toInsertList.clear();
            errorMsg.append("第").append(rowNumber).append("行日期格式错误");
            throw new Exception(errorMsg.toString());
        } catch (Exception e) {
            toInsertList.clear();
            throw new Exception("文件内容未知错误");
        }
        return toInsertList;
    }
}
