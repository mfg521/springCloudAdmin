package com.mfg.auth.gate.v2.filter;

import com.alibaba.fastjson.JSONObject;
import com.mfg.api.vo.authority.PermissionInfo;
import com.mfg.api.vo.log.LogInfo;
import com.mfg.auth.client.config.ServiceAuthConfig;
import com.mfg.auth.client.config.UserAuthConfig;
import com.mfg.auth.client.jwt.ServiceAuthUtil;
import com.mfg.auth.client.jwt.UserAuthUtil;
import com.mfg.auth.common.context.BaseContextHandler;
import com.mfg.auth.common.msg.BaseResponse;
import com.mfg.auth.common.msg.auth.TokenForbiddenResponse;
import com.mfg.auth.common.util.jwt.IJWTInfo;
import com.mfg.auth.gate.v2.feign.ILogService;
import com.mfg.auth.gate.v2.feign.IUserService;
import com.mfg.auth.gate.v2.utils.DBLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mengfanguang
 * @date 2018/10/11
 */
@Configuration
@Slf4j
public class AccessGatewayFilter implements GlobalFilter {


    @Autowired
    @Lazy  //懒加载，在使用到这个类的时候，spring容器才会加载
    private IUserService userService;

    @Autowired
    @Lazy
    private ILogService logService;

    @Value("${gate.ignore.startWith}")  //对应application.yml 里面的 gate ignore.startWith
    private String startWith;

    private  static final String GATE_WAY_PREFIX="/api";

    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private ServiceAuthUtil serviceAuthUtil;


    /**
     *
     * @param serverWebExchange
     * @param gatewayFilterChain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        log.info("check token and user permission");
        LinkedHashSet requiredAttribute=serverWebExchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        ServerHttpRequest serverHttpRequest=serverWebExchange.getRequest();
        String requestUri=serverHttpRequest.getPath().pathWithinApplication().value();
        System.out.println(requestUri.toString());
        if(requiredAttribute!=null){
            Iterator<URI> iterator=requiredAttribute.iterator();
            while(iterator.hasNext()){
                URI next=iterator.next();
                if(next.getPath().startsWith(GATE_WAY_PREFIX)){
                    requestUri=next.getPath().substring(GATE_WAY_PREFIX.length());
                }
            }
        }
        final String method=serverHttpRequest.getMethod().toString();
        System.out.println(method);
        BaseContextHandler.setToken(null);
        ServerHttpRequest.Builder mutate=serverHttpRequest.mutate();

        //不进行拦截的地址
        if(isStartWith(requestUri)){
            ServerHttpRequest bulid=mutate.build();
            return gatewayFilterChain.filter(serverWebExchange.mutate().request(bulid).build());
        }

        IJWTInfo user=null;
        try{
            user=getJWTUser(serverHttpRequest,mutate);

        }catch (Exception e){
            log.error("用户Token过期异常",e);
            return getVoidMono(serverWebExchange,new TokenForbiddenResponse("User Token Forbidden or Expired!"));
        }

        List<PermissionInfo> permissionInfos =userService.getAllPermissionInfo();

        //判断资源是否启用权限约束
        Stream<PermissionInfo> permissionInfoStream =getpermissionInfos(requestUri,method,permissionInfos);
        //判断完成后再将Stream转换成List,然后再将List转成array
        List<PermissionInfo> permissionInfoList=permissionInfoStream.collect(Collectors.toList());
        PermissionInfo[] permissionInfoArray=permissionInfoList.toArray(new PermissionInfo[]{});

        //如果用户启动了权限限制，在这里对用用户的权限进行check
        if(permissionInfoArray.length>0){
            if(checkUserPermission(permissionInfoArray,serverWebExchange,user)){
                return getVoidMono(serverWebExchange, new TokenForbiddenResponse("User Forbidden!Does not has Permission!"));
            }
        }

        //申请客户端(client)密钥token,  在getJWTUser()方法中也有这个操作,不过获取的是userToken
        mutate.header(serviceAuthConfig.getTokenHeader(), serviceAuthUtil.getClientToken());
        ServerHttpRequest build = mutate.build();
        return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());

    }



    /**
     * 返回session中的用户信息。从session中的token解析，而userPublicKey在程序启动的时候就已经完成
     *
     * @param request
     * @param ctx
     * @return
     */
    private IJWTInfo getJWTUser(ServerHttpRequest request, ServerHttpRequest.Builder ctx) throws Exception {
        List<String> stringList=request.getHeaders().get(userAuthConfig.getTokenHeader()); //userAuthConfig.getTokenHeader()  Authorization
        String authToken=null;
        if(stringList!=null){
            authToken=stringList.get(0);
        }else{
            authToken=request.getCookies().get("Admin-Token").toString().split("Admin-Token=")[1].split("]")[0];
        }
        if(StringUtils.isNotBlank(authToken)){
            stringList=request.getQueryParams().get("token");
            if(stringList!=null){
                authToken=stringList.get(0);
            }
        }
        ctx.header(userAuthConfig.getTokenHeader(),authToken);
        BaseContextHandler.setToken(authToken);

        String userName=userAuthUtil.getInfoFromTokenCipher(authToken);
        return new IJWTInfo() {
            @Override
            public String getUniqueName() {
                return userName;
            }

            @Override
            public String getId() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }


    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri){
        boolean flag=false;
        for(String s:startWith.split(",")){
            if(requestUri.startsWith(s)){
                return true;
            }
        }
        return flag;
    }

    /**
     * 网关抛异常
     * @param serverWebExchange
     * @param baseResponse
     * @return
     */
    @NotNull
    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, BaseResponse baseResponse){
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        byte[] bytes=JSONObject.toJSONString(baseResponse).getBytes();
        DataBuffer buffer=serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }


    /**
     *
     * @param requestUri
     * @param method
     * @param permissionInfoList  所有权限列表，当调用parallelStream()方法后，会对其进行平行处理。没有顺讯
     *                            如果想要有顺序，可以使用parallelStreamOrdered。
     * @return
     */
    private Stream<PermissionInfo> getpermissionInfos(final String requestUri,final String method,List<PermissionInfo> permissionInfoList){
        //对 permissionInfoList 中的每一个permission进行过滤 当满足test时才会将其放入到Stream<permission>
        return permissionInfoList.parallelStream().filter(new Predicate<PermissionInfo>() {
            @Override
            public boolean test(PermissionInfo permissionInfo) {
                String uri=permissionInfo.getUri();
                //url 中 { 出现的位置 >0
                if(uri.indexOf("{")>0){
                    uri=uri.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
                }
                String regEx="^"+uri+"$";
                //requestUri是否满足regEx 规则 compile matcher find 是三种匹配规则
                Boolean ifMatchRegEx= Pattern.compile(regEx).matcher(requestUri).find();
                return (ifMatchRegEx) && method.equals(permissionInfo.getMethod());
            }
        });
    }


    /**
     *如果用户启用了权限约束，就会调用这个方法，来check用户的权限
     * @param permissions
     * @param ctx
     * @param user
     * @return
     */
    private boolean checkUserPermission(PermissionInfo[] permissions, ServerWebExchange ctx, IJWTInfo user) {
        List<PermissionInfo> permissionInfos = userService.getPermissionByUsername(user.getUniqueName());
        PermissionInfo current = null;
        for (PermissionInfo info : permissions) {
            boolean anyMatch = permissionInfos.parallelStream().anyMatch(new Predicate<PermissionInfo>() {
                @Override
                public boolean test(PermissionInfo permissionInfo) {
                    return permissionInfo.getCode().equals(info.getCode());
                }
            });
            if (anyMatch) {
                current = info;
                break;
            }
        }
        if (current == null) {
            return true;
        } else {
            if (!RequestMethod.GET.toString().equals(current.getMethod())) {
                setCurrentUserInfoAndLog(ctx, user, current);
            }
            return false;
        }
    }

    private void setCurrentUserInfoAndLog(ServerWebExchange serverWebExchange, IJWTInfo user, PermissionInfo pm) {
        String host = serverWebExchange.getRequest().getRemoteAddress().toString();
        LogInfo logInfo = new LogInfo(pm.getMenu(), pm.getName(), pm.getUri(), new Date(), user.getId(), user.getName(), host);
        DBLog.getInstance().setLogService(logService).offerQueue(logInfo);
    }


}
