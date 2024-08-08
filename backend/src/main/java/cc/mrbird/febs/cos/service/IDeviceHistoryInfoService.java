package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DeviceHistoryInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 设备上报历史数据 service层
 *
 * @author FanK
 */
public interface IDeviceHistoryInfoService extends IService<DeviceHistoryInfo> {

    /**
     * 分页获取设备上报历史数据信息
     *
     * @param page              分页对象
     * @param deviceHistoryInfo 设备上报历史数据信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectHistoryPage(Page<DeviceHistoryInfo> page, DeviceHistoryInfo deviceHistoryInfo);
}
