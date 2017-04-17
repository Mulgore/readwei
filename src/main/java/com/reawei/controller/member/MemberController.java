package com.reawei.controller.member;

import com.baomidou.kisso.annotation.Permission;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.reawei.common.utils.StringReplaceUtil;
import com.reawei.controller.sys.BaseController;
import com.reawei.entity.RwMember;
import com.reawei.service.IRwMemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author xingwu
 * @since 2017-03-19
 */
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {

    @Resource
    private IRwMemberService memberService;

    /**
     * 会员列表页面
     *
     * @return
     */
    @Permission("7001")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listView() {
        return "member/list";
    }

    /**
     * 会员列表
     *
     * @return
     */
    @Permission("7001")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public String getList() {
        Page<RwMember> page = getPage();
        EntityWrapper<RwMember> wrapper = new EntityWrapper<RwMember>();
        RwMember member = new RwMember();
        wrapper.setEntity(member);
        page = memberService.selectPage(page, wrapper);
        for (RwMember mem : page.getRecords()) {
            mem.setIdCard(StringReplaceUtil.idCardReplaceWithStar(mem.getIdCard()));
        }
        return jsonPage(page);
    }

}
