package com.mocent.service;


import com.baomidou.mybatisplus.service.IService;
import com.mocent.model.Resource;
import com.mocent.model.result.Tree;
import com.mocent.shiro.ShiroUser;

import java.util.List;

/**
 *
 * Resource 表数据服务层接口
 *
 */
public interface IResourceService extends IService<Resource> {

    List<Resource> selectAll();

    List<Tree> selectAllMenu();

    List<Tree> selectAllTree();

    List<Tree> selectTree(ShiroUser shiroUser);

}