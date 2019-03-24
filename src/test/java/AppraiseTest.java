import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.songm.mapper.ProductMapper;
import top.songm.model.request.Manager;
import top.songm.model.request.Product;
import top.songm.model.response.AppraiseRow;
import top.songm.model.response.ProductInfo;
import top.songm.service.AppraiseService;
import top.songm.service.ManagerService;
import top.songm.service.ProductService;
import top.songm.utils.AESUtil;
import top.songm.utils.Constant;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/12 16:43
 */
public class AppraiseTest extends TmallApplicationTests {

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void findByProductId() {
//        List<AppraiseRow> list = appraiseService.findByProductIdWithPage(1, 0 , 20);
//        ProductInfo product = productMapper.findById(3);
//        Manager manager = new Manager();
//        manager.setPhone("13037232106");
//        manager.setPassword("songm123");
//        managerService.login(manager);
//        List<Product> list = productMapper.findByManagerAndStatusWithPage(0, 10, Integer.valueOf(AESUtil.decrypt(Constant.secretKey, "D8r0l9KmOQuKvFmqbrgkIw==")), 0);
//        ProductInfo productInfo = productService.findInfoById(1);
//        System.out.println(productInfo);
        Product product = new Product();
        product.setManagerId(1);
        product.setName("123");
        product.setMoney(10.01);
        product.setDiscountType(1);
        product.setDiscountDesc("testsetestset");
        product.setDiscount(9.9);
        product.setFirstPicUrl("safjsaifjsdajf");
        int i = productMapper.saveReturnId(product);
        System.out.println("完成");
    }

}
