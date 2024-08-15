<template>
  <a-modal v-model="show" title="操作记录详情" @cancel="onClose" :width="1200">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="recordData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">操作记录信息</span></a-col>
        <a-col :span="8"><b>操作记录名称：</b>
          {{ recordData.name }}
        </a-col>
        <a-col :span="8"><b>操作记录编号：</b>
          {{ recordData.code }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ recordData.phone }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>省份：</b>
          {{ recordData.province }}
        </a-col>
        <a-col :span="8"><b>城市：</b>
          {{ recordData.city }}
        </a-col>
        <a-col :span="8"><b>区：</b>
          {{ recordData.area }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="16"><b>收货地址：</b>
          {{ recordData.address }}
        </a-col>
        <a-col :span="8"><b>注册时间：</b>
          {{ recordData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">电子处方</span></a-col>
        <a-col :span="24">
          <a-table :columns="columns" :data-source="durgList">
            <template slot="contentShow" slot-scope="text, record">
              <template>
                <a-tooltip>
                  <template slot="title">
                    {{ record.cause }}
                  </template>
                  {{ record.cause.slice(0, 15) }} ...
                </a-tooltip>
              </template>
            </template>
          </a-table>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'recordView',
  props: {
    recordShow: {
      type: Boolean,
      default: false
    },
    recordData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.recordShow
      },
      set: function () {
      }
    },
    columns () {
      return [{
        title: '处方单号',
        dataIndex: 'code'
      }, {
        title: '病因',
        dataIndex: 'cause',
        scopedSlots: { customRender: 'contentShow' }
      }, {
        title: '出具人',
        dataIndex: 'checkIssuer',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '出具机构',
        dataIndex: 'checkAgency',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '发布时间',
        dataIndex: 'createDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '状态',
        dataIndex: 'status',
        customRender: (text, row, index) => {
          switch (text) {
            case 0:
              return <a-tag color='red'>未处理</a-tag>
            case 1:
              return <a-tag color='green'>已处理</a-tag>
            default:
              return '- -'
          }
        }
      }]
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      durgList: []
    }
  },
  watch: {
    recordShow: function (value) {
      if (value) {
        // 药品信息
        this.$get(`/cos/medication-info/list/byrecord/${this.recordData.id}`).then((r) => {
          this.durgList = r.data.data
        })
      }
    }
  },
  methods: {
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
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
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
