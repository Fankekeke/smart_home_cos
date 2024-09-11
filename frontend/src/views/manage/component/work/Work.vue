<template>
  <div style="width: 100%">
    <a-row style="margin-top: 25px">
      <a-col :span="24">
        <a-skeleton :loading="loading" active :paragraph="{ rows: 10 }"/>
        <a-alert v-if="!loading" message="设备上报历史统计" type="info" close-text="Close Now" style="margin-bottom: 15px"/>
        <a-row :gutter="15" v-if="!loading">
          <a-col :span="7">
            <a-list :data-source="dataList" bordered>
              <a-list-item slot="renderItem" slot-scope="item, index">
                <a-list-item-meta>
                  <a slot="title" @click="selectHistoryByDevice(item.id)">{{ item.name }} <span style="font-size: 13px">{{ item.code }}</span></a>
                  <div slot="description" style="font-size: 13px">
                    <div style="margin-top: 10px">设备类型：{{ item.typeName }}</div>
                    <div style="margin-top: 8px">所属用户：{{ item.userName }}</div>
                    <div style="margin-top: 8px">
                      <a-row :gutter="8">
                        <a-col :span="20">
                          上次在线时间：{{ item.lastOpenDate ? item.lastOpenDate : '- -' }}
                        </a-col>
                        <a-col :span="4">
                          <a-tag size="mini" v-if="item.openFlag == 0" color="pink">状态：关</a-tag>
                          <a-tag size="mini" v-if="item.openFlag == 1" color="blue">状态：开</a-tag>
                        </a-col>
                      </a-row>
                    </div>
                  </div>
                </a-list-item-meta>
                <!--                <a-row :gutter="15">-->
                <!--                  <a-col :span="24" style="font-size: 13px;font-family: SimHei">-->
                <!--                    -->
                <!--                  </a-col>-->
                <!--                </a-row>-->
              </a-list-item>
            </a-list>
          </a-col>
          <a-col :span="17">
            <div style="background:#ECECEC; padding: 30px">
              <a-card hoverable :bordered="false" style="width: 100%">
                <a-skeleton active v-if="loading" />
                <apexchart v-if="!loading" type="bar" height="300" :options="chartOptions1" :series="series1"></apexchart>
              </a-card>
            </div>
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import {mapState} from 'vuex'

export default {
  name: 'House',
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  data () {
    return {
      page: {
        current: 1,
        total: 0,
        size: 999
      },
      dataList: [],
      deviceList: [],
      loading: false,
      checkFlag: '1',
      series: [{
        name: 'Series 1',
        data: [80, 50, 30, 40, 100, 20]
      }],
      chartOptions: {
        chart: {
          height: 350,
          type: 'radar'
        },
        title: {
          text: 'Basic Radar Chart'
        },
        xaxis: {
          categories: ['January', 'February', 'March', 'April', 'May', 'June']
        }
      },
      series1: [],
      chartOptions1: {
        chart: {
          type: 'bar',
          height: 300
        },
        title: {
          text: '本日数据统计',
          align: 'left'
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '55%'
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: []
        },
        yaxis: {
          title: {
            text: ''
          }
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return val + ' 条'
            }
          }
        }
      }
    }
  },
  watch: {
    checkFlag: function (value) {
      this.selectSchoolRate(value)
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    selectHistoryByDevice (deviceId) {
      this.$get(`/cos/device-history-info/selectHistoryByDevice`, {deviceId}).then((r) => {
        let values = []
        if (r.data.data !== null && r.data.data.length !== 0) {
          if (this.chartOptions1.xaxis.categories.length === 0) {
            this.chartOptions1.xaxis.categories = r.data.data.map(obj => { return obj.date })
          }
          let itemData = { name: '统计', data: r.data.data.map(obj => { return obj.value }) }
          values.push(itemData)
          this.series1 = values
        }
      })
    },
    selectDeviceList () {
      this.$get(`/cos/device-info/list`).then((r) => {
        this.deviceList = r.data.data
        this.loading = false
      })
    },
    selectSchoolRate (type) {
      this.loading = true
      this.$get(`/cos/score-line-info/selectSchoolRate/type/${type}`).then((r) => {
        this.dataList = r.data.data
        this.loading = false
      })
    },
    pageChange (page, pageSize) {
      this.page.size = pageSize
      this.page.current = page
      this.fetch()
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      // 如果分页信息为空，则设置为默认值
      params.size = this.page.size
      params.current = this.page.current
      params.userId = this.currentUser.userId
      this.$get('/cos/device-info/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataList = data.records
        this.page = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-alert-message {
  font-size: 14px;
  font-family: SimHei;
}
</style>
