/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.zhengcaiyun.idata.develop.dal.dao;

import cn.zhengcaiyun.idata.develop.dal.JsonColumnHandler;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dto.label.SpecialAttributeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author shiyin
 * @date 2021-06-17 21:11
 */

@Mapper
public interface DevLabelDefineMyDao {

    @Result(column = "label_attributes", property = "labelAttributes", javaType = List.class, typeHandler = JsonColumnHandler.class)
    @Result(column = "special_attribute", property = "specialAttribute", javaType = SpecialAttributeDto.class, typeHandler = JsonColumnHandler.class)
    @Result(column = "enum_attributes", property = "enumAttributes", javaType = List.class, typeHandler = JsonColumnHandler.class)
    @Select("<script>" +
            "select * " +
            "from dev_label_define " +
            "where dev_label_define.del != 1 " +
            "and (dev_label_define.label_attributes like concat('%', #{enumCode}, '%') " +
            "or dev_label_define.label_param_type = #{enumCode})" +
            "</script>")
    List<DevLabelDefine> selectLabelDefineByEnumCode(String enumCode);

    @Result(column = "label_attributes", property = "labelAttributes", javaType = List.class, typeHandler = JsonColumnHandler.class)
    @Result(column = "special_attribute", property = "specialAttribute", javaType = SpecialAttributeDto.class, typeHandler = JsonColumnHandler.class)
    @Result(column = "enum_attributes", property = "enumAttributes", javaType = List.class, typeHandler = JsonColumnHandler.class)
    @Select("<script>" +
            "SELECT * " +
            "FROM dev_label_define " +
            "WHERE dev_label_define.del != 1 AND dev_label_define.label_tag NOT LIKE '%_DISABLE' " +
                "AND FIND_IN_SET(dev_label_define.label_code, #{labelCodes}) " +
            "ORDER BY dev_label_define.label_index" +
            "</script>")
    List<DevLabelDefine> selectLabelDefinesByLabelCodes(String labelCodes);
}
