package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DeviceInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IDeviceInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用户信息 控制层
 *
 * @author FanK
 */
@RestController
@RequestMapping("/cos/user-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInfoController {

    private final IUserInfoService userInfoService;

    private final IDeviceInfoService deviceInfoService;

    /**
     * 分页获取用户管理
     *
     * @param page     分页对象
     * @param userInfo 用户管理
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<UserInfo> page, UserInfo userInfo) {
        return R.ok(userInfoService.selectUserPage(page, userInfo));
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/selectDetailByUserId/{userId}")
    public R selectDetailByUserId(@PathVariable("userId") Integer userId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("user", null);
                put("order", Collections.emptyList());
            }
        };

        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        if (userInfo == null) {
            return R.ok(result);
        }
        result.put("user", userInfo);
        // 设备信息
        result.put("order", deviceInfoService.list(Wrappers.<DeviceInfo>lambdaQuery().eq(DeviceInfo::getUserId, userInfo.getId())));
        return R.ok(result);
    }

    /**
     * 查询用户管理详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(userInfoService.getById(id));
    }

    /**
     * 查询用户管理列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(userInfoService.list());
    }

    /**
     * 新增用户管理
     *
     * @param userInfo 用户管理
     * @return 结果
     */
    @PostMapping
    public R save(UserInfo userInfo) {
        // 创建时间
        userInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(userInfoService.save(userInfo));
    }

    /**
     * 修改用户管理
     *
     * @param userInfo 用户管理
     * @return 结果
     */
    @PutMapping
    public R edit(UserInfo userInfo) {
        return R.ok(userInfoService.updateById(userInfo));
    }

    /**
     * 删除用户管理
     *
     * @param ids ids
     * @return 用户管理
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(userInfoService.removeByIds(ids));
    }
}
