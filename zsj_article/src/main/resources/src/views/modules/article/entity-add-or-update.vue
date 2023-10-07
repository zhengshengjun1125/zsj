<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="文章标题" prop="artTitle">
      <el-input v-model="dataForm.artTitle" placeholder="文章标题"></el-input>
    </el-form-item>
    <el-form-item label="文章所属分类id" prop="artClassId">
      <el-input v-model="dataForm.artClassId" placeholder="文章所属分类id"></el-input>
    </el-form-item>
    <el-form-item label="文章内容 需要是一个html格式或者md格式" prop="artContent">
      <el-input v-model="dataForm.artContent" placeholder="文章内容 需要是一个html格式或者md格式"></el-input>
    </el-form-item>
    <el-form-item label="文章月访问量" prop="artRequestMonth">
      <el-input v-model="dataForm.artRequestMonth" placeholder="文章月访问量"></el-input>
    </el-form-item>
    <el-form-item label="文章日访问量" prop="artRequestDay">
      <el-input v-model="dataForm.artRequestDay" placeholder="文章日访问量"></el-input>
    </el-form-item>
    <el-form-item label="文章总访问量" prop="artRequestTotal">
      <el-input v-model="dataForm.artRequestTotal" placeholder="文章总访问量"></el-input>
    </el-form-item>
    <el-form-item label="文章作者" prop="artAuther">
      <el-input v-model="dataForm.artAuther" placeholder="文章作者"></el-input>
    </el-form-item>
    <el-form-item label="文章发布时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="文章发布时间"></el-input>
    </el-form-item>
    <el-form-item label="文章修改时间" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder="文章修改时间"></el-input>
    </el-form-item>
    <el-form-item label="0表示删除 1表示正常" prop="artStatus">
      <el-input v-model="dataForm.artStatus" placeholder="0表示删除 1表示正常"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          artTitle: '',
          artClassId: '',
          artContent: '',
          artRequestMonth: '',
          artRequestDay: '',
          artRequestTotal: '',
          artAuther: '',
          createTime: '',
          updateTime: '',
          artStatus: ''
        },
        dataRule: {
          artTitle: [
            { required: true, message: '文章标题不能为空', trigger: 'blur' }
          ],
          artClassId: [
            { required: true, message: '文章所属分类id不能为空', trigger: 'blur' }
          ],
          artContent: [
            { required: true, message: '文章内容 需要是一个html格式或者md格式不能为空', trigger: 'blur' }
          ],
          artRequestMonth: [
            { required: true, message: '文章月访问量不能为空', trigger: 'blur' }
          ],
          artRequestDay: [
            { required: true, message: '文章日访问量不能为空', trigger: 'blur' }
          ],
          artRequestTotal: [
            { required: true, message: '文章总访问量不能为空', trigger: 'blur' }
          ],
          artAuther: [
            { required: true, message: '文章作者不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '文章发布时间不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '文章修改时间不能为空', trigger: 'blur' }
          ],
          artStatus: [
            { required: true, message: '0表示删除 1表示正常不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/article/entity/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.artTitle = data.entity.artTitle
                this.dataForm.artClassId = data.entity.artClassId
                this.dataForm.artContent = data.entity.artContent
                this.dataForm.artRequestMonth = data.entity.artRequestMonth
                this.dataForm.artRequestDay = data.entity.artRequestDay
                this.dataForm.artRequestTotal = data.entity.artRequestTotal
                this.dataForm.artAuther = data.entity.artAuther
                this.dataForm.createTime = data.entity.createTime
                this.dataForm.updateTime = data.entity.updateTime
                this.dataForm.artStatus = data.entity.artStatus
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/article/entity/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'artTitle': this.dataForm.artTitle,
                'artClassId': this.dataForm.artClassId,
                'artContent': this.dataForm.artContent,
                'artRequestMonth': this.dataForm.artRequestMonth,
                'artRequestDay': this.dataForm.artRequestDay,
                'artRequestTotal': this.dataForm.artRequestTotal,
                'artAuther': this.dataForm.artAuther,
                'createTime': this.dataForm.createTime,
                'updateTime': this.dataForm.updateTime,
                'artStatus': this.dataForm.artStatus
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
