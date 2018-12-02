package cn.idea.modules.common.service;

import cn.idea.modules.common.bean.BaseQVo;
import cn.idea.modules.common.bean.BaseVo;
import cn.idea.modules.common.bean.ConflictingQVo;
import cn.idea.modules.common.bean.PageVo;
import cn.idea.modules.common.consts.UserConst;
import cn.idea.modules.common.dao.BaseMapper;
import cn.idea.modules.common.exception.JudgeException;
import cn.idea.modules.common.exception.ServiceException;
import com.google.common.base.MoreObjects;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;

@Log4j2
public abstract class BaseService<V extends BaseVo, Q extends BaseQVo> {
    // 默认页大小
    protected static final int DEFAULT_PAGE_SIZE = 15;

    // 页上限
    protected static final int PAGE_SIZE_UPPER_LIMIT = 100;

    // 对象的名称
    protected final String beanName;

    @Autowired
    private BaseMapper<V, Q> baseMapper;

    protected BaseService(String beanName) {
        this.beanName = beanName;
    }

    @Transactional
    public BaseVo save(V v) throws ServiceException {
        try {
            preSave(v);
        } catch (JudgeException e) {
            log.info("关键信息已存在，无法被保存, v = {}", v);
            throw new ServiceException("%s保存失败，原因为:%s", beanName, e.getMessage());
        }

        int res = baseMapper.add(v);
        ServiceException.when(res == 0, "存储失败，请重新提交");

        postSave(v);
        return BaseVo.ofValid(v.getId());
    }

    /**
     * 在save前的预处理
     * 1. 校验功能：
     * 1.1 用于检查数据的合理性
     * 1.2 检查实体是否存在于数据库
     * 2. 完善数据：
     * 1.1 其他关联数据的处理
     *
     * @param v 实体，没有id属性
     */
    abstract protected void preSave(V v) throws JudgeException, ServiceException;

    /**
     * [可选]存储实体的后处理
     * 完善数据：其他关联数据的处理
     *
     * @param v 实体信息
     * @throws ServiceException 服务异常，用来中断保存
     */
    protected void postSave(V v) throws ServiceException {

    }

    public V view(int id) throws ServiceException {
        V v = baseMapper.find(id);
        if (v == null) {
            throw new ServiceException("%s信息不存在", beanName);
        }
        postView(v);
        return v;
    }

    /**
     * [可选]查看实体的后处理
     * 完善数据：其他关联数据的处理
     *
     * @param v 实体信息
     * @throws ServiceException 服务异常，用来中断查看
     */
    protected void postView(V v) throws ServiceException {

    }

    @Transactional
    public BaseVo update(V v) throws ServiceException {
        V _v = baseMapper.find(v.getId());
        if (_v == null) {
            throw new ServiceException("没有找到对应的%s", beanName);
        }

        try {
            preUpdate(v, _v);
        } catch (JudgeException e) {
            throw new ServiceException("%s信息更新失败，原因为:%s", beanName, e.getMessage());
        }

        int res = baseMapper.update(v);
        ServiceException.when(res == 0, "更新失败，请重新提交");
        postUpdate(v, _v);
        return BaseVo.ofValid(v.getId());
    }


    /**
     * 在update前做一个校验，其中要更新的数据是否已存在不用校验
     * 1. 校验功能：
     * 1.1 用于检查数据的合理性
     * 1.2 检查实体是否与数据库中其他数据冲突
     * 2. 完善数据：
     * 1.1 其他关联数据的处理
     *
     * @param newV 变更的实体，一定拥有id属性
     * @param oldV 原有的实体
     */
    abstract protected void preUpdate(V newV, V oldV) throws JudgeException;

    /**
     * [可选]更新实体的后处理
     * 完善数据：其他关联数据的处理
     *
     * @param newV 实体信息
     * @param oldV 原有实体信息
     * @throws ServiceException 服务异常，用来中断更新
     */
    protected void postUpdate(V newV, V oldV) throws ServiceException {
    }

    @Transactional
    public BaseVo delete(int id) throws ServiceException {
        V v = baseMapper.find(id);
        if (v == null) {
            throw new ServiceException("找不到%s信息", beanName);
        }

        try {
            judgeAssociative(v);
        } catch (JudgeException e) {
            throw new ServiceException("无法删除%s，原因为:%s", beanName, e.getMessage());
        }
        int res = baseMapper.delete(id);
        ServiceException.when(res == 0, "删除失败，请重新提交");

        postDelete(v);
        return BaseVo.ofInvalid(id);
    }

    /**
     * 用于检测该实体是否与其他数据存在关联关系，用于delete方法
     * (钩子方法)
     *
     * @param v 要删除的实体，该实体是有效的
     */
    protected void judgeAssociative(V v) throws JudgeException {
    }

    /**
     * [可选]删除实体的后处理
     * 完善数据：其他关联数据的处理
     *
     * @param v 实体信息
     * @throws ServiceException 服务异常，用来中断删除
     */
    protected void postDelete(V v) throws ServiceException {
    }

    public PageVo<V> list(int no, @Nullable Integer pageSize, @Nullable Q q) throws ServiceException {
        if (no <= 0) no = 1;

        if (q != null && q instanceof ConflictingQVo && ((ConflictingQVo) q).isConflicting) {
            throw new ServiceException("查询条件存在冲突");
        }

        int _pageSize = MoreObjects.firstNonNull(pageSize, DEFAULT_PAGE_SIZE);
        if (_pageSize <= 0) _pageSize = DEFAULT_PAGE_SIZE;

        if (_pageSize > PAGE_SIZE_UPPER_LIMIT) {
            throw new ServiceException("单页数据量最大为%s", PAGE_SIZE_UPPER_LIMIT);
        }

        // 获得页面信息
        boolean isFindAll = (q == null);
        Integer total = isFindAll ? baseMapper.getTotalNum() : baseMapper.getQueryTotalNum(q);
        int offset = (no - 1) * _pageSize;
        boolean notFound = (total == null || total == 0 || offset >= total);
        if (notFound) { // 没有找到条目,则返回
            throw new ServiceException("没有找到%s信息", beanName);
        }
        Integer totalPage = total / _pageSize + (total % _pageSize == 0 ? 0 : 1);

        if (!isFindAll) q.setPageRange(offset, _pageSize);
        List<V> vl = isFindAll ? baseMapper.findPagedList(offset, _pageSize) : baseMapper.queryPagedList(q);
        postList(vl);

        return new PageVo<>(no, vl, totalPage, total);
    }

    /**
     * [可选]查询实体的后处理
     * 完善数据：其他关联数据的处理
     *
     * @param vl 实体列表
     * @throws ServiceException 服务异常，用来中断查询
     */
    protected void postList(List<V> vl) throws ServiceException {
    }

    /**
     * 用于判断账号是否合法，用户账号应该不能同管理员的账号一致
     *
     * @param account 用户账号
     * @throws JudgeException 抛出和一般账号判重相近的异常信息
     */
    protected void judgeAccountIllegal(String account, String msg) throws JudgeException {
        if (UserConst.ADMIN_CODE.equals(account)) {
            throw new JudgeException(msg);
        }
    }
}
