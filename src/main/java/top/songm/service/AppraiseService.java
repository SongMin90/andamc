package top.songm.service;

import top.songm.model.request.Appraise;
import top.songm.model.response.AppraiseRow;

import java.util.List;

/**
 * @author songm
 * @datetime 2019/2/13 17:34
 */
public interface AppraiseService {

    List<AppraiseRow> findByProductIdWithPage(int productId, int position, int pageSize);

    void add(Appraise appraise);
}
