package com.jiangnan.controller;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    //查询所有友链
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink() {
        ResponseResult result = linkService.getAllLink();
        return result;
    }
}
