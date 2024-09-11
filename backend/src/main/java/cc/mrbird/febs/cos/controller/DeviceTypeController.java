package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DeviceType;
import cc.mrbird.febs.cos.service.IDeviceTypeService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 设备类型 控制层
 *
 * @author FanK
 */
@RestController
@RequestMapping("/cos/device-type")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceTypeController {

    private final IDeviceTypeService deviceTypeService;

    /**
     * 分页获取设备类型信息
     *
     * @param page       分页对象
     * @param deviceType 设备类型信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DeviceType> page, DeviceType deviceType) {
        return R.ok(deviceTypeService.selectDeviceTypePage(page, deviceType));
    }

    /**
     * 获取首页统计数据
     *
     * @return 结果
     */
    @GetMapping("/homeData")
    public R homeData() {
        return R.ok(deviceTypeService.homeData());
    }

    /**
     * 查询设备类型信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(deviceTypeService.getById(id));
    }

    /**
     * 查询设备类型信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(deviceTypeService.list());
    }

    /**
     * 新增设备类型信息
     *
     * @param deviceType 设备类型信息
     * @return 结果
     */
    @PostMapping
    public R save(DeviceType deviceType) {
        // 类型编号
        deviceType.setCode("DT-" + System.currentTimeMillis());
        // 创建时间
        deviceType.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(deviceTypeService.save(deviceType));
    }

    /**
     * 修改设备类型信息
     *
     * @param deviceType 设备类型信息
     * @return 结果
     */
    @PutMapping
    public R edit(DeviceType deviceType) {
        return R.ok(deviceTypeService.updateById(deviceType));
    }

    /**
     * 删除设备类型信息
     *
     * @param ids ids
     * @return 设备类型信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(deviceTypeService.removeByIds(ids));
    }
}
