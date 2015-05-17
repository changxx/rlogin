package com.rlogin.dao.mapper.gjj;

import com.rlogin.domain.gjj.GjjLoan;
import com.rlogin.domain.gjj.GjjLoanExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GjjLoanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_loan
     *
     * @mbggenerated Sun May 17 11:53:26 CST 2015
     */
    int countByExample(GjjLoanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_loan
     *
     * @mbggenerated Sun May 17 11:53:26 CST 2015
     */
    int deleteByExample(GjjLoanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_loan
     *
     * @mbggenerated Sun May 17 11:53:26 CST 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_loan
     *
     * @mbggenerated Sun May 17 11:53:26 CST 2015
     */
    int insert(GjjLoan record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_loan
     *
     * @mbggenerated Sun May 17 11:53:26 CST 2015
     */
    int insertSelective(GjjLoan record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_loan
     *
     * @mbggenerated Sun May 17 11:53:26 CST 2015
     */
    List<GjjLoan> selectByExample(GjjLoanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_loan
     *
     * @mbggenerated Sun May 17 11:53:26 CST 2015
     */
    GjjLoan selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_loan
     *
     * @mbggenerated Sun May 17 11:53:26 CST 2015
     */
    int updateByExampleSelective(@Param("record") GjjLoan record, @Param("example") GjjLoanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_loan
     *
     * @mbggenerated Sun May 17 11:53:26 CST 2015
     */
    int updateByExample(@Param("record") GjjLoan record, @Param("example") GjjLoanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_loan
     *
     * @mbggenerated Sun May 17 11:53:26 CST 2015
     */
    int updateByPrimaryKeySelective(GjjLoan record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_loan
     *
     * @mbggenerated Sun May 17 11:53:26 CST 2015
     */
    int updateByPrimaryKey(GjjLoan record);
}