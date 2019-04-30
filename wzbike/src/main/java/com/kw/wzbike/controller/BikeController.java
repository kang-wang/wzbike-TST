package com.kw.wzbike.controller;

import com.kw.wzbike.common.ServerResponse;
import com.kw.wzbike.service.IBikeService;
import com.kw.wzbike.pojo.Bike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: wzbike
 * @description: 用于接收请求和对应用户
 * 加上@Controller注解后，Spring容器就会对其实例化
 * @author: kw
 * @create: 2019/03/01 15:43
 */
@RestController
@RequestMapping(value = "/bike/")
public class BikeController {

    @Autowired
    private IBikeService iBikeService;

    @RequestMapping(value = "addBike")
    public String add(@RequestBody Bike bike){

        iBikeService.save(bike);
        System.out.println(bike+"laile ++++++++++++++++++++");
        return "hello world";
    }

    @RequestMapping(value = "findNear")
    public ServerResponse<List<GeoResult<Bike>>> findNear(double longitude, double latitude) {
        return iBikeService.findNear(longitude, latitude);
    }

}
