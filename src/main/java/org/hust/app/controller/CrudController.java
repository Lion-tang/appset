package org.hust.app.controller;

import org.hust.app.client.CRUDClient;
import org.hust.app.entity.Car;
import org.hust.app.entity.Goods;
import org.hust.app.entity.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/**
 * @Classname CrudController
 * @Description 通过接口调用sdk
 * @Date 2021/3/25 22:25
 * @Created by zyt
 */
@RestController
public class CrudController {

    @Autowired
    private CRUDClient crudClient;



    @PostMapping("/insert")
    public ResponseData insert(@RequestBody Goods goods)  {
        Random random = new Random();
        crudClient.insertRecord(random.nextInt()+"", goods.getUid(),goods.getAttr());
        return ResponseData.success("新增成功");
    }


    @PostMapping("/insertDetail")
    public ResponseData insertDetail(@RequestBody Goods goods){
        crudClient.insertDetail(goods.getUid(), goods.getAttr());
        return ResponseData.success("修改成功");
    }


}
