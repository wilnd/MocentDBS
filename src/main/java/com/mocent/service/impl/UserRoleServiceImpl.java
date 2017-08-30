package com.mocent.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mocent.mapper.UserRoleMapper;
import com.mocent.model.UserRole;
import com.mocent.service.IUserRoleService;
import org.springframework.stereotype.Service;

/**
 *
 * UserRole 表数据服务层接口实现类
 *
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}