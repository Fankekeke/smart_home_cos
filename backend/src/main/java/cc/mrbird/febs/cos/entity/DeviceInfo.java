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
 * 设备管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备类型
     */
    private Integer typeId;

    /**
     * 设备是否在线（0.否 1.是）
     */
    private String onlineFlag;

    /**
     * 设备创建时间
     */
    private String createDate;

    /**
     * 设备开关状态（0.关闭 1.开启）
     */
    private String openFlag;

    /**
     * 设备值
     */
    private String deviceValue;

    /**
     * 设备报警值
     */
    private String alertValue;

    /**
     * 值类型（1.整数 2.小数 2.boolean）
     */
    private String valueType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 上次开启时间
     */
    private String lastOpenDate;

    /**
     * 设置位置
     */
    private String address;

    @TableField(exist = false)
    private String deviceName;

    @TableField(exist = false)
    private String typeName;

    @TableField(exist = false)
    private String userName;
}
