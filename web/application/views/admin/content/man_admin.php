<div class="row">
  <div class="col-xl-3 col-sm-6 mb-3">
    <div class="card text-white bg-success o-hidden h-100">
      <div class="card-body">
        <div class="card-body-icon">
          <i class="fas fa-fw fa-user"></i>
        </div>
        <div class="mr-5">Admin</div>
      </div> 
      <?php 
        if($permission == 'super'){
            $href = base_url("admin/formadmin");
            $info = "";
        }
        else if($permission == 'admin'){
            $href = "#";
            $info = "<div class='nav-item'><i class='fas fa-fw fa-info-circle'></i><span>Anda Tidak Mempunyai Hak Akses Untuk Menambah Ataupun Merubah Data Admin</span></div>";
        }
      ?>
      <a class="card-footer text-white clearfix small z-1" href="<?php echo $href?>">
        <span class="float-left">Tambah Admin Baru</span>
        <span class="float-right">
          <i class="fas fa-angle-right"></i>
        </span>
      </a>
    </div>
  </div>
  <div class="col-xl-3 col-sm-6 mb-3">
    <div class="card text-white bg-danger o-hidden h-100">
      <div class="card-body">
        <div class="card-body-icon">
          <i class="fas fa-fw fa-chart-area"></i>
        </div>
        <div class="mr-5">Dashboard Antrian</div>
      </div>
      <a class="card-footer text-white clearfix small z-1" href="<?php echo base_url("admin/dashboard")?>">
        <span class="float-left">Antrian</span>
        <span class="float-right">
          <i class="fas fa-angle-right"></i>
        </span>
      </a>
    </div>
  </div>
  <?php echo $info?>
</div>
<div class="row">
  <div class="col-xl-12 col-sm-12 mb-12">
    <div class="card mb-3">
          <div class="card-header bg-success" style="color: white">
            <i class="fas fa-table"></i>
              Tabel Admin Terdaftar
          </div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                <thead>
                  <tr>
                    <th>Username</th>
                    <th>Nama Petugas</th>
                    <th style="width: 80px">Status</th>
                    <th style="width: 50px">Update</th>
                  </tr>
                </thead>
                <tfoot>
                  <tr>
                    <th>Username</th>
                    <th>Nama Petugas</th>
                    <th style="width: 80px">Status</th>
                    <th style="width: 50px">Update</th>
                  </tr>
                </tfoot>
                <tbody>
                  <?php foreach($admin as $adm):?>
                    <tr>
                        <?php 
                            if($permission == 'super'){
                                $perm = base_url("admin/formeditadmin/").$adm->id_admin;
                                $deladm = base_url("admin/deleteadmin/").$adm->id_admin;
                                $modals = "deleteadmin";
                            }
                            else if($permission == 'admin'){
                                $perm = "#";
                                $deladm = "";
                                $modals = "";
                            }
                        ?>
                        <?php
                            $status = $adm->status;
                            if($status == 'non-aktif'){
                                $button = "btn btn-danger";
                            } 
                            else {
                                $button = "btn btn-success";
                            }
                        ?>
                        <td><?php echo $adm->username?></td>
                        <td><?php echo $adm->nama?></td>
                        <td><label class="<?php echo $button?>"><?php echo $adm->status?></label></td>
                        <td>
                          <a class="btn btn-info" href="<?php echo $perm?>"><i class="fa fa-gear"></i></a>
                        </td>
                    </tr>
                  <?php endforeach;?>
                </tbody>
              </table>
            </div>
          </div>
        </div>
  </div>
</div>