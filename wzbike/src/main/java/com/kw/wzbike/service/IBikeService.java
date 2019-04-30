package com.kw.wzbike.service;

import com.kw.wzbike.common.ServerResponse;
import com.kw.wzbike.pojo.Bike;
import org.springframework.data.geo.GeoResult;

import java.util.List;

public interface IBikeService {
    public void save(Bike bike);

    ServerResponse<List<GeoResult<Bike>>> findNear(double longitude, double latitude);
}
