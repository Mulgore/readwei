package com.reawei.controller.address;

import com.baomidou.kisso.annotation.Permission;
import com.reawei.controller.sys.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 收货地址表 前端控制器
 * </p>
 *
 * @author xingwu
 * @since 2017-03-19
 */
@Controller
@RequestMapping("/address")
public class AddressController extends BaseController {

    @Permission("9001")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listView() {
        return "address/list";
    }

    @Permission("9001")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public String getList() {
        return jsonPage(null);
    }
}
