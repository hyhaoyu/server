package com.haoyu.service.impl;

import com.haoyu.mapper.TbAdminMapper;
import com.haoyu.mapper.TbStudentMapper;
import com.haoyu.pojo.TbStudent;
import com.haoyu.pojo.TbStudentExample;
import com.haoyu.pojo.vo.Image;
import com.haoyu.pojo.vo.Student;
import com.haoyu.pojo.vo.StudentList;
import com.haoyu.service.StudentService;
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
public class StudentServiceImpl implements StudentService {

    @Autowired
    private TbStudentMapper studentMapper;
    @Autowired
    private TbAdminMapper adminMapper;
    @Autowired
    private IdWorker idWorker;

    private void existUsername(TbStudent student){
        TbStudentExample example = new TbStudentExample();
        TbStudentExample.Criteria criteria = example.createCriteria();

        criteria.andUsernameEqualTo(student.getUsername());
        //判断用户名是否存在
        List<TbStudent> studentList = studentMapper.selectByExample(example);
        if(studentList != null && studentList.size() != 0){
            throw new RuntimeException("用户已存在");
        }
    }

    @Override
    public Student login(String username, String password) {

        TbStudentExample example = new TbStudentExample();
        TbStudentExample.Criteria criteria = example.createCriteria();

        criteria.andUsernameEqualTo(username);
        //根据用户名查询数据
        List<TbStudent> studentList = studentMapper.selectByExample(example);
        //判断数据是否存在
        if(studentList != null && studentList.size() == 1){
            //将用户密码加密密
            String encodingPassword = DigestUtils.md5DigestAsHex(password.getBytes());
            if(encodingPassword.equals(studentList.get(0).getPassword())){
                Student _student = new Student();
                BeanUtils.copyProperties(studentList.get(0), _student);
                _student.setToken(TokenWorker.generateToken(_student.getId(),password));
                return _student;
            }
        }
        return null;
    }

    @Override
    public void register(TbStudent student) {

        //判断用户名是否存在
        existUsername(student);
        //将用户注册信息保存到数据库
        //使用雪花算法生成唯一ID
        student.setId(String.valueOf(idWorker.nextId()));
        //对密码进行md5加密
        student.setPassword(DigestUtils.md5DigestAsHex(student.getPassword().getBytes()));
        student.setUsername(student.getUsername());
        student.setPhone(student.getPhone());
        student.setRegistrationDate(new Date());

        studentMapper.insert(student);

    }

    @Override
    public void deleteStudentByStudentId(String studentId, String token) {

        //验证权限
        if(adminMapper.selectByPrimaryKey(TokenWorker.getIdFromJWT(token)) == null){
            throw new RuntimeException("无查询权限，请以管理员身份登录");
        }
        if(StringUtils.isBlank(studentId) || studentMapper.deleteByPrimaryKey(studentId) == 0) {
            throw new RuntimeException("用户不存在");
        }
    }

    @Override
    public Image updateStudentAvatar(String studentId, MultipartFile imgFile, String token) throws IOException {

        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);

        //验证权限
        if(!id.equals(studentId) && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无更新权限，请以管理员或者用户本人身份登录");
        }
        TbStudent tbStudent = studentMapper.selectByPrimaryKey(studentId);
        if(tbStudent == null){
            throw new RuntimeException("用户更新错误");
        }

        //保存图片
        if(imgFile != null) {
            String picUrl = "/images/" + ImgFileWorker.saveImg(imgFile, "student", studentId);
            tbStudent.setAvatarUrl(picUrl);
            studentMapper.updateByPrimaryKey(tbStudent);
            return new Image(studentId, picUrl);
        }
        else{
            return null;
        }
    }

    @Override
    public void updateStudent(TbStudent student,  String token){

        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);

        //验证权限
        if(!id.equals(student.getId()) && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无更新权限，请以管理员或者用户本人身份登录");
        }
        TbStudent tbStudent = studentMapper.selectByPrimaryKey(student.getId());
        if(tbStudent == null){
            throw new RuntimeException("用户更新错误");
        }
        else if(!tbStudent.getUsername().equals(student.getUsername())){
            //判断用户名是否存在
            existUsername(student);
        }

        if(StringUtils.isBlank(student.getPassword())){
            student.setPassword(tbStudent.getPassword());
        }
        else{
            student.setPassword(DigestUtils.md5DigestAsHex(student.getPassword().getBytes()));
        }
        student.setRegistrationDate(tbStudent.getRegistrationDate());
        studentMapper.updateByPrimaryKey(student);

    }

    @Override
    public StudentList queryStudent(String name, String courseId, Integer pageNum, Integer pageSize, String token) {

        //验证权限
        if(adminMapper.selectByPrimaryKey(TokenWorker.getIdFromJWT(token)) ==null){
            throw new RuntimeException("无查询权限，请以管理员身份登录");
        }

        TbStudentExample example = new TbStudentExample();
        example.setDistinct(true);
        TbStudentExample.Criteria criteria1 = example.createCriteria();
        TbStudentExample.Criteria criteria2 = example.createCriteria();

        //查询条件
        if(StringUtils.isNotBlank(name)){
            criteria1.andRealNameLike("%"+name+"%");
            criteria2.andUsernameLike("%"+name+"%");
            example.or(criteria2);
        }
        if(StringUtils.isNotBlank(courseId)){
            criteria1.andCourseIdEqualTo(courseId);
            criteria2.andCourseIdEqualTo(courseId);
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
        List<TbStudent> tbStudentList = studentMapper.selectByExample(example);
        long total = studentMapper.countByExample(example);
        List<Student> studentList = new ArrayList<>();

        //判断数据是否存在
        if(tbStudentList != null && tbStudentList.size() != 0){

            for(TbStudent tbStudent : tbStudentList){
                Student _student = new Student();
                BeanUtils.copyProperties(tbStudent, _student);
                studentList.add(_student);
            }
            return new StudentList(total, pageNum, studentList);
        }
        return null;
    }
}
