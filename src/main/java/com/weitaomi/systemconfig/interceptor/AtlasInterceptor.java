package com.weitaomi.systemconfig.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;

/**
 * atlas强制从主库读取数据拦截器
 * Created by yuguodong on 2016/7/6.
 */
@Intercepts(@Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }))
public class AtlasInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 执行拦截逻辑
        if (invocation.getTarget() instanceof StatementHandler) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
            // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
            // 可以分离出最原始的的目标类)
            while (metaStatementHandler.hasGetter("h")) {
                Object object = metaStatementHandler.getValue("h");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            // 分离最后一个代理对象的目标类
            while (metaStatementHandler.hasGetter("target")) {
                Object object = metaStatementHandler.getValue("target");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            // 获取要拦截的对象方法名
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
            String targetMethodUrl=mappedStatement.getId();
            String targetMethodName=targetMethodUrl.substring(targetMethodUrl.lastIndexOf(".")+1);
            //正则匹配方法名中是否含有tomaster字符串，不区分大小写
            if(targetMethodName.matches(".*[tT][oO][mM][aA][sS][tT][eE][rR].*")){
                // 根据boundsql获取要拦截的sql
                BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
                String sql = boundSql.getSql();
                // 重写sql
                String atlasSql = "/*master*/"+sql;
                metaStatementHandler.setValue("delegate.boundSql.sql", atlasSql);
            }
        }

        // 将执行权交给下一个拦截器
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // Do nothing 
    }
}