package com.rlogin.domain.gjj;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GjjRepayPlanDetailExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    public GjjRepayPlanDetailExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("Id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("Id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("Id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("Id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("Id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("Id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("Id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("Id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("Id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("Id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("Id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("Id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andLoanIdIsNull() {
            addCriterion("loan_id is null");
            return (Criteria) this;
        }

        public Criteria andLoanIdIsNotNull() {
            addCriterion("loan_id is not null");
            return (Criteria) this;
        }

        public Criteria andLoanIdEqualTo(String value) {
            addCriterion("loan_id =", value, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanIdNotEqualTo(String value) {
            addCriterion("loan_id <>", value, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanIdGreaterThan(String value) {
            addCriterion("loan_id >", value, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanIdGreaterThanOrEqualTo(String value) {
            addCriterion("loan_id >=", value, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanIdLessThan(String value) {
            addCriterion("loan_id <", value, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanIdLessThanOrEqualTo(String value) {
            addCriterion("loan_id <=", value, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanIdLike(String value) {
            addCriterion("loan_id like", value, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanIdNotLike(String value) {
            addCriterion("loan_id not like", value, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanIdIn(List<String> values) {
            addCriterion("loan_id in", values, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanIdNotIn(List<String> values) {
            addCriterion("loan_id not in", values, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanIdBetween(String value1, String value2) {
            addCriterion("loan_id between", value1, value2, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanIdNotBetween(String value1, String value2) {
            addCriterion("loan_id not between", value1, value2, "loanId");
            return (Criteria) this;
        }

        public Criteria andLoanTermIsNull() {
            addCriterion("loan_term is null");
            return (Criteria) this;
        }

        public Criteria andLoanTermIsNotNull() {
            addCriterion("loan_term is not null");
            return (Criteria) this;
        }

        public Criteria andLoanTermEqualTo(Integer value) {
            addCriterion("loan_term =", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermNotEqualTo(Integer value) {
            addCriterion("loan_term <>", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermGreaterThan(Integer value) {
            addCriterion("loan_term >", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermGreaterThanOrEqualTo(Integer value) {
            addCriterion("loan_term >=", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermLessThan(Integer value) {
            addCriterion("loan_term <", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermLessThanOrEqualTo(Integer value) {
            addCriterion("loan_term <=", value, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermIn(List<Integer> values) {
            addCriterion("loan_term in", values, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermNotIn(List<Integer> values) {
            addCriterion("loan_term not in", values, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermBetween(Integer value1, Integer value2) {
            addCriterion("loan_term between", value1, value2, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanTermNotBetween(Integer value1, Integer value2) {
            addCriterion("loan_term not between", value1, value2, "loanTerm");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalIsNull() {
            addCriterion("loan_Principal is null");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalIsNotNull() {
            addCriterion("loan_Principal is not null");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalEqualTo(Double value) {
            addCriterion("loan_Principal =", value, "loanPrincipal");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalNotEqualTo(Double value) {
            addCriterion("loan_Principal <>", value, "loanPrincipal");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalGreaterThan(Double value) {
            addCriterion("loan_Principal >", value, "loanPrincipal");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalGreaterThanOrEqualTo(Double value) {
            addCriterion("loan_Principal >=", value, "loanPrincipal");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalLessThan(Double value) {
            addCriterion("loan_Principal <", value, "loanPrincipal");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalLessThanOrEqualTo(Double value) {
            addCriterion("loan_Principal <=", value, "loanPrincipal");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalIn(List<Double> values) {
            addCriterion("loan_Principal in", values, "loanPrincipal");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalNotIn(List<Double> values) {
            addCriterion("loan_Principal not in", values, "loanPrincipal");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalBetween(Double value1, Double value2) {
            addCriterion("loan_Principal between", value1, value2, "loanPrincipal");
            return (Criteria) this;
        }

        public Criteria andLoanPrincipalNotBetween(Double value1, Double value2) {
            addCriterion("loan_Principal not between", value1, value2, "loanPrincipal");
            return (Criteria) this;
        }

        public Criteria andLoanRateIsNull() {
            addCriterion("loan_rate is null");
            return (Criteria) this;
        }

        public Criteria andLoanRateIsNotNull() {
            addCriterion("loan_rate is not null");
            return (Criteria) this;
        }

        public Criteria andLoanRateEqualTo(Double value) {
            addCriterion("loan_rate =", value, "loanRate");
            return (Criteria) this;
        }

        public Criteria andLoanRateNotEqualTo(Double value) {
            addCriterion("loan_rate <>", value, "loanRate");
            return (Criteria) this;
        }

        public Criteria andLoanRateGreaterThan(Double value) {
            addCriterion("loan_rate >", value, "loanRate");
            return (Criteria) this;
        }

        public Criteria andLoanRateGreaterThanOrEqualTo(Double value) {
            addCriterion("loan_rate >=", value, "loanRate");
            return (Criteria) this;
        }

        public Criteria andLoanRateLessThan(Double value) {
            addCriterion("loan_rate <", value, "loanRate");
            return (Criteria) this;
        }

        public Criteria andLoanRateLessThanOrEqualTo(Double value) {
            addCriterion("loan_rate <=", value, "loanRate");
            return (Criteria) this;
        }

        public Criteria andLoanRateIn(List<Double> values) {
            addCriterion("loan_rate in", values, "loanRate");
            return (Criteria) this;
        }

        public Criteria andLoanRateNotIn(List<Double> values) {
            addCriterion("loan_rate not in", values, "loanRate");
            return (Criteria) this;
        }

        public Criteria andLoanRateBetween(Double value1, Double value2) {
            addCriterion("loan_rate between", value1, value2, "loanRate");
            return (Criteria) this;
        }

        public Criteria andLoanRateNotBetween(Double value1, Double value2) {
            addCriterion("loan_rate not between", value1, value2, "loanRate");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyIsNull() {
            addCriterion("loan_penalty is null");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyIsNotNull() {
            addCriterion("loan_penalty is not null");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyEqualTo(Double value) {
            addCriterion("loan_penalty =", value, "loanPenalty");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyNotEqualTo(Double value) {
            addCriterion("loan_penalty <>", value, "loanPenalty");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyGreaterThan(Double value) {
            addCriterion("loan_penalty >", value, "loanPenalty");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyGreaterThanOrEqualTo(Double value) {
            addCriterion("loan_penalty >=", value, "loanPenalty");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyLessThan(Double value) {
            addCriterion("loan_penalty <", value, "loanPenalty");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyLessThanOrEqualTo(Double value) {
            addCriterion("loan_penalty <=", value, "loanPenalty");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyIn(List<Double> values) {
            addCriterion("loan_penalty in", values, "loanPenalty");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyNotIn(List<Double> values) {
            addCriterion("loan_penalty not in", values, "loanPenalty");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyBetween(Double value1, Double value2) {
            addCriterion("loan_penalty between", value1, value2, "loanPenalty");
            return (Criteria) this;
        }

        public Criteria andLoanPenaltyNotBetween(Double value1, Double value2) {
            addCriterion("loan_penalty not between", value1, value2, "loanPenalty");
            return (Criteria) this;
        }

        public Criteria andLoanSumIsNull() {
            addCriterion("loan_sum is null");
            return (Criteria) this;
        }

        public Criteria andLoanSumIsNotNull() {
            addCriterion("loan_sum is not null");
            return (Criteria) this;
        }

        public Criteria andLoanSumEqualTo(Double value) {
            addCriterion("loan_sum =", value, "loanSum");
            return (Criteria) this;
        }

        public Criteria andLoanSumNotEqualTo(Double value) {
            addCriterion("loan_sum <>", value, "loanSum");
            return (Criteria) this;
        }

        public Criteria andLoanSumGreaterThan(Double value) {
            addCriterion("loan_sum >", value, "loanSum");
            return (Criteria) this;
        }

        public Criteria andLoanSumGreaterThanOrEqualTo(Double value) {
            addCriterion("loan_sum >=", value, "loanSum");
            return (Criteria) this;
        }

        public Criteria andLoanSumLessThan(Double value) {
            addCriterion("loan_sum <", value, "loanSum");
            return (Criteria) this;
        }

        public Criteria andLoanSumLessThanOrEqualTo(Double value) {
            addCriterion("loan_sum <=", value, "loanSum");
            return (Criteria) this;
        }

        public Criteria andLoanSumIn(List<Double> values) {
            addCriterion("loan_sum in", values, "loanSum");
            return (Criteria) this;
        }

        public Criteria andLoanSumNotIn(List<Double> values) {
            addCriterion("loan_sum not in", values, "loanSum");
            return (Criteria) this;
        }

        public Criteria andLoanSumBetween(Double value1, Double value2) {
            addCriterion("loan_sum between", value1, value2, "loanSum");
            return (Criteria) this;
        }

        public Criteria andLoanSumNotBetween(Double value1, Double value2) {
            addCriterion("loan_sum not between", value1, value2, "loanSum");
            return (Criteria) this;
        }

        public Criteria andLoanLeftIsNull() {
            addCriterion("loan_left is null");
            return (Criteria) this;
        }

        public Criteria andLoanLeftIsNotNull() {
            addCriterion("loan_left is not null");
            return (Criteria) this;
        }

        public Criteria andLoanLeftEqualTo(Double value) {
            addCriterion("loan_left =", value, "loanLeft");
            return (Criteria) this;
        }

        public Criteria andLoanLeftNotEqualTo(Double value) {
            addCriterion("loan_left <>", value, "loanLeft");
            return (Criteria) this;
        }

        public Criteria andLoanLeftGreaterThan(Double value) {
            addCriterion("loan_left >", value, "loanLeft");
            return (Criteria) this;
        }

        public Criteria andLoanLeftGreaterThanOrEqualTo(Double value) {
            addCriterion("loan_left >=", value, "loanLeft");
            return (Criteria) this;
        }

        public Criteria andLoanLeftLessThan(Double value) {
            addCriterion("loan_left <", value, "loanLeft");
            return (Criteria) this;
        }

        public Criteria andLoanLeftLessThanOrEqualTo(Double value) {
            addCriterion("loan_left <=", value, "loanLeft");
            return (Criteria) this;
        }

        public Criteria andLoanLeftIn(List<Double> values) {
            addCriterion("loan_left in", values, "loanLeft");
            return (Criteria) this;
        }

        public Criteria andLoanLeftNotIn(List<Double> values) {
            addCriterion("loan_left not in", values, "loanLeft");
            return (Criteria) this;
        }

        public Criteria andLoanLeftBetween(Double value1, Double value2) {
            addCriterion("loan_left between", value1, value2, "loanLeft");
            return (Criteria) this;
        }

        public Criteria andLoanLeftNotBetween(Double value1, Double value2) {
            addCriterion("loan_left not between", value1, value2, "loanLeft");
            return (Criteria) this;
        }

        public Criteria andPlanTimeIsNull() {
            addCriterion("plan_time is null");
            return (Criteria) this;
        }

        public Criteria andPlanTimeIsNotNull() {
            addCriterion("plan_time is not null");
            return (Criteria) this;
        }

        public Criteria andPlanTimeEqualTo(Date value) {
            addCriterionForJDBCDate("plan_time =", value, "planTime");
            return (Criteria) this;
        }

        public Criteria andPlanTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("plan_time <>", value, "planTime");
            return (Criteria) this;
        }

        public Criteria andPlanTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("plan_time >", value, "planTime");
            return (Criteria) this;
        }

        public Criteria andPlanTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("plan_time >=", value, "planTime");
            return (Criteria) this;
        }

        public Criteria andPlanTimeLessThan(Date value) {
            addCriterionForJDBCDate("plan_time <", value, "planTime");
            return (Criteria) this;
        }

        public Criteria andPlanTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("plan_time <=", value, "planTime");
            return (Criteria) this;
        }

        public Criteria andPlanTimeIn(List<Date> values) {
            addCriterionForJDBCDate("plan_time in", values, "planTime");
            return (Criteria) this;
        }

        public Criteria andPlanTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("plan_time not in", values, "planTime");
            return (Criteria) this;
        }

        public Criteria andPlanTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("plan_time between", value1, value2, "planTime");
            return (Criteria) this;
        }

        public Criteria andPlanTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("plan_time not between", value1, value2, "planTime");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andParseTimeIsNull() {
            addCriterion("parse_time is null");
            return (Criteria) this;
        }

        public Criteria andParseTimeIsNotNull() {
            addCriterion("parse_time is not null");
            return (Criteria) this;
        }

        public Criteria andParseTimeEqualTo(Date value) {
            addCriterionForJDBCDate("parse_time =", value, "parseTime");
            return (Criteria) this;
        }

        public Criteria andParseTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("parse_time <>", value, "parseTime");
            return (Criteria) this;
        }

        public Criteria andParseTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("parse_time >", value, "parseTime");
            return (Criteria) this;
        }

        public Criteria andParseTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("parse_time >=", value, "parseTime");
            return (Criteria) this;
        }

        public Criteria andParseTimeLessThan(Date value) {
            addCriterionForJDBCDate("parse_time <", value, "parseTime");
            return (Criteria) this;
        }

        public Criteria andParseTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("parse_time <=", value, "parseTime");
            return (Criteria) this;
        }

        public Criteria andParseTimeIn(List<Date> values) {
            addCriterionForJDBCDate("parse_time in", values, "parseTime");
            return (Criteria) this;
        }

        public Criteria andParseTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("parse_time not in", values, "parseTime");
            return (Criteria) this;
        }

        public Criteria andParseTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("parse_time between", value1, value2, "parseTime");
            return (Criteria) this;
        }

        public Criteria andParseTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("parse_time not between", value1, value2, "parseTime");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated do_not_delete_during_merge Fri May 22 14:26:26 CST 2015
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table gjj_repay_plan_detail
     *
     * @mbggenerated Fri May 22 14:26:26 CST 2015
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}