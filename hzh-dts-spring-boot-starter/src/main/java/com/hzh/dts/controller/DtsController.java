package com.hzh.dts.controller;


import com.hzh.dts.config.SyncConfig;
import com.hzh.dts.core.DTSDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: hzh
 * @Date: 2023/2/3 15:44
 */
@RestController
@RequestMapping("dts")
public class DtsController {
    @Autowired
    private DTSDispatcher dtsDispatcher;

    @PostMapping("sync")
    public void sync(@RequestBody SyncConfig syncConfig) {
        dtsDispatcher.sync(syncConfig);
    }

}
