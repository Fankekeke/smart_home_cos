package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DeviceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 设备类型 mapper层
 *
 * @author FanK
 */
public interface DeviceTypeMapper extends BaseMapper<DeviceType> {

    /**
     * 分页获取设备类型信息
     *
     * @param page       分页对象
     * @param deviceType 设备类型信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDeviceTypePage(Page<DeviceType> page, @Param("deviceType") DeviceType deviceType);

    /**
     * 根据事件获取数据上报数量
     *
     * @param year 年份
     * @param month 月份
     * @return 结果
     */
    Integer selectDataByMonth(@Param("year") Integer year, @Param("month") Integer month);

    /**
     * 根据事件获取数据报警数量
     *
     * @param year 年份
     * @param month 月份
     * @return 结果
     */
    Integer selectAlertByMonth(@Param("year") Integer year, @Param("month") Integer month);

    /**
     * 十天内上报数量数量统计
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectDataNumWithinDays(@Param("userId") Integer userId);

    /**
     * 十天内报警统计
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectAlertNumWithinDays(@Param("userId") Integer userId);
}
