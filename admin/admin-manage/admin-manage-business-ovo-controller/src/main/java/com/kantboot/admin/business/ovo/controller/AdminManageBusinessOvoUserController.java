package com.kantboot.admin.business.ovo.controller;

import com.kantboot.base.controller.BaseAdminController;
import com.kantboot.business.ovo.module.entity.BusOvoUser;
import com.kantboot.business.ovo.module.entity.BusOvoUserInAdmin;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/adminManage/business/ovo/user")
public class AdminManageBusinessOvoUserController
        extends BaseAdminController<BusOvoUserInAdmin,Long>
{
}
