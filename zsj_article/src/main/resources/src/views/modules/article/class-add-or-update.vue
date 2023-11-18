<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="分类名称" prop="className">
      <el-input v-model="dataForm.className" placeholder="分类名称"></el-input>
    </el-form-item>
    <el-form-item label="分类创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="分类创建时间"></el-input>
    </el-form-item>
    <el-form-item label="分类修改时间" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder="分类修改时间"></el-input>
    </el-form-item>
    <el-form-item label="分类状态" prop="classStatus">
      <el-input v-model="dataForm.classStatus" placeholder="分类状态"></el-input>
    </el-form-item>
    <el-form-item label="分类父id 为0为根节点" prop="classFatherId">
      <el-input v-model="dataForm.classFatherId" placeholder="分类父id 为0为根节点"></el-input>
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
          className: '',
          createTime: '',
          updateTime: '',
          classStatus: '',
          classFatherId: ''
        },
        dataRule: {
          className: [
            { required: true, message: '分类名称不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '分类创建时间不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '分类修改时间不能为空', trigger: 'blur' }
          ],
          classStatus: [
            { required: true, message: '分类状态不能为空', trigger: 'blur' }
          ],
          classFatherId: [
            { required: true, message: '分类父id 为0为根节点不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/article/class/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.className = data.class.className
                this.dataForm.createTime = data.class.createTime
                this.dataForm.updateTime = data.class.updateTime
                this.dataForm.classStatus = data.class.classStatus
                this.dataForm.classFatherId = data.class.classFatherId
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
              url: this.$http.adornUrl(`/article/class/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'className': this.dataForm.className,
                'createTime': this.dataForm.createTime,
                'updateTime': this.dataForm.updateTime,
                'classStatus': this.dataForm.classStatus,
                'classFatherId': this.dataForm.classFatherId
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
