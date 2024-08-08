package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DeviceOfflineRecord;
import cc.mrbird.febs.cos.service.IDeviceOfflineRecordService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 设备上下线记录 控制层
 *
 * @author FanK
 */
@RestController
@RequestMapping("/cos/device-offline-record")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceOfflineRecordController {

    private final IDeviceOfflineRecordService deviceOfflineRecordService;

    /**
     * 分页获取设备上下线记录信息
     *
     * @param page                分页对象
     * @param deviceOfflineRecord 设备上下线记录信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DeviceOfflineRecord> page, DeviceOfflineRecord deviceOfflineRecord) {
        return R.ok(deviceOfflineRecordService.selectOfflineRecordPage(page, deviceOfflineRecord));
    }

    /**
     * 查询设备上下线记录信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(deviceOfflineRecordService.getById(id));
    }

    /**
     * 查询设备上下线记录信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(deviceOfflineRecordService.list());
    }

    /**
     * 新增设备上下线记录信息
     *
     * @param deviceOfflineRecord 设备上下线记录信息
     * @return 结果
     */
    @PostMapping
    public R save(DeviceOfflineRecord deviceOfflineRecord) {
        return R.ok(deviceOfflineRecordService.save(deviceOfflineRecord));
    }

    /**
     * 修改设备上下线记录信息
     *
     * @param deviceOfflineRecord 设备上下线记录信息
     * @return 结果
     */
    @PutMapping
    public R edit(DeviceOfflineRecord deviceOfflineRecord) {
        return R.ok(deviceOfflineRecordService.updateById(deviceOfflineRecord));
    }

    /**
     * 删除设备上下线记录信息
     *
     * @param ids ids
     * @return 设备上下线记录信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(deviceOfflineRecordService.removeByIds(ids));
    }
}
