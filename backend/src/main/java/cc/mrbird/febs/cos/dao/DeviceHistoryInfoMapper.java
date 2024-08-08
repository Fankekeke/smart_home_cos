package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DeviceHistoryInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 设备上报历史数据 mapper层
 *
 * @author FanK
 */
public interface DeviceHistoryInfoMapper extends BaseMapper<DeviceHistoryInfo> {

    /**
     * 分页获取设备上报历史数据信息
     *
     * @param page              分页对象
     * @param deviceHistoryInfo 设备上报历史数据信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectHistoryPage(Page<DeviceHistoryInfo> page, @Param("deviceHistoryInfo") DeviceHistoryInfo deviceHistoryInfo);
}
