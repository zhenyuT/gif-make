package cn.tianzy.controller;

import cn.tianzy.model.GifConfigModel;
import cn.tianzy.service.GifConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    GifConfigService gifConfigService;

    @RequestMapping({"/","/index2"})
    public String index2(Model model, @RequestParam(defaultValue = "") String pwd){
        try {
            model.addAttribute("hello", "hello world");

            List<GifConfigModel> list = gifConfigService.getGifConfigList();
            model.addAttribute("gifList", list);

            if(pwd.equals("123456")){
                model.addAttribute("couldDel", true);
            } else {
                model.addAttribute("couldDel", false);
            }

            return "index2";
        } catch (Exception e) {
            log.error("", e);
            return "error";
        }
    }

    @RequestMapping("gif_make")
    public String gif_make(Model model){
        return "gifMake";
    }

}
