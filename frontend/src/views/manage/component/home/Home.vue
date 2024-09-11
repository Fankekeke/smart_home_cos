<template>
  <div>
    <a-row style="margin-top: 15px">
      <a-col :span="24">
        <div style="background: #ECECEC; padding: 30px;" v-if="user.roleId == 74 || user.roleId == 76">
          <a-row :gutter="16">
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本月上报数量</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.monthNum }}
                    <span style="font-size: 20px;margin-top: 3px">条</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本月报警数量</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.monthAlertNum }}
                    <span style="font-size: 20px;margin-top: 3px">条</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本年上报数量</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.yearNum }}
                    <span style="font-size: 20px;margin-top: 3px">条</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
            <a-col :span="6">
              <a-card hoverable>
                <a-row>
                  <a-col :span="24" style="font-size: 13px;margin-bottom: 8px;font-family: SimHei">本年报警数量</a-col>
                  <a-col :span="4"><a-icon type="arrow-up" style="font-size: 30px;margin-top: 3px"/></a-col>
                  <a-col :span="18" style="font-size: 28px;font-weight: 500;font-family: SimHei">
                    {{ titleData.yearAlertNum }}
                    <span style="font-size: 20px;margin-top: 3px">条</span>
                  </a-col>
                </a-row>
              </a-card>
            </a-col>
          </a-row>
        </div>
      </a-col>
    </a-row>
    <a-row style="margin-top: 15px" v-if="user.roleId == 74 || user.roleId == 76" :gutter="25">
      <a-col :span="12">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart  v-if="!loading" type="line" height="300" :options="chartOptions" :series="series"></apexchart>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart v-if="!loading" type="bar" height="300" :options="chartOptions1" :series="series1"></apexchart>
        </a-card>
      </a-col>
    </a-row>
<!--    <a-row style="margin-top: 15px" v-if="user.roleId == 74 || user.roleId == 76">-->
<!--      <a-col :span="12">-->
<!--        <a-card hoverable :bordered="false" style="width: 100%">-->
<!--          <a-skeleton active v-if="loading" />-->
<!--          <apexchart  v-if="!loading" type="line" height="300" :options="chartOptions2" :series="series2"></apexchart>-->
<!--        </a-card>-->
<!--      </a-col>-->
<!--      <a-col :span="12">-->
<!--        <a-card hoverable :bordered="false" style="width: 100%">-->
<!--          <a-skeleton active v-if="loading" />-->
<!--          <apexchart v-if="!loading" type="bar" height="300" :options="chartOptions3" :series="series3"></apexchart>-->
<!--        </a-card>-->
<!--      </a-col>-->
<!--    </a-row>-->
    <a-row style="margin-top: 15px">
      <a-col :span="24">
        <a-card hoverable :loading="loading" :bordered="false" title="公告信息" style="margin-top: 15px">
          <div style="padding: 0 22px">
            <a-list item-layout="vertical" :pagination="pagination" :data-source="bulletinList">
              <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">
                <template slot="actions">
              <span key="message">
                <a-icon type="message" style="margin-right: 8px" />
                {{ item.date }}
              </span>
                </template>
                <a-list-item-meta :description="item.content" style="font-size: 14px">
                  <a slot="title">{{ item.title }}</a>
                </a-list-item-meta>
              </a-list-item>
            </a-list>
          </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import {mapState} from 'vuex'
export default {
  name: 'Home',
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    })
  },
  data () {
    return {
      pagination: {
        onChange: page => {
          console.log(page)
        },
        pageSize: 2
      },
      bulletinList: [],
      titleData: {
        monthNum: 0,
        monthAlertNum: 0,
        yearNum: 0,
        yearAlertNum: 0
      },
      loading: false,
      series: [{
        name: '条',
        data: []
      }],
      chartOptions: {
        chart: {
          type: 'line',
          height: 300
        },
        xaxis: {
          categories: []
        },
        stroke: {
          curve: 'stepline'
        },
        dataLabels: {
          enabled: false
        },
        title: {
          text: '近十天报警统计',
          align: 'left'
        },
        markers: {
          hover: {
            sizeOffset: 4
          }
        }
      },
      series1: [],
      chartOptions1: {
        chart: {
          type: 'bar',
          height: 300
        },
        title: {
          text: '近十天数据统计',
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
      },
      series2: [{
        name: '条',
        data: []
      }],
      chartOptions2: {
        chart: {
          type: 'line',
          height: 300
        },
        xaxis: {
          categories: []
        },
        stroke: {
          curve: 'stepline'
        },
        dataLabels: {
          enabled: false
        },
        title: {
          text: '近十天报警统计',
          align: 'left'
        },
        markers: {
          hover: {
            sizeOffset: 4
          }
        }
      },
      series3: [],
      chartOptions3: {
        chart: {
          type: 'bar',
          height: 300
        },
        title: {
          text: '近十天统计',
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
  mounted () {
    this.loading = true
    this.selectHomeData()
    setTimeout(() => {
      this.loading = false
    }, 200)
  },
  methods: {
    selectHomeData () {
      this.$get('/cos/device-type/homeData').then((r) => {
        let titleData = { userNum: r.data.userNum, deviceNum: r.data.deviceNum, historyNum: r.data.historyNum, alertNum: r.data.alertNum }
        this.$emit('setTitle', titleData)
        this.titleData.monthNum = r.data.monthNum
        this.titleData.monthAlertNum = r.data.monthAlertNum
        this.titleData.yearNum = r.data.yearNum
        this.titleData.yearAlertNum = r.data.yearAlertNum

        this.bulletinList = r.data.bulletin
        let values = []
        if (r.data.numDayList !== null && r.data.numDayList.length !== 0) {
          if (this.chartOptions1.xaxis.categories.length === 0) {
            this.chartOptions1.xaxis.categories = r.data.numDayList.map(obj => { return obj.days })
          }
          let itemData = { name: '统计', data: r.data.numDayList.map(obj => { return obj.count }) }
          values.push(itemData)
          this.series1 = values
        }
        this.series[0].data = r.data.alertDayList.map(obj => { return obj.count })
        this.chartOptions.xaxis.categories = r.data.alertDayList.map(obj => { return obj.days })

        // if (r.data.putNumWithinDays !== null && r.data.putNumWithinDays.length !== 0) {
        //   if (this.chartOptions2.xaxis.categories.length === 0) {
        //     this.chartOptions2.xaxis.categories = r.data.putNumWithinDays.map(obj => { return obj.days })
        //   }
        //   let itemData = { name: '出库统计', data: r.data.putNumWithinDays.map(obj => { return obj.count }) }
        //   values.push(itemData)
        //   this.series2 = values
        // }
        // this.series3[0].data = r.data.putPriceWithinDays.map(obj => { return obj.price })
        // this.chartOptions3.xaxis.categories = r.data.putPriceWithinDays.map(obj => { return obj.days })
      })
    }
  }
}
</script>

<style scoped>

</style>
