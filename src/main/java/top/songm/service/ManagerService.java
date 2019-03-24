package top.songm.service;

import top.songm.model.request.Manager;

import javax.servlet.http.HttpServletRequest;

/**
 * @author songm
 * @datetime 2019/2/17 10:21
 */
public interface ManagerService {

    void login(Manager manager, HttpServletRequest request);
}
