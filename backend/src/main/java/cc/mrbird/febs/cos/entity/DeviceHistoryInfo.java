package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 设备上报历史数据
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeviceHistoryInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 设备ID
     */
    private Integer deviceId;

    /**
     * 设备值
     */
    private String deviceValue;


    /**
     * 预警值
     */
    private String alertValue;

    /**
     * 上报时间
     */
    private String createDate;

    /**
     * 设备值
     */
    @TableField(exist = false)
    private Double deviceValue1;

    @TableField(exist = false)
    private String deviceName;

    @TableField(exist = false)
    private String typeName;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private Integer hour;

    @TableField(exist = false)
    private Integer day;

    @TableField(exist = false)
    private Integer month;

    @TableField(exist = false)
    private Integer userId;

    @TableField(exist = false)
    private String valueType;

    @TableField(exist = false)
    private Integer eventId;
}
