package top.songm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.songm.BaseLogger;
import top.songm.model.request.Manager;
import top.songm.model.request.Product;
import top.songm.model.response.Msg;
import top.songm.model.response.ProductInfo;
import top.songm.service.ManagerService;
import top.songm.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/12 14:47
 */
@RestController
@RequestMapping("/manager")
public class ManagerController extends BaseLogger<ManagerController> {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ProductService productService;

    /**
     * 登录，设置token在session中
     * @param msg
     * @param manager
     * @return
     */
    @PostMapping("/login")
    public Msg login(Msg msg, @RequestBody Manager manager, HttpServletRequest request) {
        managerService.login(manager, request);
        return msg;
    }

    /**
     * 获取产品列表
     * @param msg
     * @param position
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/findProductListByManagerWithPage")
    public Msg findProductListByManagerWithPage(Msg msg, @RequestParam(value = "position") int position, @RequestParam(value = "pageSize") int pageSize, HttpServletRequest request) {
        List<Product> list = productService.findProductListByManagerWithPage(position, pageSize, request);
        msg.setData(list);
        return msg;
    }

    /**
     * 移除产品
     * @param msg
     * @param ids
     * @return
     */
    @PostMapping("/remove")
    public Msg login(Msg msg, @RequestBody int[] ids) {
        productService.remove(ids);
        return msg;
    }

    /**
     * 产品详情
     * @param msg
     * @param id
     * @return
     */
    @GetMapping("/productInfo/{id}")
    public Msg findInfoById(Msg msg, @PathVariable("id") int id) {
        ProductInfo productInfo = productService.findInfoById(id);
        msg.setData(productInfo);
        return msg;
    }

    /**
     * 添加或编辑产品信息
     * @param msg
     * @param productInfo
     * @param request
     * @return
     */
    @PostMapping("/addOrEditProductInfo")
    public Msg addOrEditProductInfo(Msg msg, ProductInfo productInfo, HttpServletRequest request) {
        productService.addOrEditProductInfo(productInfo, request);
        return msg;
    }

}
