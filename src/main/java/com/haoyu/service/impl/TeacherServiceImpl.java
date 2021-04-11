package com.haoyu.service.impl;

import com.haoyu.mapper.TbAdminMapper;
import com.haoyu.mapper.TbCourseMapper;
import com.haoyu.mapper.TbStudentCourseMapper;
import com.haoyu.mapper.TbTeacherMapper;
import com.haoyu.pojo.TbTeacher;
import com.haoyu.pojo.TbTeacherExample;
import com.haoyu.pojo.vo.Image;
import com.haoyu.pojo.vo.Teacher;
import com.haoyu.pojo.vo.TeacherDetail;
import com.haoyu.pojo.vo.TeacherList;
import com.haoyu.service.TeacherService;
import com.haoyu.util.IdWorker;
import com.haoyu.util.ImgFileWorker;
import com.haoyu.util.TokenWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TbTeacherMapper teacherMapper;
    @Autowired
    private TbAdminMapper adminMapper;

    @Autowired
    private IdWorker idWorker;

    //判断用户名是否存在
    public void existUsername(TbTeacher teacher){
        TbTeacherExample example = new TbTeacherExample();
        TbTeacherExample.Criteria criteria = example.createCriteria();

        criteria.andUsernameEqualTo(teacher.getUsername());

        List<TbTeacher> teacherList = teacherMapper.selectByExample(example);
        if(teacherList != null && teacherList.size() != 0){
            throw new RuntimeException("用户已存在");
        }
    }

    @Override
    public Teacher login(String username, String password) {

        TbTeacherExample example = new TbTeacherExample();
        TbTeacherExample.Criteria criteria = example.createCriteria();

        criteria.andUsernameEqualTo(username);
        //根据用户名查询数据
        List<TbTeacher> teacherList = teacherMapper.selectByExample(example);
        //判断数据是否存在
        if(teacherList != null && teacherList.size() == 1){
            //将用户密码加密密
            String encodingPassword = DigestUtils.md5DigestAsHex(password.getBytes());
            if(encodingPassword.equals(teacherList.get(0).getPassword())){
                Teacher _teacher = new Teacher();
                BeanUtils.copyProperties(teacherList.get(0),_teacher);
                _teacher.setToken(TokenWorker.generateToken(_teacher.getId(), password, 1));
                return _teacher;
            }
        }
        return null;
    }

    @Override
    public void register(TbTeacher teacher) {

        //判断用户名是否存在
        existUsername(teacher);
        //将用户注册信息保存到数据库
        //使用雪花算法生成唯一ID
        teacher.setId(String.valueOf(idWorker.nextId()));
        //对密码进行md5加密
        teacher.setPassword(DigestUtils.md5DigestAsHex(teacher.getPassword().getBytes()));
        teacher.setUsername(teacher.getUsername());
        teacher.setPhone(teacher.getPhone());
        teacher.setRegistrationDate(new Date());

        teacherMapper.insert(teacher);
    }

    @Override
    public void deleteTeacherByTeacherId(String teacherId, String token) {
        //验证权限
        if(adminMapper.selectByPrimaryKey(TokenWorker.getIdFromJWT(token)) ==null){
            throw new RuntimeException("无查询权限，请以管理员身份登录");
        }
        if(StringUtils.isBlank(teacherId) || teacherMapper.deleteByPrimaryKey(teacherId) == 0) {
            throw new RuntimeException("用户不存在");
        }
    }

    @Override
    public Image updateTeacherAvatar(String teacherId, MultipartFile imgFile, String token) throws IOException {
        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);

        //验证权限
        if(!id.equals(teacherId) && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无更新权限，请以管理员或者用户本人身份登录");
        }
        TbTeacher tbTeacher = teacherMapper.selectByPrimaryKey(teacherId);
        if(tbTeacher == null){
            throw new RuntimeException("用户更新错误");
        }

        //保存图片
        if(imgFile != null) {
            String picUrl = "/images/" + ImgFileWorker.saveImg(imgFile, "teacher", teacherId);
            tbTeacher.setAvatarUrl(picUrl);
            teacherMapper.updateByPrimaryKey(tbTeacher);
            return new Image(teacherId, picUrl);
        }
        else{
            return null;
        }
    }

    @Override
    public void updateTeacher(TbTeacher teacher, String token) {

        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);

        //验证权限
        if(!id.equals(teacher.getId()) && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无更新权限，请以管理员或者用户本人身份登录");
        }
        TbTeacher tbTeacher = teacherMapper.selectByPrimaryKey(teacher.getId());
        if(tbTeacher == null){
            throw new RuntimeException("更新错误");
        }
        else if(!tbTeacher.getUsername().equals(teacher.getUsername())){
            //判断用户名是否存在
            existUsername(teacher);
        }

        if(StringUtils.isBlank(teacher.getPassword())){
            teacher.setPassword(tbTeacher.getPassword());
        }
        else{
            teacher.setPassword(DigestUtils.md5DigestAsHex(teacher.getPassword().getBytes()));
        }
        teacher.setPassword(tbTeacher.getPassword());
        teacher.setRegistrationDate(tbTeacher.getRegistrationDate());
        teacherMapper.updateByPrimaryKey(teacher);

    }

    @Override
    public TeacherList queryTeacher(String name, Integer pageNum, Integer pageSize, String token) {

        //验证权限
        TokenWorker.verifyToken(token);

        TbTeacherExample example = new TbTeacherExample();
        TbTeacherExample.Criteria criteria1 = example.createCriteria();
        TbTeacherExample.Criteria criteria2 = example.createCriteria();

        //查询条件
        if(StringUtils.isNotBlank(name)){
            criteria1.andRealNameLike("%"+name+"%");
            criteria2.andUsernameLike("%"+name+"%");
            example.or(criteria2);
        }

        if(pageNum != null && pageSize != null){
            example.setStartCount((pageNum-1)*pageSize);
            example.setPageSize(pageSize);
        }
        else{
            pageNum = 1;
            example.setStartCount(null);
            example.setPageSize(null);
        }

        //根据用户名查询数据
        List<TbTeacher> tbTeacherList = teacherMapper.selectByExample(example);
        long total = teacherMapper.countByExample(example);
        List<Teacher> teacherList = new ArrayList<>();

        //判断数据是否存在
        if(tbTeacherList != null && tbTeacherList.size() != 0){
            for(TbTeacher tbTeacher : tbTeacherList){
                Teacher _teacher = new Teacher();
                BeanUtils.copyProperties(tbTeacher,_teacher);
                teacherList.add(_teacher);
            }
            return new TeacherList(total, pageNum, teacherList);
        }
        return null;

    }

    @Override
    public TeacherDetail queryTeacherById(String teacherId, String token) {

        //验证权限
        TokenWorker.verifyToken(token);
        TbTeacher tbTeacher = teacherMapper.selectByPrimaryKey(teacherId);
        if(tbTeacher != null){
            TeacherDetail teacher = new TeacherDetail();
            BeanUtils.copyProperties(tbTeacher, teacher);
            return  teacher;
        }
        else{
            throw new RuntimeException("无该讲师信息");
        }

    }
}
