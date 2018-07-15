package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.vo.OrderProductVo;
import com.mmall.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * 描述:
 * OrderController
 *
 * @outhor Leo
 * @create 2018-07-09 下午 11:26
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    public static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping(value = "create_order.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<OrderVo> createOrder(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iOrderService.createOrder(user.getId(), shippingId);
    }

    @RequestMapping(value = "cancel_order.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> cancelOrder(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iOrderService.cancelOrder(user.getId(), orderNo);
    }

    @RequestMapping(value = "get_order_cart_product.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<OrderProductVo> getOrderCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping(value = "get_order_detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<OrderVo> getOrderDetail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iOrderService.getOrderDetail(user.getId(), orderNo);
    }

    @RequestMapping(value = "get_order_list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> getOrderList(HttpSession session,
                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }

    @RequestMapping(value = "pay.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse pay(HttpSession session, HttpServletRequest request, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(user.getId(), orderNo, path);
    }

    @RequestMapping(value = "alipay_callback.do", method = RequestMethod.POST)
    @ResponseBody
    public Object alipayCallBack(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for(Iterator iter = requestParams.keySet().iterator();iter.hasNext();) {
            String name = (String) iter.next();
            String valueStr = "";
            String[] values = (String[]) requestParams.get(name);
            for(int i = 0; i< values.length;i++) {
                valueStr = (i == values.length -1) ? valueStr + values[i] : valueStr + values[i]  + ",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"),params.get("trade_status"),params.toString());
        //非常重要，验证回调的正确性，是不是支付宝发的，并且要避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());

            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
        }

        //todo 验证各种数据
        ServerResponse serverResponse = iOrderService.aliCallBack(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    @RequestMapping(value = "query_order_pay_status.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if(serverResponse.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }

}