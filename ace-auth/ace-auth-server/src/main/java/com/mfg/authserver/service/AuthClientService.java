package com.mfg.authserver.service;


import java.util.List;

/**
 * Created by ace on 2017/9/10.
 */
public interface AuthClientService {

    /**
     *  生成客户端token
     * @param clientId
     * @param secret
     * @return
     * @throws Exception
     */

    public String apply(String clientId, String secret) throws Exception;

    /**
     * 获取授权的客户端列表
     * @param serviceId
     * @param secret
     * @return
     */
    public List<String> getAllowedClient(String serviceId, String secret);

    /**
     * 获取服务授权的客户端列表
     * @param serviceId
     * @return
     */
    public List<String> getAllowedClient(String serviceId);

    public void registryClient();

    /**
     * 验证客户端
     * @param clientId
     * @param secret
     * @throws Exception
     */
    public void validate(String clientId, String secret) throws Exception;
}
