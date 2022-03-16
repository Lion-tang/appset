package org.hust.app.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.hust.app.entity.VO.TxRecordVO;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
@Date 2021/11/26
@Description 查找主链的链下信息
@author zltang
**/

@Repository
public interface TxRecordMapper extends BaseMapper<TxRecordVO> {
    @DS("slave_0")
    @Select("select uid, attr ,block_time_stamp time from g1_recordcrud_insert_method0 where uid = '${uid}'")
     List<TxRecordVO> selectRecordVO0(String uid);


    @DS("slave_1")
    @Select("select uid, attr,block_time_stamp time from g1_recordcrud_insert_method1 where uid = '${uid}'")
     List<TxRecordVO> selectRecordVO1(String uid);

}
