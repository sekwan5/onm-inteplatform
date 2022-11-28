package com.sk.signet.onm.common.grid;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class GridPagerInterceptor implements Interceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        // MetaObject metaStatementHandler = MetaObject.forObject(statementHandler,
        // DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
                DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);

        String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        RowBounds rb = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
        // rowbounds 유무 확인 ( 없으면 그냥 실행 )
        if (rb == null || rb == rb.DEFAULT) {
            return invocation.proceed();
        }

        StringBuffer sb = new StringBuffer();
        // sb.append("SELECT one.* FROM ( \n");
        // sb.append(originalSql + ") one\n");
        sb.append(originalSql + " \n");
        sb.append("LIMIT ").append(rb.getLimit()).append(" OFFSET ").append(rb.getOffset());

        // RowBounds 정보제거
        metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
        metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
        // 변경된 쿼리로 전송
        metaStatementHandler.setValue("delegate.boundSql.sql", sb.toString());

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties arg0) {
    }
}