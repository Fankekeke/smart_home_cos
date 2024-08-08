package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DeviceInfo;
import cc.mrbird.febs.cos.service.IDeviceInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备管理 控制层
 *
 * @author FanK
 */
@RestController
@RequestMapping("/cos/device-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceInfoController {

    private final IDeviceInfoService deviceInfoService;

    /**
     * 分页获取设备管理信息
     *
     * @param page       分页对象
     * @param deviceInfo 设备管理信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DeviceInfo> page, DeviceInfo deviceInfo) {
        return R.ok(deviceInfoService.selectDevicePage(page, deviceInfo));
    }

    /**
     * 查询设备管理信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(deviceInfoService.getById(id));
    }

    /**
     * 查询设备管理信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(deviceInfoService.list());
    }

    /**
     * 新增设备管理信息
     *
     * @param deviceInfo 设备管理信息
     * @return 结果
     */
    @PostMapping
    public R save(DeviceInfo deviceInfo) {
        return R.ok(deviceInfoService.save(deviceInfo));
    }

    /**
     * 修改设备管理信息
     *
     * @param deviceInfo 设备管理信息
     * @return 结果
     */
    @PutMapping
    public R edit(DeviceInfo deviceInfo) {
        return R.ok(deviceInfoService.updateById(deviceInfo));
    }

    /**
     * 删除设备管理信息
     *
     * @param ids ids
     * @return 设备管理信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(deviceInfoService.removeByIds(ids));
    }
}
