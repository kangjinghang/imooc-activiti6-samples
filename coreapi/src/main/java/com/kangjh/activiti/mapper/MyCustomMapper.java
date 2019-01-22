package com.kangjh.activiti.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author kangjinghang
 * @version 1.0 2019-01-22
 */
public interface MyCustomMapper {

    @Select("SELECT * FROM ACT_RU_TASK")
    List<Map<String, Object>> findAll();

}
