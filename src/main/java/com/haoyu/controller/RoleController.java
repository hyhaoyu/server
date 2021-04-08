package com.haoyu.controller;

import com.haoyu.pojo.TbAdmin;
import com.haoyu.pojo.vo.Result;
import com.haoyu.util.TokenWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/role")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoleController {

    //登录
    @RequestMapping(value = "",method = RequestMethod.GET)
    public Result getRole(@RequestHeader(value = "authorization",required = false)String token){

        try {
            if(StringUtils.isBlank(token)){
                return new Result(false,"请先登录");
            }
            else {
                Integer role = TokenWorker.getRoleFromJWT(token);
                return new Result(true,"用户角色查询成功", role);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"请先登录");
        }

    }
}
