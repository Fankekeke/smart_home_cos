package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.service.IUserInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * 用户信息 实现层
 *
 * @author FanK
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    /**
     * 分页获取用户管理
     *
     * @param page     分页对象
     * @param userInfo 用户管理
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectUserPage(Page<UserInfo> page, UserInfo userInfo) {
        return baseMapper.selectUserPage(page, userInfo);
    }
}