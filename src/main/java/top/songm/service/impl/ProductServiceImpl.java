package top.songm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.songm.mapper.AppraiseMapper;
import top.songm.mapper.ImageMapper;
import top.songm.mapper.ProductMapper;
import top.songm.model.request.Image;
import top.songm.model.request.Product;
import top.songm.model.response.ProductInfo;
import top.songm.model.response.ProductRow;
import top.songm.service.ProductService;
import top.songm.utils.AESUtil;
import top.songm.utils.Constant;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 0:28
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AppraiseMapper appraiseMapper;

    @Autowired
    private ImageMapper imageMapper;

    @Override
    public List<ProductRow> findAllByPage(int position, int pageSize) {
        List<ProductRow> pList = new ArrayList();
        List<ProductRow> list = productMapper.findAllByPage(position, pageSize);
        // 便利设置好评率
        for (ProductRow pr : list) {
            int appraiseCount = pr.getAppraiseCount();
            // 总评价数
            int count = appraiseMapper.countByProductId(pr.getId());
            if (count <= 0) {
                pr.setAppraiseCount(100);
            } else {
                appraiseCount = (int) (Double.valueOf(appraiseCount) / Double.valueOf(count) * 100.0D);
                pr.setAppraiseCount(appraiseCount);
            }
            pList.add(pr);
        }
        return pList;
    }

    @Override
    public ProductInfo findById(int id) {
        ProductInfo pr = productMapper.findById(id);

        // 设置好评率
        int appraiseCount = pr.getAppraiseCount();
        int count = appraiseMapper.countByProductId(pr.getId());
        if (count <= 0) {
            pr.setAppraiseCount(100);
        } else {
            appraiseCount = (int) (Double.valueOf(appraiseCount) / Double.valueOf(count) * 100.0D);
            pr.setAppraiseCount(appraiseCount);
        }

        // 设置评价总数
        pr.setAppraiseAllCount(count);

        // 设置首图
        List<String> imageList = new ArrayList();
        imageList.add(pr.getFirstPicUrl());

        // 设置其他图片
        List<Image> images = imageMapper.findByDataId(pr.getId(), 0);
        for (Image image : images) {
            imageList.add(image.getUrl());
        }
        pr.setImageList(imageList);

        return pr;
    }

    @Override
    public List<Product> findProductListByManagerWithPage(int position, int pageSize, HttpServletRequest request) {
        String token = request.getSession().getAttribute("token").toString();
        List<Product> list = productMapper.findByManagerAndStatusWithPage(position, pageSize, Integer.valueOf(AESUtil.decrypt(Constant.secretKey, token)));
        return list;
    }

    @Override
    public void remove(int[] ids) {
        for (int id : ids) {
            // 删除产品
            productMapper.deleteById(id);
            // 移除产品图片
            imageMapper.removeByDataIdAndType(id, 0);
        }
    }

    @Override
    public ProductInfo findInfoById(int id) {
        ProductInfo pr = productMapper.getById(id);
        // 设置其他图片
        List<String> imageList = new ArrayList();
        List<Image> images = imageMapper.findByDataId(pr.getId(), 0);
        for (Image image : images) {
            imageList.add(image.getUrl());
        }
        pr.setImageList(imageList);
        return pr;
    }

    @Override
    public void addOrEditProductInfo(ProductInfo productInfo, HttpServletRequest request) {
        String token = request.getSession().getAttribute("token").toString();
        int managerId = Integer.valueOf(AESUtil.decrypt(Constant.secretKey, token));
        int productId = productInfo.getId();
        if (productId == 0) {
            // 添加
            Product product = new Product();
            product.setManagerId(managerId);
            product.setName(productInfo.getName());
            product.setMoney(productInfo.getMoney());
            product.setDiscountType(productInfo.getDiscountType());
            product.setDiscountDesc(productInfo.getDiscountDesc());
            product.setDiscount(productInfo.getDiscount());
            product.setFirstPicUrl(productInfo.getFirstPicUrl());
            product.setRemoveStatus(productInfo.getRemoveStatus());
            product.setDiscountTemp(productInfo.getDiscountTemp());
            productId = productMapper.saveReturnId(product);
        } else {
            // 编辑
            Product product = new Product();
            product.setId(productInfo.getId());
            product.setName(productInfo.getName());
            product.setMoney(productInfo.getMoney());
            product.setDiscountType(productInfo.getDiscountType());
            product.setDiscountDesc(productInfo.getDiscountDesc());
            product.setDiscount(productInfo.getDiscount());
            product.setFirstPicUrl(productInfo.getFirstPicUrl());
            product.setRemoveStatus(productInfo.getRemoveStatus());
            product.setDiscountTemp(productInfo.getDiscountTemp());
            productMapper.updateById(product);
        }
        // 移除原图片
        imageMapper.removeByDataIdAndType(productId, 0);
        // 储存图片
        String[] imgs = request.getParameterValues("imgs");
        if (imgs != null && imgs.length > 0) {
            for (String imgUrl : imgs) {
                Image image = new Image();
                image.setDataId(productId);
                image.setUrl(imgUrl);
                image.setType(0);
                imageMapper.save(image);
            }
        }
    }
}
