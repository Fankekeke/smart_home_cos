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
 * 设备报警配置
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeviceAlertInfo implements Serializable {

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
     * 设备类型ID
     */
    private Integer deviceTypeId;

    /**
     * 预警类型（1.设备开关状态时常报警 2.目标值越界）
     */
    private String type;

    /**
     * 报警值
     */
    private Integer score;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private String deviceName;

    @TableField(exist = false)
    private String typeName;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private Integer userId;
}
