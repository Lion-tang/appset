package org.hust.app.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.hust.app.entity.VO.TxDetailVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
@Date 2021/11/26
@Description 查询辅链的链下详细信息
@author zltang
**/

@Repository
public interface TxDetailMapper extends BaseMapper<TxDetailVO> {
    @DS("slave_0")
    @Select("select uid,attr from g2_detailcrud_insert_method0 where uid like '%${uid}%'")
    List<TxDetailVO> selectDetailVO0(String uid);

    @DS("slave_1")
    @Select("select uid,attr from g2_detailcrud_insert_method1 where uid like '%${uid}%'")
    List<TxDetailVO> selectDetailVO1(String uid);
}
