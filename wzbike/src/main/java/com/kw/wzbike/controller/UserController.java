package com.kw.wzbike.controller;

import com.kw.wzbike.common.ServerResponse;
import com.kw.wzbike.pojo.User;
import com.kw.wzbike.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: wzbike
 * @description:
 * @author: kw
 * @create: 2019/03/04 17:05
 */
@RestController
@RequestMapping(value = "/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * @description:  发送手机验证码
     * @author: kw
     * @date: 2019/3/13
     * @param: [countryCode, phoneNum]
     * @return: com.kw.wzbike.common.ServerResponse<java.lang.String>
     */
    @RequestMapping(value = "getCode")
    public ServerResponse<String> getVerityCode(String countryCode, String phoneNum) {
        return iUserService.sendMsg(countryCode, phoneNum);
    }

    /**
     * @description:  判断验证码是否一致
     * @author: kw
     * @date: 2019/3/13
     * @param: [phoneNum, verifyCode]
     * @return: com.kw.wzbike.common.ServerResponse<java.lang.String>
     */
    @RequestMapping(value = "verify")
    public ServerResponse<String> verify(String phoneNum, String verifyCode) {
        return iUserService.verify(phoneNum, verifyCode);
    }

    /**
     * @description:  注册
     * @author: kw
     * @date: 2019/3/13
     * @param: [user]
     * @return: com.kw.wzbike.common.ServerResponse<com.kw.wzbike.pojo.User>
     */
    @RequestMapping(value = "register")
    public ServerResponse<User> register(@RequestBody User user) {
        return iUserService.register(user);
    }

    /**
     * @description:  充值押金
     * @author: kw
     * @date: 2019/3/13
     * @param: [user]
     * @return: com.kw.wzbike.common.ServerResponse<com.kw.wzbike.pojo.User>
     */
    @RequestMapping(value = "deposit")
    public ServerResponse<User> deposit(@RequestBody User user) {
        return iUserService.update(user);
    }

    /**
     * @description:  实名认证
     * @author: kw
     * @date: 2019/3/18
     * @param: [user]
     * @return: com.kw.wzbike.common.ServerResponse<com.kw.wzbike.pojo.User>
     */
    @RequestMapping(value = "identity")
    public ServerResponse<User> identity(@RequestBody User user) {
        return iUserService.update(user);
    }
}
