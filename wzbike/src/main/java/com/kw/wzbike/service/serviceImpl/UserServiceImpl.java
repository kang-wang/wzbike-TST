package com.kw.wzbike.service.serviceImpl;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.kw.wzbike.common.Constant;
import com.kw.wzbike.common.ServerResponse;
import com.kw.wzbike.pojo.User;
import com.kw.wzbike.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @program: wzbike
 * @description:
 * @author: kw
 * @create: 2019/03/05 13:46
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ServerResponse<String> sendMsg(String countryCode, String phoneNum) {

        //使用腾讯云的短信API
        int appid = Integer.parseInt(stringRedisTemplate.opsForValue().get(Constant.REDIS_APPID));
        String appkey = stringRedisTemplate.opsForValue().get(Constant.REDIS_APPKEY);
        //生成一个随机的数字
        String code = ""+(int)((Math.random()*9+1)*1000);
        SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
        try {
            //向对应手机号的用户发送短信
            SmsSingleSenderResult result = ssender.send(0,countryCode,phoneNum, Constant.VERIFYCATION_CODE_TEMPLATE + code,"","");
            //将发送的手机号作为key，验证码作为value保存在Redis中
            stringRedisTemplate.opsForValue().set(phoneNum,code,Constant.VERIFYCATION_CODE_TIMEOUT_SECOND, TimeUnit.SECONDS);
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("获取验证码失败");
        }
        return ServerResponse.createBySuccessMessage("验证码已发送");
    }

    @Override
    public ServerResponse<String> verify(String phoneNum, String verifyCode) {
        //调用redisTemplate,根据手机号的key，查找对应的验证码
        String code = stringRedisTemplate.opsForValue().get(phoneNum);
        System.out.println(phoneNum);
        if(code != null && code.equals(verifyCode)){
            return ServerResponse.createBySuccessMessage("验证成功");
        }
        return ServerResponse.createByErrorMessage("您输入的验证码错误，请重新输入");
    }

    @Override
    public ServerResponse<User> register(User user) {
        //调用MongoDB的dao
        try {
                mongoTemplate.insert(user);
        }catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<User> update(User user) {
        Update update = new Update();
        if (user.getDeposit() != null) {
            update.set("deposit", user.getDeposit());
        }
        if (user.getStatus() != null) {
            update.set("status", user.getStatus());
        }
        if (user.getRealName() != null) {
            update.set("realName", user.getRealName());
        }
        if (user.getIdCard() != null) {
            update.set("idCard", user.getIdCard());
        }
        try {
            mongoTemplate.updateFirst(new Query(Criteria.where("phoneNum").is(user.getPhoneNum())),
                    update,User.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("充值失败");
        }
        return ServerResponse.createBySuccessMessage("充值成功");
    }
}
