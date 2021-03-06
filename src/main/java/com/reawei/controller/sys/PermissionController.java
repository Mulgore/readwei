package com.reawei.controller.sys;


import com.baomidou.kisso.annotation.Permission;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.reawei.entity.RwPermission;
import com.reawei.service.IRwPermissionService;
import com.reawei.service.IRwRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <p>
 * 权限管理相关操作
 * </p>
 *
 * @Author hubin
 * @Date 2016-04-15
 */
@Controller
@RequestMapping("/perm/permission")
public class PermissionController extends BaseController {

    @Resource
    private IRwPermissionService permissionService;

    @Autowired
    private IRwRolePermissionService rolePermissionService;

    @Permission("2003")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        return "/permission/list";
    }

    @ResponseBody
    @Permission("2003")
    @RequestMapping(value = "/getPermissionList", method = RequestMethod.GET)
    public String getPermissionList() {
        Page<RwPermission> page = getPage();
        page.setOrderByField("pid");
        page.setAsc(true);
        return jsonPage(permissionService.selectPage(page, null));
    }

    @ResponseBody
    @Permission("2003")
    @RequestMapping(value = "/delete/{permId}", method = RequestMethod.POST)
    public String delete(@PathVariable Long permId) {
        boolean exist = rolePermissionService.existRolePermission(permId);
        if (exist) {
            return "false";
        }
        return booleanToString(permissionService.deleteById(permId));
    }

    /**
     * 添加系统菜单权限页面
     *
     * @return
     */
    @Permission("2003")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPermission() {
        return "/permission/save";
    }

    /**
     * 添加系统菜单权限实现
     *
     * @return
     */
    @Permission("2003")
    @RequestMapping(value = "/add/do", method = RequestMethod.POST)
    @ResponseBody
    public String savePermission(RwPermission permission) {
        boolean rlt = false;
        EntityWrapper<RwPermission> wrapper = new EntityWrapper<RwPermission>();
        RwPermission perm = new RwPermission();
        perm.setPid(0L);
        wrapper.setEntity(perm);
        Integer perTotal = permissionService.selectCount(wrapper);
        permission.setPid(0L);
        if (perTotal >= 1000) {
            permission.setPermCode(String.valueOf(perTotal + 1));
        }
        if (perTotal < 1000) {
            permission.setPermCode(perTotal + 1 + "0");
        }
        if (perTotal < 100) {
            permission.setPermCode(perTotal + 1 + "00");
        }
        if (perTotal < 10) {
            permission.setPermCode(perTotal + 1 + "000");
        }
        permission.setState(0);
        permission.setType(0);
        permission.setSort(perTotal + 1);
        permission.setId((perTotal + 1) * 1L);
        rlt = permissionService.insert(permission);
        if (!rlt) {
            callbackFail("权限添加失败！！！");
        }
        return callbackSuccess(rlt);
    }

    /**
     * 添加子菜单或权限实现
     *
     * @return
     */
    @Permission("2003")
    @RequestMapping(value = "/child/add", method = RequestMethod.GET)
    public String addChildPermission(Model model, Integer permId) {
        model.addAttribute("permInfo", permissionService.selectById(permId));
        return "/permission/child/save";
    }

    /**
     * 添加系统菜单权限实现
     *
     * @return
     */
    @Permission("2003")
    @RequestMapping(value = "/child/add/do", method = RequestMethod.POST)
    @ResponseBody
    public String saveChildPermission(RwPermission permission) {
        boolean rlt = false;
        EntityWrapper<RwPermission> wrapper = new EntityWrapper<RwPermission>();
        RwPermission perm = new RwPermission();
        wrapper.setEntity(perm);
        perm.setPermCode(permission.getPermCode());
        RwPermission check = permissionService.selectOne(wrapper);
        if (check != null) {
            return callbackFail("权限编码已存在！！！");
        }
        perm.setId(permission.getPid());
        Integer perTotal = permissionService.selectCount(wrapper) + 1;
        permission.setState(0);
        RwPermission per = permissionService.selectById((permission.getPid() * 10) + perTotal);
        if (per != null) {
            perTotal = perTotal + 1;
        }
        permission.setSort(perTotal);
        permission.setId(((permission.getPid() * 10) + perTotal) * 1L);
        rlt = permissionService.insert(permission);
        if (!rlt) {
            return callbackFail("权限添加失败！！！");
        }
        return callbackSuccess(rlt);
    }
}
