package com.kw.wzbike.service.serviceImpl;

import com.kw.wzbike.common.ServerResponse;
import com.kw.wzbike.pojo.Bike;
import com.kw.wzbike.service.IBikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: wzbike
 * @description:
 * @author: kw
 * @create: 2019/03/12 17:33
 */
@Service
public class BikeServiceImpl implements IBikeService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Bike bike) {
        mongoTemplate.insert(bike);
    }

    @Override
    public ServerResponse<List<GeoResult<Bike>>> findNear(double longitude, double latitude) {

        try {
            //查询附近
            NearQuery nearQuery = NearQuery.near(longitude, latitude);
            //查询的范围、距离和单位
            nearQuery.maxDistance(0.2, Metrics.KILOMETERS);
            GeoResults<Bike> geoResults = mongoTemplate.geoNear(nearQuery.query(new Query(Criteria.where("status").is(0)).limit(20)), Bike.class);
            //return ServerResponse.createBySuccess(mongoTemplate.findAll(Bike.class));
            return ServerResponse.createBySuccess(geoResults.getContent());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ServerResponse.createByErrorMessage("服务器异常");


    }
}
