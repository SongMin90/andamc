package top.songm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.songm.BaseLogger;
import top.songm.model.response.Msg;
import top.songm.model.response.ProductInfo;
import top.songm.model.response.ProductRow;
import top.songm.service.ProductService;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 0:23
 */
@RestController
@RequestMapping("/product")
public class ProductController extends BaseLogger<ProductController> {

    @Autowired
    private ProductService productService;

    @GetMapping("/findAllByPage")
    public Msg findAllByPage(Msg msg, @RequestParam(value = "position") int position, @RequestParam(value = "pageSize") int pageSize) {
        List<ProductRow> list = productService.findAllByPage(position, pageSize);
        msg.setData(list);
        return msg;
    }

    @GetMapping("/findById")
    public Msg findById(Msg msg, @RequestParam(value = "id") int id) {
        ProductInfo productInfo = productService.findById(id);
        msg.setData(productInfo);
        return msg;
    }

}
