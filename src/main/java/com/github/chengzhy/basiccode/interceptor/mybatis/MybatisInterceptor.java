package com.github.chengzhy.basiccode.interceptor.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

/**
 * mybatis参数拦截器
 *
 * <p>{@link org.apache.ibatis.plugin.Intercepts @Intercepts} mybatis拦截器注解
 * <p>type设置拦截的类型，mybatis一共可以拦截四种类型
 * <ul>
 *     <li>Executor:拦截执行器的方法</li>
 *     <li>ParameterHandler:拦截参数的处理</li>
 *     <li>ResultHandler:拦截结果集的处理</li>
 *     <li>StatementHandler:拦截Sql语法构建的处理</li>
 * </ul>
 * method设置拦截type中的某方法
 * args设置拦截方法中的参数
 * @author chengzhy
 * @date 2021/8/16 15:13
 **/
@Intercepts({@Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = Statement.class)})
@ConditionalOnProperty(prefix = "mybatis.interceptor", value = "enable", havingValue = "true")
@Component
public class MybatisInterceptor implements Interceptor {

    /**
     * 代理对象每次调用的方法，就是要进行拦截的时候要执行的方法。在这个方法里面做自定义的逻辑处理
     *
     * @param invocation
     * @return 程序返回值
     * @throws Throwable 异常错误
     * @see Interceptor#intercept(Invocation)
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object handler = invocation.getTarget();
        // 拦截ParameterHandler的setParameters方法 动态设置参数
        if (handler instanceof Executor) {
            return executorIntercept(invocation);
        } else if (handler instanceof ParameterHandler) {
            return parameterHandlerIntercept(invocation);
        } else if (handler instanceof ResultSetHandler){
            return resultSetHandlerIntercept(invocation);
        } else {
            return statementHandlerIntercept(invocation);
        }
    }

    /**
     * plugin方法是拦截器用于封装目标对象的，通过该方法可以返回目标对象本身，也可以返回一个它的代理
     *
     * 当返回的是代理的时候可以对其中的方法进行拦截来调用intercept方法 -- Plugin.wrap(target, this)
     * 当返回的是当前对象的时候就不会调用intercept方法，相当于当前拦截器无效
     * @param o
     * @return
     */
    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    /**
     * 用于在mybatis配置文件中指定一些属性的，注册当前拦截器的时候可以设置一些属性
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {

    }

    private Object executorIntercept(Invocation invocation) throws Throwable {
        // TODO
        return invocation.proceed();
    }

    private Object parameterHandlerIntercept(Invocation invocation) throws Throwable {
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        // TODO
        return invocation.proceed();
    }

    private Object resultSetHandlerIntercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        // TODO
        return result;
    }

    private Object statementHandlerIntercept(Invocation invocation) throws Throwable {
        // TODO
        return invocation.proceed();
    }

    private void filterSameValue(Map<?, ?> map) {
        if (map == null || map.size() < 1) {
            return;
        }
        for (Map.Entry<?, ?> entry : map.entrySet()) {

        }
    }

}
