package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DeviceAlertInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 设备报警配置 mapper层
 *
 * @author FanK
 */
public interface DeviceAlertInfoMapper extends BaseMapper<DeviceAlertInfo> {

    /**
     * 分页获取设备报警配置信息
     *
     * @param page            分页对象
     * @param deviceAlertInfo 设备报警配置信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDeviceAlertPage(Page<DeviceAlertInfo> page, @Param("deviceAlertInfo") DeviceAlertInfo deviceAlertInfo);
}
