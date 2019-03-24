package top.songm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.songm.model.request.Appraise;
import top.songm.model.response.AppraiseRow;
import top.songm.model.response.Msg;
import top.songm.service.AppraiseService;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 0:23
 */
@RestController
@RequestMapping("/appraise")
public class AppraiseController {

    @Autowired
    private AppraiseService appraiseService;

    @GetMapping("/findByProductIdWithPage")
    public Msg findByProductIdWithPage(Msg msg, @RequestParam(value = "productId") int productId, @RequestParam(value = "position") int position, @RequestParam(value = "pageSize") int pageSize) {
        List<AppraiseRow> list = appraiseService.findByProductIdWithPage(productId, position, pageSize);
        msg.setData(list);
        return msg;
    }

    @PostMapping("/add")
    public Msg add(Msg msg, @RequestBody Appraise appraise) {
        appraiseService.add(appraise);
        return msg;
    }
}
