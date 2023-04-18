package com.kantboot.admin.system.controller;


import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.system.module.entity.SysDictI18n;
import com.kantboot.system.repository.SysDictI18nRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理-字典国际化 前端控制器
 * @author 方某方
 */
@CrossOrigin
@RestController
@RequestMapping("/adminManage/system/dictI18n")
public class AdminManageSysDictI18nController extends BaseAdminController<SysDictI18n,Long> {

}
