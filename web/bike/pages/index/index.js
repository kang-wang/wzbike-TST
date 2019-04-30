
var myutil = require("../../utils/myutil.js")
Page({
  data: {
    log: 0,
    lat: 0,
    controls:[],
    markers:[]
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {

    var that = this;

    //获得设备位置
    wx.getLocation({
      success: function(res) {
        //console.log(res)
        var longitude = res.longitude;
        var latitude = res.latitude;
        that.setData({
          log: longitude,
          lat: latitude,
        })
        findBikes(longitude, latitude, that);
      },
    })

    //获取设备信息
    wx.getSystemInfo({
      success: function(res) {
        //console.log(res);
        var windowWidth = res.windowWidth;
        var windowHeight = res.windowHeight;
        that.setData({
          controls: [

            //扫码
            {
              id: 1,
              //控件(按钮)的背景图片
              iconPath: '/images/scan.png',
              //控件的相对页面的位置
              position: {
                width: 100,
                height: 100,
                left: windowWidth / 2 - 50,
                top: windowHeight - 120
              },
              //是否可点击
              clickable:true
            },

            //回到原点
            {
              id: 2,
              //控件(按钮)的背景图片
              iconPath: '/images/central.png',
              //控件的相对页面的位置
              position: {
                width: 26,
                height: 26,
                left: 10,
                top: windowHeight - 60
              },
              //是否可点击
              clickable: true
            },

            //报修
            {
              id: 3,
              //控件(按钮)的背景图片
              iconPath: '/images/repair.png',
              //控件的相对页面的位置
              position: {
                width: 26,
                height: 26,
                left: windowWidth - 40,
                top: windowHeight - 95
              },
              //是否可点击
              clickable: true
            },

            //充值
            {
              id: 4,
              //控件(按钮)的背景图片
              iconPath: '/images/recharge.png',
              //控件的相对页面的位置
              position: {
                width: 26,
                height: 26,
                left: windowWidth - 40,
                top: windowHeight - 60
              },
              //是否可点击
              clickable: true
            },

            //所在位置
            {
              id: 5,
              //控件(按钮)的背景图片
              iconPath: '/images/location.png',
              //控件的相对页面的位置
              position: {
                width: 26,
                height: 26,
                left: windowWidth / 2 - 12,
                top: windowHeight / 2 - 12
              },
              //是否可点击
              clickable: true
            },

            //添加车辆
            {
              id: 6,
              //控件(按钮)的背景图片
              iconPath: '/images/add.png',
              //控件的相对页面的位置
              position: {
                width: 26,
                height: 26,
                left: 10,
                top: 5
              },
              //是否可点击
              clickable: true
            },
          ]
        })

      },
    })
  },

  /**
   * 控件被点击事件
   */
  controltap:function(e) {
    var that = this;
    var cid = e.controlId;
    console.log(cid);
    switch(cid) {

      //扫码按钮
      case 1: {

        var status = myutil.getStatus("status");
        if(status == 0) {
          wx.navigateTo({
            url: '../register/register',
          })
        } else if (status == 1) {
          wx.navigateTo({
            url: '../deposit/deposit',
          })
        } else if (status == 2) {
          wx.navigateTo({
            url: '../identity/identity',
          })
        }

        break;
      }

      //定位按钮
      case 2: {
        this.mapCtx.moveToLocation();
        break;
      }
      //添加车辆
      case 6: {
        //获取当前已有的车辆
        var bicycle = that.data.markers;
        //获取移动后的中心位置
        this.mapCtx.getCenterLocation({
          success: function(res) {
            var log = res.longitude;
            var lat = res.latitude;
            //在移动后添加一辆车
            // bicycle.push(
            //   {
            //     iconPath: "/images/bicycle.png",
            //     width: 35,
            //     height: 40,
            //     longitude:log,
            //     latitude:lat,
            //   }
            // );
            // //重新赋值
            // that.setData({
            //   markers:bicycle
            // });
            wx.request({
              url: 'http://localhost:8080/bike/addBike',
              data: {
                //longitude: log,
                //latitude: lat,
                location: [log, lat],
                status: 0,
              },
              method: 'POST',
              success: function(res) {
                findBikes(log, lat, that);
              }
            })
          },
        });
        break;
      }
    }
  },

  /**
   * 移动后地图视野发生变化触发的事件
   */
  regionchange:function(e) {
    var that = this;
    //获取移动后的位置
    var etype = e.type;
    if(e.type == 'end') {
      this.mapCtx.getCenterLocation({
        success: function(res) {
          var log = res.longitude;
          var lat = res.latitude;
          findBikes(log, lat, that);
        }
      })
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady:function() {
    //创建map上下文
    this.mapCtx = wx.createMapContext("myMap");
  }

})


/**
 * 查询附近单车
 */
function findBikes(longitude, latitude, that) {
  wx.request({
    url: 'http://localhost:8080/bike/findNear',
    method: 'GET',
    data: {
      longitude: longitude,
      latitude: latitude,
    },
    success: function (res) {
      var bikes = res.data.data.map((geoResult) => {
        return {
          longitude: geoResult.content.location[0],
          latitude: geoResult.content.location[1],
          bikeId: geoResult.content.bikeId,
          iconPath: "/images/bicycle.png",
          width: 35,
          height: 40,
        }
      })
      console.log("附近有"+bikes.length+"辆单车");
      that.setData({
        markers: bikes,
      })
    }
  })
}