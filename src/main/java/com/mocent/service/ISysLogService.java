package com.mocent.service;


import com.baomidou.mybatisplus.service.IService;
import com.mocent.model.SysLog;
import com.mocent.util.PageInfo;

/**
 *
 * SysLog 表数据服务层接口
 *
 */
public interface ISysLogService extends IService<SysLog> {

    void selectDataGrid(PageInfo pageInfo);

}