package top.songm.service;

import top.songm.model.request.Product;
import top.songm.model.response.ProductInfo;
import top.songm.model.response.ProductRow;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 0:28
 */
public interface ProductService {

    List<ProductRow> findAllByPage(int position, int pageSize);

    ProductInfo findById(int id);

    List<Product> findProductListByManagerWithPage(int position, int pageSize, HttpServletRequest request);

    void remove(int[] ids);

    ProductInfo findInfoById(int id);

    void addOrEditProductInfo(ProductInfo productInfo, HttpServletRequest request);
}
