package org.hust.app.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hust.app.entity.VO.TxRecordVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TxRecordMapper extends BaseMapper<TxRecordVO> {
    @DS("slave_0")
    @Select("select locate,uid,main_tx_hash txHash,block_time_stamp time from g1_RecordCRUD_insert_method0 where uid like '%${uid}%'")
     List<TxRecordVO> selectRecordVO0(String uid);
//    {
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.like("uid", uid);
//        return this.selectList(queryWrapper);
//
//    }

    @DS("slave_1")
    @Select("select locate,uid,main_tx_hash txHash,block_time_stamp time from g1_RecordCRUD_insert_method1 where uid like '%${uid}%'")
     List<TxRecordVO> selectRecordVO1(String uid);
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.like("uid",uid);
//        return this.selectList(queryWrapper);
//    }
}
