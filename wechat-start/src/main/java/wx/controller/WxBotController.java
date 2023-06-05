package wx.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wx.api.intf.WxApiService;
import wx.util.Result;

import javax.annotation.Resource;

@RequestMapping("/wx")
@RestController
public class WxBotController {

    @Resource
    WxApiService wxApiService;

    /**
     * 添加微信客服账号
     *
     * @return
     */
    @PostMapping("chat")
    public Result check(String userId, String question) {
        JSONObject res = wxApiService.aiChat(userId, question);
        return Result.success("success", res);
    }
}
