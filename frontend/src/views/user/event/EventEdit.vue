<template>
  <a-modal v-model="show" title="修改场景事件" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='场景事件名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='场景内容备注' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'content'
            ]"/>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row :gutter="10">
        <a-divider orientation="left">
          <span style="font-size: 13px">设备信息</span>
        </a-divider>
        <a-col :span="24">
          <a-table :columns="columns" :data-source="dataList" :pagination="false">
            <template slot="nameShow" slot-scope="text, record">
              <a-select style="width: 100%" :value="record.deviceId" @change="handleChange($event, record)">
                <a-select-option v-for="(item, index) in deviceList" :key="index" :value="item.id">{{ item.name }}</a-select-option>
              </a-select>
            </template>
            <template slot="addressShow" slot-scope="text, record">
              <span>{{ record.address }}</span>
            </template>
            <template slot="typeIdShow" slot-scope="text, record">
              <a-select disabled v-model="record.typeId">
                <a-select-option :value="item.id" v-for="(item, index) in typeList" :key="index">{{ item.name }}</a-select-option>
              </a-select>
            </template>
            <template slot="openFlagShow" slot-scope="text, record">
              <a-switch v-model="record.openFlag" checked-children="开" un-checked-children="关"/>
            </template>
          </a-table>
          <a-button @click="dataAdd" type="primary" ghost style="margin-top: 10px;width: 100%">
            添加设备事件
          </a-button>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'deviceEdit',
  props: {
    deviceEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.deviceEditVisiable
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '设备名称',
        dataIndex: 'name',
        scopedSlots: {customRender: 'nameShow'}
      }, {
        title: '设备位置',
        dataIndex: 'address',
        scopedSlots: {customRender: 'addressShow'}
      }, {
        title: '设备类型',
        dataIndex: 'typeId',
        scopedSlots: {customRender: 'typeIdShow'}
      }, {
        title: '设置开关',
        dataIndex: 'openFlag',
        scopedSlots: {customRender: 'openFlagShow'}
      }]
    }
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      typeList: [],
      dataList: [],
      deviceList: []
    }
  },
  mounted () {
    this.selectTypeList()
    this.queryDeviceList()
  },
  methods: {
    queryEventDetail (id) {
      this.$get('/cos/event-info/queryEventDetail', {
        eventId: id
      }).then((r) => {
        r.data.data.forEach((e, index) => {
          e.openFlag = e.openFlag == 1 ? true : false
        })
        this.dataList = r.data.data
      })
    },
    queryDeviceList () {
      this.$get('/cos/device-info/selectDeviceByUserId', {userId: this.currentUser.userId}).then((r) => {
        this.deviceList = r.data.data
      })
    },
    handleChange (value, record) {
      if (value) {
        this.deviceList.forEach(e => {
          if (e.id === value) {
            record.openFlag = 0
            record.typeId = e.typeId
            record.address = e.address
            record.deviceId = e.id
            console.log(record)
          }
        })
      }
    },
    dataAdd () {
      this.dataList.push({deviceId: null, address: '', typeId: null, openFlag: true})
    },
    selectTypeList () {
      this.$get('/cos/device-type/list').then((r) => {
        this.typeList = r.data.data
      })
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    setFormValues ({...device}) {
      this.rowId = device.id
      this.queryEventDetail(device.id)
      let fields = ['name', 'model', 'typeId', 'onlineFlag', 'openFlag', 'content']
      let obj = {}
      Object.keys(device).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(device['images'])
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = device[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      if (this.dataList.length === 0) {
        this.$message.error('请添加设备信息')
        return false
      }
      this.form.validateFields((err, values) => {
        this.dataList.forEach((e, index) => {
          e.openFlag = e.openFlag ? 1 : 0
        })
        values.eventDetail = JSON.stringify(this.dataList)
        values.id = this.rowId
        if (!err) {
          this.loading = true
          this.$put('/cos/event-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
