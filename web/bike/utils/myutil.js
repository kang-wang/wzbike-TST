

function getStatus(key) {
  var status = wx.getStorageSync(key);
  //没有取到
  if (!status) {
    status = getApp().globalData[key];
  }
  return status;
}

function getPhoneNum(key) {
  var phoneNum = wx.getStorageSync(key);
  //没有取到
  if (!phoneNum) {
    phoneNum = getApp().globalData[key];
  }
  return phoneNum;
}

module.exports = {
  getStatus,
  getPhoneNum,
}