package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DeviceInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 设备管理 service层
 *
 * @author FanK
 */
public interface IDeviceInfoService extends IService<DeviceInfo> {

    /**
     * 分页获取设备管理信息
     *
     * @param page       分页对象
     * @param deviceInfo 设备管理信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDevicePage(Page<DeviceInfo> page, DeviceInfo deviceInfo);

    /**
     * 场景事件处理
     *
     * @param eventId 事件ID
     */
    void eventCheck(Integer eventId);
}
