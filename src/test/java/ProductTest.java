import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.songm.model.response.ProductInfo;
import top.songm.model.response.ProductRow;
import top.songm.service.ProductService;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/12 16:43
 */
public class ProductTest extends TmallApplicationTests {

    @Autowired
    private ProductService productService;

    @Test
    public void findAllByPage() {
        List<ProductRow> list = productService.findAllByPage(0, 10);
        System.out.println(123);
    }

    @Test
    public void findById() {
        ProductInfo productInfo = productService.findById(1);
        System.out.println(123);
    }
}
