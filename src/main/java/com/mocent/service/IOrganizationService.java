package com.mocent.service;


import com.baomidou.mybatisplus.service.IService;
import com.mocent.model.Organization;
import com.mocent.model.result.Tree;

import java.util.List;

/**
 *
 * Organization 表数据服务层接口
 *
 */
public interface IOrganizationService extends IService<Organization> {

    List<Tree> selectTree();

    List<Organization> selectTreeGrid();

}