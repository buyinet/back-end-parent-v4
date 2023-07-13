package com.kantboot.admin.business.ovo.controller;

import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.business.ovo.module.entity.BusOvoPost;
import com.kantboot.business.ovo.module.entity.BusOvoVip;
import com.kantboot.business.ovo.service.service.IBusOvoPostService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/adminManage/business/ovo/vip")
public class AdminManageBusinessOvoVipController
        extends BaseAdminController<BusOvoVip,Long> {



}
