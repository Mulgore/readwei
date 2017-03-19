package com.readwei.controller.sys;

import com.baomidou.kisso.annotation.Permission;
import com.baomidou.mybatisplus.plugins.Page;
import com.readwei.entity.Role;
import com.readwei.entity.RolePermission;
import com.readwei.service.IPermissionService;
import com.readwei.service.IRolePermissionService;
import com.readwei.service.IRoleService;
import com.readwei.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 角色管理相关操作
 * </p>
 *
 * @Author hubin
 * @Date 2016-04-15
 */
@Controller
@RequestMapping("/perm/role")
public class RoleController extends BaseController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private IUserRoleService userRoleService;


    @Permission("2002")
    @RequestMapping("/list")
    public String list(Model model) {
        return "/role/list";
    }


    @ResponseBody
    @Permission("2002")
    @RequestMapping("/getRoleList")
    public String getUserList() {
        Page<Role> page = getPage();
        return jsonPage(roleService.selectPage(page, null));
    }


    @ResponseBody
    @Permission("2003")
    @RequestMapping("/delete/{roleId}")
    public String delete(@PathVariable Long roleId) {
        boolean exist = userRoleService.existRoleUser(roleId);
        if (exist) {
            return "false";
        }
        return booleanToString(roleService.deleteById(roleId));
    }


    @Permission("2002")
    @RequestMapping("/edit")
    public String edit(Model model, Long id) {
        if (id != null) {
            model.addAttribute("role", roleService.selectById(id));
        }
        return "/role/edit";
    }


    @ResponseBody
    @Permission("2002")
    @RequestMapping("/editRole")
    public String editRole(Role role) {
        boolean rlt = false;
        if (role != null) {
            if (role.getId() != null) {
                rlt = roleService.updateById(role);
            } else {
                rlt = roleService.insert(role);
            }
        }
        return callbackSuccess(rlt);
    }

    /**
     * 跳转到权限分配页面
     *
     * @param model
     * @param id
     * @return
     */
    @Permission("2003")
    @RequestMapping("/assigning")
    public String assigning(Model model, Long id) {
        model.addAttribute("roleId", id);
        return "/role/assigning";
    }

    /**
     * 获取权限列表
     *
     * @param roleId
     * @return
     */
    @Permission("2003")
    @RequestMapping("/right")
    @ResponseBody
    public String right(Long roleId) {
        Page<com.readwei.entity.Permission> page = getPage();
        Page<com.readwei.entity.Permission> list = permissionService.selectPage(page, null);
        for (com.readwei.entity.Permission perm : list.getRecords()) {
            boolean exist = rolePermissionService.existRolePermission(perm.getId(), roleId);
            perm.setEnable(exist);
        }
        page.setRecords(list.getRecords());
        page.setTotal(list.getTotal());
        return jsonPage(page);
    }

    /**
     * 添加权限
     *
     * @return
     */
    @Permission("2003")
    @RequestMapping("/add")
    @ResponseBody
    public String addRolePerm(Long roleId, Long permId) {
        RolePermission perm = new RolePermission();
        perm.setRid(roleId);
        perm.setPid(permId);
        boolean rlt = rolePermissionService.insert(perm);
        return callbackSuccess(rlt);
    }

    /**
     * 删除权限
     *
     * @return
     */
    @Permission("2003")
    @RequestMapping("/del")
    @ResponseBody
    public String delRolePerm(Long roleId, Long permId) {
        Map<String, Object> parm = new HashMap();
        parm.put("rid", roleId);
        parm.put("pid", permId);
        boolean rlt = rolePermissionService.deleteByMap(parm);
        return callbackSuccess(rlt);
    }

}