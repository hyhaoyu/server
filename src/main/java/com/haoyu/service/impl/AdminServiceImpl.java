package com.haoyu.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.haoyu.mapper.TbAdminMapper;
import com.haoyu.pojo.TbAdmin;
import com.haoyu.pojo.TbAdminExample;
import com.haoyu.pojo.vo.Admin;
import com.haoyu.service.AdminService;
import com.haoyu.util.IdWorker;
import com.haoyu.util.TokenWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    TbAdminMapper adminMapper;
    @Autowired
    private IdWorker idWorker;

    @Override
    public Admin login(String username, String password) {

        TbAdminExample example = new TbAdminExample();
        TbAdminExample.Criteria criteria = example.createCriteria();

        criteria.andUsernameEqualTo(username);

        //根据用户名查询数据
        List<TbAdmin> adminList = adminMapper.selectByExample(example);
        //判断数据是否存在
        if(adminList != null && adminList.size() == 1){
            //将用户密码加密密
            String encodingPassword = DigestUtils.md5DigestAsHex(password.getBytes());
            if(encodingPassword.equals(adminList.get(0).getPassword())){
                Admin _admin=new Admin();
                BeanUtils.copyProperties(adminList.get(0),_admin);
                _admin.setToken(TokenWorker.generateToken(_admin.getId(),password));
                return _admin;
            }
        }
        return null;
    }

    @Override
    public void register(TbAdmin admin) {

        TbAdminExample example = new TbAdminExample();
        TbAdminExample.Criteria criteria = example.createCriteria();

        criteria.andUsernameEqualTo(admin.getUsername());

        //根据用户名查询数据
        List<TbAdmin> adminList = adminMapper.selectByExample(example);
        //判断数据是否存在
        if(adminList != null && adminList.size() > 0){
            throw  new RuntimeException("用户已存在");
        }
        //将用户注册信息保存到数据库
        //使用雪花算法生成唯一ID
        admin.setId(String.valueOf(idWorker.nextId()));
        //对密码进行md5加密
        admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
        admin.setUsername(admin.getUsername());

        adminMapper.insert(admin);
    }
}
