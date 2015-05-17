package com.rlogin.dao.mapper.gjj;

import com.rlogin.domain.gjj.GjjAccDetail;
import com.rlogin.domain.gjj.GjjAccDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GjjAccDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_acc_detail
     *
     * @mbggenerated Sun May 17 22:57:30 CST 2015
     */
    int countByExample(GjjAccDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_acc_detail
     *
     * @mbggenerated Sun May 17 22:57:30 CST 2015
     */
    int deleteByExample(GjjAccDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_acc_detail
     *
     * @mbggenerated Sun May 17 22:57:30 CST 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_acc_detail
     *
     * @mbggenerated Sun May 17 22:57:30 CST 2015
     */
    int insert(GjjAccDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_acc_detail
     *
     * @mbggenerated Sun May 17 22:57:30 CST 2015
     */
    int insertSelective(GjjAccDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_acc_detail
     *
     * @mbggenerated Sun May 17 22:57:30 CST 2015
     */
    List<GjjAccDetail> selectByExample(GjjAccDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_acc_detail
     *
     * @mbggenerated Sun May 17 22:57:30 CST 2015
     */
    GjjAccDetail selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_acc_detail
     *
     * @mbggenerated Sun May 17 22:57:30 CST 2015
     */
    int updateByExampleSelective(@Param("record") GjjAccDetail record, @Param("example") GjjAccDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_acc_detail
     *
     * @mbggenerated Sun May 17 22:57:30 CST 2015
     */
    int updateByExample(@Param("record") GjjAccDetail record, @Param("example") GjjAccDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_acc_detail
     *
     * @mbggenerated Sun May 17 22:57:30 CST 2015
     */
    int updateByPrimaryKeySelective(GjjAccDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gjj_acc_detail
     *
     * @mbggenerated Sun May 17 22:57:30 CST 2015
     */
    int updateByPrimaryKey(GjjAccDetail record);
}