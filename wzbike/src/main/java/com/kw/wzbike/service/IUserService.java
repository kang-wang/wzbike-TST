package com.kw.wzbike.service;

import com.kw.wzbike.common.ServerResponse;
import com.kw.wzbike.pojo.User;

public interface IUserService {
    /**
     * @description:  发送手机验证码
     * @author: kw
     * @date: 2019/3/13
     * @param: [countryCode, phoneNum]
     * @return: com.kw.wzbike.common.ServerResponse<java.lang.String>
     */
    ServerResponse<String> sendMsg(String countryCode, String phoneNum);

    /**
     * @description:  判断输入和接收的验证码是否一致
     * @author: kw
     * @date: 2019/3/13
     * @param: [phoneNum, verifyCode]
     * @return: com.kw.wzbike.common.ServerResponse<java.lang.String>
     */
    ServerResponse<String> verify(String phoneNum, String verifyCode);

    /**
     * @description:  如果一致则注册成功
     * @author: kw
     * @date: 2019/3/13
     * @param: [user]
     * @return: com.kw.wzbike.common.ServerResponse<com.kw.wzbike.pojo.User>
     */
    ServerResponse<User> register(User user);

    /**
     * @description:  更新个人信息
     * @author: kw
     * @date: 2019/3/13
     * @param: [user]
     * @return: com.kw.wzbike.common.ServerResponse<com.kw.wzbike.pojo.User>
     */
    ServerResponse<User> update(User user);
}
