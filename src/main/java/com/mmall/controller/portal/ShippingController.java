package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 描述:
 * ShippingController
 *
 * @outhor Leo
 * @create 2018-07-08 下午 2:03
 */
@Controller
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping(value = "/add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> delete(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iShippingService.delete(user.getId(), shippingId);
    }

    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iShippingService.update(user.getId(), shipping);
    }

    @RequestMapping(value = "/select.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Shipping> select(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iShippingService.selectShippingByUserIdAndShippingId(user.getId(), shippingId);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iShippingService.selectShippingByUserId(user.getId(), pageNum, pageSize);
    }

}