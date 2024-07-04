package com.jiangnan.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "新增标签的请求参数dto") //这个是关于swagger的注解
public class TabListDto {

    private String name;

    private String remark;

}
