///*
// * Copyright (c) 2011-2022, baomidou (jobob@qq.com).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.hzh.dts.core.strategy;
//
//import cn.hutool.db.sql.SqlBuilder;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import net.sf.jsqlparser.JSQLParserException;
//import net.sf.jsqlparser.expression.Alias;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.Function;
//import net.sf.jsqlparser.expression.LongValue;
//import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
//import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
//import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
//import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
//import net.sf.jsqlparser.parser.CCJSqlParserUtil;
//import net.sf.jsqlparser.schema.Column;
//import net.sf.jsqlparser.statement.select.*;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//
///**
// * JsqlParser Count Optimize
// *
// * @author hubin
// * @since 2017-06-20
// * @deprecated 2022-05-31
// */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class JsqlParserCountOptimize {
//    private static final List<SelectItem> COUNT_SELECT_ITEM = Collections.singletonList(defaultCountSelectItem());
//
//    private boolean optimizeJoin = false;
//
//    /**
//     * 获取jsqlparser中count的SelectItem
//     */
//    private static SelectItem defaultCountSelectItem() {
//        Function function = new Function();
//        ExpressionList expressionList = new ExpressionList(Collections.singletonList(new LongValue(1)));
//        function.setName("COUNT");
//        function.setParameters(expressionList);
//        return new SelectExpressionItem(function);
//    }
//
//    public static void parser(String sql) throws JSQLParserException {
//        Select selectStatement = (Select) CCJSqlParserUtil.parse(sql);
//        PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
//        Distinct distinct = plainSelect.getDistinct();
//        GroupByElement groupBy = plainSelect.getGroupBy();
//        List<OrderByElement> orderBy = plainSelect.getOrderByElements();
//        Expression where = plainSelect.getWhere();
//        String s = where.toString();
//        System.out.println(s);
//        System.out.println(groupBy);
//        System.out.println(orderBy);
//        SelectBody selectBody = selectStatement.getSelectBody();
//
//        List<OrderByElement> orderByElements=new ArrayList<>();
//        OrderByElement e = new OrderByElement();
//        e.setAsc(true);
//        e.setExpression(new Column("id"));
//        orderByElements.add(e);
//        plainSelect.setOrderByElements(orderByElements);
//
//
//        GreaterThan greaterThan = new GreaterThan() ;
//        greaterThan.setLeftExpression( new Column( "id")) ;
//        greaterThan.setRightExpression( new LongValue( 10000)) ;
//        plainSelect.setWhere(new AndExpression(where,greaterThan));
//        System.out.println(plainSelect);
//    }
//
//    public static void main(String[] args) throws JSQLParserException {
//        parser("select * from abc where a= 1 and a in(1,2,3) order by id desc");
//    }
//}
