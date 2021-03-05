package com.douban.spider.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MovieMapper {
    @Insert("insert into movie(name, rate) values(#{name},#{rate})")
    void add(@Param("name") String name, @Param("rate") String rate);
}
