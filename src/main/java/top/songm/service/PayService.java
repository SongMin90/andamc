package top.songm.service;

import top.songm.model.request.PayData;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author songm
 * @datetime 2019/3/30 17:09
 */
public interface PayService {

    Map<String, String> getRequestPaymentData(HttpServletRequest request, List<PayData> payDataList);

    Map<String, String> getRequestPaymentData2(HttpServletRequest request, int id);

    void callback(HttpServletRequest request);
}
