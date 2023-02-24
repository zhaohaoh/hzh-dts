package com.hzh.dts.core.strategy.mysql;

import com.hzh.dts.config.SyncConfig;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class IdSyncStrategy extends MysqlSyncAbstractStrategy {


    private void setWhere(long index, PlainSelect plainSelect) {
        Expression where = plainSelect.getWhere();
        GreaterThan greaterThan = new GreaterThan();
        greaterThan.setLeftExpression(new Column("id"));
        greaterThan.setRightExpression(new LongValue(index));
        if (where != null) {
            plainSelect.setWhere(new AndExpression(where, greaterThan));
        } else {
            plainSelect.setWhere(greaterThan);
        }
    }

    private void setOrderBy(PlainSelect plainSelect) {
        List<OrderByElement> orderByElements = new ArrayList<>();
        OrderByElement e = new OrderByElement();
        e.setAsc(true);
        e.setExpression(new Column("id"));
        orderByElements.add(e);
        plainSelect.setOrderByElements(orderByElements);
    }

    @Override
    protected String getSql(long index, SyncConfig syncConfig) {
        Select selectStatement = null;
        try {
            selectStatement = (Select) CCJSqlParserUtil.parse(syncConfig.getSql());
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
        //补充where条件中的id>条件
        setWhere(index, plainSelect);
        //根据id同步默认id升序
        setOrderBy(plainSelect);
        //转换sql
        return plainSelect + " limit " + syncConfig.getTargetMapping().getCommitBatch();
    }

    @Override
    protected long getNextIndex(long index, SyncConfig syncConfig, Collection<Map<String, Object>> collection) {
        Map[] array = collection.toArray(new Map[0]);
        Map map = array[array.length - 1];
        Object id = map.get("id");
        return Long.parseLong(id.toString());
    }
}
