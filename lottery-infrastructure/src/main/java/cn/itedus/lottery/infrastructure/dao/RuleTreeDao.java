package cn.itedus.lottery.infrastructure.dao;

import cn.itedus.lottery.infrastructure.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChuYang
 * @version 1.0
 */
@Mapper
public interface RuleTreeDao {

    /**
     * 规则树查询
     * @param id
     * @return
     */
    RuleTree queryRuleTreeByTreeId(Long id);

    /**
     * 规则树简要信息查询
     * @param treeId
     * @return
     */
    RuleTree queryTreeSummaryInfo(Long treeId);
}
