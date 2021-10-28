package com.fisco.app.controller;

import com.fisco.app.client.CRUDClient;
import com.fisco.app.entity.Goods;
import com.fisco.app.entity.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/query/{uid}")
    public ResponseData query(@PathVariable("uid") String uid) throws Exception {

        return ResponseData.success(crudClient.query(uid,"TestCRUD"));
    }


    @PostMapping("/insert")
    public ResponseData insert(@RequestBody Goods goods)  {
        crudClient.insert(goods.getUid(), goods.getHash(), goods.getDesc(),"TestCRUD");
        return ResponseData.success("新增成功");
    }


    @PutMapping("/update")
    public ResponseData update(@RequestBody Goods goods){
        crudClient.edit(goods.getUid(), goods.getHash(), goods.getDesc(),"TestCRUD");
        return ResponseData.success("修改成功");
    }

    @DeleteMapping("/remove/{uid}")
    public ResponseData remove(@PathVariable("uid") String uid){
        crudClient.remove(uid,"TestCRUD");
        return ResponseData.success("删除成功");
    }
}
