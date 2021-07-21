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

package cn.zhengcaiyun.idata.develop.dto.query;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-21 15:52
 **/
public class MeasureDataQueryDto {
    /**
     * 数仓层级为空
     */
    private String dbSchema;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 表别名
     */
    private String tableAlias;
    /**
     * 查询维度列
     */
    private List<DimColumnDto> dimensions;
    /**
     * 查询指标列
     */
    private List<MeasureColumnDto> measures;
    /**
     * 每页大小
     */
    private Integer pageSize;
    /**
     * 页数
     */
    private Integer pageNo;
    /**
     * 排序条件
     */
    private OrderByDto orderBy;
    /**
     * 过滤条件
     */
    private List<FilterDto> filters;

    /**
     * 是否聚合
     */
    private Boolean aggregate;

    public String getDbSchema() {
        return dbSchema;
    }

    public void setDbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public List<DimColumnDto> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<DimColumnDto> dimensions) {
        this.dimensions = dimensions;
    }

    public List<MeasureColumnDto> getMeasures() {
        return measures;
    }

    public void setMeasures(List<MeasureColumnDto> measures) {
        this.measures = measures;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public OrderByDto getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderByDto orderBy) {
        this.orderBy = orderBy;
    }

    public List<FilterDto> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDto> filters) {
        this.filters = filters;
    }

    public Boolean getAggregate() {
        return aggregate;
    }

    public void setAggregate(Boolean aggregate) {
        this.aggregate = aggregate;
    }
}
