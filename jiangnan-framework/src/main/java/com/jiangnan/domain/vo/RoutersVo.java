package com.jiangnan.domain.vo;

import com.jiangnan.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutersVo {

//    private List<MenuVo> menu;
    private List<Menu> menus;

}
