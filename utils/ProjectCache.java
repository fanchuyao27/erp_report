package com.sfp.utils;

import com.sfp.entity.Order;
import com.sfp.entity.Overview;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ProjectCache {
    private static ProjectCache cache;

    //缓存物料信息，物料号（longCode）为Key，具体物料信息为Value
    private Map<String, Order> materialCache = null;

    // 缓存top60物料信息
    private Map<String, List<Order>> top60Cache = null;

    // 缓存top60物料信息
    private Map<String, List<Order>> largeAmountCache = null;

    //缓存年度订单概览信息
    private Map<String, List<Overview>> overviewCache = null;

    //缓存年度Top20供应商 ---- key为供应商
    private Map<String, List<Order>> top20Cache = null;

    //缓存年度Top20供应商 ---- key为年份
    private Map<String, List<Order>> top20YearCache = null;

    public ProjectCache() {
    }

    //单例，双检锁
    public static ProjectCache getInstance(){
        if (cache == null){
            synchronized (ProjectCache.class){
                if (cache == null){
                    cache = new ProjectCache();
                    cache.materialCache = new HashMap<>();
                    cache.top60Cache = new HashMap<>();
                    cache.largeAmountCache = new HashMap<>();
                    cache.overviewCache = new HashMap<>();
                    cache.top20Cache = new HashMap<>();
                    cache.top20YearCache = new HashMap<>();
                }
            }
        }
        return cache;
    }

   // 清空缓存
    public void clearAll(){
        if (cache != null){
            cache.materialCache.clear();
            cache.top60Cache.clear();
            cache.largeAmountCache.clear();
            cache.overviewCache.clear();
            cache.top20Cache.clear();
            cache.top20YearCache.clear();
        }
    }

}
