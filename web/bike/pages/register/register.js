// pages/register/register.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    countryCodes: ['86','80','84','87'],
    countryCodeIndex: 0,
    phoneNum: ''
  },

  /**
       onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  bindCountryCodeChange: function (e) {
    console.log(e);
    this.setData({
      countryCodeIndex: e.detail.value
    })
  },

  inputPhoneNum: function (e) {
    console.log(e);
    this.setData({
      phoneNum: e.detail.value
    })
  },

  getVerifyCode: function (e) {
    //获取国家代码的索引
    var index = this.data.countryCodeIndex;
    //获取索引取值
    var countryCode = this.data.countryCodes[index];
    //获取输入的手机号
    var phoneNum = this.data.phoneNum;
    console.log(countryCode);
    console.log(phoneNum);

    //向后台发送请求
    wx.request({
      url: 'http://localhost:8080/user/getCode',
      data: {
        countryCode: countryCode,
        phoneNum: phoneNum,
      },
      method: 'GET',
      success: function (res) {
        wx.showToast({
          //验证码已发送
          title: res.data.msg,
          icon: 'success',
          duration: 2000,
        })
      }
    })
  },

  formSubmit: function (e) {
    var phoneNum = e.detail.value.phoneNum;
    var verifyCode = e.detail.value.verifyCode;
    //微信小程序发送请求的协议是https，地址必须是域名，不能带端口号 
    //验证
    wx.request({
      url: 'http://localhost:8080/user/verify',
      header: {'content-type': 'application/x-www-form-urlencoded'},
      method: 'POST',
      data: {
        phoneNum: phoneNum,
        verifyCode: verifyCode,
      },
      success: function (res) {
        console.log(res);
        if(res.data.status == 0){
          //注册
          wx.request({
            url: 'http://localhost:8080/user/register',
            method: 'POST',
            data: {
              phoneNum: phoneNum,
              registerDate: new Date(),
              status: 1,
            },
            success: function (res) {
              if(res.data.status==0){
                wx.navigateTo({
                  url: '../deposit/deposit',
                })
                //记录用户的状态 0未注册 1手机号绑定完成 2已实名认证
                //更新getApp().globalData，即内存的数据
                getApp().globalData.status = 1;
                getApp().globalData.phoneNum = phoneNum;
                //将用户的信息保存到手机内存
                wx.setStorageSync('status', 1);
                wx.setStorageSync('phoneNum', phoneNum);
              } else {
                wx.showModal({
                  title: '提示',
                  //注册失败
                  content: res.data.msg,
                })
              }
            }
          })
        } else {
          wx.showModal({
            title: '提示',
            //您输入的验证码错误，请重新输入
            content: res.data.msg,
            showCancel: false,
          })
        }
      },
    })
  }
})